package ui.stepDefinitions.Jarvis_secured_HLR;

import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured_HLR.ProgramValidationPage;

public class ProgramValidationSteps extends BaseTest {

    @And("HLR User completes program validation and moves to Credit Review")
    public void completeProgramValidationAndMoveToCreditReview() throws Exception {
        ProgramValidationPage programPage = new ProgramValidationPage(BaseTest.getPage());

        // 1. Open Partner Details for editing
        programPage.openPartnerDetailsForEdit();

        // 2. Select program
        programPage.selectProgram("Average Banking Program");

        // 3. Submit Partner Details
        programPage.submitPartnerDetails();

        // 4. Collapse Partner Details section
        programPage.collapsePartnerDetails();

        // 5. Move to Credit Review
        programPage.moveToCreditReview();

        log.info("Program validation completed and moved to Credit Review.");
    }
}
