package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import dynamicData.DynamicDataClass;
import hooks.BaseTest;

/**
 * Page Object for Credit Review stage in the Jarvis Secured portal (LAP flow).
 * Handles KYC Verification resolution and reassignment to Tenjin.
 */
public class CreditReviewPage extends BaseTest {

    private final Page page;

    public CreditReviewPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  KYC VERIFICATION (RESOLVE)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Resolves KYC verification for non-financial co-applicant.
     * Navigates directly to Verification tab, edits and resolves the KYC.
     */
    public void resolveKycVerification() {
        log.info("Resolving KYC Verification for non-financial co-applicant...");

        // Navigate directly to Verification tab
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

    // ═══════════════════════════════════════════════════════════════════════════
    //  NAVIGATE BACK TO APP FORM
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Navigates back to the App Form tab after KYC resolution.
     * Uses direct URL navigation to ensure Application Actions is available.
     */
    public void navigateBackToAppForm() {
        log.info("Navigating back to App Form tab...");

        // Click the App Form link
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("App Form")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        // Navigate directly to the appForm URL to ensure we're on the right page
        String appFormId = (String) DynamicDataClass.getValue("appFormId");
        String currentUrl = page.url();
        String baseUrl = currentUrl.contains("/application/")
                ? currentUrl.replaceAll("/application/.*", "")
                : "https://jarvis.int.creditsaison.corp";
        String appFormUrl = baseUrl + "/application/" + appFormId + "/appForm";
        log.info("Navigating to: {}", appFormUrl);
        page.navigate(appFormUrl);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);

        log.info("Back on App Form tab.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  REASSIGN TO TENJIN
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Reassigns the application to Tenjin user via Application Actions dropdown.
     * Must be called AFTER navigateBackToAppForm() so the dropdown is in the DOM.
     */
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

    // ═══════════════════════════════════════════════════════════════════════════
    //  MOVE TO CREDIT APPROVAL
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Attempts to move to Credit Approval. Handles validation errors:
     * 1. "Loan Amount value should not..." → fills loan requirements first
     * 2. "Sanction loan amount is..." → fills secondary collateral validation
     * Then retries the move.
     */
    public void moveToCreditApproval(CollateralDetailsPage collateralPage, LoanRequirementsPage loanReqPage) {
        log.info("Attempting to move application to Credit Approval...");

        // First attempt
        selectMoveToCreditApproval();
        page.waitForTimeout(2000);

        // Check for "Loan Amount value should not" validation error
        Locator loanAmountError = page.getByText("Loan Amount value should not");
        try {
            loanAmountError.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(3000));
            log.info("Validation error: Loan Amount value issue. Filling Loan Requirements...");
            loanReqPage.fillLoanRequirements();

            // Retry move
            selectMoveToCreditApproval();
            page.waitForTimeout(2000);
        } catch (Exception e) {
            log.info("No 'Loan Amount' validation error. Checking for other validations...");
        }

        // Check for "Sanction loan amount is" validation error
        Locator sanctionError = page.getByText("Sanction loan amount is");
        try {
            sanctionError.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(3000));
            log.info("Validation error: Sanction loan amount issue. Filling secondary collateral...");
            collateralPage.fillSecondaryCollateralValidation();

            // Final retry move
            selectMoveToCreditApproval();
            page.waitForTimeout(2000);
        } catch (Exception e) {
            log.info("No 'Sanction loan amount' validation error.");
        }

        // Wait for acceptance/confirmation
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Application moved to Credit Approval.");
    }

    private void selectMoveToCreditApproval() {
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to Credit Approval").click();
        page.waitForTimeout(1000);
    }
}
