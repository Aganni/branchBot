package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.SalariedCoappPage;
import java.util.Map;

public class SalariedCoappSteps extends BaseTest {

    @And("User completes Salaried Co-Applicant details and proceeds")
    public void completeSalariedCoApplicantFlow() {
        SalariedCoappPage coAppPage = new SalariedCoappPage(BaseTest.getPage());

        // 1. Navigate to Co-Applicants section and open new applicant form
        coAppPage.clickAddApplicant();

        // 2. KYC / Personal details
        Map<String, String> personalData = TestDataProvider.getMap("dsa_secured.salaried_coapplicant.personal");
        coAppPage.fillPersonalDetails(personalData);

        // 3. Address details
        Map<String, String> addressData = TestDataProvider.getMap("dsa_secured.salaried_coapplicant.addresses");
        coAppPage.fillAddressDetails(addressData);

        // 4. Employment details
        Map<String, String> employmentData = TestDataProvider.getMap("dsa_secured.salaried_coapplicant.employment");
        coAppPage.fillEmploymentDetails(employmentData);

        // 5. Submit
        coAppPage.clickSubmit();
    }
}
