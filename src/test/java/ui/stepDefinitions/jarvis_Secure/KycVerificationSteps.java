package ui.stepDefinitions.jarvis_Secure;

import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.KycVerificationPage;

public class KycVerificationSteps extends BaseTest {

    @And("User verifies KYC for non-financial co-applicant")
    public void verifyKycForNonFinancialCoApplicant() throws Exception {
        KycVerificationPage kycPage = new KycVerificationPage(BaseTest.getPage());

        // 1. Navigate to the Verification tab
        kycPage.navigateToVerificationTab();

        // 2. Click Edit to open KYC resolution
        kycPage.clickEditForKycResolution();

        // 3. Resolve KYC
        kycPage.resolveKyc();

        // 4. Navigate back to App Form
        kycPage.navigateBackToAppForm();

        log.info("KYC verification completed for non-financial co-applicant.");
    }
}
