package ui.stepDefinitions.jarvis_Secure;

import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.CollateralDetailsPage;
import ui.pages.jarvis_secured.CreditReviewPage;
import ui.pages.jarvis_secured.LoanRequirementsPage;

public class LoanRequirementsSteps extends BaseTest {

    @And("User fills loan requirements and moves to Credit Approval")
    public void fillLoanRequirementsAndMoveToCreditApproval() throws Exception {
        CreditReviewPage creditReviewPage = new CreditReviewPage(BaseTest.getPage());
        CollateralDetailsPage collateralPage = new CollateralDetailsPage(BaseTest.getPage());
        LoanRequirementsPage loanReqPage = new LoanRequirementsPage(BaseTest.getPage());
        // 1. Reassign appform to Tenjin
        String assigneeEmail = BaseTest.getUserEmail();
        creditReviewPage.reassignToTenjin(assigneeEmail);
        // 2. First attempt to Move to Credit Approval — will get validation error
        creditReviewPage.moveToCreditApproval();
        // 3. Fill secondary collateral details (secondary agency + valuation)
        collateralPage.fillSecondaryCollateralValidation();
        // 4. Fill Loan Requirements & Terms
        loanReqPage.fillLoanRequirements();
        // 5. Final Move to Credit Approval — should succeed
        creditReviewPage.moveToCreditApproval();
        log.info("Loan requirements completed and application moved to Credit Approval.");
    }
}
