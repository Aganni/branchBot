package ui.pages.dsa_secured;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import java.nio.file.Paths;


public class Dedupe_bureauPage extends BaseTest {
    private final Page page;
    private static final String CLOSE_TAB_BTN = "button:has-text('close Tab')";

    public Dedupe_bureauPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // Clicks NEXT to move from Co-Applicant section to Exposure Dedupe section.
    public void moveToDedupeSection() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("NEXT")).click();
        log.info("Moved from Co-Applicant to Exposure Dedupe section.");
    }

    // Clicks next to move from Dedupe to Bureau Output screen.
    public void moveToBureauOutput() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("next")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Moved from Dedupe to Bureau Output screen.");
    }

    // Downloads the bureau report via a popup page triggered by "Download Report" button.
    // Bureau report generation can take time — waits up to 90 seconds for the button to appear.
    public void downloadBureauReport() {
        // Wait for bureau processing to complete and Download Report button to appear
        Locator downloadBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Download Report")).first();

        // Poll until the button becomes visible — bureau processing may take up to 90s
        downloadBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(90000));
        log.info("Download Report button is now visible.");

        Download download = page.waitForDownload(() -> {
            Page popupPage = page.waitForPopup(() -> {
                downloadBtn.click();
            });
            popupPage.close();
        });
        log.info("Bureau report downloaded: {}", download.suggestedFilename());
    }

    // Navigates to Bank Statement screen, selects applicant, fills dates, handles
    // the statement upload popup (confirm, upload files, finish), then returns to main page.
    public void completeBankStatement(String applicantName, String fromDate, String toDate,
                                      String[] bankStatementFiles) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Bank Statement")).click();
        log.info("Clicked Bank Statement button.");

        // Select applicant
        page.getByPlaceholder("Select Applicant").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(applicantName)).click();
        log.info("Selected applicant: {}", applicantName);

        // Fill date range
        page.getByLabel("From Date *").click();
        page.getByLabel("From Date *").fill(fromDate);

        page.getByLabel("To Date *").click();
        page.getByLabel("To Date *").fill(toDate);
        log.info("Filled date range {} to {}.", fromDate, toDate);

        // Click Continue
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue").setExact(true)).click();
        log.info("Clicked Continue.");

        // Handle the statement upload popup
        Page uploadPopup = page.waitForPopup(() -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue to Statement Upload")).click();
        });
        log.info("Statement upload popup opened.");

        uploadPopup.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("I Confirm")).click();
        log.info("Clicked I Confirm on upload popup.");

        // Upload each bank statement file
        for (String file : bankStatementFiles) {
            uploadPopup.getByText("UPLOAD YOUR BANK E-STATEMENTS").click();
            uploadPopup.locator("input[type='file']").setInputFiles(Paths.get(file));
            log.info("Uploaded bank statement file: {}", file);
        }

        // Finish the upload
        uploadPopup.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Or click here to finish")).click();
        uploadPopup.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Finish")).click();
        log.info("Bank statement upload completed and popup finished.");
        uploadPopup.locator(CLOSE_TAB_BTN).click();
    }

    // Clicks Save and Next to proceed to the next section.
    public void clickSaveAndNext()  {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save and Next")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        log.info("Clicked Save and Next. Moving to next section.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
