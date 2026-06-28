package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.IndividualConsentPage;

public class IndividualConsentSteps extends BaseTest {

    @And("User completes OTP consent verification for individual applicants")

    public void completeIndividualConsentFlow() throws Exception {
        IndividualConsentPage consentPage = new IndividualConsentPage(BaseTest.getPage());

        // Pull the mock OTP string ("123456") defined inside normal.yaml
        String otpValue = TestDataProvider.get("dsa_secured.Applicant_consent_details.SEND OTP");

        consentPage.verifyIndividualApplicantsConsent(otpValue);
    }

    public void verifyValidationAndTriggerEntityConsent() throws Exception {
        IndividualConsentPage consentPage = new IndividualConsentPage(BaseTest.getPage());

        // 1. Verify the mandatory consent error message pops up
        consentPage.verifyMandatoryConsentValidation();

        // 2. Click on Re-trigger Consent for the Entity applicant
        consentPage.clickReTriggerEntityConsent();
    }
}