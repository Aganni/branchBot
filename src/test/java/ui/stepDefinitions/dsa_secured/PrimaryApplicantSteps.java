package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.PrimaryApplicantPage;

public class PrimaryApplicantSteps extends BaseTest {

    @And("User completes Primary Applicant details and proceeds")
    public void completePrimaryApplicantFlow() throws Exception {
        PrimaryApplicantPage applicantPage = new PrimaryApplicantPage(BaseTest.getPage());
        applicantPage.selectApplicantType("applicantType", "Individual");
        applicantPage.verifyPanNumber(TestDataProvider.get("dsa_secured.primary_applicant.kyc.pan"));
        applicantPage.verifyEmailAddress(TestDataProvider.get("dsa_secured.primary_applicant.kyc.email"));

        applicantPage.fillFamilyDetails(
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.father_name"),
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.mother_name"),
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.spouse_name")
        );
        applicantPage.selectBackgroundProfile(
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.category"),
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.religion"),
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.education"),
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.marital_status"),
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.nationality"),
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.disability")
        );
        applicantPage.selectInternalDeclarations(
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.preferred_address"),
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.related_interest"),
                TestDataProvider.get("dsa_secured.primary_applicant.kyc.related_party_control")
        );
        applicantPage.clickNext();

        // 2. Process Address.
        applicantPage.fillCurrentAddress(
                TestDataProvider.get("dsa_secured.primary_applicant.addresses.line1"),
                TestDataProvider.get("dsa_secured.primary_applicant.addresses.line2"),
                TestDataProvider.get("dsa_secured.primary_applicant.addresses.pincode"),
                TestDataProvider.get("dsa_secured.primary_applicant.addresses.ownership")
        );
        applicantPage.checkAddressConsents();
        applicantPage.clickNext();

        // 3. Process Financial Obligations
        applicantPage.addFinancialObligation(
                TestDataProvider.get("dsa_secured.primary_applicant.obligations.type"),
                TestDataProvider.get("dsa_secured.primary_applicant.obligations.financier"),
                TestDataProvider.get("dsa_secured.primary_applicant.obligations.emi")
        );
        applicantPage.clickNext();

        // 4. Process Corporate Employment Fields
        applicantPage.fillEmploymentProfile(
                TestDataProvider.get("dsa_secured.primary_applicant.employment.type"),
                TestDataProvider.get("dsa_secured.primary_applicant.employment.employer"),
                TestDataProvider.get("dsa_secured.primary_applicant.employment.income"),
                TestDataProvider.get("dsa_secured.primary_applicant.employment.office_email"),
                TestDataProvider.get("dsa_secured.primary_applicant.employment.designation")
        );

        applicantPage.fillOfficeAddress(
                TestDataProvider.get("dsa_secured.primary_applicant.employment.line1"),
                TestDataProvider.get("dsa_secured.primary_applicant.employment.line2"),
                TestDataProvider.get("dsa_secured.primary_applicant.employment.pincode"),
                TestDataProvider.get("dsa_secured.primary_applicant.employment.ownership")
        );
        applicantPage.clickNext();

        // 5. Process References Forms
        applicantPage.fillReference1(
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref1_name"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref1_phone"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref1_rel"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref1_line1"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref1_line2"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref1_pin"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref1_own")
        );
        applicantPage.fillReference2(
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref2_name"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref2_phone"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref2_rel"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref2_line1"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref2_line2"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref2_pin"),
                TestDataProvider.get("dsa_secured.primary_applicant.references.ref2_own")
        );
        applicantPage.clickNext();

        // 6. Process Bank Forms
        applicantPage.fillDisbursalBankDetails(
                TestDataProvider.get("dsa_secured.primary_applicant.bank.bank1_name"),
                TestDataProvider.get("dsa_secured.primary_applicant.bank.bank1_acc_name"),
                TestDataProvider.get("dsa_secured.primary_applicant.bank.bank1_acc_num"),
                TestDataProvider.get("dsa_secured.primary_applicant.bank.bank1_type"),
                TestDataProvider.get("dsa_secured.primary_applicant.bank.bank1_ifsc")
        );
        applicantPage.fillCollectionsBankDetails(
                TestDataProvider.get("dsa_secured.primary_applicant.bank.bank2_name"),
                TestDataProvider.get("dsa_secured.primary_applicant.bank.bank2_acc_name"),
                TestDataProvider.get("dsa_secured.primary_applicant.bank.bank2_acc_num"),
                TestDataProvider.get("dsa_secured.primary_applicant.bank.bank2_type"),
                TestDataProvider.get("dsa_secured.primary_applicant.bank.bank2_ifsc")
        );
        applicantPage.submitAndNavigateToCoApplicants();
    }
}