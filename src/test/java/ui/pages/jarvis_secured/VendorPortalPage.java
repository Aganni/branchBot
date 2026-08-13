package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

import java.nio.file.Paths;

/**
 * Page Object for the Vendor Portal (3rd party).
 * Handles:
 *   - Navigating and logging in via Internal SSO (same SSO as Jarvis)
 *   - Finding the assigned case
 *   - Moving case to WIP (Work in Progress)
 *   - Uploading the final report
 *   - Submitting report to KSF
 *
 * URL: https://vendor.portal.{env}.creditsaison.in/signin
 */
public class VendorPortalPage extends BaseTest {

    private final Page page;

    public VendorPortalPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  NAVIGATION & LOGIN
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Navigates to the Vendor Portal sign-in page.
     */
    public void navigateToVendorPortal(String vendorPortalUrl) {
        log.info("Navigating to Vendor Portal: {}", vendorPortalUrl);
        page.navigate(vendorPortalUrl);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Vendor Portal loaded.");
    }

    /**
     * Logs in via Internal SSO (same Google SSO session as Jarvis).
     * Since the user is already authenticated via Google, clicking the SSO button
     * should auto-authenticate without requiring credentials.
     */
    public void loginViaInternalSSO() {
        log.info("Logging in via Internal SSO...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue with Internal SSO")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Vendor Portal SSO login completed.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CASE MANAGEMENT
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks on a case row by its case ID prefix to select it.
     * Uses text-based locator since the Vendor Portal shows the full/partial case ID.
     */
    public void selectCase(String caseIdPrefix) {
        log.info("Selecting case with prefix: {}...", caseIdPrefix);
        page.locator("text=/" + caseIdPrefix + "/").first().click();
        page.waitForTimeout(2000);
        log.info("Case selected: {}", caseIdPrefix);
    }

    /**
     * Moves the selected case to WIP (Work in Progress).
     */
    public void moveToWip() {
        log.info("Moving case to WIP...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Move to WIP")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Case moved to WIP.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  REPORT UPLOAD & SUBMISSION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks "Upload Final FI Verdict here" to open the upload section.
     */
    public void clickUploadFinalVerdict() {
        log.info("Opening upload section...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload Final FI Verdict here")).click();
        page.waitForTimeout(2000);
        log.info("Upload section opened.");
    }

    /**
     * Uploads a report file.
     * Clicks "Upload Report" button which triggers a file chooser, then sets the file.
     */
    public void uploadReport(String filePath) {
        log.info("Uploading report: {}", filePath);
        Locator uploadBtn = page.locator("button").filter(new Locator.FilterOptions().setHasText("Upload Report"));
        // Wait for file chooser triggered by clicking Upload Report
        com.microsoft.playwright.FileChooser fileChooser = page.waitForFileChooser(() -> {
            uploadBtn.click();
        });
        fileChooser.setFiles(Paths.get(filePath));
        page.waitForTimeout(3000);
        log.info("Report uploaded.");
    }

    /**
     * Fills the remarks field before submission.
     */
    public void fillRemarks(String remarks) {
        log.info("Filling remarks: {}", remarks);
        page.getByPlaceholder("Write Remarks").click();
        page.getByPlaceholder("Write Remarks").fill(remarks);
        page.waitForTimeout(300);
    }

    /**
     * Submits the report to KSF (Credit Saison).
     */
    public void submitToKsf() {
        log.info("Submitting report to KSF...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit to KSF")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Report submitted to KSF.");
    }
}
