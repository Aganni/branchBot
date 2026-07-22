package ui.stepDefinitions.jarvis_Secure;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.CreditReviewPage;
public class CreditReviewSteps extends BaseTest {
    @And("User resolves KYC verification and reassigns application in Credit Review")
    public void resolveKycAndReassign() throws Exception {
        CreditReviewPage creditReviewPage = new CreditReviewPage(BaseTest.getPage());
        // 1. Resolve KYC Verification for non-financial co-applicant
        creditReviewPage.resolveKycVerification();
        // 2. Navigate back to App Form tab
        creditReviewPage.navigateBackToAppForm();
        // 3. Reassign application to Tenjin
        String assigneeEmail = BaseTest.getUserEmail();
        creditReviewPage.reassignToTenjin(assigneeEmail);
        log.info("KYC resolved and application reassigned to Tenjin in Credit Review.");
    }
}
