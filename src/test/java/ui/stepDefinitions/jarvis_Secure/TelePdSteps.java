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

        // 4. Select pending applicant (Hannah Isaac)
        pdPage.selectApplicantFromCombo("Hannah IsaacPENDING");

        // 5. Fill Tele PD sections
        fillBasicDetails(pdPage);
        fillEmploymentDetails(pdPage);
        fillIncomeDetails(pdPage);
        fillBureauDetails(pdPage);
        fillPropertyAndCollateralDetails(pdPage);
        fillTelePdStatusAndDoneBy(pdPage);

        // 6. Save
        pdPage.clickSave();

        // 7. Fill Schedule (post-save the datetime field becomes editable)
        pdPage.fillScheduleDateTime("2026-07-10T12:25");

        // 8. Submit
        pdPage.clickSubmit();

        log.info("Tele PD completed for Hannah Isaac.");
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

    private void fillEmploymentDetails(TelePdPage pdPage) {
        log.info("Filling Employment / Business Details...");
        pdPage.clickSectionButton("Employment / Business Details");
        pdPage.selectDropdownByLabel("Nature of Company / Business", "LTD");
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

        // Schedule for in-person PD (click multiple times to activate the datetime input)
        pdPage.clickScheduleField();
        pdPage.clickScheduleField();
        pdPage.clickScheduleField();
        pdPage.clickScheduleField();

        // Tele PD Done By
        pdPage.selectTelePdDoneBy("ten", "Tenjin");
    }
}
