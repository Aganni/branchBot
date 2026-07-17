package ui.stepDefinitions.jarvis_Secure;

import dynamicData.DynamicDataClass;
import hooks.BaseTest;
import io.cucumber.java.en.When;
import ui.pages.jarvis_secured.LoginDeskPage;

public class LoginDeskSteps extends BaseTest {

    @When("User switches to Jarvis and moves the LAP application to CAM")
    public void switchToJarvisAndMoveToCam() throws Exception {
        // 1. Load Jarvis credentials and switch portal context
        BaseTest.getCredentials("jarvis");
        BaseTest.switchToJarvisPortal();

        // 2. Login to Jarvis via Google SSO
        LoginDeskPage loginDeskPage = new LoginDeskPage(BaseTest.getPage());
        loginDeskPage.login();

        // 3. Navigate to Application tab
        loginDeskPage.navigateToApplicationTab();

        // 3. Filter by Product = LAP and Status = Login_desk in Progress
        loginDeskPage.filterByProductAndStatus("LAP", "Login_desk in Progress");

        // 4. Search by Individual Applicant name
        loginDeskPage.searchByIndividualApplicant("Noah johnson");

        // 5. Open the first application
        loginDeskPage.openFirstApplication();

        // Capture application identifiers from the URL and page for test summary
        String currentUrl = BaseTest.getPage().url();
        if (currentUrl.contains("/application/")) {
            String appFormId = currentUrl.replaceAll(".*/application/([^/]+).*", "$1");
            DynamicDataClass.setValue("appFormId", appFormId);
            DynamicDataClass.get().setAppFormId(appFormId);
            log.info("Captured AppForm ID from URL: {}", appFormId);
        }

        // Capture Partner Loan ID from the page (displayed below applicant name)
        try {
            String partnerLoanId = BaseTest.getPage().locator("text=/LAP[A-Z0-9]+/").first().textContent();
            if (partnerLoanId != null && !partnerLoanId.trim().isEmpty()) {
                DynamicDataClass.get().setPartnerLoanId(partnerLoanId.trim());
                DynamicDataClass.setValue("partnerLoanId", partnerLoanId.trim());
                log.info("Captured Partner Loan ID from page: {}", partnerLoanId.trim());
            }
        } catch (Exception e) {
            log.info("Could not capture Partner Loan ID from page: {}", e.getMessage());
            // Fallback to system property
            String partnerLoanId = System.getProperty("partnerLoanId");
            if (partnerLoanId != null && !partnerLoanId.isEmpty()) {
                DynamicDataClass.get().setPartnerLoanId(partnerLoanId);
                DynamicDataClass.setValue("partnerLoanId", partnerLoanId);
            }
        }

        // 5. Reassign application
        String assigneeEmail = BaseTest.getUserEmail();
        loginDeskPage.reassignApplication("L4", assigneeEmail, "ReAssign_LAP");

        // 6. Move application to CAM
        loginDeskPage.moveToCam("Moving_AppFrom");

        log.info("LAP application successfully moved to CAM in Jarvis.");
    }
}
