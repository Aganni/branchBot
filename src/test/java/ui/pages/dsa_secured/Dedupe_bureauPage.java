package ui.pages.dsa_secured;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

public class Dedupe_bureauPage extends BaseTest {
    private final Page page;

    public Dedupe_bureauPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    //Clicks NEXT to move from Co-Applicant section to Exposure Dedupe section.
    public void moveToDedupeSection() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("NEXT")).click();
        log.info("Moved from Co-Applicant to Exposure Dedupe section.");
    }

    //Clicks next to move from Dedupe to Bureau Output screen.
    public void moveToBureauOutput() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("next")).click();
        log.info("Moved from Dedupe to Bureau Output screen.");
    }

    //Downloads the bureau report. Waits for the page to settle, then clicks "Download Report" and handles the file download.
    public void downloadBureauReport() {
        page.waitForTimeout(5000);
        Locator downloadBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Download Report")).first();
        downloadBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(30000));
        Download download = page.waitForDownload(() -> {
            downloadBtn.click();
        });
        log.info("Bureau report downloaded: {}", download.suggestedFilename());
    }

    //Navigates to Bank Statement section and fills the date range for statement upload.
    public void completeBankStatement(String fromDate, String toDate) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Bank Statement")).click();
        log.info("Clicked Bank Statement button.");

        page.getByLabel("From Date *").click();
        page.getByLabel("From Date *").fill(fromDate);

        page.getByLabel("To Date *").click();
        page.getByLabel("To Date *").fill(toDate);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue").setExact(true)).click();
        log.info("Filled date range {} to {} and clicked Continue.", fromDate, toDate);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue to Statement Upload")).click();
        log.info("Clicked Continue to Statement Upload.");
    }

    //Clicks Save and Next to proceed to the next section.
    public void clickSaveAndNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save and Next")).click();
        log.info("Clicked Save and Next. Moving to next section.");
    }
}
