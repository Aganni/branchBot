package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.PrimaryApplicantPage;
import java.util.Map;

public class PrimaryApplicantSteps extends BaseTest {

    @And("User completes Primary Applicant details and proceeds")
    public void completePrimaryApplicantFlow() {
        PrimaryApplicantPage applicantPage = new PrimaryApplicantPage(BaseTest.getPage());

        // 1. Process KYC Details
        Map<String, String> kycData = TestDataProvider.getMap("dsa_secured.primary_applicant.kyc");
        applicantPage.completeKycTab(kycData);

        // 2. Process Address Configurations
        Map<String, String> addressData = TestDataProvider.getMap("dsa_secured.primary_applicant.addresses");
        applicantPage.completeAddressesTab(addressData);

        // 3. Process Financial Obligations
        Map<String, String> obligationData = TestDataProvider.getMap("dsa_secured.primary_applicant.obligations");
        applicantPage.completeObligationsTab(obligationData);

        // 4. Process Corporate Employment Fields
        Map<String, String> employmentData = TestDataProvider.getMap("dsa_secured.primary_applicant.employment");
        applicantPage.completeEmploymentTab(employmentData);

        // 5. Process References Forms
        Map<String, String> referenceData = TestDataProvider.getMap("dsa_secured.primary_applicant.references");
        applicantPage.completeReferencesTab(referenceData);

        // 6. Finalize Financial Profile and Submit
        Map<String, String> bankData = TestDataProvider.getMap("dsa_secured.primary_applicant.bank");
        applicantPage.completeBankDetailsTab(bankData);
    }
}
