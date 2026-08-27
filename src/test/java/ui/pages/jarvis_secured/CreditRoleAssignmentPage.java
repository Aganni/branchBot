package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Page Object for the Jarvis Admin Portal — Credit Role Assignment &amp; Hierarchy Mapping.
 * Used in the Tranche Disbursement flow to assign CREDIT department role with product code,
 * designation, approval amount, stages, sub-product allocation, and hierarchy mapping.
 *
 * Handles:
 *   - Navigating to Admin Portal and User Dashboard
 *   - Searching for a user
 *   - Editing user: department → CREDIT, product code, designation, approval amount, stages
 *   - Assigning sub-product (Auto Allocation)
 *   - Adding Role/Level assignment
 *   - Hierarchy Mapping: department, LPC, user, manager
 */
public class CreditRoleAssignmentPage extends BaseTest {

    private final Page page;

    public CreditRoleAssignmentPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  NAVIGATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Navigates to the Admin Portal via Jarvis sidebar.
     * Loads Jarvis base URL, opens sidebar, clicks Admin Portal link.
     */
    public void navigateToAdminPortal() {
        log.info("Navigating to Admin Portal...");
        String jarvisBaseUrl;
        try {
            jarvisBaseUrl = BaseTest.initializeEnvironment("jarvisUrl");
        } catch (Exception e) {
            log.error("Failed to get Jarvis URL from environment", e);
            throw new RuntimeException("Cannot determine Jarvis URL for Admin Portal navigation", e);
        }
        page.navigate(jarvisBaseUrl);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        // Open sidebar and click Admin Portal link
        page.locator(".el-icon-right").click();
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Admin Portal")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        // Dismiss drawer overlay if visible
        try {
            Locator drawerTint = page.locator(".drawer-tint-div");
            if (drawerTint.isVisible()) {
                drawerTint.click();
                page.waitForTimeout(500);
            }
        } catch (Exception e) {
            // No drawer to dismiss
        }

        log.info("Admin Portal loaded.");
    }

