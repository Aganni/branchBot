package ui.stepDefinitions.Jarvis_secured_HLR;

import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured_HLR.CAMStagePage;
import ui.pages.jarvis_secured_HLR.EntityCoApplicantPage;

public class EntityCoApplicantSteps extends BaseTest {

    @And("HLR User completes entity co-applicant mandatory fields and moves to Credit Review")
    public void completeEntityCoApplicantAndMoveToCreditReview() throws Exception {
        CAMStagePage camPage = new CAMStagePage(BaseTest.getPage());
        EntityCoApplicantPage entityPage = new EntityCoApplicantPage(BaseTest.getPage());
        // 1. Reload and open entity co-applicant
        camPage.reloadPage();
        entityPage.openCoApplicantDetails("Amazon.com Inc.");
        // 2. Submit the dialog to trigger mandatory field validation
        entityPage.submitDialog();
        // 3. Click Arrow Right icon to navigate to the mandatory fields section
        entityPage.clickArrowRightIcon();
        // 4. Fill operational date (1991-06-04)
        entityPage.fillOperationalDate("1991", "Jun", "4");
        // 5. Fill entity shareholding
        entityPage.fillEntityShareholding("20");
        // 6. Submit entity co-applicant final details
        entityPage.submitFinalDetails();
        // 7. Reassign application to Tenjin before moving stage
        String assigneeEmail = BaseTest.getUserEmail();
        entityPage.reassignToTenjin("L4", assigneeEmail);
        // 8. Move application to Credit Review
        entityPage.moveToCreditReview();
        log.info("Entity co-applicant mandatory fields completed and moved to Credit Review.");
    }
}
