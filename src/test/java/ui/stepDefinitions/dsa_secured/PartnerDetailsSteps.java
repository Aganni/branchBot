package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.Then;
import ui.pages.dsa_secured.PartnerDetailsPage;

import java.util.LinkedHashMap;
import java.util.Map;

public class PartnerDetailsSteps extends BaseTest {

    @Then("User completes LAP Partner details")
    public void completePartnerDetails() {
        Map<String, String> details = new LinkedHashMap<>();

        // Dynamic lookups fetching values straight from normal.yaml
        details.put("program",                TestDataProvider.get("dsa_secured.partner_details.program"));
        details.put("Scheme",                 TestDataProvider.get("dsa_secured.partner_details.scheme"));
        details.put("subProduct",             TestDataProvider.get("dsa_secured.partner_details.subProduct"));
        details.put("branch",                 TestDataProvider.get("dsa_secured.partner_details.branch"));
        details.put("branchFullName",         TestDataProvider.get("dsa_secured.partner_details.branchFullName"));
        details.put("sales Manager",          TestDataProvider.get("dsa_secured.partner_details.sales_manager"));

        PartnerDetailsPage page = new PartnerDetailsPage(BaseTest.getPage());
        page.fillMandatoryDetails(details);
        page.clickSaveAndNext();
    }
}
