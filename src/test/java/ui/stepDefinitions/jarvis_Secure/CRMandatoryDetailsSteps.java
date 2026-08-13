package ui.stepDefinitions.jarvis_Secure;
import com.microsoft.playwright.Page;
import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.CRMandatoryDetailsPage;
public class CRMandatoryDetailsSteps extends BaseTest {
    private static final String CR = "dsa_secured.jarvis_secured.cr_mandatory.";
    @And("User fills mandatory details and moves to Credit Approval")
    public void fillMandatoryDetailsAndMoveToCreditApproval() {
        log.info("Starting CR Mandatory Details flow...");
        Page jarvisPage = BaseTest.getPage();
        CRMandatoryDetailsPage crPage = new CRMandatoryDetailsPage(jarvisPage);

        String totalWorkExp = TestDataProvider.get(CR + "total_work_experience");
        String yearsInCurrent = TestDataProvider.get(CR + "years_in_current");
        String coAppWorkExp = TestDataProvider.get(CR + "co_applicant_work_exp");
        String coAppYearsInCurrent = TestDataProvider.get(CR + "co_applicant_years_in_current");
        String industrySector = TestDataProvider.get(CR + "industry_sector");
        String productCategory = TestDataProvider.get(CR + "product_category");
        String revenue = TestDataProvider.get(CR + "revenue");
        String level = TestDataProvider.get(CR + "level");
        String userEmail = TestDataProvider.get(CR + "user_email");

        // STEP 1: Primary Applicant — Employment Details
        log.info("── Step 1: Primary Applicant Employment Details ──");
        crPage.openPrimaryApplicantDetails();
        crPage.clickEmploymentTab();
        crPage.fillTotalWorkExperience(totalWorkExp);
        crPage.fillTotalYearsInCurrent(yearsInCurrent);
        crPage.submitPrimaryApplicant();
        // Close the Primary Applicant modal
        crPage.closeModal();

        // STEP 2: Financial Co-Applicant — Work Experience
        log.info("── Step 2: Financial Co-Applicant Work Experience ──");
        crPage.openCoApplicantDetails();
        crPage.viewFinancialCoApplicant();
        crPage.clickEditOnCoApplicantModal();
        crPage.openCoApplicantEmploymentSection();
        crPage.fillCoApplicantWorkExp(coAppWorkExp);
        crPage.fillCoApplicantYearsInCurrent(coAppYearsInCurrent);
        crPage.saveCoApplicantEmployment();

        // STEP 3: Entity Co-Applicant — Industry/Revenue Details
        log.info("── Step 3: Entity Co-Applicant Industry/Revenue Details ──");
        crPage.openAndEditEntityCoApplicant();
        crPage.openEntityDetailsSection();
        crPage.selectIndustrySector(industrySector);
        crPage.selectProductCategory(productCategory);
        crPage.fillRevenue(revenue);
        crPage.verifyAndSubmitEntity();

        // STEP 4: Move to Credit Approval
        log.info("── Step 4: Move to Credit Approval ──");
        crPage.moveToCreditApproval(level, userEmail);

        log.info("CR Mandatory Details completed. Application moved to Credit Approval.");
    }

    @And("User moves application from Credit Approval to Terms")
    public void moveFromCreditApprovalToTerms() {
        log.info("Starting Credit Approval → Terms flow...");
        Page jarvisPage = BaseTest.getPage();
        CRMandatoryDetailsPage crPage = new CRMandatoryDetailsPage(jarvisPage);

        // STEP 5: Navigate to Credit Approval stage
        log.info("── Step 5: Navigate to Credit Approval stage ──");
        crPage.navigateToCreditApprovalStage();

        // STEP 6: Attempt Move to Terms → Reg Check validation
        log.info("── Step 6: Attempt Move to Terms (triggers Reg Check) ──");
        crPage.attemptMoveToTerms();

        // STEP 7: Resolve Reg Check
        log.info("── Step 7: Resolve Regulatory Check ──");
        crPage.resolveRegCheck();

        // STEP 8: Move to Terms (should succeed now)
        log.info("── Step 8: Move to Terms ──");
        crPage.moveToTerms();
        log.info("Application successfully moved to Terms stage.");
    }
}
