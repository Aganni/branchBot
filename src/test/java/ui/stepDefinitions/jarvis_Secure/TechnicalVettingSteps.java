package ui.stepDefinitions.jarvis_Secure;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.AdminPortalPage;
import ui.pages.jarvis_secured.FiDashboardPage;
import ui.pages.jarvis_secured.TechnicalVettingPage;
import ui.pages.jarvis_secured.VendorPortalPage;

/**
 * Step definitions for the Technical Vetting flow.
 * Orchestrates two rounds of technical vetting across 4 portals:
 *   - Jarvis main app (TechnicalVettingPage)
 *   - Admin Portal (AdminPortalPage)
 *   - FI/FCU Dashboard popup (FiDashboardPage)
 *   - Vendor Portal (VendorPortalPage)
 *
 * Flow from codegen:
 *   1. Move to Credit Approval (initial) → Dismiss alert
 *   2. Re-Assign application
 *   3. Move to Credit Approval (with level/user) → "Technical vetting should be" error
 *   4. Trigger Verification → TECHNICAL tab → TRIGGER TECHNICAL → Go to Dashboard (popup)
 *   5. Admin Portal (new tab) → Vendor Admin Console → User tab → Assign TECH VENDOR → Update
 *   6. FI Dashboard: search → expand → open case → vendor config → Update Documents → Initiate TECHNICAL
 *   7. Vendor Portal (new tab): SSO login → select case → Move to WIP → Upload → Submit to KSF
 *   8. FI Dashboard: verify WIP → verify Report Received → Accept → Fill vetting form (Round 1: Poor) → Submit
 *   9. Jarvis: Close dialog → Move to Credit Approval again → error → Check Details → View TECHNICAL Details
 *  10. FI Dashboard Round 2: open case → Update Documents (single) → vendor → Initiate → Upload direct → Accept → Fill form (Round 2: Very good) → Submit
 *  11. Jarvis: Close dialog → Final Move to Credit Approval (should succeed)
 */
public class TechnicalVettingSteps extends BaseTest {

    private static final String TV = "dsa_secured.jarvis_secured.technical_vetting.";

