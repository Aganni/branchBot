package ui.stepDefinitions.dsa_secured;

import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.FeeDetailsPage;

public class FeeDetailsSteps extends BaseTest {

    @And("User completes fee payment via sandbox payment gateway")
    public void userCompletesFeePayment() {
        FeeDetailsPage feeDetailsPage = new FeeDetailsPage(BaseTest.getPage());

        // 1. Click Calculate Login Fee and wait for calculation
        feeDetailsPage.clickCalculateLoginFee();

        // 2. Generate the payment link
        feeDetailsPage.clickGenerateNewLink();

        // 3. Complete payment in sandbox (Netbanking -> Test Bank -> OTP -> Submit)
        feeDetailsPage.completePaymentInSandbox();

        // 4. Return to DSA portal and proceed
        feeDetailsPage.navigateBackAndProceed();

        // 5. Confirm payment received
        feeDetailsPage.confirmPaymentReceived();

        log.info("Fee payment flow completed successfully.");
    }
}
