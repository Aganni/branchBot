package ui.pages.jarvis_secured_HLR;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;
import java.nio.file.Paths;
public class PropertyVisitPage extends BaseTest {
    private final Page page;
    public PropertyVisitPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // Navigates to PD & Property Visit section and clicks the Property Visit tab.
    public void navigateToPropertyVisitTab() {
        log.info("Navigating to Property Visit tab...");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("PD & Property Visit")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Property Visit")).click();
        page.waitForTimeout(2000);
        log.info("Property Visit tab loaded.");
    }

    public void navigateToPropertyVisitViaUrl() {
        log.info("Navigating to Property Visit tab...");
        String currentUrl = page.url();
        // Ensure we're on the pdForm page
        if (!currentUrl.contains("/pdForm")) {
            String pdFormUrl = currentUrl.replaceAll("/application/([^/]+).*", "/application/$1/pdForm");
            page.navigate(pdFormUrl);
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.waitForTimeout(3000);
        }
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Property Visit")).click();
        page.waitForTimeout(2000);
        log.info("Property Visit tab loaded.");
    }

    // EDIT MODE & APPLICANT SELECTION
    public void clickEdit() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(2000);
    }

    public void selectOwnerType(String type) {
        log.info("Selecting owner type: {}", type);
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName(type)).click();
        page.waitForTimeout(500);
    }

    public void selectApplicants(String applicant1, String applicant2) {
        log.info("Selecting applicants: {}, {}", applicant1, applicant2);
        page.getByPlaceholder("Select Applicants").click();
        page.waitForTimeout(500);
        page.getByText(applicant1).click();
        page.waitForTimeout(500);
        page.getByText(applicant2, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);
        // Close the dropdown by clicking on the heading
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Property Visit Details")).click();
        page.waitForTimeout(500);
    }

    // GRID FIELD INTERACTIONS
    /**
     * Fills a text field in the grid by double-clicking the cell to activate Input Editor.
     * Note: A single click before dblclick can interfere with grid cell activation,
     * so we go straight to dblclick which implicitly includes the first click.
     */
    private void fillGridCell(String rowName, String value) {
        Locator cell = page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(rowName))
                .getByRole(AriaRole.GRIDCELL).nth(1);
        cell.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        cell.dblclick();
        page.waitForTimeout(500);
        page.getByLabel("Input Editor").fill(value);
        page.waitForTimeout(300);
        // Press Tab to confirm the value and move out of the editor
        page.keyboard().press("Tab");
        page.waitForTimeout(300);
    }

    //Clicks a grid cell (for dropdowns that open on single click).
    private void clickGridCell(String rowName) {
        Locator cell = page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(rowName))
                .getByRole(AriaRole.GRIDCELL).nth(1);
        cell.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        cell.click();
        page.waitForTimeout(500);
    }

    //Selects a dropdown option in a grid cell (dblclick to open dropdown).
    private void selectGridDropdown(String rowName, String optionText) {
        Locator cell = page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(rowName))
                .getByRole(AriaRole.GRIDCELL).nth(1);
        cell.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        cell.dblclick();
        page.waitForTimeout(500);
        page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForTimeout(500);
        page.getByText(optionText).click();
        page.waitForTimeout(500);
    }

    //Selects an option from a grid cell that uses role=OPTION (dblclick to activate).
    private void selectGridOption(String rowName, String optionName) {
        Locator cell = page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(rowName))
                .getByRole(AriaRole.GRIDCELL).nth(1);
        cell.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        cell.dblclick();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(optionName)).click();
        page.waitForTimeout(500);
    }

    // PROPERTY VISIT DETAILS - FILL ALL FIELDS
    public void fillPropertyAddress(String address) {
        log.info("Filling Property Address: {}", address);
        fillGridCell("Property Address", address);
    }
    public void selectTypeOfProperty(String type) {
        log.info("Selecting Type Of Property: {}", type);
        selectGridDropdown("Type Of Property", type);
    }
    public void selectTypesOfCollateral(String collateral) {
        log.info("Selecting Types of Collateral: {}", collateral);
        Locator cell = page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Types of Collateral"))
                .getByRole(AriaRole.GRIDCELL).nth(1);
        cell.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        cell.click();
        page.waitForTimeout(500);
        cell.dblclick();
        page.waitForTimeout(500);
        // Click the dropdown picker to open the options list
        page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForTimeout(500);
        page.getByLabel("Select Field").getByText(collateral).click();
        page.waitForTimeout(500);
    }

    public void fillPropertyValue(String value) {
        log.info("Filling Property Value As Per Customer: {}", value);
        fillGridCell("Property Value As Per Customer", value);
    }

    public void fillPropertyArea(String area) {
        log.info("Filling Property Area: {}", area);
        fillGridCell("Property Area", area);
    }

    public void selectWithinGeoLimits(String value) {
        log.info("Selecting Within Geo Limits: {}", value);
        Locator cell = page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Within Geo Limits (75 Kms)"))
                .getByRole(AriaRole.GRIDCELL).nth(1);
        cell.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        cell.dblclick();
        page.waitForTimeout(500);
        page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForTimeout(500);
        page.getByLabel("Select Field").getByText(value).click();
        page.waitForTimeout(500);
    }

    public void fillSpokeName(String name) {
        log.info("Filling Spoke Name: {}", name);
        fillGridCell("Spoke Name", name);
    }

    public void fillContactNoPersonMet(String contact) {
        log.info("Filling Contact No Of The Person Met: {}", contact);
        fillGridCell("Contact No Of The Person Met", contact);
    }

    public void fillAgeOfProperty(String age) {
        log.info("Filling Age Of The Property: {}", age);
        fillGridCell("Age Of The Property", age);
    }

    public void fillTotalNoOfUnits(String units) {
        log.info("Filling Total No Of Units: {}", units);
        fillGridCell("Total No Of Units", units);
    }

    public void fillVacantUnits(String units) {
        log.info("Filling Vacant Units: {}", units);
        fillGridCell("Vacant Units", units);
    }

    public void selectAccessRoadToProperty(String road1, String road2) {
        log.info("Selecting Access Road To Property: {}, {}", road1, road2);
        Locator cell = page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Access Road To Property"))
                .getByRole(AriaRole.GRIDCELL).nth(1);
        cell.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        // First option: click → double-click (auto-selects first option)
        cell.click();
        page.waitForTimeout(500);
        cell.dblclick();
        page.waitForTimeout(1000);
        // Second option: click → double-click (auto-selects second option)
        cell.click();
        page.waitForTimeout(500);
        cell.dblclick();
        page.waitForTimeout(1000);
    }

    public void selectHabitation(String habitation) {
        log.info("Selecting Habitation: {}", habitation);
        Locator cell = page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Habitation"))
                .getByRole(AriaRole.GRIDCELL).nth(1);
        cell.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        cell.click();
        page.waitForTimeout(500);
        cell.dblclick();
        page.waitForTimeout(500);
        // Click the dropdown picker to open the options list
        page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForTimeout(500);
        page.getByLabel("Select Field").getByText(habitation).click();
        page.waitForTimeout(500);
    }

    public void fillHabitationValue(String value) {
        log.info("Filling Habitation value: {}", value);
        Locator cell = page.getByRole(AriaRole.GRIDCELL, new Page.GetByRoleOptions().setName(value.substring(0, 3)));
        cell.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        cell.dblclick();
        page.waitForTimeout(500);
        page.getByLabel("Input Editor").fill(value);
        page.waitForTimeout(300);
        page.keyboard().press("Tab");
        page.waitForTimeout(300);
    }

    public void fillAgencyPropertyVisitDoneBy(String name) {
        log.info("Filling Agency Property Visit Done By: {}", name);
        fillGridCell("Agency Property Visit Done By", name);
    }

    public void fillRateReferenceVicinity(String value) {
        log.info("Filling Rate Reference in The Vicinity (INR): {}", value);
        fillGridCell("Rate Reference in The Vicinity", value);
    }

    public void selectAgencyPropertyVisitFeedback(String feedback) {
        log.info("Selecting Agency Property Visit Feedback: {}", feedback);
        Locator cell = page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Agency Property Visit Feedback"))
                .getByRole(AriaRole.GRIDCELL).nth(1);
        cell.scrollIntoViewIfNeeded();
        page.waitForTimeout(300);
        cell.click();
        page.waitForTimeout(500);
        cell.dblclick();
        page.waitForTimeout(500);
        // Click the dropdown picker to open the options list
        page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForTimeout(500);
        page.getByLabel("Select Field").getByText(feedback, new Locator.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);
    }

    public void fillDeviationsRemarks(String remarks) {
        log.info("Filling Deviations & Remarks: {}", remarks);
        fillGridCell("Deviations & Remarks", remarks);
    }

    public void fillTechnicalVendorRemarks(String remarks) {
        log.info("Filling Technical Vendor Remarks: {}", remarks);
        fillGridCell("Technical Vendor Remarks", remarks);
    }

    // VISIT DONE BY (multi-user selection)
    // Accepts a variable number of users since the configured count differs per
    // product's test data (e.g. LAP defines 3 users, HLR defines 4).
    public void selectVisitDoneByUsers(String searchText, String... users) {
        log.info("Selecting Visit Done By users ({} user(s))...", users.length);

        // Element UI multi-select: the search input sits inside el-select__tags and intercepts clicks on the placeholder input underneath.
        Locator searchInput = page.locator(".el-select__tags input.el-select__input");

        // Scroll into view and wait for layout to settle
        searchInput.scrollIntoViewIfNeeded();
        page.waitForTimeout(2000);

        // Click to open the dropdown and activate the search input
        searchInput.click();
        page.waitForTimeout(1000);

        // Fill search text to filter the user list
        searchInput.fill(searchText);
        page.waitForTimeout(2000);

        // Select each configured user from the filtered dropdown
        for (String user : users) {
            page.locator("li").filter(new Locator.FilterOptions().setHasText(user)).click();
            page.waitForTimeout(500);
        }

        // Click heading to close dropdown
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Property Visit Details")).click();
        page.waitForTimeout(500);
        log.info("Visit Done By users selected.");
    }

    // FILE UPLOADS (Photographs + Business Photographs)
    public void uploadPropertyVisitImages(String[] imagePaths) {
        log.info("Uploading Property Visit images...");

        // ── Round 1: Upload 1st image to Photographs → Save ──
        clickEdit();

        page.getByText("Photographs", new Page.GetByTextOptions().setExact(true)).first().scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);

        Locator firstUpload = page.locator(".el-upload input[type='file']").first();
        firstUpload.setInputFiles(Paths.get(imagePaths[0]));
        log.info("  Uploaded image 1 of 4 to Photographs");
        page.waitForTimeout(5000);

        clickSave();
        log.info("Round 1 saved.");

        // ── Round 2: Upload remaining 3 images to Photographs → Save ──
        clickEdit();

        page.getByText("Photographs", new Page.GetByTextOptions().setExact(true)).first().scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);

        for (int i = 1; i < imagePaths.length; i++) {
            Locator uploadInput = page.locator(".el-upload input[type='file']").first();
            uploadInput.setInputFiles(Paths.get(imagePaths[i]));
            log.info("  Uploaded image {} of 4 to Photographs", i + 1);
            page.waitForTimeout(4000);
        }
        log.info("Uploaded 4 images to Photographs section.");

        clickSave();
        log.info("Round 2 saved.");

        // ── Round 3: Upload 4 images to Business Photographs → Save ──
        clickEdit();

        page.getByText("Business Photographs").scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        log.info("Uploading 4 images to Business Photographs section...");

        for (int i = 0; i < imagePaths.length; i++) {
            // After Photographs are all uploaded and saved, the only remaining
            // .el-upload input on the page is the Business Photographs uploader.
            Locator businessUpload = page.locator(".el-upload input[type='file']").last();
            businessUpload.setInputFiles(Paths.get(imagePaths[i]));
            log.info("  Uploaded image {} of 4 to Business Photographs", i + 1);
            page.waitForTimeout(5000);
            // Keep Business Photographs section in view
            page.getByText("Business Photographs").scrollIntoViewIfNeeded();
            page.waitForTimeout(500);
        }
        log.info("Uploaded 4 images to Business Photographs section.");

        clickSave();
        log.info("Round 3 saved. All Property Visit images uploaded.");
    }

    //  SAVE & SUBMIT
    public void clickSave() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
    }

    public void clickSubmit() {
        Locator submitBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));

        // Try to find Submit button - it may already be visible or may need Edit first
        page.waitForTimeout(2000);
        if (submitBtn.isVisible()) {
            submitBtn.scrollIntoViewIfNeeded();
            page.waitForTimeout(500);
            submitBtn.click();
        } else {
            // Enter Edit mode to reveal Submit
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
            page.waitForTimeout(2000);
            submitBtn.scrollIntoViewIfNeeded();
            page.waitForTimeout(500);
            submitBtn.click();
        }

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Property Visit form submitted.");
    }

    // POST-SUBMIT: NAVIGATE TO APPFORM & VERIFY VALIDATION
    public void navigateToAppformAndAttemptStageMove() {
        log.info("Navigating to Appform after Property Visit submit...");

        // Click 'Go to Appform' link
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Go to Appform")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        // Scroll to the right side where Application Actions is located
        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);

        // Click Application Actions dropdown
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);

        // Select "Move to Credit Approval"
        page.locator("li").filter(new Locator.FilterOptions().setHasText("Move to Credit Approval")).click();
        page.waitForTimeout(3000);

        Locator validationMsg = page.getByText("Sales PD visit is mandatory");
        validationMsg.first().waitFor(new Locator.WaitForOptions().setTimeout(10000));
        log.info("Validation message verified: 'Sales PD visit is mandatory' displayed.");
    }
}
