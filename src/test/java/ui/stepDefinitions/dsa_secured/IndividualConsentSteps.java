package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.IndividualConsentPage;

public class IndividualConsentSteps extends BaseTest {

    @And("User completes OTP consent verification for individual applicants")
    public void completeCoApplicantsConsent(int applicantCount) throws Exception {
        IndividualConsentPage consentPage = new IndividualConsentPage(BaseTest.getPage());

        // Process OTP Verification loops sequentially
        String otpValue = TestDataProvider.get("dsa_secured.consent_details.SEND OTP");
        consentPage.verifyCoApplicantsConsent(applicantCount, otpValue);

        // Advance past the Co-Applicants section dashboard layout
        consentPage.clickNext();
    }
}