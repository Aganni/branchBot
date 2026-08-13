package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

/**
 * Page Object for the Jarvis Admin Portal.
 * Handles:
 *   - Navigating to Admin Portal (same domain as Jarvis)
 *   - Opening Vendor Admin Console
 *   - Switching to User tab
 *   - Assigning vendor type (TECH VENDOR) to a user
 *   - Updating the user configuration
 *
 * URL pattern: https://jarvis.{env}.creditsaison.corp/adminPortal
 */
public class AdminPortalPage extends BaseTest {

    private final Page page;

    public AdminPortalPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  NAVIGATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Navigates to the Admin Portal.
     * Uses the Jarvis base URL and appends /adminPortal path.
     * Since it's on the same Jarvis domain, the existing auth session is reused.
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
     * Opens the Vendor Admin Console from the Admin Portal.
     */
    public void openVendorAdminConsole() {
        log.info("Opening Vendor Admin Console...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Vendor Admin Console")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("Vendor Admin Console opened.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  USER TAB & VENDOR ASSIGNMENT
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Switches to the User tab in Vendor Admin Console.
     */
    public void switchToUserTab() {
        log.info("Switching to User tab...");
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("User")).click();
        page.waitForTimeout(2000);
        log.info("User tab active.");
    }

    /**
     * Searches for a user by email and clicks Edit to open the Update User dialog.
     * Flow: Click "Search with" → select "Email" → type search text → click/submit → Click "Edit" button.
     *
     * Locators:
     *   - Search with dropdown: locator("button").filter(hasText("Search with")) or getByText("Email")
     *   - Type here field: getByPlaceholder("Type here", exact)
     *   - Edit button: getByRole(BUTTON, "Edit")
     *
     * @param userSearchText text to search for the user (e.g. "tenji")
     */
    public void searchAndEditUser(String userSearchText) {
        log.info("Searching for user: {}", userSearchText);
        // Tenjin user is typically the first row — try clicking Edit directly
        // If search is needed, use the Search with dropdown
        try {
            // Try clicking Edit button in the row containing the user email
            page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Tenjin user"))
                    .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Edit")).click();
            page.waitForTimeout(2000);
            log.info("Edit dialog opened directly for Tenjin user.");
            return;
        } catch (Exception e) {
            log.info("Direct row click failed, trying search approach...");
        }

        // Fallback: use Search with dropdown
        page.locator("xpath=(//input[@placeholder='Search with'])[1]").click();
        page.waitForTimeout(500);
        page.getByText("Email", new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        page.getByPlaceholder("Type here", new Page.GetByPlaceholderOptions().setExact(true)).fill(userSearchText);
        page.waitForTimeout(500);
        page.getByPlaceholder("Type here", new Page.GetByPlaceholderOptions().setExact(true)).click();
        page.waitForTimeout(2000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).first().click();
        page.waitForTimeout(2000);
        log.info("Edit dialog opened for user search: {}", userSearchText);
    }

    /**
     * Assigns a vendor type to the user in the Update User dialog.
     * First clears any existing vendor by clicking the clear (⊗) icon,
     * then searches for the new vendor and selects it.
     *
     * Locators:
     *   - Clear icon (⊗): locator(".el-icon-circle-close") or nth icon in Vendor Id field
     *   - Vendor input: locator("form div").filter(hasText("Vendor Id Required")).getByPlaceholder("Enter")
     *   - Vendor option: locator("span").filter(hasText(vendorName))
     *
     * @param vendorSearchText text to type in the vendor search (e.g. "tec")
     * @param vendorName       the vendor option to select (e.g. "TECH VENDOR")
     */
    public void assignVendorToUser(String vendorSearchText, String vendorName) {
        log.info("Assigning vendor [{}] to user...", vendorName);

        // Clear existing vendor by clicking the ⊗ (clear/close) icon in the Vendor Id field
        try {
            Locator clearIcon = page.locator(".el-icon-circle-close");
            if (clearIcon.count() > 0 && clearIcon.first().isVisible()) {
                clearIcon.first().click();
                page.waitForTimeout(500);
                log.info("Existing vendor cleared.");
            }
        } catch (Exception e) {
            log.info("No existing vendor to clear, trying alternative locator...");
            try {
                // Alternative: the ⊗ icon inside the Vendor Id field area
                page.getByLabel("Update User").locator("i").nth(2).click();
                page.waitForTimeout(500);
            } catch (Exception ex) {
                log.info("No clear icon found.");
            }
        }

        // Click the Vendor Id field and type search text
        // Use a precise label-based locator scoped to the Update User dialog
        Locator vendorInput = page.getByLabel("Update User")
                .locator(".el-form-item")
                .filter(new Locator.FilterOptions().setHasText("Vendor Id"))
                .locator("input")
                .first();
        vendorInput.click();
        page.waitForTimeout(500);
        vendorInput.fill(vendorSearchText);
        page.waitForTimeout(1000);

        // Select the vendor from the dropdown
        page.locator("span").filter(new Locator.FilterOptions().setHasText(vendorName)).click();
        page.waitForTimeout(500);

        log.info("Vendor [{}] assigned.", vendorName);
    }

    /**
     * Clicks the Update button to save the user's vendor assignment.
     */
    public void clickUpdate() {
        log.info("Clicking Update to save changes...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("User updated successfully.");
    }
}
