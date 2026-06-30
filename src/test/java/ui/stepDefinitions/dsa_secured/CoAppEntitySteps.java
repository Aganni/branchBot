package ui.stepDefinitions.dsa_secured;
import backend.Utils.AwsSqsUtils;
import backend.payload.SqsQueuePayload;
import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.CoAppEntityPage;
public class CoAppEntitySteps extends BaseTest {
    @And("User adds Entity Co-Applicant details and submits Appform")
    public void completeCoAppEntityFlow() throws Exception {
        CoAppEntityPage entityPage = new CoAppEntityPage(BaseTest.getPage());
        String email = TestDataProvider.get("dsa.co_applicant_entity.email");

        // 1. Initialize and Process Entity Profile Identification
        entityPage.clickAddApplicant();
        entityPage.selectApplicantType(TestDataProvider.get("dsa_secured.co_applicant_entity.kyc.applicant_type"));

        // 2. Perform PAN and UDYAM Verification
        entityPage.verifyCompanyPan(TestDataProvider.get("dsa_secured.co_applicant_entity.kyc.pan"));
        entityPage.verifyUdyamDetails(TestDataProvider.get("dsa_secured.co_applicant_entity.kyc.udyam"));

        // 3.Corporate Profile Details
        entityPage.EntityProfileDetails(
                TestDataProvider.get("dsa_secured.co_applicant_entity.kyc.registration_date"),
                TestDataProvider.get("dsa_secured.co_applicant_entity.kyc.business_type"),
                TestDataProvider.get("dsa_secured.co_applicant_entity.kyc.phone_number"),
                TestDataProvider.get("dsa_secured.co_applicant_entity.kyc.email")
        );
        // 4.Corporate Operating Address Details
        entityPage.OperatingAddressDetails(
                TestDataProvider.get("dsa_secured.co_applicant_entity.addresses.line1"),
                TestDataProvider.get("dsa_secured.co_applicant_entity.addresses.line2"),
                TestDataProvider.get("dsa_secured.co_applicant_entity.addresses.pincode"),
                TestDataProvider.get("dsa_secured.co_applicant_entity.addresses.ownership")
        );

        // 5. Submit Form Configuration
        entityPage.submitForm();
        entityPage.clickSendEmail();
    }
}