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
    private static final String BANK_STATEMENT_PATH = "src/test/resources/testdata/bank_statement.pdf";

    public BankingPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ── Public Methods ───────────────────────────────────────────────────────

    public void selectBankAndProceed(String bankName) {
        String lpc = TestDataProvider.getLpc();

        if ("FCL".equals(lpc) || "SEP".equals(lpc)) {
            selectBankViaCombobox(bankName);
        } else {
            selectBankViaInput(bankName);
        }
    }

    public void submitAndMoveToNext() {
        String lpc = TestDataProvider.getLpc();

        if ("FCL".equals(lpc) || "SEP".equals(lpc)) {
            waitForBankToBeAdded();
        } else {
            reloadAndSubmitBankDetails();
        }

        log.info("Clicking Save and Next...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save and Next")).click();
    }

    // ── Private Helpers: Bank Selection ───────────────────────────────────────

    private void selectBankViaCombobox(String bankName) {
        log.info("Selecting bank via combobox (FCL/SEP): {}", bankName);

        Locator bankCombobox = page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Bank Name"));
        bankCombobox.click();
        bankCombobox.fill(bankName.substring(0, Math.min(4, bankName.length())));
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(bankName)).click();
        log.info("Selected bank: {}", bankName);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue").setExact(true)).click();

        log.info("Waiting for statement upload popup...");
        Page perfiosPage = page.waitForPopup(() -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue to Statement Upload")).click();
        });

        handleStatementUpload(perfiosPage);
    }

    private void selectBankViaInput(String bankName) {
        log.info("Selecting bank via input (UBL): {}", bankName);

        page.locator(BANK_NAME_INPUT).fill(bankName);
        page.keyboard().press("Enter");

        log.info("Setting up listener for new tab...");
        Page perfiosPage = page.waitForPopup(() -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue").setExact(true)).click();
        });

        waitForPerfiosUrl(perfiosPage);
        handlePerfiosUpload(perfiosPage);
    }

    // ── Private Helpers: Perfios Upload ──────────────────────────────────────

    private void handleStatementUpload(Page perfiosPage) {
        perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("I Confirm")).click();
        log.info("Clicked I Confirm");

        perfiosPage.getByText("UPLOAD YOUR BANK E-STATEMENTS").click();
        perfiosPage.getByLabel("UPLOAD YOUR BANK E-STATEMENTS").setInputFiles(Paths.get(BANK_STATEMENT_PATH));
        log.info("Uploaded bank statement");

        perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Or click here to finish")).click();
        log.info("Clicked finish");

        try {
            String doneUrl = initializeEnvironment("dsaPortalUrl").replace("/signin", "/bankStatementSubmitDone");
            perfiosPage.navigate(doneUrl);
        } catch (Exception e) {
            log.warn("Could not resolve dsaPortalUrl: {}", e.getMessage());
        }

        perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("close Tab")).click();
        perfiosPage.close();
        log.info("Closed Perfios tab");
    }

    private void handlePerfiosUpload(Page perfiosPage) {
        perfiosPage.locator(I_CONFIRM_BTN).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        perfiosPage.locator(I_CONFIRM_BTN).click();
        log.info("Clicked I Confirm");

        perfiosPage.setInputFiles(FILE_INPUT, Paths.get(BANK_STATEMENT_PATH));
        log.info("Uploaded bank statement");

        perfiosPage.locator(FINISH_BTN).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
        perfiosPage.locator(FINISH_BTN).click();
        log.info("Clicked finish");

        perfiosPage.locator(CLOSE_TAB_BTN).click();
        log.info("Closed Perfios tab");
    }

    // ── Private Helpers: Submit ───────────────────────────────────────────────

    private void waitForBankToBeAdded() {
        log.info("Waiting for bank to be added...");
        page.getByText("Added").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(120000));
        log.info("Bank added successfully - card is visible");
    }

    private void reloadAndSubmitBankDetails() {
        page.reload();
        log.info("Page reloaded. Submitting bank details...");
        page.locator(SUBMIT_BANK_DETAILS_BTN).click();
    }

    // ── Private Helpers: Utility ──────────────────────────────────────────────

    private void waitForPerfiosUrl(Page perfiosPage) {
        log.info("Waiting for Perfios URL...");
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
    }
}
