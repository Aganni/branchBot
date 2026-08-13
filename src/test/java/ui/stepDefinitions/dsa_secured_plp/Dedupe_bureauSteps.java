package ui.stepDefinitions.dsa_secured_plp;

import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured_plp.Dedupe_bureauPage;

public class Dedupe_bureauSteps extends BaseTest {

    @And("User navigates to PLP Exposure dedupe and bureau output, downloads bureau report")
    public void processBureauAndDownload() {
        Dedupe_bureauPage dedupePage = new Dedupe_bureauPage(BaseTest.getPage());

        // 1. Move from Co-Applicant to Dedupe section
        dedupePage.moveToDedupeSection();

        // 2. Move from Dedupe to Bureau Output screen
        dedupePage.moveToBureauOutput();

        // 3. Download the bureau report (handles popup)
        dedupePage.downloadBureauReport();

        // 4. Navigate to Bank Statement, select applicant, fill dates, upload files
        String[] bankFiles = {
                "src/test/resources/testdata/June2026.pdf",
                "src/test/resources/testdata/july2026.pdf"
        };
        dedupePage.completeBankStatement(
                "Noah johnson (Primary)",
                "01/06/2025",
                "01/06/2026",
                bankFiles
        );

        // 5. Save and move to next section
        dedupePage.clickSaveAndNext();
    }
}
