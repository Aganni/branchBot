package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

import java.util.regex.Pattern;

/**
 * Page Object for the Jarvis Admin Portal — User Dashboard.
 * Handles:
 *   - Navigating to Admin Portal and opening User Dashboard
 *   - Searching for a user by email
 *   - Editing user department and designation
 *   - Submitting user changes
 *
 * Used to change user role to SALES/SALES_MANAGER before triggering DOGH.
 */
public class UserDashboardPage extends BaseTest {

    private final Page page;

    public UserDashboardPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  NAVIGATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Navigates to Admin Portal via the Jarvis sidebar.
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
     * Opens the User Dashboard from the Admin Portal.
     */
    public void openUserDashboard() {
        log.info("Opening User Dashboard...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("User Dashboard")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("User Dashboard opened.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SEARCH & EDIT USER
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
        page.waitForTimeout(2000);
        log.info("User search completed for: {}", searchText);
    }

    /**
     * Clicks View button to open user details.
     */
    public void clickView() {
        log.info("Clicking View on user...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View").setExact(true)).click();
        page.waitForTimeout(2000);
        log.info("User details view opened.");
    }

    /**
     * Clicks Edit button to enable editing of user details.
     */
    public void clickEdit() {
        log.info("Clicking Edit on user details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);
        log.info("User details in edit mode.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  DEPARTMENT & DESIGNATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Selects the department from the dropdown.
     *
     * @param department the department name (e.g. "SALES")
     */
    public void selectDepartment(String department) {
        log.info("Selecting department: {}", department);
        page.getByPlaceholder("Select the department").click();
        page.waitForTimeout(500);
        page.locator("li").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^" + department + "$"))).click();
        page.waitForTimeout(500);
        log.info("Department selected: {}", department);
    }

    /**
     * Selects the designation from the dropdown.
     *
     * @param designation the designation name (e.g. "SALES_MANAGER")
     */
    public void selectDesignation(String designation) {
        log.info("Selecting designation: {}", designation);
        page.getByPlaceholder("Select the designation").click();
        page.waitForTimeout(500);
        page.getByText(designation, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);
        log.info("Designation selected: {}", designation);
    }

    /**
     * Clicks Submit to save user changes.
     */
    public void submitChanges() {
        log.info("Submitting user changes...");
        page.getByText("Submit").click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        log.info("User changes submitted.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  COMPLETE FLOW
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Completes the full User Dashboard flow to change department and designation.
     * Navigates to Admin Portal → User Dashboard → Search → View → Edit → Select dept/designation → Submit.
     *
     * @param searchText  user search text (e.g. "tenjin")
     * @param department  target department (e.g. "SALES")
     * @param designation target designation (e.g. "SALES_MANAGER")
     */
    public void changeUserDepartmentAndDesignation(String searchText, String department, String designation) {
        log.info("── Changing user [{}] to department [{}] / designation [{}] ──", searchText, department, designation);
        navigateToAdminPortal();
        openUserDashboard();
        searchUser(searchText);
        clickView();
        clickEdit();
        selectDepartment(department);
        selectDesignation(designation);
        submitChanges();
        log.info("User department/designation change completed.");
    }
}
