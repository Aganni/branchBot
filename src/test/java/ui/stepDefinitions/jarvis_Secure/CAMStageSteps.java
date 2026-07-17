package ui.stepDefinitions.jarvis_Secure;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.CAMStagePage;

public class CAMStageSteps extends BaseTest {

    @And("User completes CAM stage mandatory fields and moves to Credit Review")
    public void completeCamAndMoveToCreditReview() throws Exception {
        CAMStagePage camPage = new CAMStagePage(BaseTest.getPage());

        // 1. Attempt Move to Credit Review — triggers mandatory field validation
        camPage.attemptMoveToCreditReview();

        // ── PRIMARY APPLICANT DETAILS ──

        // 2. Fill Aadhaar last 4 digits
        String aadhaar = TestDataProvider.get("dsa_secured.jarvis_secured.cam.aadhaar_last4");
        camPage.fillAadhaarDetails(aadhaar);

        // 3. Fill Employment details
        String employerCategory = TestDataProvider.get("dsa_secured.jarvis_secured.cam.employer_category");
        String employeeId = TestDataProvider.get("dsa_secured.jarvis_secured.cam.employee_id");
        String industrySector = TestDataProvider.get("dsa_secured.jarvis_secured.cam.industry_sector");
        String officeContact = TestDataProvider.get("dsa_secured.jarvis_secured.cam.office_contact");
        camPage.fillEmploymentDetails(employerCategory, employeeId, industrySector, officeContact);

        // 4. Fill Personal details
        String residentialStatus = TestDataProvider.get("dsa_secured.jarvis_secured.cam.residential_status");
        camPage.fillPersonalDetails(residentialStatus);

        // ── CO-APPLICANT NON-FINANCIAL DETAILS ──

        // 6. Open co-applicant details
        String coApplicantName = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_name");
        camPage.openCoApplicantDetails(coApplicantName);

        // 7. Submit co-applicant and select OVD
        String coAppOvdType = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_ovd_type");
        camPage.submitCoApplicantAndSelectOvd(coAppOvdType);

        // 8. Fill Voter ID and verify
        String voterId = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_voter_id");
        camPage.fillCoApplicantVoterIdAndVerify(voterId);

        // 9. Fill last name and residential status
        String lastName = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_last_name");
        String coAppResStatus = TestDataProvider.get("dsa_secured.jarvis_secured.cam.co_applicant_residential_status");
        camPage.fillCoApplicantLastNameAndResidentialStatus(lastName, coAppResStatus);

        // 10. Submit co-applicant final details
        camPage.submitCoApplicantFinalDetails();

        // ── CO-APPLICANT FINANCIAL DETAILS (Hannah) ──

        // 11. Reload and open financial co-applicant
        camPage.reloadPage();
        camPage.openFinancialCoApplicantDetails("Hannah");

        // 12. Submit the dialog to trigger mandatory field validation
        camPage.submitFinancialCoApplicantDialog();

        // 13. Fill financial co-applicant mandatory fields (hardcoded values)
        camPage.fillFinancialCoApplicantMandatoryFields(
                "5647",         // aadhaar last 4 digits
                "own",          // residential status
                "CAT A",        // employer category
                "654",          // employee id
                "Plastic",      // industry sector
                "9876543211"    // office contact number
        );

        // 14. Submit financial co-applicant final details
        camPage.submitFinancialCoApplicantFinalDetails();

        log.info("CAM stage completed. Application moved to Credit Review.");
    }
}
