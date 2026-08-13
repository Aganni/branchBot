package ui.stepDefinitions.jarvis_Secure;

import hooks.BaseTest;
import io.cucumber.java.en.When;
import ui.pages.jarvis_secured.SalesGatingPage;

import static dynamicData.DynamicDataClass.getValue;
import static dynamicData.DynamicDataClass.setValue;

/**
 * Step definitions for the Sales Gating flow.
 * Calls Shield API to fetch linkedIndividuals, then Deathlok addData
 * to mark sales gating as COMPLETED for each applicant.
 */
public class SalesGatingSteps extends BaseTest {

    @When("User completes Sales Gating for app {string}")
    public void completeSalesGatingForApp(String appFormId) {
        log.info("Starting Sales Gating for appFormId: {}", appFormId);
        setValue("appFormId", appFormId);

        SalesGatingPage salesGatingPage = new SalesGatingPage(appFormId);
        salesGatingPage.completeSalesGatingForAllApplicants();

        log.info("Sales Gating completed for appFormId: {}", appFormId);
    }

    /**
     * Uses the appFormId already stored in TestSessionData (set during DSA intake flow).
     */
    @When("User completes Sales Gating for the application")
    public void completeSalesGatingForCurrentApp() {
        String appFormId = getValue("appFormId").toString();
        log.info("Starting Sales Gating for current appFormId: {}", appFormId);

        SalesGatingPage salesGatingPage = new SalesGatingPage(appFormId);
        salesGatingPage.completeSalesGatingForAllApplicants();

        log.info("Sales Gating completed for appFormId: {}", appFormId);
    }
}
