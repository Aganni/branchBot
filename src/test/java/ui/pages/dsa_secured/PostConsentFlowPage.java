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

    public void BankStatement() {
        log.info("Transitioning from Bureau Output to Bank Statement layout phase.");
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                        .setName(java.util.regex.Pattern.compile("Bank Statement", java.util.regex.Pattern.CASE_INSENSITIVE)))
                .click(new com.microsoft.playwright.Locator.ClickOptions().setTimeout(30000));

        log.info("Successfully clicked BANK STATEMENT navigation action button.");
    }
}