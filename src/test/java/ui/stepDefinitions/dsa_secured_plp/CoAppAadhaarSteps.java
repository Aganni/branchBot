package ui.stepDefinitions.dsa_secured_plp;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured_plp.CoAppAadhaarPage;

public class CoAppAadhaarSteps extends BaseTest {

    @And("User adds Aadhaar Co-Applicant details and submits PLP Appform")
    public void completeCoAppAadhaarFlow() throws Exception {
        CoAppAadhaarPage aadhaarPage = new CoAppAadhaarPage(BaseTest.getPage());

        // 1. Process Identification Layer
        aadhaarPage.clickAddApplicant();
        aadhaarPage.selectApplicantType(TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.applicant_type"));
        aadhaarPage.selectType(TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.type"));
        aadhaarPage.checkFormTypeOption();

        aadhaarPage.verifyAadhaarOvd(
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.ovd_type"),
                //TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.dob"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.aadhaar_last_4")
        );

        // 2. Populate Profile Information fields
        aadhaarPage.KYCDetails(
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.salutation"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.name"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.relationship"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.phone_number"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.email"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.gender"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.father_name"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.mother_name"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.category"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.religion"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.education"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.marital_status"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.nationality"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.kyc.disability")
        );
        aadhaarPage.clickNext();

        // 3. Add Address details
        aadhaarPage.AddressDetails(
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.addresses.line1"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.addresses.line2"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.addresses.pincode"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.addresses.ownership")
        );
        aadhaarPage.checkAddressConsents();
        aadhaarPage.clickNext();

        // 4. Process Financial Obligations Sub-form
        aadhaarPage.AddObligation(
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.obligations.type"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.obligations.financier"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.obligations.emi"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.obligations.account_number"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.obligations.outstanding"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.obligations.remaining_tenure"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.obligations.obligate"),
                TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.obligations.closure_type")
        );
        aadhaarPage.submitInitialForm();

        // 5. Authorize Signatures and Finalize Segment
        aadhaarPage.processOtpVerification(TestDataProvider.get("dsa_secured_plp.co_applicant_aadhaar.consent.otp"));
    }
}
