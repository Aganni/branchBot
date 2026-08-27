package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import java.util.regex.Pattern;

public class NonFinancialCoApplicantPage extends BaseTest {
    private final Page page;
    public NonFinancialCoApplicantPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    //  OPEN CO-APPLICANT DETAILS AND CLICK VIEW ON BHASKAR
    public void openCoApplicantDetails(String coApplicantName) {
        log.info("Opening Non-Financial CoApplicant details for: {}", coApplicantName);
        // Wait for any loading masks to disappear before interacting
        try {
            page.locator(".el-loading-mask").first()
                    .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(15000));
        } catch (Exception e) {
            log.info("No loading mask detected or already hidden.");
        }
        page.waitForTimeout(2000);

        // Open the co-applicant details section
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName(Pattern.compile("CoApplicant Details"))).click();
        page.waitForTimeout(2000);

        // Wait for View buttons to appear
        Locator viewButtons = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View"));
        viewButtons.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        page.waitForTimeout(1000);

        // Find BHASKAR by name and click View on that card
        Locator nameHeading = page.locator("h3").filter(new Locator.FilterOptions().setHasText(coApplicantName));
        int count = nameHeading.count();
        log.info("Found {} h3 elements with text '{}' in CoApplicant section", count, coApplicantName);

        if (count == 0) {
            throw new RuntimeException("Non-Financial CoApplicant '" + coApplicantName + "' not found in the list.");
        }

        // Navigate from the h3 up to the details-card container, then find the View button within it
        nameHeading.first()
                .locator("xpath=ancestor::div[contains(@class,'details-card')]")
                .first()
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("View"))
                .click();
        log.info("Clicked View for non-financial co-applicant: {}", coApplicantName);
        page.waitForTimeout(1000);
    }

    //  CLICK EDIT IN THE DIALOG
    public void clickEdit() {
        log.info("Clicking Edit button...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);
        log.info("Edit button clicked.");
    }

    //  CLICK SUBMIT IN DIALOG (triggers mandatory field validation)
    public void clickSubmitInDialog() {
        log.info("Clicking Submit in dialog...");
        page.getByRole(AriaRole.DIALOG).getByText("Submit").click();
        page.waitForTimeout(3000);
        log.info("Submit clicked in dialog.");
    }

    //  FILL RESIDENTIAL STATUS
    public void fillResidentialStatus(String residentialStatus) {
        log.info("Selecting residential status: {}", residentialStatus);
        page.getByPlaceholder("Select the residential status").click();
        page.locator("li").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^" + residentialStatus + "$"))).click();
        page.waitForTimeout(500);
        log.info("Residential status selected: {}", residentialStatus);
    }

    //  SUBMIT FINAL DETAILS
    public void submitFinalDetails() {
        log.info("Clicking final submit...");
        page.locator("div:nth-child(25) > .el-col > .cs-fab > .info").click();
        page.waitForTimeout(2000);
        log.info("Non-financial co-applicant details submitted successfully.");
    }

    //  RELOAD PAGE
    public void reloadPage() {
        log.info("Reloading page...");
        try {
            page.reload(new Page.ReloadOptions()
                    .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED)
                    .setTimeout(30000));
        } catch (com.microsoft.playwright.PlaywrightException e) {
            log.warn("Reload encountered an issue: {}. Waiting for page to stabilize.", e.getMessage());
        }
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Page reloaded.");
    }
}
