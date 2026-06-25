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
        coAppPage.clickAddApplicant();
        Map<String, String> personalData = TestDataProvider.getMap("dsa_secured.salaried_coapplicant.personal");
        coAppPage.PersonalDetails(personalData);
        Map<String, String> addressData = TestDataProvider.getMap("dsa_secured.salaried_coapplicant.addresses");
        coAppPage.AddressDetails(addressData);
        Map<String, String> employmentData = TestDataProvider.getMap("dsa_secured.salaried_coapplicant.employment");
        coAppPage.EmploymentDetails(employmentData);
        coAppPage.clickSubmit();
    }
}
