package ui.stepDefinitions.dsa_secured_plp;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.Then;
import ui.pages.dsa_secured_plp.PartnerDetailsPage;

import java.util.LinkedHashMap;
import java.util.Map;

public class PartnerDetailsSteps extends BaseTest {

    @Then("User completes PLP Partner details")
    public void completePartnerDetails() {
        Map<String, String> details = new LinkedHashMap<>();

        // Dynamic lookups fetching values straight from normal.yaml
        details.put("partnerNameOptionIndex", TestDataProvider.get("dsa_secured_plp.partner_details.partnerNameOptionIndex"));
        details.put("program",                TestDataProvider.get("dsa_secured_plp.partner_details.program"));
        details.put("Scheme",                 TestDataProvider.get("dsa_secured_plp.partner_details.scheme"));
        details.put("subProduct",             TestDataProvider.get("dsa_secured_plp.partner_details.subProduct"));
        details.put("branch",                 TestDataProvider.get("dsa_secured_plp.partner_details.branch"));
        details.put("branchFullName",         TestDataProvider.get("dsa_secured_plp.partner_details.branchFullName"));
        details.put("sales Manager",          TestDataProvider.get("dsa_secured_plp.partner_details.sales_manager"));

        PartnerDetailsPage page = new PartnerDetailsPage(BaseTest.getPage());
        page.fillMandatoryDetails(details);
        page.clickSaveAndNext();
    }
}
