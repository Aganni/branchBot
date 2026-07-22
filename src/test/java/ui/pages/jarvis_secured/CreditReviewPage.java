package ui.pages.jarvis_secured;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;
public class CreditReviewPage extends BaseTest {
    private final Page page;
    public CreditReviewPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // KYC VERIFICATION (RESOLVE)
    // Resolves KYC verification for non-financial co-applicant. Navigates directly to Verification tab, edits and resolves the KYC.
    public void resolveKycVerification() {
        log.info("Resolving KYC Verification for non-financial co-applicant...");
        // Navigate directly to Verification tab
        page.waitForTimeout(10000);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Verification")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        // Click Edit button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" edit")).click();
        page.waitForTimeout(2000);
        // Click Resolve button (first one)
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Resolve")).click();
        page.waitForTimeout(2000);
        // Confirm Resolve in the modal
        page.getByLabel("Resolve Kyc").getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Resolve")).click();
        page.waitForTimeout(3000);
        log.info("KYC Verification resolved successfully.");
    }

    // NAVIGATE BACK TO APP FORM
    //Navigates back to the App Form tab after KYC resolution. Uses direct URL navigation to ensure Application Actions is available.
    public void navigateBackToAppForm() {
        log.info("Navigating back to App Form tab...");
        // Wait a few seconds after resolve, still on Verification tab
        page.waitForTimeout(5000);
        // Reload the page
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        // Click App Form tab
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("App Form")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Back on App Form tab.");
    }

    // REASSIGN TO TENJIN
    // Reassigns the application to Tenjin user via Application Actions dropdown.Must be called AFTER navigateBackToAppForm() so the dropdown is in the DOM.
    public void reassignToTenjin(String assigneeEmail) {
        log.info("Reassigning application to Tenjin user [{}]...", assigneeEmail);
        // Application Actions should now be available on App Form tab
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Re-Assign").click();
        page.waitForTimeout(1000);
        // Select Level
        page.getByPlaceholder("All Level").click();
        page.locator("li").filter(new Locator.FilterOptions().setHasText("L4")).click();
        page.waitForTimeout(500);
        // Select User
        page.getByPlaceholder("User email id").click();
        page.waitForTimeout(1000);
        page.getByText(assigneeEmail).click();
        page.waitForTimeout(500);
        // Click ReAssign button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ReAssign")).click();
        log.info("Clicked ReAssign button.");
        page.waitForTimeout(3000);
        // Dismiss alert/notification
        try {
            Locator alert = page.getByRole(AriaRole.ALERT).locator("div").nth(2);
            if (alert.isVisible()) {
                alert.click();
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            log.info("No alert to dismiss after reassign.");
        }
        page.waitForLoadState(LoadState.NETWORKIDLE);
        log.info("Application reassigned to Tenjin successfully.");
    }

    // MOVE TO CREDIT APPROVAL
    //Clicks "Move to Credit Approval" from Application Actions dropdown.
     public void moveToCreditApproval() {
        log.info("Moving application to Credit Approval...");
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to Credit Approval").click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Move to Credit Approval action completed.");
    }
}
