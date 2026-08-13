package ui.stepDefinitions.jarvis_Secure;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.FeeDetailsPage;
import ui.pages.jarvis_secured.InsuranceDetailsPage;
import ui.pages.jarvis_secured.SpreadRatePage;
import ui.pages.jarvis_secured.UserDashboardPage;

/**
 * Step definitions for the Docket Initiation stage in Jarvis.
 * Covers:
 *   1. Fee Details — processing fee percentage
 *   2. Spread Rate — rate approval via Loan Requirements & Terms
 *   3. Insurance Details — general insurance + property insurance collateral
 *   4. User Dashboard — change dept/designation to SALES/SALES_MANAGER for DOGH access
 *   5. DOGH Trigger — trigger DOGH for insurance before final move
 *   6. Final move to Docket Initiation after all validations are resolved
 */
public class DocketInitiationSteps extends BaseTest {

    // Hardcoded test data for Docket Initiation
    private static final String PROCESSING_FEE_PERCENTAGE = "1.50";
    private static final String SPREAD_RATE = "-3";
    private static final String INSURANCE_PROVIDER = "ICICI Lombard";
    private static final String POLICY_HOLDER_NAME = "Hannah Isaac";
    private static final String POLICY_SUM_INSURED = "250000";
    private static final String POLICY_TENURE = "3 years";
    private static final String INSURANCE_PREMIUM = "25000";
    private static final String NOMINEE_NAME = "Noah johnson";
    private static final String NOMINEE_RELATIONSHIP = "brother";
    private static final String PROPERTY_INSURANCE_PREMIUM = "6840";

    // Admin Portal — User Dashboard
    private static final String USER_SEARCH_TEXT = "tenjin";
    private static final String DEPARTMENT = "SALES";
    private static final String DESIGNATION = "SALES_MANAGER";

    @And("User completes Fee Details for Docket Initiation")
    public void completeFeeDetails() {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  Docket Initiation — Fee Details");
        log.info("═══════════════════════════════════════════════════════════════");

        Page jarvisPage = BaseTest.getPage();
        FeeDetailsPage feeDetailsPage = new FeeDetailsPage(jarvisPage);

        // Attempt move → get fee validation → fill and submit fee
        feeDetailsPage.attemptMoveToDocketInitiation();
        feeDetailsPage.completeFeeDetails(PROCESSING_FEE_PERCENTAGE);

        log.info("Fee Details step completed.");
    }

    @And("User completes Spread Rate for Docket Initiation")
    public void completeSpreadRate() {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  Docket Initiation — Spread Rate / Rate Approval");
        log.info("═══════════════════════════════════════════════════════════════");

        Page jarvisPage = BaseTest.getPage();
        SpreadRatePage spreadRatePage = new SpreadRatePage(jarvisPage);

        // Attempt move → get rate approval validation → fill and submit spread
        spreadRatePage.attemptMoveToDocketInitiation();
        spreadRatePage.completeSpreadRate(SPREAD_RATE);

        // Refresh page after spread rate submission and wait for it to settle
        log.info("Refreshing page after spread rate submission...");
        jarvisPage.reload(new Page.ReloadOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));
        jarvisPage.waitForTimeout(5000);
        log.info("Page refreshed and ready for next stage movement.");

        log.info("Spread Rate step completed.");
    }

    @And("User completes Insurance Details for Docket Initiation")
    public void completeInsuranceDetails() {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  Docket Initiation — Insurance Details");
        log.info("═══════════════════════════════════════════════════════════════");

        Page jarvisPage = BaseTest.getPage();
        InsuranceDetailsPage insuranceDetailsPage = new InsuranceDetailsPage(jarvisPage);

        // Attempt move → get property validation → fill general + property insurance
        insuranceDetailsPage.attemptMoveToDocketInitiation();
        insuranceDetailsPage.completeInsuranceDetails(
                INSURANCE_PROVIDER,
                POLICY_HOLDER_NAME,
                POLICY_SUM_INSURED,
                POLICY_TENURE,
                INSURANCE_PREMIUM,
                NOMINEE_NAME,
                NOMINEE_RELATIONSHIP,
                PROPERTY_INSURANCE_PREMIUM
        );

        log.info("Insurance Details step completed.");
    }

    @And("User changes user role to SALES for DOGH access")
    public void changeUserRoleToSales() {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  Docket Initiation — Change User Department/Designation");
        log.info("═══════════════════════════════════════════════════════════════");

        // Open a new tab for Admin Portal to change user dept/designation
        BrowserContext context = BaseTest.getPage().context();
        Page adminPage = context.newPage();
        UserDashboardPage userDashboardPage = new UserDashboardPage(adminPage);

        userDashboardPage.changeUserDepartmentAndDesignation(USER_SEARCH_TEXT, DEPARTMENT, DESIGNATION);

        // Close the admin tab and bring back the main page
        adminPage.close();
        BaseTest.getPage().bringToFront();
        BaseTest.getPage().waitForTimeout(1000);

        log.info("User role changed to SALES/SALES_MANAGER. Admin tab closed.");
    }

    @And("User triggers DOGH for insurance")
    public void triggerDOGHForInsurance() {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  Docket Initiation — Trigger DOGH");
        log.info("═══════════════════════════════════════════════════════════════");

        Page jarvisPage = BaseTest.getPage();
        InsuranceDetailsPage insuranceDetailsPage = new InsuranceDetailsPage(jarvisPage);
        FeeDetailsPage feeDetailsPage = new FeeDetailsPage(jarvisPage);

        // Attempt move → get "Trigger DOGH for all" validation
        feeDetailsPage.attemptMoveToDocketInitiation();
        jarvisPage.waitForTimeout(1000);

        // Trigger DOGH via Insurance Details
        insuranceDetailsPage.triggerDOGH();

        log.info("DOGH trigger step completed.");
    }

    @And("User moves application to Docket Initiation")
    public void moveApplicationToDocketInitiation() {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  Docket Initiation — Final Move");
        log.info("═══════════════════════════════════════════════════════════════");

        Page jarvisPage = BaseTest.getPage();
        FeeDetailsPage feeDetailsPage = new FeeDetailsPage(jarvisPage);

        // All validations resolved — final move should succeed
        feeDetailsPage.attemptMoveToDocketInitiation();
        jarvisPage.waitForTimeout(3000);

        log.info("Application successfully moved to Docket Initiation.");
    }
}