    /**
     * Opens the User Dashboard from Admin Portal.
     */
    public void openUserDashboard() {
        log.info("Opening User Dashboard...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("User Dashboard")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("User Dashboard opened.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SEARCH & OPEN USER
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Searches for a user by email in the User Dashboard.
     *
     * @param searchText the email search text (e.g. "tenjin")
     */
    public void searchUser(String searchText) {
        log.info("Searching for user: {}", searchText);
        Locator searchInput = page.getByPlaceholder("Type to Search by Email");
        searchInput.click();
        searchInput.fill(searchText);
        searchInput.press("Enter");
        page.waitForTimeout(2000);
        log.info("User search completed for: {}", searchText);
    }

    /**
     * Clicks the View button to open user details.
     */
    public void clickView() {
        log.info("Clicking View on user...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View").setExact(true)).click();
        page.waitForTimeout(2000);
        log.info("User details view opened.");
    }

    /**
     * Clicks the Edit button to enable editing of user details.
     */
    public void clickEdit() {
        log.info("Clicking Edit on user details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);
        log.info("User details in edit mode.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  DEPARTMENT & ROLE CONFIGURATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Selects the department from the dropdown.
     *
     * @param department the department name (e.g. "CREDIT")
     */
    public void selectDepartment(String department) {
        log.info("Selecting department: {}", department);
        page.getByPlaceholder("Select the department").click();
        page.waitForTimeout(500);
        page.getByText(department, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);
        log.info("Department selected: {}", department);
    }

    /**
     * Clicks Submit to save the department change.
     * This first Submit is needed before proceeding to product code/designation.
     */
    public void submitDepartment() {
        log.info("Submitting department change...");
        page.getByText("Submit").click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        log.info("Department submitted.");
    }

    /**
     * Clicks Edit again after department submission to configure product/designation/stages.
     */
    public void clickEditForRoleConfig() {
        log.info("Clicking Edit for role configuration...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);
        log.info("Role configuration in edit mode.");
    }

    /**
     * Selects the product code by typing a search term and selecting from the dropdown.
     *
     * @param searchText the product code search text (e.g. "la")
     * @param productCode the product code to select (e.g. "LAP")
     */
    public void selectProductCode(String searchText, String productCode) {
        log.info("Selecting product code: {} (search: {})", productCode, searchText);
        page.getByPlaceholder("Select the product code").click();
        page.getByPlaceholder("Select the product code").fill(searchText);
        page.waitForTimeout(1000);
        page.locator("li").filter(new Locator.FilterOptions()
                .setHasText(java.util.regex.Pattern.compile("^" + productCode + "$"))).click();
        page.waitForTimeout(500);
        log.info("Product code selected: {}", productCode);
    }

    /**
     * Selects the designation from the dropdown.
     *
     * @param designation the designation (e.g. "L4")
     */
    public void selectDesignation(String designation) {
        log.info("Selecting designation: {}", designation);
        page.getByPlaceholder("Select the designation").click();
        page.waitForTimeout(500);
        page.locator("li").filter(new Locator.FilterOptions().setHasText(designation)).click();
        page.waitForTimeout(500);
        log.info("Designation selected: {}", designation);
    }

    /**
     * Fills the approval amount.
     *
     * @param amount the approval amount (e.g. "50000000")
     */
    public void fillApprovalAmount(String amount) {
        log.info("Filling approval amount: {}", amount);
        page.getByPlaceholder("Enter approval amount").click();
        page.getByPlaceholder("Enter approval amount").fill(amount);
        page.waitForTimeout(300);
        log.info("Approval amount filled: {}", amount);
    }

    /**
     * Selects multiple stages from the stages multi-select dropdown.
     * Clicks the 5th div's el-select input to open the stages dropdown,
     * then selects each stage by text.
     *
     * @param stages list of stage names to select (e.g. "Login desk", "Cam", "Credit review", etc.)
     */
    public void selectStages(List<String> stages) {
        log.info("Selecting stages: {}", stages);
        // Open the stages dropdown (5th el-form-item content area)
        page.locator("div:nth-child(5) > .el-form-item__content > .el-select > .el-select__tags > .el-select__input").click();
        page.waitForTimeout(500);

        for (String stage : stages) {
            page.getByText(stage, new Page.GetByTextOptions().setExact(true)).click();
            page.waitForTimeout(300);
        }
        page.waitForTimeout(500);
        log.info("Stages selected: {}", stages);
    }

    /**
     * Selects the sub-product for Auto Allocation.
     *
     * @param subProduct the sub-product name (e.g. "Regular")
     */
    public void selectSubProduct(String subProduct) {
        log.info("Selecting sub-product (Auto Allocation): {}", subProduct);
        page.locator("div").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^Sub Product \\(Auto Allocation\\)" + subProduct + "$")))
                .getByRole(AriaRole.TEXTBOX).first().click();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.LISTITEM).click();
        page.waitForTimeout(500);
        log.info("Sub-product selected: {}", subProduct);
    }

    /**
     * Clicks "+ Assign Role/Level" button to add the role assignment.
     */
    public void clickAssignRoleLevel() {
        log.info("Clicking + Assign Role/Level...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Assign Role/Level")).click();
        page.waitForTimeout(2000);
        log.info("Role/Level assigned.");
    }

    /**
     * Clicks Submit on the main content area to save role configuration.
     */
    public void submitRoleConfig() {
        log.info("Submitting role configuration...");
        page.getByRole(AriaRole.MAIN).locator("div").filter(
                new Locator.FilterOptions().setHasText("Submit")).nth(3).click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        log.info("Role configuration submitted.");
    }

    /**
     * Clicks the Back button to return to User Dashboard main screen.
     */
    public void clickBack() {
        log.info("Clicking Back...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Back")).click();
        page.waitForTimeout(2000);
        log.info("Back to User Dashboard.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HIERARCHY MAPPING
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Opens the Hierarchy Mapping section.
     */
    public void openHierarchyMapping() {
        log.info("Opening Hierarchy Mapping...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Hierarchy Mapping")).click();
        page.waitForTimeout(2000);
        log.info("Hierarchy Mapping opened.");
    }

    /**
     * Selects the department in the hierarchy mapping form.
     *
     * @param department the department (e.g. "CREDIT")
     */
    public void selectHierarchyDepartment(String department) {
        log.info("Selecting hierarchy department: {}", department);
        page.locator("form").getByPlaceholder("Select the department").click();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.LIST).getByText(department).click();
        page.waitForTimeout(500);
        log.info("Hierarchy department selected: {}", department);
    }

    /**
     * Selects the LPC (Loan Product Code) in the hierarchy mapping form.
     *
     * @param lpc the LPC (e.g. "LAP")
     */
    public void selectHierarchyLpc(String lpc) {
        log.info("Selecting hierarchy LPC: {}", lpc);
        page.getByPlaceholder("Select the lpc").click();
        page.waitForTimeout(500);
        page.getByText(lpc).click();
        page.waitForTimeout(500);
        log.info("Hierarchy LPC selected: {}", lpc);
    }

    /**
     * Fills the user email in the hierarchy mapping form.
     * Uses type-ahead search and selects from the option list.
     *
     * @param searchText the user search text (e.g. "tenj")
     * @param userOption the option text to select (e.g. "tenjin.user@creditsaison-in.")
     */
    public void selectHierarchyUser(String searchText, String userOption) {
        log.info("Selecting hierarchy user: {}", userOption);
        page.getByPlaceholder("Please Input user email").click();
        page.getByPlaceholder("Please Input user email").fill(searchText);
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(userOption)).click();
        page.waitForTimeout(500);
        log.info("Hierarchy user selected: {}", userOption);
    }

    /**
     * Fills the manager email in the hierarchy mapping form.
     * Uses type-ahead search and selects from the option list.
     *
     * @param searchText    the manager search text (e.g. "niv")
     * @param managerOption the option text to select (e.g. "nivedita.rawat@creditsaison-")
     */
    public void selectHierarchyManager(String searchText, String managerOption) {
        log.info("Selecting hierarchy manager: {}", managerOption);
        page.getByPlaceholder("Please Input managers email").click();
        page.getByPlaceholder("Please Input managers email").fill(searchText);
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(managerOption)).click();
        page.waitForTimeout(500);
        log.info("Hierarchy manager selected: {}", managerOption);
    }

