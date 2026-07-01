package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.EntityConsentPage;

public class EntityConsentSteps extends BaseTest {

    private EntityConsentPage consentPage;

    @And("User provides the consent for Entity applicant")
    public void userProvidesConsentForEntityApplicant() {
        consentPage = new EntityConsentPage(BaseTest.getPage());

        // 1. Click Re-trigger Consent on the DSA page to send the email notification
        consentPage.clickRetriggerConsent();

        // 2. Open Google Groups, sign in if needed, and navigate to the notification email
        String email = TestDataProvider.get("dsa_secured.co_applicant_entity.Consent.email");
        String password = TestDataProvider.get("dsa_secured.co_applicant_entity.Consent.password");
        consentPage.loginAndNavigateToGroups(email, password);

        // 3. Open the latest email, scroll down, click "Verify My Email" and complete consent
        consentPage.openLatestEmailAndConsent();

        // 4. Return focus to the main DSA application page
        consentPage.returnToMainWorkspace();
    }
}
