package ui.stepDefinitions.dsa_secured_plp;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured_plp.CoAppSalariedPage;

public class CoAppSalariedSteps extends BaseTest {
    @And("User adds Salaried Co-Applicant details and submits PLP Appform")
    public void completeCoApplicantFlow() throws Exception {
        CoAppSalariedPage coApplicantPage = new CoAppSalariedPage(BaseTest.getPage());

        // 1. Process KYC layout
        coApplicantPage.clickAddApplicant();
        coApplicantPage.selectApplicantType(TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.applicant_type"));
        coApplicantPage.selectType(TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.type"));
        coApplicantPage.verifyPanNumber(TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.pan"));

        coApplicantPage.KYCDetails(
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.relationship"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.phone_number"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.email"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.gender"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.father_name"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.mother_name"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.category"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.religion"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.education"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.nationality"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.kyc.disability")
        );
        coApplicantPage.clickNext();

        coApplicantPage.AddressDetails(
                TestDataProvider.get("dsa_secured_plp.co_applicant.addresses.line1"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.addresses.ownership")
        );
        coApplicantPage.checkAddressConsents();
        coApplicantPage.clickNext();
        coApplicantPage.clickNext();

        coApplicantPage.EmploymentDetails(
                TestDataProvider.get("dsa_secured_plp.co_applicant.employment.type"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.employment.employer"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.employment.income"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.employment.office_email"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.employment.designation"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.employment.line1"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.employment.line2"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.employment.pincode"),
                TestDataProvider.get("dsa_secured_plp.co_applicant.employment.ownership")
        );
        coApplicantPage.submitForm();

        coApplicantPage.processOtpVerification(TestDataProvider.get("dsa_secured_plp.co_applicant.consent.otp"));
        //coApplicantPage.clickFinalNext();
    }
}
