package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.CoAppDLPage;

public class CoAppDLSteps extends BaseTest {

    @And("User adds Driving License Co-Applicant details and submits Appform")
    public void completeCoAppDlFlow() throws Exception {
        CoAppDLPage dlPage = new CoAppDLPage(BaseTest.getPage());

        // 1. Process KYC / Identification Tab
        dlPage.clickAddApplicant();
        dlPage.selectApplicantType(TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.applicant_type"));
        dlPage.selectType(TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.type"));
        dlPage.checkFormTypeOption();

        dlPage.verifyDrivingLicense(
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.ovd_type"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.dob"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.dl_number"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.dl_expiry")
        );

        dlPage.fillKYCDetails(
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.relationship"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.phone_number"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.email"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.gender"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.father_name"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.mother_name"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.category"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.religion"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.education"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.marital_status"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.nationality"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.kyc.disability")
        );
        dlPage.clickNext();

        // 2. Process Address Tab
        dlPage.fillAddressDetails(
                TestDataProvider.get("dsa_secured.co_applicant_dl.addresses.line1"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.addresses.line2"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.addresses.pincode"),
                TestDataProvider.get("dsa_secured.co_applicant_dl.addresses.ownership")
        );
        dlPage.checkAddressConsents();
        dlPage.clickNext();

        // 3. Complete Submission and Validate via Otp Panel
        dlPage.submitInitialForm();
        dlPage.processOtpVerification(TestDataProvider.get("dsa_secured.co_applicant_dl.consent.otp"));
        dlPage.clickFinalNext();
    }
}
