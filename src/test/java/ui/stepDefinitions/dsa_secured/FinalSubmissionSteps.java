package ui.stepDefinitions.dsa_secured;

import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.FinalSubmissionPage;

public class FinalSubmissionSteps extends BaseTest {

    @And("User completes final submission with email verification")
    public void userCompletesFinalSubmission() {
        FinalSubmissionPage finalPage = new FinalSubmissionPage(
                BaseTest.getPage(), BaseTest.getContext());

        // 1. Click Submit on the login/fee page
        finalPage.clickSubmit();

        // 2. Handle Swift Loan popup - select Yes
        finalPage.handleSwiftLoanPopup();

        // 3. Click Next and Submit (triggers email verification validation)
        finalPage.clickNextAndSubmit();

        // 4. Go to Primary Applicant section and trigger email verification
        finalPage.triggerEmailVerification();

        // 5. Open Google Groups, sign in, and navigate to the verification email
        finalPage.loginAndNavigateToGroups("tenjin.user@creditsaison-in.com", "CreditQA@4860");

        // 6. Open the latest email, click "Verify My Email" and complete verification
        finalPage.openLatestEmailAndVerify();

        // 7. Return to main page, navigate through all sections, and final submit
        finalPage.reloadAndSubmitApplication();

        log.info("Final submission with email verification completed successfully.");
    }
}
