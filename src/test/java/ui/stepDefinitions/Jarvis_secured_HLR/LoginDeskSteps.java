package ui.stepDefinitions.Jarvis_secured_HLR;

import data.TestDataProvider;
import dynamicData.DynamicDataClass;
import hooks.BaseTest;
import io.cucumber.java.en.When;
import ui.pages.jarvis_secured_HLR.LoginDeskPage;

public class LoginDeskSteps extends BaseTest {

    @When("User switches to Jarvis and moves the HLR application to CAM")
    public void switchToJarvisAndMoveToCam() throws Exception {
        // 1. Load Jarvis credentials and switch portal context
        BaseTest.getCredentials("jarvis");
        BaseTest.switchToJarvisPortal();

        // 2. Login to Jarvis via Google SSO
        LoginDeskPage loginDeskPage = new LoginDeskPage(BaseTest.getPage());
        loginDeskPage.login();

        // 3. Navigate to Application tab
        loginDeskPage.navigateToApplicationTab();

        // 3. Filter by Product = HLR and Status = Login_desk in Progress
        loginDeskPage.filterByProductAndStatus("HLR", "Login_desk in Progress");

        // 4. Copy App ID from first row (hover → copy button) and search by it
        String appFormId = loginDeskPage.copyAndSearchByAppId();
        DynamicDataClass.setValue("appFormId", appFormId);
        DynamicDataClass.get().setAppFormId(appFormId);
        log.info("Stored AppForm ID: {}", appFormId);

        // 5. Open the first application
        loginDeskPage.openFirstApplication();

        // Capture Partner Loan ID from the page (LPC code displayed below applicant name)
        try {
            String partnerLoanId = BaseTest.getPage().locator("text=/dsa-[a-f0-9\\-]+/i").first().textContent();
            if (partnerLoanId != null && !partnerLoanId.trim().isEmpty()) {
                DynamicDataClass.get().setPartnerLoanId(partnerLoanId.trim());
                DynamicDataClass.setValue("partnerLoanId", partnerLoanId.trim());
                log.info("Captured Partner Loan ID from page: {}", partnerLoanId.trim());
            }
        } catch (Exception e) {
            log.info("Could not capture Partner Loan ID from page: {}", e.getMessage());
            // Fallback: try LAPBEN pattern
            try {
                String partnerLoanId = BaseTest.getPage().locator("text=/LAP[A-Z]{3}[0-9]+/").first().textContent();
                if (partnerLoanId != null && !partnerLoanId.trim().isEmpty()) {
                    DynamicDataClass.get().setPartnerLoanId(partnerLoanId.trim());
                    DynamicDataClass.setValue("partnerLoanId", partnerLoanId.trim());
                    log.info("Captured Partner Loan ID (fallback) from page: {}", partnerLoanId.trim());
                }
            } catch (Exception ex) {
                log.info("Could not capture Partner Loan ID with fallback: {}", ex.getMessage());
            }
        }

        // Store PAN Card and Mobile Number from test data for summary
        try {
            String panCard = TestDataProvider.get("dsa_secured.primary_applicant.kyc.pan");
            DynamicDataClass.setValue("pan_card", panCard);
            log.info("Stored PAN Card: {}", panCard);
        } catch (Exception e) {
            log.info("Could not get PAN from test data: {}", e.getMessage());
        }

        try {
            String mobileNumber = TestDataProvider.get("dsa_secured.lead_details.phone_number");
            DynamicDataClass.setValue("mobile_number", mobileNumber);
            log.info("Stored Mobile Number: {}", mobileNumber);
        } catch (Exception e) {
            log.info("Could not get mobile number from test data: {}", e.getMessage());
        }

        // 6. Reassign application
        String assigneeEmail = BaseTest.getUserEmail();
        loginDeskPage.reassignApplication("L4", assigneeEmail, "ReAssign_HLR");

        // 7. Move application to CAM
        loginDeskPage.moveToCam("Moving_AppFrom");

        log.info("HLR application successfully moved to CAM in Jarvis.");
    }
}