    /**
     * Clicks "Add Arrow Right icon" to submit the hierarchy mapping entry.
     */
    public void submitHierarchyMapping() {
        log.info("Submitting hierarchy mapping...");
        page.getByText("Add Arrow Right icon").click();
        page.waitForTimeout(2000);
        log.info("Hierarchy mapping submitted.");
    }

    /**
     * Closes the Admin Portal page/tab by clicking the close button.
     */
    public void closeAdminPortal() {
        log.info("Closing Admin Portal...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("")).click();
        page.waitForTimeout(1000);
        log.info("Admin Portal closed.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  COMPLETE FLOW — CREDIT ROLE + HIERARCHY
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Completes the full Admin Portal flow for Credit role assignment and hierarchy mapping.
     *
     * Flow:
     *   1. Navigate to Admin Portal → User Dashboard
     *   2. Search user → View → Edit → Select CREDIT department → Submit
     *   3. Edit again → Product code, Designation, Approval amount, Stages, Sub-product
     *   4. + Assign Role/Level → Submit → Back
     *   5. Hierarchy Mapping → Department, LPC, User, Manager → Add
     *   6. Close Admin Portal tab
     *
     * @param userSearchText      user to search (e.g. "tenjin")
     * @param department          department to assign (e.g. "CREDIT")
     * @param productCodeSearch   product code search text (e.g. "la")
     * @param productCode         product code to select (e.g. "LAP")
     * @param designation         designation (e.g. "L4")
     * @param approvalAmount      approval amount (e.g. "50000000")
     * @param stages              list of stages to assign
     * @param subProduct          sub-product for auto allocation (e.g. "Regular")
     * @param hierarchyDept       hierarchy mapping department (e.g. "CREDIT")
     * @param hierarchyLpc        hierarchy LPC (e.g. "LAP")
     * @param hierarchyUserSearch user search text for hierarchy (e.g. "tenj")
     * @param hierarchyUser       user option to select (e.g. "tenjin.user@creditsaison-in.")
     * @param hierarchyMgrSearch  manager search text (e.g. "niv")
     * @param hierarchyManager    manager option to select (e.g. "nivedita.rawat@creditsaison-")
     */
    public void completeCreditRoleAssignment(String userSearchText, String department,
                                             String productCodeSearch, String productCode,
                                             String designation,
                                             String approvalAmount, List<String> stages,
                                             String subProduct, String hierarchyDept,
                                             String hierarchyLpc, String hierarchyUserSearch,
                                             String hierarchyUser, String hierarchyMgrSearch,
                                             String hierarchyManager) {
        log.info("── Completing Credit Role Assignment & Hierarchy Mapping ──");

        // Phase 1: Navigate and change department
        navigateToAdminPortal();
        openUserDashboard();
        searchUser(userSearchText);
        clickView();
        clickEdit();
        selectDepartment(department);
        submitDepartment();

        // Phase 2: Configure role (product code, designation, stages, sub-product)
        clickEditForRoleConfig();
        selectProductCode(productCodeSearch, productCode);
        selectDesignation(designation);
        fillApprovalAmount(approvalAmount);
        selectStages(stages);
        selectSubProduct(subProduct);
        clickAssignRoleLevel();
        submitRoleConfig();
        clickBack();

        // Phase 3: Hierarchy Mapping
        openHierarchyMapping();
        selectHierarchyDepartment(hierarchyDept);
        selectHierarchyLpc(hierarchyLpc);
        selectHierarchyUser(hierarchyUserSearch, hierarchyUser);
        selectHierarchyManager(hierarchyMgrSearch, hierarchyManager);
        submitHierarchyMapping();

        // Close Admin Portal tab
        closeAdminPortal();

        log.info("Credit Role Assignment & Hierarchy Mapping completed.");
    }
}
