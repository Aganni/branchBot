package ui.pages.dsa;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

public class EligibilityCheckPage extends BaseTest {

    private final Page page;

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final String SUCCESS_MESSAGE = "text='Your Loan Application has passed the Prechecks! Please continue...'";

    public EligibilityCheckPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void waitForPrechecksAndClickNext() {
        log.info("Waiting for Eligibility Prechecks countdown to finish...");

        page.locator(SUCCESS_MESSAGE).waitFor(new Locator.WaitForOptions().setTimeout(60000));
        log.info("Prechecks passed successfully!");

        // Click the proceed element - could be link, button, or any clickable with "CONTINUE"
        page.waitForTimeout(2000);
        Locator proceedElement = page.locator("text=/CONTINUE TO/i").first();
        proceedElement.click(new Locator.ClickOptions().setTimeout(10000));
        log.info("Clicked proceed button on eligibility check page");
    }
}
