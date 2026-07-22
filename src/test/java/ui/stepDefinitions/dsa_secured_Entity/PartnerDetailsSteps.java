package ui.stepDefinitions.dsa_secured_Entity;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.Then;
import ui.pages.dsa_secured.PartnerDetailsPage;

import java.util.LinkedHashMap;
import java.util.Map;

public class PartnerDetailsSteps extends BaseTest {

    @Then("User completes LAP Entity Partner details")
    public void completePartnerDetailsEntity() {
        Map<String, String> details = new LinkedHashMap<>();

        details.put("partnerNameOptionIndex", TestDataProvider.get("dsa_secured.partner_details.partnerNameOptionIndex"));
        details.put("Scheme",                 TestDataProvider.get("dsa_secured.partner_details.scheme"));
        details.put("subProduct",       TestDataProvider.get("dsa_secured.partner_details.subProduct"));
        details.put("branch",                 TestDataProvider.get("dsa_secured.partner_details.branch"));
        details.put("sales Manager",          TestDataProvider.get("dsa_secured.partner_details.sales_manager"));

        PartnerDetailsPage page = new PartnerDetailsPage(BaseTest.getPage());
        page.fillMandatoryDetails(details);
        page.clickSaveAndNext();
    }
}
