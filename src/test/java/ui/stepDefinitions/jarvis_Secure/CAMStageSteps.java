package ui.stepDefinitions.jarvis_Secure;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.CAMStagePage;

public class CAMStageSteps extends BaseTest {

    @And("User completes CAM stage primary applicant mandatory fields")
    public void completeCamPrimaryApplicantFields() throws Exception {
        CAMStagePage camPage = new CAMStagePage(BaseTest.getPage());

        // 1. Attempt Move to Credit Review — triggers mandatory field validation
        camPage.attemptMoveToCreditReview();

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

        log.info("CAM stage primary applicant mandatory fields completed.");
    }
}
