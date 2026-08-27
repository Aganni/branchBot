package ui.stepDefinitions.jarvis_Secure;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.NonFinancialCoApplicantPage;

public class NonFinancialCoApplicantSteps extends BaseTest {

    @And("User completes non-financial co-applicant mandatory fields")
    public void completeNonFinancialCoApplicantFields() throws Exception {
        NonFinancialCoApplicantPage nonFinancialPage = new NonFinancialCoApplicantPage(BaseTest.getPage());

        // 1. Open co-applicant details section and click View on BHASKAR
        String coApplicantName = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_name");
        nonFinancialPage.openCoApplicantDetails(coApplicantName);

        // 2. Click Edit
        nonFinancialPage.clickEdit();

        // 3. Click Submit in dialog (triggers mandatory field validation)
        nonFinancialPage.clickSubmitInDialog();

        // 4. Fill residential status
        String residentialStatus = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_residential_status");
        nonFinancialPage.fillResidentialStatus(residentialStatus);

        // 5. Final submit
        nonFinancialPage.submitFinalDetails();

        // 6. Reload page
        nonFinancialPage.reloadPage();

        log.info("Non-financial co-applicant mandatory fields completed.");
    }
}