    @And("User completes Technical Vetting for the application")
    public void completeTechnicalVetting() throws Exception {
        log.info("Starting Technical Vetting flow...");

        Page jarvisPage = BaseTest.getPage();
        BrowserContext context = jarvisPage.context();
        TechnicalVettingPage tvPage = new TechnicalVettingPage(jarvisPage);

        String appFormId;
        Object storedAppFormId = dynamicData.DynamicDataClass.getValue("appFormId");
        if (storedAppFormId != null) {
            appFormId = storedAppFormId.toString();
        } else {
            // Fallback: extract appFormId from current Jarvis URL
            // URL pattern: .../application/{appFormId}/appForm
            String currentUrl = jarvisPage.url();
            String[] parts = currentUrl.split("/application/");
            if (parts.length > 1) {
                appFormId = parts[1].split("/")[0];
            } else {
                throw new IllegalStateException("Cannot determine appFormId from DynamicDataClass or URL: " + currentUrl);
            }
            log.info("Extracted appFormId from URL: {}", appFormId);
            dynamicData.DynamicDataClass.setValue("appFormId", appFormId);
        }
        String level = TestDataProvider.get(TV + "level");
        String userEmail = TestDataProvider.get(TV + "user_email");
        String vendorUser = TestDataProvider.get(TV + "vendor_user");
        String vendorSearchText = TestDataProvider.get(TV + "vendor_search_text");
        String vendorName = TestDataProvider.get(TV + "vendor_name");
        String adminVendorSearch = TestDataProvider.get(TV + "admin_vendor_search");
        String adminVendorName = TestDataProvider.get(TV + "admin_vendor_name");
        String adminUserSearch = TestDataProvider.get(TV + "admin_user_search");
        String vendorPortalUrl = TestDataProvider.get(TV + "vendor_portal_url");
        String reportFilePath = TestDataProvider.get(TV + "report_file_path");
        String remarks = TestDataProvider.get(TV + "remarks");

        // Vetting form data - Round 1
        String propertyValueR1 = TestDataProvider.get(TV + "property_value_round1");
        String propertyVisitOption = TestDataProvider.get(TV + "property_visit_option");
        String observation = TestDataProvider.get(TV + "observation");
        String legalIssues = TestDataProvider.get(TV + "legal_issues");
        String marketabilityR1 = TestDataProvider.get(TV + "marketability_round1");
        String marketabilityReasonR1 = TestDataProvider.get(TV + "marketability_reason_round1");
        String titleClearanceR1 = TestDataProvider.get(TV + "title_clearance_round1");

        // Vetting form data - Round 2
        String propertyValueR2 = TestDataProvider.get(TV + "property_value_round2");
        String marketabilityR2 = TestDataProvider.get(TV + "marketability_round2");
        String marketabilityReasonR2 = TestDataProvider.get(TV + "marketability_reason_round2");
        String titleClearanceR2 = TestDataProvider.get(TV + "title_clearance_round2");

        // Tag people - Round 1
        String[] tagPeopleR1 = {
                TestDataProvider.get(TV + "tag_person_r1_1"),
                TestDataProvider.get(TV + "tag_person_r1_2"),
                TestDataProvider.get(TV + "tag_person_r1_3")
        };

        // Tag people - Round 2
        String[] tagPeopleR2 = {
                TestDataProvider.get(TV + "tag_person_r2_1"),
                TestDataProvider.get(TV + "tag_person_r2_2")
        };

        // Vendor user search text for Round 2
        String vendorUserSearchR2 = TestDataProvider.get(TV + "vendor_user_search_r2");

        // ═══════════════════════════════════════════════════════════════════════
        //  PHASE 1: Move to Credit Approval → Triggers Technical Vetting Error
        // ═══════════════════════════════════════════════════════════════════════
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  PHASE 1: Move to Credit Approval → Triggers Technical Vetting Error");
        log.info("═══════════════════════════════════════════════════════════════");

        // Attempt Move to Credit Approval (with level/user dialog) → triggers vetting error
        tvPage.attemptMoveToCreditApproval(level, userEmail);
        tvPage.verifyTechnicalVettingError();

        // ═══════════════════════════════════════════════════════════════════════
        //  PHASE 2: Trigger Verification → Open FI Dashboard
        // ═══════════════════════════════════════════════════════════════════════
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  PHASE 2: Trigger Verification → Open FI Dashboard");
        log.info("═══════════════════════════════════════════════════════════════");

        tvPage.clickTriggerVerification();
        tvPage.selectTechnicalAndTrigger();
        Page fiDashboardPopup = tvPage.openFiDashboard();
        FiDashboardPage fiPage = new FiDashboardPage(fiDashboardPopup);

        // ═══════════════════════════════════════════════════════════════════════
        //  PHASE 3: Admin Portal — Assign TECH VENDOR to user
        // ═══════════════════════════════════════════════════════════════════════
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  PHASE 3: Admin Portal — Assign TECH VENDOR to user");
        log.info("═══════════════════════════════════════════════════════════════");

        Page adminPage = context.newPage();
        AdminPortalPage adminPortalPage = new AdminPortalPage(adminPage);
        adminPortalPage.navigateToAdminPortal();
        adminPortalPage.openVendorAdminConsole();
        adminPortalPage.switchToUserTab();
        adminPortalPage.searchAndEditUser(adminUserSearch);
        adminPortalPage.assignVendorToUser(adminVendorSearch, adminVendorName);
        adminPortalPage.clickUpdate();
        log.info("Admin Portal vendor config complete. Closing tab.");
        adminPage.close();

        // ═══════════════════════════════════════════════════════════════════════
        //  PHASE 4: FI Dashboard — Configure Round 1 and Initiate
        // ═══════════════════════════════════════════════════════════════════════
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  PHASE 4: FI Dashboard — Configure Round 1 and Initiate");
        log.info("═══════════════════════════════════════════════════════════════");

        fiDashboardPopup.bringToFront();
        fiPage.searchByAppId(appFormId);
        fiPage.expandApplicationRecord();
        String caseId = fiPage.getCaseId();
        log.info("Captured Case ID for Vendor Portal: {}", caseId);
        fiPage.openCaseDetails(caseId.substring(0, 6));
        fiPage.selectVendorName(vendorSearchText, vendorName);
        fiPage.selectVendorUser(vendorUser);
        fiPage.tagPeople(tagPeopleR1);
        fiPage.fillRemarks(remarks);
        fiPage.updateDocuments();
        fiPage.initiateTechnical();

        // Verify Open status
        fiPage.refreshSearch();
        fiPage.expandApplicationRecord();
        fiPage.verifyStatusOpen();

        // ═══════════════════════════════════════════════════════════════════════
        //  PHASE 5: Vendor Portal — Login, Move to WIP, Upload, Submit
        // ═══════════════════════════════════════════════════════════════════════
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  PHASE 5: Vendor Portal — Login, Upload, Submit");
        log.info("═══════════════════════════════════════════════════════════════");

        Page vendorPage = context.newPage();
        VendorPortalPage vendorPortalPage = new VendorPortalPage(vendorPage);
        vendorPortalPage.navigateToVendorPortal(vendorPortalUrl);
        vendorPortalPage.loginViaInternalSSO();
        vendorPortalPage.selectCase(caseId);
        vendorPortalPage.moveToWip();

        // Verify Work in Progress on FI Dashboard
        fiDashboardPopup.bringToFront();
        fiPage.verifyStatusWorkInProgress();

        // Back to Vendor Portal — upload and submit
        vendorPage.bringToFront();
        vendorPortalPage.clickUploadFinalVerdict();
        vendorPortalPage.uploadReport(reportFilePath);
        vendorPortalPage.fillRemarks(remarks);
        vendorPortalPage.submitToKsf();
        log.info("Vendor Portal submission complete. Closing tab.");
        vendorPage.close();

        // ═══════════════════════════════════════════════════════════════════════
        //  PHASE 6: FI Dashboard — Accept Report, Fill Form (Round 1), Submit
        // ═══════════════════════════════════════════════════════════════════════
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  PHASE 6: FI Dashboard — Round 1 Vetting Form");
        log.info("═══════════════════════════════════════════════════════════════");

        fiDashboardPopup.bringToFront();
        fiPage.verifyStatusReportReceived();

        // Re-open case details to access Accept
        fiPage.refreshSearch();
        fiPage.expandApplicationRecord();
        fiPage.openCaseDetails(caseId.substring(0, 6));
        fiPage.acceptReport();
        fiPage.fillVettingForm(propertyValueR1, propertyVisitOption, observation, remarks,
                legalIssues, marketabilityR1, marketabilityReasonR1, titleClearanceR1, remarks);
        fiPage.submitVettingForm();
        fiPage.verifyVettingSaved();

        // Verify Closed
        fiPage.verifyStatusClosed();
        log.info("ROUND 1 COMPLETE — Technical vetting closed.");

        // ═══════════════════════════════════════════════════════════════════════
        //  PHASE 7: Jarvis — Close dialog, Attempt Move Again → Error → Round 2
        // ═══════════════════════════════════════════════════════════════════════
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  PHASE 7: Jarvis — Trigger Round 2");
        log.info("═══════════════════════════════════════════════════════════════");

        jarvisPage.bringToFront();
        jarvisPage.reload();
        jarvisPage.waitForLoadState(LoadState.NETWORKIDLE);
        jarvisPage.waitForTimeout(3000);
        tvPage.closeDialog();
        tvPage.attemptMoveToCreditApproval(level, userEmail);
        tvPage.verifyTechnicalVettingError();
        tvPage.clickCheckDetails();
        tvPage.retriggerSecondTechnical();

        // ═══════════════════════════════════════════════════════════════════════
        //  PHASE 8: FI Dashboard — Round 2 (Direct Upload, no Vendor Portal)
        // ═══════════════════════════════════════════════════════════════════════
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  PHASE 8: FI Dashboard — Round 2 Direct Upload");
        log.info("═══════════════════════════════════════════════════════════════");

        fiDashboardPopup.bringToFront();
        fiPage.refreshSearch();
        fiPage.expandApplicationRecord();
        // Round 2 — pick the "Not Initiated" case (not the Closed one from Round 1)
        String caseIdR2 = fiPage.getNotInitiatedCaseId();
        log.info("Captured Round 2 Case ID: {}", caseIdR2);
        fiPage.openCaseDetails(caseIdR2);

        // Round 2: Update Documents (single) → vendor config → Initiate → Upload
        fiPage.updateDocumentsSingle();
        fiPage.selectVendorName(vendorSearchText, vendorName);
        fiPage.selectVendorUserWithSearch(vendorUserSearchR2, vendorUser);
        fiPage.tagPeople(tagPeopleR2);
        fiPage.fillRemarks(remarks);
        fiPage.initiateTechnical();

        // Direct upload (no Vendor Portal needed — Upload Report appears after Initiate)
        fiPage.uploadReportDirect(reportFilePath);

        // Accept and fill Round 2 form
        fiPage.verifyStatusReportReceived();
        fiPage.acceptReport();
        fiPage.fillVettingFormRound2(propertyValueR2, propertyVisitOption, observation, remarks,
                legalIssues, marketabilityR2, marketabilityReasonR2, titleClearanceR2, remarks);
        fiPage.submitVettingForm();
        fiPage.verifyVettingSaved();

        // Verify Vetting Clear + Closed
        fiPage.verifyVettingClear();
        fiPage.expandApplicationRecord();
        fiDashboardPopup.getByText("Closed").nth(1).click();
        fiDashboardPopup.waitForTimeout(1000);
        log.info("ROUND 2 COMPLETE — Technical vetting closed.");

        // Close FI Dashboard popup
        fiDashboardPopup.close();

        // ═══════════════════════════════════════════════════════════════════════
        //  PHASE 9: Final Move to Credit Approval (should succeed now)
        // ═══════════════════════════════════════════════════════════════════════
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  PHASE 9: Final Move to Credit Approval");
        log.info("═══════════════════════════════════════════════════════════════");

        jarvisPage.bringToFront();
        tvPage.closeDialog();
        tvPage.finalMoveToCreditApproval(level, userEmail);
        log.info("Technical Vetting flow completed. Application moved to Credit Approval.");
    }
}
