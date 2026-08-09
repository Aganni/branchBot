package ui.stepDefinitions.jarvis_Secure;

import com.microsoft.playwright.Page;
import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import ui.pages.jarvis_secured.LoginDeskPage;
import ui.pages.jarvis_secured.PdVisitPage;
import ui.pages.jarvis_secured.TelePdPage;

public class TelePdStandaloneSteps extends BaseTest {

    private static final String TP = "dsa_secured.jarvis_secured.tele_pd.";
    private static final String PV = "dsa_secured.jarvis_secured.pd_visit.";
    private TelePdPage telePdPage;
    private String currentAppFormId;

    @When("User completes standalone Tele PD for app {string}")
    public void completeStandaloneTelePd(String appFormId) throws Exception {
        this.currentAppFormId = appFormId;

        // Open BlackPanther in a new tab
        telePdPage = new TelePdPage(BaseTest.getPage());
        String blackPantherUrl = BaseTest.initializeEnvironment("blackPantherUrl");
        telePdPage.navigateToBlackPanther(blackPantherUrl);
        telePdPage.loginViaGoogleSSO();

        // Search and open the application
        telePdPage.searchByAppId(appFormId);
        telePdPage.openApplicationFromResults("Noah johns");
        telePdPage.navigateToPdAndPropertyVisit();

        // Complete Tele PD for all 3 applicants
        completeTelePdForApplicant(telePdPage, "Hannah Isaac", "Hannah Isaac", false);
        completeTelePdForApplicant(telePdPage, "Noah johnson", "Noah johnson", false);
        completeTelePdForApplicant(telePdPage, "Amazon.com Inc.", "Amazon.com Inc.", true);
        log.info("Standalone Tele PD completed for all applicants.");
    }

