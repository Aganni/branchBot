package ui.stepDefinitions.jarvis_Secure;

import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.CollateralDetailsPage;
import ui.pages.jarvis_secured.CreditReviewPage;
import ui.pages.jarvis_secured.LoanRequirementsPage;

public class CreditReviewSteps extends BaseTest {

    @And("User completes Credit Review stage and moves to Credit Approval")
    public void completeCreditReviewAndMoveToCreditApproval() throws Exception {
        CreditReviewPage creditReviewPage = new CreditReviewPage(BaseTest.getPage());
        CollateralDetailsPage collateralPage = new CollateralDetailsPage(BaseTest.getPage());
        LoanRequirementsPage loanReqPage = new LoanRequirementsPage(BaseTest.getPage());

        // 1. Resolve KYC Verification for non-financial co-applicant
        creditReviewPage.resolveKycVerification();

        // 2. Navigate back to App Form tab
        creditReviewPage.navigateBackToAppForm();

        // 3. Reassign application to Tenjin
        String assigneeEmail = BaseTest.getUserEmail();
        creditReviewPage.reassignToTenjin(assigneeEmail);

        // 4. Fill Collateral Details (CERSAI)
        collateralPage.fillCollateralDetails();

        // 5. Move to Credit Approval (handles validation errors, fills loan requirements & secondary collateral as needed)
        creditReviewPage.moveToCreditApproval(collateralPage, loanReqPage);

        log.info("Credit Review stage completed and application moved to Credit Approval.");
    }
}
