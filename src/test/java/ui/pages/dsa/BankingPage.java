package ui.pages.dsa;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

import java.nio.file.Paths;

public class BankingPage extends BaseTest {

    private final Page page;

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final String SUBMIT_BANK_DETAILS_BTN = "button:has-text('Submit Bank Details')";
    private static final String BANK_STATEMENT_PATH = "src/test/resources/testdata/bank_statement.pdf";

    public BankingPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void selectBankAndProceed(String bankName) {
        log.info("Starting bank statement upload");

        // Click "Continue" under Bank Statement Upload — opens Perfios popup
        Page perfiosPage = page.waitForPopup(() -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue").setExact(true)).click();
        });

        handlePerfiosUpload(perfiosPage);
    }

    public void submitAndMoveToNext() {
        // Wait for bank card to show "Added"
        log.info("Waiting for bank to be added...");
        page.getByText("Added").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(120000));
        log.info("Bank added successfully");

        // Submit Bank Details only for UBL
        String lpc = data.TestDataProvider.getLpc();
        if ("UBL".equals(lpc)) {
            page.locator(SUBMIT_BANK_DETAILS_BTN).click();
            log.info("Clicked Submit Bank Details");
        }

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save and Next")).click();
        log.info("Clicked Save and Next");
    }

    // ── Private ──────────────────────────────────────────────────────────────

    private void handlePerfiosUpload(Page perfiosPage) {
        perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("I Confirm")).click();
        log.info("Clicked I Confirm");

        // Upload bank statement via file chooser
        var fileChooser = perfiosPage.waitForFileChooser(() -> {
            perfiosPage.getByText("UPLOAD YOUR BANK E-STATEMENTS").click();
        });
        fileChooser.setFiles(Paths.get(BANK_STATEMENT_PATH));
        log.info("Uploaded bank statement");

        // Wait for upload to process and click finish
        perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Or click here to finish"))
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
        perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Or click here to finish")).click();
        log.info("Clicked 'Or click here to finish'");

        // Handle "Upload More / Finish" dialog
        perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Finish"))
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        perfiosPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Finish")).click();
        log.info("Clicked Finish");

        // Close Perfios tab
        page.waitForTimeout(3000);
        if (!perfiosPage.isClosed()) {
            perfiosPage.close();
        }
        log.info("Perfios upload complete");
    }
}
