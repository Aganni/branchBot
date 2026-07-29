package ui.stepDefinitions.jarvis_Secure;

import dynamicData.DynamicDataClass;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.TelePdPage;

public class TelePdSteps extends BaseTest {

    @And("User completes PD and Property Visit for all applicants")
    public void completePdAndPropertyVisit() throws Exception {
        TelePdPage pdPage = new TelePdPage(BaseTest.getPage());
        String blackPantherUrl = BaseTest.initializeEnvironment("blackPantherUrl");
        pdPage.navigateToBlackPanther(blackPantherUrl);
        pdPage.loginViaGoogleSSO();

        // 2. Search by App ID and open the application
        String appFormId = DynamicDataClass.get().getAppFormId();
        pdPage.searchByAppId(appFormId);
        pdPage.openApplicationFromResults("Noah johns");

        // 3. Navigate to PD & Property Visit
        pdPage.navigateToPdAndPropertyVisit();

        // 4. Complete Tele PD for all 3 applicants
        completeTelePdForApplicant(pdPage, "Hannah IsaacPENDING", "Hannah IsaacIN_PROGRESS", false);
        completeTelePdForApplicant(pdPage, "Noah johnsonPENDING", "Noah johnsonIN_PROGRESS", false);
        completeTelePdForApplicant(pdPage, "Amazon.com Inc.PENDING", "Amazon.com Inc.IN_PROGRESS", true);

        log.info("Tele PD completed for all applicants.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  COMPLETE TELE PD FOR A SINGLE APPLICANT
    // ═══════════════════════════════════════════════════════════════════════════

    private void completeTelePdForApplicant(TelePdPage pdPage, String pendingName, String inProgressName, boolean isEntity) {
        log.info("Starting Tele PD for: {}", pendingName);

        // Select the PENDING applicant
        pdPage.selectApplicantFromCombo(pendingName);

        // Fill all sections
        fillBasicDetails(pdPage);
        fillEmploymentDetails(pdPage, isEntity);
        fillIncomeDetails(pdPage);
        fillBureauDetails(pdPage);
        fillPropertyAndCollateralDetails(pdPage);
        fillTelePdStatusAndDoneBy(pdPage);

        // First Save (page reloads, resets to applicant selection)
        pdPage.clickSave();

        // Re-select the applicant (now IN_PROGRESS)
        pdPage.selectApplicantFromCombo(inProgressName);

        // Fill Schedule datetime
        pdPage.fillScheduleDateTime("2026-06-20T12:12");

        // Second Save (page reloads again)
        pdPage.clickSave();

        // Re-select the applicant again after second save
        pdPage.selectApplicantFromCombo(inProgressName);

        // Fill Additional Collateral and Income
        pdPage.selectDropdownByLabelAndText("Additional Collateral/Address", "Yes", "Yes");
        pdPage.fillByPlaceholder("Enter your remarks here", "random text");
        pdPage.selectDropdownByLabelExact("Additional Income of applicant", "No");

        // Submit
        pdPage.clickSubmit();

        // Reload page to get fresh state for next applicant
        pdPage.reloadPage();

        log.info("Tele PD completed for: {}", pendingName);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SECTION: Basic Details
    // ═══════════════════════════════════════════════════════════════════════════

    private void fillBasicDetails(TelePdPage pdPage) {
        log.info("Filling Basic Details...");
        pdPage.clickSectionButton("Basic Detail");
        pdPage.fillByLabel("Contacted person", "Nikitha P S");
        pdPage.fillByLabel("Contact number", "9380800218");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SECTION: Employment / Business Details
    // ═══════════════════════════════════════════════════════════════════════════

    private void fillEmploymentDetails(TelePdPage pdPage, boolean isEntity) {
        log.info("Filling Employment / Business Details...");
        pdPage.clickSectionButton("Employment / Business Details");
        pdPage.selectDropdownByLabel("Nature of Company / Business", "LTD");
        if (isEntity) {
            pdPage.fillByLabel("Designation / Ownership", "Director");
        }
        pdPage.fillByLabel("Total Experience / Business", "12");
        pdPage.fillByLabel("Current Job / Business Vintage", "9");
        pdPage.fillByLabel("Family / Friends Involvement", "No");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SECTION: Income Details
    // ═══════════════════════════════════════════════════════════════════════════

    private void fillIncomeDetails(TelePdPage pdPage) {
        log.info("Filling Income Details...");
        pdPage.clickSectionButton("Income Details");
        pdPage.fillByLabel("Primary Income Source", "2500000");
        pdPage.selectDropdownByLabel("Nature of Income", "Bank");
        pdPage.fillByLabel("Estimated Monthly Income (", "32500");
        pdPage.fillByLabel("Secondary / Family Income", "25600");
        pdPage.fillByLabel("Monthly Household Income", "28600");
        pdPage.fillByLabel("Existing Monthly Obligations", "25000");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SECTION: Bureau Details
    // ═══════════════════════════════════════════════════════════════════════════

    private void fillBureauDetails(TelePdPage pdPage) {
        log.info("Filling Bureau Details...");
        pdPage.clickSectionButton("Bureau Details");
        pdPage.fillByLabel("Reasons for delayed EMI", "random text");
        pdPage.fillByLabel("Details of CIBIL enquiries in", "random text");
        pdPage.fillByLabel("End use of recently taken", "random text");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SECTION: Property & Collateral Details
    // ═══════════════════════════════════════════════════════════════════════════

    private void fillPropertyAndCollateralDetails(TelePdPage pdPage) {
        log.info("Filling Property & Collateral Details...");
        pdPage.clickSectionButton("Loan Requirement & End Use");
        pdPage.clickSectionButton("Property & Collateral Details");

        // Enter name placeholder and click New
        pdPage.clickEnterNamePlaceholder();
        pdPage.clickNewCollateralButton();

        // Select owner type and property owner
        pdPage.selectOwnerType("Existing");
        pdPage.selectPropertyOwner("Hannah Isaac");

        // Property details
        pdPage.selectDropdownByLabelAndText("Property ownership Status", "Purchased", "Purchased");
        pdPage.fillByLabel("Property ownership since when?", "2019");
        pdPage.fillByLabel("Occupied by whom and since", "nikitha, 2022");
        pdPage.fillByLabel("Property tax in whose name?", "Nikitha");
        pdPage.fillByLabel("Property tax paid till which", "2026");
        pdPage.fillByLabel("Approx. Market Value (", "465000000");
        pdPage.fillByPlaceholder("Enter document details", "yes");
        pdPage.fillByLabel("Known Legal / Technical issues", "no issues");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SECTION: Tele PD Status & Done By
    // ═══════════════════════════════════════════════════════════════════════════

    private void fillTelePdStatusAndDoneBy(TelePdPage pdPage) {
        log.info("Filling Tele PD Status and Done By...");
        // Tele PD Status
        pdPage.selectDropdownByLabelAndText("Tele PD Status", "Positive", "Positive");

        // Tele PD Done By
        pdPage.selectTelePdDoneBy("ten", "Tenjin");
    }
}
