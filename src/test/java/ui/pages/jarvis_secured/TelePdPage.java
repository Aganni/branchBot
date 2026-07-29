package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

import java.util.regex.Pattern;

/**
 * Page Object for the Tele PD flow in BlackPanther portal.
 * Handles login, app search, and Tele PD form filling.
 */
public class TelePdPage extends BaseTest {
    private Page page;

    public TelePdPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // LOGIN TO BLACKPANTHER
    public void navigateToBlackPanther(String url) {
        log.info("Opening BlackPanther in a new tab: {}", url);
        Page newTab = page.context().newPage();
        newTab.navigate(url);
        newTab.waitForLoadState(LoadState.NETWORKIDLE);
        newTab.waitForTimeout(2000);
        // Switch all subsequent interactions to the new tab
        this.page = newTab;
        log.info("BlackPanther opened in new tab.");
    }

    public void loginViaGoogleSSO() {
        log.info("Logging in to BlackPanther via Google SSO...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("google Sign in with Google")).click();
        page.waitForTimeout(2000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("CloudflareSSO")).click();
        page.waitForTimeout(10000);

        log.info("BlackPanther login submitted.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SEARCH & OPEN APPLICATION BY APP ID
    // ═══════════════════════════════════════════════════════════════════════════

    public void searchByAppId(String appFormId) {
        log.info("Searching by App ID: {}", appFormId);
        // Click "Search by" button to open search type dropdown
        page.locator("button").filter(new Locator.FilterOptions().setHasText("Search by")).click();
        page.waitForTimeout(500);

        // Select "App ID" search type
        page.getByLabel("App ID").click();
        page.waitForTimeout(500);

        // Type the app form ID
        page.getByPlaceholder("Type to search...").click();
        page.getByPlaceholder("Type to search...").fill(appFormId);
        page.waitForTimeout(500);

        // Click the search button
        page.locator("div").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^App ID$"))).getByRole(AriaRole.BUTTON).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("App ID search completed.");
    }

    public void openApplicationFromResults(String applicantName) {
        log.info("Opening application for: {}", applicantName);
        page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName(applicantName)).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Application opened.");
    }

    public void clickBackButton() {
        // The back/navigation button (nth(2) as per codegen)
        page.getByRole(AriaRole.BUTTON).nth(2).click();
        page.waitForTimeout(1000);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  NAVIGATION TO PD & PROPERTY VISIT
    // ═══════════════════════════════════════════════════════════════════════════

    public void navigateToPdAndPropertyVisit() {
        log.info("Navigating to PD & Property Visit section...");
        // Click the 3-dot menu button next to applicant name
        Locator threeDotBtn = page.locator("(//button[contains(@class, 'inline-flex') and contains(@class, 'rounded-md') and contains(@class, 'h-10') and contains(@class, 'w-10')])[3]");
        threeDotBtn.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        threeDotBtn.click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(2000);
        // Now click PD & Property Visit from the menu
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("PD & Property Visit")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("PD & Property Visit section loaded.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  APPLICANT SELECTION
    // ═══════════════════════════════════════════════════════════════════════════

    public void selectApplicantFromCombo(String applicantText) {
        log.info("Selecting applicant: {}", applicantText);
        // Scroll to top to ensure the dropdown is visible
        page.evaluate("window.scrollTo(0, 0)");
        page.waitForTimeout(1000);
        // Click the "Select Applicant/Co-applicant" dropdown
        Locator dropdown = page.getByRole(AriaRole.COMBOBOX).first();
        dropdown.scrollIntoViewIfNeeded();
        page.waitForTimeout(500);
        dropdown.click();
        page.waitForTimeout(1000);
        // Select the option containing the applicant name
        page.getByText(applicantText).first().click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SECTION BUTTONS (expand accordion sections)
    // ═══════════════════════════════════════════════════════════════════════════

    public void clickSectionButton(String sectionName) {
        log.info("Opening section: {}", sectionName);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(sectionName)).click();
        page.waitForTimeout(1000);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  FORM FIELD INTERACTIONS
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillByLabel(String label, String value) {
        page.getByLabel(label).click();
        page.getByLabel(label).fill(value);
        page.waitForTimeout(300);
    }

    public void selectDropdownByLabel(String comboLabel, String optionLabel) {
        page.getByLabel(comboLabel).click();
        page.waitForTimeout(500);
        page.getByLabel(optionLabel, new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForTimeout(500);
    }

    /**
     * For dropdowns where the option needs .getByText() within the label locator.
     * e.g., page.getByLabel("Positive").getByText("Positive").click()
     */
    public void selectDropdownByLabelAndText(String comboLabel, String optionLabel, String optionText) {
        page.getByLabel(comboLabel).click();
        page.waitForTimeout(500);
        page.getByLabel(optionLabel).getByText(optionText).click();
        page.waitForTimeout(500);
    }

    /**
     * For dropdowns where the option label needs exact matching.
     * e.g., selecting "No" without matching "No issues" etc.
     */
    public void selectDropdownByLabelExact(String comboLabel, String optionLabel) {
        page.getByLabel(comboLabel).click();
        page.waitForTimeout(500);
        page.getByLabel(optionLabel, new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForTimeout(500);
    }

    public void fillByPlaceholder(String placeholder, String value) {
        page.getByPlaceholder(placeholder).click();
        page.getByPlaceholder(placeholder).fill(value);
        page.waitForTimeout(300);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PROPERTY & COLLATERAL DETAILS
    // ═══════════════════════════════════════════════════════════════════════════

    public void clickEnterNamePlaceholder() {
        page.getByPlaceholder("Enter name").click();
        page.waitForTimeout(500);
    }

    public void clickNewCollateralButton() {
        page.locator("button").filter(new Locator.FilterOptions().setHasText("New")).click();
        page.waitForTimeout(1000);
    }

    public void selectOwnerType(String ownerType) {
        page.getByLabel(ownerType).getByText(ownerType).click();
        page.waitForTimeout(500);
    }

    public void selectPropertyOwner(String ownerName) {
        page.getByLabel("Name of Property Owner").click();
        page.waitForTimeout(500);
        page.getByLabel(ownerName).getByText(ownerName).click();
        page.waitForTimeout(500);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TELE PD DONE BY (USER SELECTION)
    // ═══════════════════════════════════════════════════════════════════════════

    public void selectTelePdDoneBy(String searchText, String userName) {
        page.locator("div").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^Select users$"))).nth(1).click();
        page.waitForTimeout(500);
        page.getByPlaceholder("Search users...").fill(searchText);
        page.waitForTimeout(1000);
        page.getByText(userName, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SCHEDULE FOR IN-PERSON PD
    // ═══════════════════════════════════════════════════════════════════════════

    public void clickScheduleField() {
        page.getByLabel("Schedule for in-person PD").click();
        page.waitForTimeout(300);
    }

    public void fillScheduleDateTime(String dateTimeValue) {
        Locator scheduleField = page.getByLabel("Schedule for in-person PD");
        scheduleField.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        scheduleField.click();
        page.waitForTimeout(300);
        scheduleField.click();
        page.waitForTimeout(300);
        scheduleField.press("ArrowRight");
        scheduleField.fill(dateTimeValue);
        scheduleField.press("ArrowDown");
        page.waitForTimeout(500);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SAVE & SUBMIT
    // ═══════════════════════════════════════════════════════════════════════════

    public void clickSave() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);
    }

    public void clickSubmit() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);
    }

    public void reloadPage() {
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
    }
}
