package ui.stepDefinitions.jarvis_Secure;
import com.microsoft.playwright.Page;
import data.TestDataProvider;
import dynamicData.DynamicDataClass;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.PdVisitPage;
import ui.pages.jarvis_secured.TelePdPage;
public class TelePdSteps extends BaseTest {

    private static final String TP = "dsa_secured.jarvis_secured.tele_pd.";
    private static final String PV = "dsa_secured.jarvis_secured.pd_visit.";
    @And("User completes Tele PD Visit for all applicants")

    public void completePdAndPropertyVisit() throws Exception {

        // PHASE 1: TELE PD (BlackPanther)
        TelePdPage telePdPage = new TelePdPage(BaseTest.getPage());
        String blackPantherUrl = BaseTest.initializeEnvironment("blackPantherUrl");
        telePdPage.navigateToBlackPanther(blackPantherUrl);
        telePdPage.loginViaGoogleSSO();

        String appFormId = DynamicDataClass.get().getAppFormId();
        telePdPage.searchByAppId(appFormId);
        telePdPage.openApplicationFromResults("Noah johns");
        telePdPage.navigateToPdAndPropertyVisit();

        // Complete Tele PD for all 3 applicants
        completeTelePdForApplicant(telePdPage, "Hannah Isaac", "Hannah Isaac", false);
        completeTelePdForApplicant(telePdPage, "Noah johnson", "Noah johnson", false);
        completeTelePdForApplicant(telePdPage, "Amazon.com Inc.", "Amazon.com Inc.", true);
        log.info("Tele PD completed for all applicants.");

        // PHASE 2: JARVIS - Attempt "Move to Credit Approval" (validation error)
        Page jarvisPage = BaseTest.getPage();
        jarvisPage.bringToFront();
        jarvisPage.reload();
        jarvisPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        jarvisPage.waitForTimeout(3000);

        jarvisPage.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        jarvisPage.waitForTimeout(500);
        jarvisPage.getByPlaceholder("Application Actions").click();
        jarvisPage.waitForTimeout(1000);
        jarvisPage.getByText("Move to Credit Approval").click();
        jarvisPage.waitForTimeout(3000);
        log.info("Move to Credit Approval attempted — validation error expected for missing PD form.");

        // PHASE 3: PD VISIT (BlackPanther) — delegated to PdVisitSteps
        Page blackPantherPage = telePdPage.getBlackPantherPage();
        blackPantherPage.bringToFront();
        PdVisitSteps pdVisitSteps = new PdVisitSteps();
        pdVisitSteps.completePdVisitFlow(blackPantherPage, jarvisPage);

        // PHASE 4: JARVIS - First attempt Move to Credit Approval (validation error expected)
        jarvisPage.bringToFront();
        String currentUrl = jarvisPage.url();
        if (currentUrl.contains("/pdForm")) {
            // Navigate from /pdForm to /appForm (application detail page)
            String appDetailUrl = currentUrl.replace("/pdForm", "/appForm");
            jarvisPage.navigate(appDetailUrl);
        } else if (!currentUrl.contains("/appForm")) {
            // Ensure we're on the appForm page
            String appFormUrl = currentUrl.replaceAll("/application/([^/]+).*", "/application/$1/appForm");
            jarvisPage.navigate(appFormUrl);
        } else {
            jarvisPage.reload();
        }
        jarvisPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        jarvisPage.waitForTimeout(5000);

        jarvisPage.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        jarvisPage.waitForTimeout(500);
        jarvisPage.getByPlaceholder("Application Actions").click();
        jarvisPage.waitForTimeout(1000);
        jarvisPage.getByText("Move to Credit Approval").click();
        jarvisPage.waitForTimeout(3000);
        log.info("Move to Credit Approval attempted — validation error expected for missing mandatory fields.");

        // PHASE 5: BlackPanther - Fill mandatory fields (References + Income) for each applicant
        log.info("Switching to BlackPanther to fill mandatory fields (References + Income)...");
        blackPantherPage.bringToFront();
        blackPantherPage.reload();
        blackPantherPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        blackPantherPage.waitForTimeout(3000);

        PdVisitPage pdVisitPage = new PdVisitPage(blackPantherPage);

        String refType = TestDataProvider.get(PV + "ref_type");
        String refName = TestDataProvider.get(PV + "ref_name");
        String refContact = TestDataProvider.get(PV + "ref_contact");
        String refDept = TestDataProvider.get(PV + "ref_department");
        String refDesig = TestDataProvider.get(PV + "ref_designation");
        String refRemarks = TestDataProvider.get(PV + "ref_remarks");
        String incomeParticulars = TestDataProvider.get(PV + "income_particulars");
        String incomeMonthly = TestDataProvider.get(PV + "income_monthly");

        // ── Noah johnson ──
        log.info("Filling mandatory fields for: Noah johnson");
        pdVisitPage.clickPdVisitTab();
        pdVisitPage.selectApplicantFromCombo("Noah johnson");
        pdVisitPage.addReference(refType, refName, refContact, refDept, refDesig, refRemarks);
        pdVisitPage.addIncomeEstimation(incomeParticulars, incomeMonthly);
        pdVisitPage.clickSubmit();
        log.info("Mandatory fields submitted for: Noah johnson");

        // ── Hannah Isaac ──
        log.info("Filling mandatory fields for: Hannah Isaac");
        pdVisitPage.clickPdVisitTab();
        pdVisitPage.selectApplicantFromCombo("Hannah Isaac");
        pdVisitPage.addReference(refType, refName, refContact, refDept, refDesig, refRemarks);
        pdVisitPage.addIncomeEstimation(incomeParticulars, incomeMonthly);
        pdVisitPage.clickSubmit();
        log.info("Mandatory fields submitted for: Hannah Isaac");

        // ── Amazon.com Inc. ──
        log.info("Filling mandatory fields for: Amazon.com Inc.");
        pdVisitPage.clickPdVisitTab();
        pdVisitPage.selectApplicantFromCombo("Amazon.com Inc.");
        pdVisitPage.addReference(refType, refName, refContact, refDept, refDesig, refRemarks);
        pdVisitPage.addIncomeEstimation(incomeParticulars, incomeMonthly);
        pdVisitPage.clickSubmit();
        log.info("Mandatory fields submitted for: Amazon.com Inc.");

        log.info("All mandatory fields filled and submitted for all applicants.");

        // PHASE 6: JARVIS - Move to Credit Approval (should succeed now)
        log.info("Switching to Jarvis for final Move to Credit Approval...");
        jarvisPage.bringToFront();
        // Ensure we're on the appForm page
        String phase6Url = jarvisPage.url();
        if (!phase6Url.contains("/appForm")) {
            String appFormUrl = phase6Url.replaceAll("/application/([^/]+).*", "/application/$1/appForm");
            jarvisPage.navigate(appFormUrl);
        } else {
            jarvisPage.reload();
        }
        jarvisPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        jarvisPage.waitForTimeout(5000);

        jarvisPage.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        jarvisPage.waitForTimeout(500);
        jarvisPage.getByPlaceholder("Application Actions").click();
        jarvisPage.waitForTimeout(1000);
        jarvisPage.getByText("Move to Credit Approval").click();
        jarvisPage.waitForTimeout(5000);
        log.info("Application moved to Credit Approval successfully.");
    }

    // TELE PD - PER APPLICANT
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

        // Re-select IN_PROGRESS → fill Schedule → Second Save
        pdPage.selectApplicantFromCombo(inProgressName);
        pdPage.fillScheduleDateTime(TestDataProvider.get(TP + "schedule_datetime"));
        pdPage.clickSave();

        // Fill Additional fields → Submit (applicant remains selected after save)
        pdPage.selectDropdownByLabelAndText("Additional Collateral/Address",
                TestDataProvider.get(TP + "additional_collateral"),
                TestDataProvider.get(TP + "additional_collateral"));
        pdPage.fillByPlaceholder("Enter your remarks here", TestDataProvider.get(TP + "additional_collateral_remark"));
        pdPage.selectDropdownByLabelExact("Additional Income of applicant", TestDataProvider.get(TP + "additional_income"));
        pdPage.clickSubmit();

        pdPage.reloadPage();
        log.info("Tele PD completed for: {}", pendingName);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TELE PD SECTION FILLERS
    // ═══════════════════════════════════════════════════════════════════════════

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
