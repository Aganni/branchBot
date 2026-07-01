package ui.stepDefinitions.dsa_secured;

import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.Dedupe_bureauPage;

public class Dedupe_bureauSteps extends BaseTest {

    @And("User navigates to Exposure dedupe and bureau output, downloads bureau report")
    public void processBureauAndDownload() {
        Dedupe_bureauPage page = new Dedupe_bureauPage(BaseTest.getPage());

        // 1. Move from Co-Applicant to Dedupe section
        page.moveToDedupeSection();

        // 2. Move from Dedupe to Bureau Output screen
        page.moveToBureauOutput();

        // 3. Download the bureau report
        page.downloadBureauReport();

        // 4. Navigate to Bank Statement and fill dates
        page.completeBankStatement("01/06/2025", "01/06/2026");

        // 5. Save and move to next section
        page.clickSaveAndNext();
    }
}
