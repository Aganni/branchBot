package ui.stepDefinitions.dsa_secured_plp;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.Then;
import ui.pages.dsa_secured_plp.customerConsentPage;

public class CustomerConsentSteps extends BaseTest {

    @Then("User provides PLP customer consent via OTP verification")
    public void verifyCustomerConsent() {
        customerConsentPage consentPage = new customerConsentPage(BaseTest.getPage());
        // 1. Trigger the OTP request allocation dispatch
        consentPage.clickSendOtp();
        // 2. Extract and pipe the '123456' data payload dynamically straight from the YAML file
        String mockOtp = TestDataProvider.get("dsa_secured_plp.consent_details.SEND OTP");
        consentPage.enterConsentOtp(mockOtp);
        // 3. Confirm validation completion status adjustments and transition forward
        consentPage.verifyOtpSuccessAndProceed();
    }
}
