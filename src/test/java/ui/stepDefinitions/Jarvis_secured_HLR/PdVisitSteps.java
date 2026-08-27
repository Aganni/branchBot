package ui.stepDefinitions.Jarvis_secured_HLR;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import data.TestDataProvider;
import hooks.BaseTest;
import ui.pages.jarvis_secured_HLR.PdVisitPage;

/**
 * Step definitions for PD Visit flow in BlackPanther + Jarvis Physical PD uploads.
 * Called by TelePdSteps after Tele PD is complete and Jarvis validation error occurs.
 *
 * Flow per applicant:
 *   1. BlackPanther: Fill PD Visit form details
 *   2. Jarvis: Upload 4 Photographs + 4 Business Photographs
 *   3. BlackPanther: Verify PD Visit status is 'Completed'
 *   4. Move to next applicant
 */
public class PdVisitSteps extends BaseTest {

    private static final String PV = "dsa_secured.jarvis_secured.pd_visit.";

    /**
     * Completes the full PD Visit flow for all applicants sequentially.
     * Each applicant follows: BlackPanther details → Jarvis uploads → BlackPanther verify.
     */
    public void completePdVisitFlow(Page blackPantherPage, Page jarvisPage) {
        PdVisitPage pdVisitPage = new PdVisitPage(blackPantherPage);

        String[] imagePaths = {
                TestDataProvider.get(PV + "image_path_1"),
                TestDataProvider.get(PV + "image_path_2"),
                TestDataProvider.get(PV + "image_path_3"),
                TestDataProvider.get(PV + "image_path_4")
        };

        // ═══════════════════════════════════════════════════════════════════
        //  APPLICANT 1: Noah johnson (Individual)
        // ═══════════════════════════════════════════════════════════════════
        processApplicantPdVisit(
                pdVisitPage, blackPantherPage, jarvisPage,
                "Noah johnson", "Noah johnson", false, imagePaths);

        // ═══════════════════════════════════════════════════════════════════
        //  APPLICANT 2: Hannah Isaac (Individual)
        // ═══════════════════════════════════════════════════════════════════
        processApplicantPdVisit(
                pdVisitPage, blackPantherPage, jarvisPage,
                "Hannah Isaac", "Hannah Isaac", false, imagePaths);

        // ═══════════════════════════════════════════════════════════════════
        //  APPLICANT 3: Amazon.com Inc. (Entity)
        // ═══════════════════════════════════════════════════════════════════
        processApplicantPdVisit(
                pdVisitPage, blackPantherPage, jarvisPage,
                "Amazon.com Inc.", "Amazon.com Inc.", true, imagePaths);

        log.info("PD Visit flow completed for all applicants.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PER-APPLICANT ORCHESTRATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Processes PD Visit for a single applicant:
     *   Step 1: BlackPanther — Fill all PD Visit details
     *   Step 2: Jarvis — Upload Photographs + Business Photographs
     *   Step 3: BlackPanther — Verify PD Visit status is 'Completed'
     */
    private void processApplicantPdVisit(PdVisitPage pdVisitPage, Page blackPantherPage,
                                         Page jarvisPage, String pendingName,
                                         String inProgressName, boolean isEntity,
                                         String[] imagePaths) {
        log.info("══════════════════════════════════════════════════════════════");
        log.info("Processing PD Visit for: {}", pendingName);
        log.info("══════════════════════════════════════════════════════════════");

        // ── STEP 1: BlackPanther — Fill PD Visit details ──
        blackPantherPage.bringToFront();
        pdVisitPage.clickPdVisitTab();
        fillPdVisitDetails(pdVisitPage, pendingName, inProgressName, isEntity);
        log.info("Step 1 complete: PD Visit details filled in BlackPanther for {}", pendingName);

        // ── STEP 2: Jarvis — Upload Physical PD images ──
        log.info("Switching to Jarvis for Physical PD image uploads...");
        jarvisPage.bringToFront();
        jarvisPage.reload();
        jarvisPage.waitForLoadState(LoadState.NETWORKIDLE);
        jarvisPage.waitForTimeout(3000);

        pdVisitPage.uploadPhysicalPdImages(jarvisPage, pendingName, imagePaths);
        log.info("Step 2 complete: Images uploaded in Jarvis for {}", pendingName);

        // ── STEP 3: BlackPanther — Verify PD Visit status is 'Completed' ──
        log.info("Switching back to BlackPanther to verify PD status...");
        blackPantherPage.bringToFront();
        blackPantherPage.reload();
        blackPantherPage.waitForLoadState(LoadState.NETWORKIDLE);
        blackPantherPage.waitForTimeout(3000);

        pdVisitPage.clickPdVisitTab();
        pdVisitPage.verifyApplicantStatus(pendingName, "Completed");
        log.info("Step 3 complete: PD Visit status verified as 'Completed' for {}", pendingName);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PD VISIT DETAILS — PER APPLICANT (BlackPanther)
    // ═══════════════════════════════════════════════════════════════════════════

    private void fillPdVisitDetails(PdVisitPage pdPage, String pendingName, String inProgressName, boolean isEntity) {
        log.info("Starting PD Visit form fill for: {}", pendingName);

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
            fillEntityPdVisit(pdPage, pendingName, inProgressName);
        } else {
            fillIndividualPdVisit(pdPage, pendingName, inProgressName);
        }

        log.info("PD Visit form fill completed for: {}", pendingName);
    }

    // ── ENTITY APPLICANT FLOW ──
    private void fillEntityPdVisit(PdVisitPage pdPage, String pendingName, String inProgressName) {
        // Business Details
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
    }

    // ── INDIVIDUAL APPLICANT FLOW ──
    private void fillIndividualPdVisit(PdVisitPage pdPage, String pendingName, String inProgressName) {
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
}
