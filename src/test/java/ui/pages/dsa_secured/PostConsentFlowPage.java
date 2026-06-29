package ui.pages.dsa_secured;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

public class PostConsentFlowPage extends BaseTest {
    private final Page page;

    public PostConsentFlowPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void movingToNextSatge() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("NEXT")).click();
        log.info("Moved to Exposure Dedupe successful");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("next")).click();
        log.info("Moving to bureau output screen");
    }

    public void downloadBureauReport() {
        log.info("Attempting to download the Bureau reports.");
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        Locator downloadBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Download Report")).first();
        downloadBtn.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        Download download = page.waitForDownload(new Page.WaitForDownloadOptions().setTimeout(60000), () -> {
            downloadBtn.click();
        });
        log.info("Report downloaded successfully: {}", download.suggestedFilename());
    }

    public void BankStatement() {
        log.info("Transitioning from Bureau Output to Bank Statement layout phase.");
        String bankStatementTarget = "text=BANK STATEMENT";
        page.locator(bankStatementTarget).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15000));
        page.locator(bankStatementTarget).click();
        log.info("Successfully clicked BANK STATEMENT navigation link step.");
    }
}