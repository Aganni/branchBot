package ui.stepDefinitions.jarvis_Secure;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.CAMStagePage;
import ui.pages.jarvis_secured.FinancialCoApplicantPage;
public class FinancialCoApplicantSteps extends BaseTest {

    @And("User completes financial co-applicant mandatory fields")
    public void completeFinancialCoApplicantFields() throws Exception {
        CAMStagePage camPage = new CAMStagePage(BaseTest.getPage());
        FinancialCoApplicantPage financialPage = new FinancialCoApplicantPage(BaseTest.getPage());
        // 1. Reload and open financial co-applicant
        camPage.reloadPage();
        financialPage.openCoApplicantDetails("Hannah");
        // 2. Submit the dialog to trigger mandatory field validation
        financialPage.submitDialog();
        // 3. Fill financial co-applicant mandatory fields (hardcoded values)
        financialPage.fillMandatoryFields(
                "7554",         // aadhaar last 4 digits
                "own",          // residential status
                "CAT A",        // employer category
                "1153",          // employee id
                "Plastic",      // industry sector
                "9036654355"    // office contact number
        );
        // 4. Submit financial co-applicant final details
        financialPage.submitFinalDetails();
        log.info("Financial co-applicant mandatory fields completed.");
    }
}
