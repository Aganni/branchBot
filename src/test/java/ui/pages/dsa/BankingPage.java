package ui.pages.dsa;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import data.TestDataProvider;
import hooks.BaseTest;

import java.nio.file.Paths;

public class BankingPage extends BaseTest {

    private final Page page;

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final String BANK_NAME_INPUT = "#bankName";
    private static final String I_CONFIRM_BTN = "button.btn-confirm";
    private static final String FILE_INPUT = "input[name='file']";
    private static final String FINISH_BTN = "button:has-text('Or click here to finish')";
    private static final String CLOSE_TAB_BTN = "button:has-text('close Tab')";
    private static final String SUBMIT_BANK_DETAILS_BTN = "button:has-text('Submit Bank Details')";
    private static final String BANK_STATEMENT_PATH = "src/test/resources/testdata/June2026.pdf";

    public BankingPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void selectBankAndProceed(String bankName) {
        String lpc = TestDataProvider.getLpc();

        if ("SEP".equals(lpc)) {
            log.info("Selecting bank via combobox (SEP flow): {}", bankName);

            // Select bank from combobox
            Locator bankCombobox = page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Bank Name"));
            bankCombobox.click();
            bankCombobox.fill(bankName.substring(0, Math.min(4, bankName.length())));
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(bankName)).click();
            log.info("Selected bank: {}", bankName);

            // Click Continue
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue").setExact(true)).click();

            // Wait for popup triggered by "Continue to Statement Upload"
            log.info("Waiting for statement upload popup...");
            Page perfiosPage = page.waitForPopup(() -> {
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue to Statement Upload")).click();
            });

            // I Confirm
            perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("I Confirm")).click();
            log.info("Clicked I Confirm");

            // Click upload area then set file
            perfiosPage.getByText("UPLOAD YOUR BANK E-STATEMENTS").click();
            perfiosPage.getByLabel("UPLOAD YOUR BANK E-STATEMENTS").setInputFiles(Paths.get(BANK_STATEMENT_PATH));
            log.info("Uploaded bank statement");

            // Finish
            perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Or click here to finish")).click();
            log.info("Clicked finish");

            // Navigate to done URL and close tab
            try {
                String doneUrl = initializeEnvironment("dsaPortalUrl").replace("/signin", "/bankStatementSubmitDone");
                perfiosPage.navigate(doneUrl);
            } catch (Exception e) {
                log.warn("Could not resolve dsaPortalUrl: {}", e.getMessage());
            }
            perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("close Tab")).click();
            perfiosPage.close();
            log.info("Closed Perfios tab");

        } else {
            // FCL and UBL flow
            log.info("Selecting bank (FCL/UBL flow): {}", bankName);
            page.locator(BANK_NAME_INPUT).fill(bankName);
            page.keyboard().press("Enter");

            log.info("Setting up listener for new tab...");
            Page perfiosPage = page.waitForPopup(() -> {
                page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Continue").setExact(true))
                        .click();
            });

            log.info("New tab detected. Waiting for Perfios URL...");
            boolean urlMatched = false;
            for (int i = 0; i < 30; i++) {
                String currentUrl = perfiosPage.url();
                if (currentUrl.contains("perfios") || currentUrl.contains("statement")) {
                    urlMatched = true;
                    break;
                }
                perfiosPage.waitForTimeout(1000);
            }

            if (!urlMatched) {
                throw new RuntimeException("Timed out waiting for Perfios URL. Last seen: " + perfiosPage.url());
            }

            log.info("Waiting for 'I Confirm' button...");
            perfiosPage.locator(I_CONFIRM_BTN).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
            perfiosPage.locator(I_CONFIRM_BTN).click();

            log.info("Uploading bank statement PDF...");
            perfiosPage.setInputFiles(FILE_INPUT, Paths.get(BANK_STATEMENT_PATH));

            log.info("Waiting for analysis to complete...");
            perfiosPage.locator(FINISH_BTN).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
            perfiosPage.locator(FINISH_BTN).click();

            log.info("Closing Perfios tab...");
            perfiosPage.locator(CLOSE_TAB_BTN).click();
        }
    }

    public void submitAndMoveToNext() {
        String lpc = TestDataProvider.getLpc();

        if ("UBL".equals(lpc)) {
            // UBL only: reload page and click Submit Bank Details
            page.reload();
            log.info("Page reloaded. Submitting bank details...");
            page.locator(SUBMIT_BANK_DETAILS_BTN).click();
        } else if ("SEP".equals(lpc) || "FCL".equals(lpc)) {
            // FCL and SEP: wait for bank card to show "Added" status before proceeding
            log.info("Waiting for bank to be added...");
            page.getByText("Added").waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(120000));
            log.info("Bank added successfully - card is visible");
        }
        // FCL: no extra step needed — go straight to Save and Next

        log.info("Clicking Save and Next...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save and Next")).click();
    }
}
