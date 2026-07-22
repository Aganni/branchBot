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

        // Move to Credit Approval (handles validation errors — fills loan requirements & secondary collateral as needed)
        creditReviewPage.moveToCreditApproval(collateralPage, loanReqPage);

        log.info("Loan requirements completed and application moved to Credit Approval.");
    }
}