    @And("User completes standalone PD Visit for the application")
    public void completeStandalonePdVisit() {
        // PD Visit uses the same BlackPanther page that was opened during Tele PD
        Page blackPantherPage = telePdPage.getBlackPantherPage();
        blackPantherPage.bringToFront();

        PdVisitPage pdVisitPage = new PdVisitPage(blackPantherPage);
        pdVisitPage.clickPdVisitTab();

        // Complete PD Visit for all 3 applicants (BlackPanther only, no Jarvis)
        completePdVisitForApplicant(pdVisitPage, "Noah johnson", "Noah johnson", false);
        completePdVisitForApplicant(pdVisitPage, "Hannah Isaac", "Hannah Isaac", false);
        completePdVisitForApplicant(pdVisitPage, "Amazon.com Inc.", "Amazon.com Inc.", true);
        log.info("Standalone PD Visit completed for all applicants in BlackPanther.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  FULL PER-APPLICANT FLOW: BlackPanther → Jarvis → BlackPanther verify
    // ═══════════════════════════════════════════════════════════════════════════

    @And("User completes PD Visit per-applicant with Jarvis uploads")
    public void completePdVisitPerApplicantWithJarvisUploads() throws Exception {
        log.info("Starting full per-applicant PD Visit flow (BlackPanther → Jarvis → BlackPanther verify)...");

        Page blackPantherPage = telePdPage.getBlackPantherPage();
        PdVisitPage pdVisitPage = new PdVisitPage(blackPantherPage);

        String[] imagePaths = {
                TestDataProvider.get(PV + "image_path_1"),
                TestDataProvider.get(PV + "image_path_2"),
                TestDataProvider.get(PV + "image_path_3"),
                TestDataProvider.get(PV + "image_path_4")
        };

        // ── STEP 1 for first applicant: Fill PD Visit in BlackPanther FIRST ──
        log.info("[Applicant 1] Filling PD Visit details in BlackPanther for: Noah johnson");
        blackPantherPage.bringToFront();
        blackPantherPage.waitForTimeout(1000);
        pdVisitPage.setPage(blackPantherPage);
        pdVisitPage.clickPdVisitTab();
        completePdVisitForApplicant(pdVisitPage, "Noah johnson", "Noah johnson", false);
        log.info("[Applicant 1] PD Visit details filled for: Noah johnson");

        // ── NOW open Jarvis (after first PD Visit is filled) ──
        log.info("Opening Jarvis for Physical PD uploads...");
        BaseTest.getCredentials("jarvis");
        BaseTest.switchToJarvisPortal();
        LoginDeskPage loginDeskPage = new LoginDeskPage(BaseTest.getPage());
        loginDeskPage.login();
        loginDeskPage.navigateToApplicationTab();

        String appFormId = currentAppFormId;
        loginDeskPage.searchByAppId(appFormId);
        loginDeskPage.openFirstApplication();
        Page jarvisPage = BaseTest.getPage();
        log.info("Jarvis application opened for PD uploads.");

        // ── STEP 2 for first applicant: Upload images in Jarvis ──
        log.info("[Applicant 1] Uploading images in Jarvis for: Noah johnson");
        pdVisitPage.uploadPhysicalPdImages(jarvisPage, "Noah johnson", imagePaths);
        log.info("[Applicant 1] Images uploaded for: Noah johnson");

        // ── STEP 3 for first applicant: Verify status in BlackPanther ──
        log.info("[Applicant 1] Verifying PD status in BlackPanther for: Noah johnson");
        blackPantherPage.bringToFront();
        blackPantherPage.reload();
        blackPantherPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        blackPantherPage.waitForTimeout(3000);
        pdVisitPage.setPage(blackPantherPage);
        pdVisitPage.clickPdVisitTab();
        pdVisitPage.verifyApplicantStatus("Noah johnson", "Completed");
        log.info("[Applicant 1] DONE — Noah johnson completed.");

        // ── APPLICANT 2: Hannah Isaac (Individual) ──
        processApplicantFullFlow(pdVisitPage, blackPantherPage, jarvisPage,
                "Hannah Isaac", "Hannah Isaac", false, imagePaths);

        // ── APPLICANT 3: Amazon.com Inc. (Entity) ──
        processApplicantFullFlow(pdVisitPage, blackPantherPage, jarvisPage,
                "Amazon.com Inc.", "Amazon.com Inc.", true, imagePaths);

        log.info("Full per-applicant PD Visit flow completed for all applicants.");
    }

    /**
     * Processes a single applicant through the complete cycle:
     *   1. BlackPanther: Fill PD Visit form
     *   2. Jarvis: Upload 4 Photographs + 4 Business Photographs
     *   3. BlackPanther: Verify status = Completed
     */
    private void processApplicantFullFlow(PdVisitPage pdVisitPage, Page blackPantherPage,
                                          Page jarvisPage, String pendingName,
                                          String inProgressName, boolean isEntity,
                                          String[] imagePaths) {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  PROCESSING APPLICANT: {}", pendingName);
        log.info("═══════════════════════════════════════════════════════════════");

        // ── STEP 1: BlackPanther — Fill PD Visit details ──
        log.info("[Step 1/3] Switching to BlackPanther for PD Visit data entry: {}", pendingName);
        blackPantherPage.bringToFront();
        blackPantherPage.waitForTimeout(1000);
        pdVisitPage.setPage(blackPantherPage);
        pdVisitPage.clickPdVisitTab();
        completePdVisitForApplicant(pdVisitPage, pendingName, inProgressName, isEntity);
        log.info("[Step 1/3] DONE — PD Visit details filled for: {}", pendingName);

        // ── STEP 2: Jarvis — Upload Photographs + Business Photographs ──
        log.info("[Step 2/3] Switching to Jarvis for image uploads: {}", pendingName);
        jarvisPage.bringToFront();
        jarvisPage.reload();
        jarvisPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        jarvisPage.waitForTimeout(3000);
        pdVisitPage.uploadPhysicalPdImages(jarvisPage, pendingName, imagePaths);
        log.info("[Step 2/3] DONE — Images uploaded in Jarvis for: {}", pendingName);

        // ── STEP 3: BlackPanther — Verify PD Visit status = Completed ──
        log.info("[Step 3/3] Switching to BlackPanther to verify status: {}", pendingName);
        blackPantherPage.bringToFront();
        blackPantherPage.reload();
        blackPantherPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        blackPantherPage.waitForTimeout(3000);
        pdVisitPage.setPage(blackPantherPage);
        pdVisitPage.clickPdVisitTab();
        pdVisitPage.verifyApplicantStatus(pendingName, "Completed");
        log.info("[Step 3/3] DONE — Status verified for: {}", pendingName);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  JARVIS - STANDALONE PHYSICAL PD UPLOAD
    // ═══════════════════════════════════════════════════════════════════════════

    @When("User opens Jarvis and navigates to application {string}")
    public void openJarvisAndNavigateToApplication(String appFormId) throws Exception {
        log.info("Opening Jarvis and navigating to application: {}", appFormId);

        // Load Jarvis credentials and switch portal context
        BaseTest.getCredentials("jarvis");
        BaseTest.switchToJarvisPortal();

        // Login to Jarvis via Google SSO
        LoginDeskPage loginDeskPage = new LoginDeskPage(BaseTest.getPage());
        loginDeskPage.login();

        // Navigate to Application tab and search by App ID
        loginDeskPage.navigateToApplicationTab();
        loginDeskPage.searchByAppId(appFormId);
        loginDeskPage.openFirstApplication();

        log.info("Jarvis application opened for: {}", appFormId);
    }

    @And("User uploads Physical PD images for all applicants on Jarvis")
    public void uploadPhysicalPdImagesForAllApplicants() {
        log.info("Starting Physical PD image uploads on Jarvis...");
        Page jarvisPage = BaseTest.getPage();

        String[] imagePaths = {
                TestDataProvider.get(PV + "image_path_1"),
                TestDataProvider.get(PV + "image_path_2"),
                TestDataProvider.get(PV + "image_path_3"),
                TestDataProvider.get(PV + "image_path_4")
        };

        PdVisitPage pdVisitPage = new PdVisitPage(jarvisPage);
        pdVisitPage.uploadPhysicalPdImages(jarvisPage, "Noah johnson", imagePaths);
        pdVisitPage.uploadPhysicalPdImages(jarvisPage, "Hannah Isaac", imagePaths);
        pdVisitPage.uploadPhysicalPdImages(jarvisPage, "Amazon.com Inc.", imagePaths);

        log.info("Physical PD images uploaded for all applicants on Jarvis.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PD VISIT - PER APPLICANT
    // ═══════════════════════════════════════════════════════════════════════════

    private void completePdVisitForApplicant(PdVisitPage pdPage, String pendingName, String inProgressName, boolean isEntity) {
        log.info("Starting PD Visit for: {}", pendingName);

        pdPage.selectApplicantFromCombo(pendingName);

        // Basic Details
        pdPage.fillBasicDetails(
                TestDataProvider.get(PV + "person_met_name"),
                TestDataProvider.get(PV + "person_met_contact"),
                TestDataProvider.get(PV + "person_met_relationship"),
                isEntity);

        // End Use Of Fund
        pdPage.fillEndUseOfFund(TestDataProvider.get(PV + "end_use_purpose"));

        if (isEntity) {
            // ── ENTITY-SPECIFIC FLOW ──
            // Business Details (replaces Customer Details + Work Profile for entities)
            pdPage.fillBusinessDetails(
                    TestDataProvider.get(PV + "firm_name"),
                    TestDataProvider.get(PV + "customer_business_profile"),
                    TestDataProvider.get(PV + "business_vintage"),
                    TestDataProvider.get(PV + "business_started_by"),
                    TestDataProvider.get(PV + "years_business_running"),
                    TestDataProvider.get(PV + "employees_seen_during_pd"),
                    TestDataProvider.get(PV + "area_of_space"),
                    TestDataProvider.get(PV + "office_structure"),
                    TestDataProvider.get(PV + "inventory_value"),
                    TestDataProvider.get(PV + "electricity_bill_amount"),
                    TestDataProvider.get(PV + "rent_amount"));

            // Customer Profile
            pdPage.fillCustomerProfile(TestDataProvider.get(PV + "customer_profile"));

            // Business Profile (entity only)
            pdPage.fillBusinessProfile(TestDataProvider.get(PV + "business_profile"));

            // CIBIL Obligations
            pdPage.fillCibilObligations(TestDataProvider.get(PV + "cibil_obligations"));

            // CRIF Obligations
            pdPage.fillCrifObligations(TestDataProvider.get(PV + "crif_obligations"));

            // Conditions
            pdPage.fillConditions(TestDataProvider.get(PV + "conditions"));

            // Risk / Weakness
            pdPage.fillRiskWeakness(TestDataProvider.get(PV + "risk_weakness"));

            // Open Customer Summary section (don't add yet)
            pdPage.clickSectionButton("Customer Summary");

            // Save first
            pdPage.clickSave();

            // Re-select applicant (now IN_PROGRESS)
            pdPage.clickPdVisitTab();
            pdPage.selectApplicantFromCombo(inProgressName);

            // Customer Summary (add after re-select)
            pdPage.addCustomerSummary(
                    TestDataProvider.get(PV + "summary_name"),
                    TestDataProvider.get(PV + "summary_address"),
                    TestDataProvider.get(PV + "summary_contact"),
                    TestDataProvider.get(PV + "summary_age"),
                    TestDataProvider.get(PV + "summary_cibil_score"),
                    TestDataProvider.get(PV + "summary_crif_score"),
                    TestDataProvider.get(PV + "summary_property_owner"),
                    TestDataProvider.get(PV + "summary_income_considered"));

            // Banking Details
            pdPage.addBankingDetails(
                    TestDataProvider.get(PV + "bank_holder_name"),
                    TestDataProvider.get(PV + "bank_name"),
                    TestDataProvider.get(PV + "bank_account_type"),
                    TestDataProvider.get(PV + "bank_account_number"),
                    TestDataProvider.get(PV + "bank_vintage"),
                    TestDataProvider.get(PV + "bank_avg_balance"));

            // Asset / Investment Details
            pdPage.addAssetInvestment(
                    TestDataProvider.get(PV + "asset_type"),
                    TestDataProvider.get(PV + "asset_ownership"),
                    TestDataProvider.get(PV + "asset_type_of_assets"),
                    TestDataProvider.get(PV + "asset_number_of_assets"),
                    TestDataProvider.get(PV + "asset_value"));

            // PD Done By (entity flow)
            pdPage.selectPdDoneByEntity(
                    TestDataProvider.get(PV + "pd_done_by_remark"),
                    TestDataProvider.get(PV + "pd_done_by_search"),
                    TestDataProvider.get(PV + "pd_done_by_user"),
                    TestDataProvider.get(PV + "pd_done_by_second_user"));

            // Physical PD Status
            pdPage.selectPhysicalPdStatus(TestDataProvider.get(PV + "physical_pd_status"));

            // Submit
            pdPage.clickSubmit();

        } else {
            // ── INDIVIDUAL APPLICANT FLOW ──
            // Customer Details
            pdPage.fillCustomerDetails(
                    TestDataProvider.get(PV + "no_of_dependents"),
                    TestDataProvider.get(PV + "stability_of_residence"),
                    TestDataProvider.get(PV + "no_of_family_members"),
                    TestDataProvider.get(PV + "email"),
                    TestDataProvider.get(PV + "qualification"),
                    TestDataProvider.get(PV + "earning_family_members"));

            // Work Profile (first part - open section)
            pdPage.clickSectionButton("Work Profile");

            // Save first
            pdPage.clickSave();

            // Re-select applicant (now IN_PROGRESS)
            pdPage.clickPdVisitTab();
            pdPage.selectApplicantFromCombo(inProgressName);

            // Work Profile (second part - after save/reload)
            pdPage.fillWorkProfile(
                    TestDataProvider.get(PV + "years_in_current"),
                    TestDataProvider.get(PV + "no_of_employees"),
                    TestDataProvider.get(PV + "nature_of_business"),
                    TestDataProvider.get(PV + "overall_experience"));

            // Customer Profile
            pdPage.fillCustomerProfile(TestDataProvider.get(PV + "customer_profile"));

            // CIBIL Obligations
            pdPage.fillCibilObligations(TestDataProvider.get(PV + "cibil_obligations"));

            // CRIF Obligations
            pdPage.fillCrifObligations(TestDataProvider.get(PV + "crif_obligations"));

            // Conditions
            pdPage.fillConditions(TestDataProvider.get(PV + "conditions"));

            // Risk / Weakness
            pdPage.fillRiskWeakness(TestDataProvider.get(PV + "risk_weakness"));

            // Customer Summary
            pdPage.addCustomerSummary(
                    TestDataProvider.get(PV + "summary_name"),
                    TestDataProvider.get(PV + "summary_address"),
                    TestDataProvider.get(PV + "summary_contact"),
                    TestDataProvider.get(PV + "summary_age"),
                    TestDataProvider.get(PV + "summary_cibil_score"),
                    TestDataProvider.get(PV + "summary_crif_score"),
                    TestDataProvider.get(PV + "summary_property_owner"),
                    TestDataProvider.get(PV + "summary_income_considered"));

            // PD Done By + Remarks
            pdPage.selectPdDoneBy(TestDataProvider.get(PV + "pd_done_by_remark"));

            // Save
            pdPage.clickSave();

            // Asset / Investment Details (applicant remains selected after save)
            pdPage.addAssetInvestment(
                    TestDataProvider.get(PV + "asset_type"),
                    TestDataProvider.get(PV + "asset_ownership"),
                    TestDataProvider.get(PV + "asset_type_of_assets"),
                    TestDataProvider.get(PV + "asset_number_of_assets"),
                    TestDataProvider.get(PV + "asset_value"));

            // Banking Details
            pdPage.addBankingDetails(
                    TestDataProvider.get(PV + "bank_holder_name"),
                    TestDataProvider.get(PV + "bank_name"),
                    TestDataProvider.get(PV + "bank_account_type"),
                    TestDataProvider.get(PV + "bank_account_number"),
                    TestDataProvider.get(PV + "bank_vintage"),
                    TestDataProvider.get(PV + "bank_avg_balance"));

            // Save
            pdPage.clickSave();
        }

        log.info("PD Visit completed for: {}", pendingName);
    }

    private void completeTelePdForApplicant(TelePdPage pdPage, String pendingName, String inProgressName, boolean isEntity) {
        pdPage.selectApplicantFromCombo(pendingName);

        fillBasicDetails(pdPage);
        fillEmploymentDetails(pdPage, isEntity);
        fillIncomeDetails(pdPage);
        fillBureauDetails(pdPage);
        fillPropertyAndCollateralDetails(pdPage);
        fillTelePdStatusAndDoneBy(pdPage);
        pdPage.clickSave();

        // Reload to refresh applicant status from PENDING -> IN_PROGRESS
        pdPage.reloadPage();

        // Re-select IN_PROGRESS -> fill Schedule -> Second Save
        pdPage.selectApplicantFromCombo(inProgressName);
        pdPage.fillScheduleDateTime(TestDataProvider.get(TP + "schedule_datetime"));
        pdPage.clickSave();

        // Fill Additional fields -> Submit
        pdPage.selectDropdownByLabelAndText("Additional Collateral/Address",
                TestDataProvider.get(TP + "additional_collateral"),
                TestDataProvider.get(TP + "additional_collateral"));
        pdPage.fillByPlaceholder("Enter your remarks here", TestDataProvider.get(TP + "additional_collateral_remark"));
        pdPage.selectDropdownByLabelExact("Additional Income of applicant", TestDataProvider.get(TP + "additional_income"));
        pdPage.clickSubmit();

        pdPage.reloadPage();
        log.info("Tele PD completed for: {}", pendingName);
    }

    private void fillBasicDetails(TelePdPage pdPage) {
        log.info("Filling Basic Details...");
        pdPage.clickSectionButton("Basic Detail");
        pdPage.fillByLabel("Contacted person", TestDataProvider.get(TP + "contacted_person"));
        pdPage.fillByLabel("Contact number", TestDataProvider.get(TP + "contact_number"));
    }

    private void fillEmploymentDetails(TelePdPage pdPage, boolean isEntity) {
        log.info("Filling Employment / Business Details...");
        pdPage.clickSectionButton("Employment / Business Details");
        pdPage.selectDropdownByLabel("Nature of Company / Business", TestDataProvider.get(TP + "nature_of_company"));
        if (isEntity) {
            pdPage.fillByLabel("Designation / Ownership", TestDataProvider.get(TP + "designation_ownership"));
        }
        pdPage.fillByLabel("Total Experience / Business", TestDataProvider.get(TP + "total_experience"));
        pdPage.fillByLabel("Current Job / Business Vintage", TestDataProvider.get(TP + "current_vintage"));
        pdPage.fillByLabel("Family / Friends Involvement", TestDataProvider.get(TP + "family_involvement"));
    }

    private void fillIncomeDetails(TelePdPage pdPage) {
        log.info("Filling Income Details...");
        pdPage.clickSectionButton("Income Details");
        pdPage.fillByLabel("Primary Income Source", TestDataProvider.get(TP + "primary_income_source"));
        pdPage.selectDropdownByLabel("Nature of Income", TestDataProvider.get(TP + "nature_of_income"));
        pdPage.fillByLabel("Estimated Monthly Income (", TestDataProvider.get(TP + "estimated_monthly_income"));
        pdPage.fillByLabel("Secondary / Family Income", TestDataProvider.get(TP + "secondary_income"));
        pdPage.fillByLabel("Monthly Household Income", TestDataProvider.get(TP + "monthly_household_income"));
        pdPage.fillByLabel("Existing Monthly Obligations", TestDataProvider.get(TP + "existing_obligations"));
    }

    private void fillBureauDetails(TelePdPage pdPage) {
        log.info("Filling Bureau Details...");
        pdPage.clickSectionButton("Bureau Details");
        pdPage.fillByLabel("Reasons for delayed EMI", TestDataProvider.get(TP + "delayed_emi_reason"));
        pdPage.fillByLabel("Details of CIBIL enquiries in", TestDataProvider.get(TP + "cibil_enquiries"));
        pdPage.fillByLabel("End use of recently taken", TestDataProvider.get(TP + "end_use_recent_loan"));
    }

    private void fillPropertyAndCollateralDetails(TelePdPage pdPage) {
        log.info("Filling Property & Collateral Details...");
        pdPage.clickSectionButton("Loan Requirement & End Use");
        pdPage.clickSectionButton("Property & Collateral Details");

        pdPage.clickEnterNamePlaceholder();
        pdPage.clickNewCollateralButton();
        pdPage.selectOwnerType("Existing");
        pdPage.selectPropertyOwner("Hannah Isaac");

        pdPage.selectDropdownByLabelAndText("Property ownership Status",
                TestDataProvider.get(TP + "property_ownership_status"),
                TestDataProvider.get(TP + "property_ownership_status"));
        pdPage.fillByLabel("Property ownership since when?", TestDataProvider.get(TP + "ownership_since"));
        pdPage.fillByLabel("Occupied by whom and since", TestDataProvider.get(TP + "occupied_since"));
        pdPage.fillByLabel("Property tax in whose name?", TestDataProvider.get(TP + "property_tax_name"));
        pdPage.fillByLabel("Property tax paid till which", TestDataProvider.get(TP + "property_tax_paid_till"));
        pdPage.fillByLabel("Approx. Market Value (", TestDataProvider.get(TP + "market_value"));
        pdPage.fillByPlaceholder("Enter document details", TestDataProvider.get(TP + "document_details"));
        pdPage.fillByLabel("Known Legal / Technical issues", TestDataProvider.get(TP + "legal_technical_issues"));
    }

    private void fillTelePdStatusAndDoneBy(TelePdPage pdPage) {
        log.info("Filling Tele PD Status and Done By...");
        pdPage.selectDropdownByLabelAndText("Tele PD Status",
                TestDataProvider.get(TP + "tele_pd_status"),
                TestDataProvider.get(TP + "tele_pd_status"));
        pdPage.selectTelePdDoneBy(
                TestDataProvider.get(TP + "tele_pd_done_by_search"),
                TestDataProvider.get(TP + "tele_pd_done_by_user"));
    }
}
