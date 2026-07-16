package ui.stepDefinitions.jarvis_Secure;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.CAMStagePage;

public class CAMStageSteps extends BaseTest {

    @And("User completes CAM stage mandatory fields and moves to Credit Review")
    public void completeCamAndMoveToCreditReview() throws Exception {
        CAMStagePage camPage = new CAMStagePage(BaseTest.getPage());
        camPage.attemptMoveToCreditReview();
        String aadhaar = TestDataProvider.get("dsa_secured.jarvis_secured.cam.aadhaar_last4");
        camPage.fillAadhaarDetails(aadhaar);
        String employerCategory = TestDataProvider.get("dsa_secured.jarvis_secured.cam.employer_category");
        String employeeId = TestDataProvider.get("dsa_secured.jarvis_secured.cam.employee_id");
        String industrySector = TestDataProvider.get("dsa_secured.jarvis_secured.cam.industry_sector");
        String officeContact = TestDataProvider.get("dsa_secured.jarvis_secured.cam.office_contact");
        camPage.fillEmploymentDetails(employerCategory, employeeId, industrySector, officeContact);

        // 4. Fill Personal details
        String residentialStatus = TestDataProvider.get("dsa_secured.jarvis_secured.cam.residential_status");
        camPage.fillPersonalDetails(residentialStatus);

        // 5. Submit and select OVD
//        String ovdType = TestDataProvider.get("dsa_secured.jarvis_secured.cam.ovd_type");
//        camPage.submitApplicantDetailsAndSelectOvd(ovdType);

        log.info("CAM stage completed. Application moved to Credit Review.");
    }
}
