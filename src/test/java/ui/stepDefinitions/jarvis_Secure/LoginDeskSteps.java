package ui.stepDefinitions.jarvis_Secure;
import dynamicData.DynamicDataClass;
import hooks.BaseTest;
import io.cucumber.java.en.When;
import ui.pages.jarvis_secured.LoginDeskPage;
public class LoginDeskSteps extends BaseTest {

    @When("User switches to Jarvis and moves the LAP application to CAM")
    public void switchToJarvisAndMoveToCam() throws Exception {
        // 1. Load Jarvis credentials and switch portal context (saves DSA state, creates new context, navigates to Jarvis)
        BaseTest.getCredentials("jarvis");
        BaseTest.switchToJarvisPortal();
        // 2. Login to Jarvis via Google SSO
        LoginDeskPage loginDeskPage = new LoginDeskPage(BaseTest.getPage());
        loginDeskPage.login();
        // 3. Navigate to Application tab and search by Partner LID
        loginDeskPage.navigateToApplicationTab();
        // Use partnerLoanId from DSA flow, or fallback to system property for standalone Jarvis runs
        // Run standalone with: mvn test -DpartnerLoanId=<ID_FROM_PREVIOUS_DSA_RUN>
        String partnerLoanId = DynamicDataClass.get().getPartnerLoanId();
        if (partnerLoanId == null || partnerLoanId.isEmpty()) {
            partnerLoanId = System.getProperty("partnerLoanId");
            if (partnerLoanId == null || partnerLoanId.isEmpty()) {
                throw new RuntimeException("Partner Loan ID is not available. Either run the full DSA flow first, " +
                        "or pass it via -DpartnerLoanId=<value>");
            }
            log.info("Using partnerLoanId from system property: {}", partnerLoanId);
            DynamicDataClass.get().setPartnerLoanId(partnerLoanId);
        }
        loginDeskPage.searchByPartnerLid(partnerLoanId);
        DynamicDataClass.setValue("appFormId", loginDeskPage.getAppFormIdFromFirstRow());
        log.info("AppForm ID: [{}]", DynamicDataClass.getValue("appFormId"));
        // 4. Open the application
        loginDeskPage.openFirstApplication();
        // 5. Reassign application to self
        String assigneeEmail = BaseTest.getUserEmail();
        loginDeskPage.reassignApplication("L4", assigneeEmail, "ReAssign_LAP");
        // 6. Move application to CAM
        loginDeskPage.moveToCam("Moving_AppFrom");

        log.info("LAP application successfully moved to CAM in Jarvis.");
    }
}
