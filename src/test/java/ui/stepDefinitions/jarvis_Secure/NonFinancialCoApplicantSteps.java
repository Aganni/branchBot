package ui.stepDefinitions.jarvis_Secure;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.NonFinancialCoApplicantPage;

public class NonFinancialCoApplicantSteps extends BaseTest {

    @And("User completes non-financial co-applicant mandatory fields")
    public void completeNonFinancialCoApplicantFields() throws Exception {
        NonFinancialCoApplicantPage nonFinancialPage = new NonFinancialCoApplicantPage(BaseTest.getPage());

        // 1. Open non-financial co-applicant details
        String coApplicantName = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_name");
        nonFinancialPage.openCoApplicantDetails(coApplicantName);

        // 2. Submit co-applicant and select OVD
        String coAppOvdType = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_ovd_type");
        nonFinancialPage.submitAndSelectOvd(coAppOvdType);

        // 3. Fill Voter ID and verify
        String voterId = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_voter_id");
        nonFinancialPage.fillVoterIdAndVerify(voterId);

        // 4. Fill last name and residential status
        String lastName = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_last_name");
        String coAppResStatus = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_residential_status");
        nonFinancialPage.fillLastNameAndResidentialStatus(lastName, coAppResStatus);

        // 5. Submit non-financial co-applicant final details
        nonFinancialPage.submitFinalDetails();

        log.info("Non-financial co-applicant mandatory fields completed.");
    }
}
