package ui.stepDefinitions.dsa_secured_HLR;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.CoAppPassportPage;

public class CoAppPassportSteps extends BaseTest {

    @And("HLR User adds Passport Co-Applicant details and submits Appform")
    public void completeCoAppPassportFlow() throws Exception {
        CoAppPassportPage passportPage = new CoAppPassportPage(BaseTest.getPage());

        // 1. Process Initial KYC Selection Form
        passportPage.clickAddApplicant();
        passportPage.selectApplicantType(TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.applicant_type"));
        passportPage.selectType(TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.type"));
        passportPage.checkFormTypeOption();

        // 2. Passport OVD Verification
        passportPage.PassportDetails(
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.ovd_type"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.dob"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.passport_file_number"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.passport_number"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.passport_expiry")
        );

        // 3.Demographics
        passportPage.KYCDetails(
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.relationship"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.phone_number"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.email"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.gender"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.father_name"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.mother_name"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.category"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.religion"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.education"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.marital_status"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.spouse_name"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.nationality")
        );

        passportPage.DisabilityDetails(
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.disability"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.disability_type"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.kyc.disability_percentage")
        );
        passportPage.clickNext();

        // 4.Address Validation
        passportPage.AddressDetails(
                TestDataProvider.get("dsa_secured.co_applicantpassport.addresses.line1"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.addresses.line2"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.addresses.pincode"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.addresses.ownership")
        );
        passportPage.AddressConsents();
        passportPage.clickNext();

        // 5.Financial Obligations Segment
        passportPage.FinancialObligation(
                TestDataProvider.get("dsa_secured.co_applicantpassport.obligations.type"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.obligations.financier"),
                TestDataProvider.get("dsa_secured.co_applicantpassport.obligations.emi")
        );

        // 6.Submission Entry
        passportPage.submitForm();
    }
}
