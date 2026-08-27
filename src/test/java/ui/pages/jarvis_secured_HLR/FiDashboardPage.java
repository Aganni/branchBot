package ui.pages.jarvis_secured_HLR;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * Page Object for the FI/FCU Dashboard (popup page opened from Jarvis).
 * Handles:
 *   - Searching for application by App ID
 *   - Expanding application record and opening case details
 *   - Configuring vendor, vendor user, tag people
 *   - Updating documents and initiating TECHNICAL
 *   - Accepting reports and filling the technical vetting form
 *   - Submitting vetting details
 *   - Direct report upload (Round 2 approach)
 *   - Status verification (Open, Work in Progress, Report Received, Closed)
 *
 * This page operates on the popup Page instance returned by TechnicalVettingPage.openFiDashboard()
 */
public class FiDashboardPage extends BaseTest {

    private final Page page;

    public FiDashboardPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SEARCH & NAVIGATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Searches for an application by App ID on the FI Dashboard.
     */
    public void searchByAppId(String appId) {
        log.info("Searching FI Dashboard for App ID: {}", appId);
        page.getByPlaceholder("Search by").click();
        page.waitForTimeout(500);
        page.locator("li").filter(new Locator.FilterOptions().setHasText("App ID")).click();
        page.waitForTimeout(500);
        page.getByPlaceholder("Enter App ID").click();
        page.getByPlaceholder("Enter App ID").fill(appId);
        page.getByPlaceholder("Enter App ID").press("Enter");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Search completed for App ID: {}", appId);
    }

    /**
     * Refreshes the search results (re-triggers Enter on existing App ID field).
     */
    public void refreshSearch() {
        log.info("Refreshing search results...");
        page.getByPlaceholder("Enter App ID").click();
        page.getByPlaceholder("Enter App ID").press("Enter");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Search refreshed.");
    }

    /**
     * Expands the application record by clicking the expand icon in the first row.
     */
    public void expandApplicationRecord() {
        log.info("Expanding application record...");
        page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("")).locator("i").click();
        page.waitForTimeout(2000);
        log.info("Record expanded.");
    }

    /**
     * Captures the Case ID from the expanded row's inner table.
     * When multiple cases exist, returns the last one (most recently created).
     * Returns just the hex prefix without the ellipsis (e.g. "e64502").
     */
    public String getCaseId() {
        log.info("Capturing Case ID from expanded record...");
        Locator allTruncated = page.locator("text=/[a-f0-9]{6}\\.\\.\\./");
        page.waitForTimeout(2000);
        int count = allTruncated.count();
        log.info("Found {} truncated ID elements on page", count);

        if (count >= 2) {
            // Second truncated UUID is the case ID (first is the App ID)
            String text = allTruncated.nth(1).textContent().trim();
            return text.replace("...", "").trim();
        } else if (count == 1) {
            String text = allTruncated.first().textContent().trim();
            return text.replace("...", "").trim();
        }

        throw new RuntimeException("Could not capture Case ID from FI Dashboard expanded record");
    }

    /**
     * Captures the Case ID with "Not Initiated" status from the expanded inner table.
     * Used for Round 2 when there are multiple cases (one Closed, one Not Initiated).
     * Returns just the hex prefix without the ellipsis.
     */
    public String getNotInitiatedCaseId() {
        log.info("Capturing Not Initiated Case ID from expanded record...");
        // Find the row with "Not Initiated" status and get its case ID
        Locator notInitiatedRow = page.getByText("Not Initiated").last();
        notInitiatedRow.waitFor(new Locator.WaitForOptions().setTimeout(10000));

        // Get all truncated IDs — the last one should be the Not Initiated case
        Locator allTruncated = page.locator("text=/[a-f0-9]{6}\\.\\.\\./");
        int count = allTruncated.count();
        log.info("Found {} truncated ID elements, picking last for Not Initiated case", count);

        // The last truncated UUID in the expanded section is the Not Initiated case
        String text = allTruncated.nth(count - 1).textContent().trim();
        String caseId = text.replace("...", "").trim();
        log.info("Captured Not Initiated Case ID prefix: {}", caseId);
        return caseId;
    }

    /**
     * Clicks on a specific case ID text to open its details.
     * Targets the truncated span (e.g. "55b575...") in the inner table row.
     */
    public void openCaseDetails(String caseIdPrefix) {
        log.info("Opening case details: {}...", caseIdPrefix);
        page.getByText(caseIdPrefix + "...").click();
        page.waitForTimeout(2000);
        log.info("Case details opened.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  VENDOR CONFIGURATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Selects a vendor name from the vendor dropdown.
     */
    public void selectVendorName(String vendorSearchText, String vendorName) {
        log.info("Selecting vendor: {}", vendorName);
        page.getByPlaceholder("Select vendor name").click();
        page.getByPlaceholder("Select vendor name").fill(vendorSearchText);
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.LIST).getByText(vendorName).click();
        page.waitForTimeout(500);
        log.info("Vendor selected: {}", vendorName);
    }

    /**
     * Selects a vendor user from the dropdown.
     */
    public void selectVendorUser(String vendorUser) {
        log.info("Selecting vendor user: {}", vendorUser);
        page.getByPlaceholder("Select vendor user").click();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.LIST).getByText(vendorUser).click();
        page.waitForTimeout(500);
        log.info("Vendor user selected: {}", vendorUser);
    }

    /**
     * Selects a vendor user by typing a search text first (Round 2 approach).
     */
    public void selectVendorUserWithSearch(String searchText, String vendorUser) {
        log.info("Selecting vendor user with search [{}]: {}", searchText, vendorUser);
        page.getByPlaceholder("Select vendor user").click();
        page.getByPlaceholder("Select vendor user").fill(searchText);
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.LISTITEM).getByText(vendorUser).click();
        page.waitForTimeout(500);
        log.info("Vendor user selected: {}", vendorUser);
    }

    /**
     * Tags people by searching and selecting from autocomplete.
     */
    public void tagPeople(String... people) {
        log.info("Tagging {} people...", people.length);
        for (String person : people) {
            page.getByPlaceholder("Tag People").click();
            page.getByPlaceholder("Tag People").fill(person);
            page.waitForTimeout(1000);
            // Click the first autocomplete suggestion
            try {
                page.locator("[id^='el-autocomplete-'][id$='-item-0']").click();
            } catch (Exception e) {
                // Fallback: try role-based option selection
                page.getByRole(AriaRole.OPTION).first().click();
            }
            page.waitForTimeout(500);
        }
        log.info("Tagged {} people.", people.length);
    }

    /**
     * Fills the remarks/input field.
     */
    public void fillRemarks(String remarks) {
        log.info("Filling remarks: {}", remarks);
        page.getByPlaceholder("Please input").click();
        page.getByPlaceholder("Please input").fill(remarks);
        page.waitForTimeout(300);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  UPDATE DOCUMENTS & INITIATE
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks Update Documents and selects two document checkboxes, then clicks UPDATE TECHNICAL.
     * Must be called BEFORE initiateTechnical().
     */
    public void updateDocuments() {
        log.info("Updating documents (two checkboxes)...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update Documents")).click();
        page.waitForTimeout(1000);

        // Select first two checkboxes
        page.locator(".el-col > .el-checkbox > .el-checkbox__input > .el-checkbox__inner").first().click();
        page.waitForTimeout(300);
        page.locator("div:nth-child(2) > div > .el-checkbox > .el-checkbox__input > .el-checkbox__inner").click();
        page.waitForTimeout(300);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("UPDATE TECHNICAL")).click();
        page.waitForTimeout(2000);
        log.info("Documents updated (two checkboxes).");
    }

    /**
     * Updates documents with only the first checkbox selected (for Round 2).
     * Must be called BEFORE initiateTechnical().
     */
    public void updateDocumentsSingle() {
        log.info("Updating documents (single checkbox)...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update Documents")).click();
        page.waitForTimeout(1000);

        page.locator(".el-col > .el-checkbox > .el-checkbox__input > .el-checkbox__inner").first().click();
        page.waitForTimeout(300);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("UPDATE TECHNICAL")).click();
        page.waitForTimeout(2000);
        log.info("Documents updated (single checkbox).");
    }

    /**
     * Clicks "Initiate TECHNICAL" button.
     * Must be called AFTER updateDocuments() or updateDocumentsSingle().
     */
    public void initiateTechnical() {
        log.info("Clicking Initiate TECHNICAL...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Initiate TECHNICAL")).click();
        page.waitForTimeout(3000);
        log.info("TECHNICAL initiated.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  STATUS VERIFICATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Verifies the case status shows "Open".
     */
    public void verifyStatusOpen() {
        log.info("Verifying status is Open...");
        Locator openStatus = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions()
                .setName("Open").setExact(true)).locator("span");
        openStatus.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        openStatus.click();
        page.waitForTimeout(1000);
        log.info("Status verified: Open.");
    }

    /**
     * Verifies the case shows "Work in Progress" status.
     */
    public void verifyStatusWorkInProgress() {
        log.info("Verifying status is Work in Progress...");
        refreshSearch();
        expandApplicationRecord();
        page.getByText("Work in Progress", new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(1000);
        log.info("Status verified: Work in Progress.");
    }

    /**
     * Verifies "Report Received" status by refreshing and expanding until it appears.
     * Retries a few times since the vendor submission may take time to propagate.
     */
    public void verifyStatusReportReceived() {
        log.info("Verifying status is Report Received...");
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            refreshSearch();
            expandApplicationRecord();
            try {
                Locator status = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions()
                        .setName("Report Received").setExact(true)).locator("span");
                status.waitFor(new Locator.WaitForOptions().setTimeout(10000));
                status.click();
                page.waitForTimeout(1000);
                log.info("Status verified: Report Received.");
                return;
            } catch (Exception e) {
                log.info("Report Received not found yet, retry {}/{}...", i + 1, maxRetries);
                page.waitForTimeout(5000);
            }
        }
        // Final attempt without catching
        refreshSearch();
        expandApplicationRecord();
        page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions()
                .setName("Report Received").setExact(true)).locator("span").click();
        page.waitForTimeout(1000);
        log.info("Status verified: Report Received.");
    }

    /**
     * Verifies the case status is "Closed" after vetting.
     */
    public void verifyStatusClosed() {
        log.info("Verifying status is Closed...");
        refreshSearch();
        expandApplicationRecord();
        page.getByText("Closed").nth(1).click();
        page.waitForTimeout(1000);
        log.info("Status verified: Closed.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  REPORT HANDLING
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Accepts the received report by selecting "Accept report" radio and clicking Continue.
     */
    public void acceptReport() {
        log.info("Accepting report...");
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Accept report")).click();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        page.waitForTimeout(2000);
        log.info("Report accepted.");
    }

    /**
     * Uploads a report directly on the FI Dashboard (Round 2 approach — no Vendor Portal).
     * Uses waitForFileChooser to handle the file upload dialog triggered by clicking the button.
     */
    public void uploadReportDirect(String filePath) {
        log.info("Uploading report directly: {}", filePath);
        Locator uploadBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload Report").setExact(true));
        com.microsoft.playwright.FileChooser fileChooser = page.waitForFileChooser(() -> {
            uploadBtn.click();
        });
        fileChooser.setFiles(Paths.get(filePath));
        page.waitForTimeout(3000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).click();
        page.waitForTimeout(2000);
        log.info("Report uploaded directly.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TECHNICAL VETTING FORM
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Fills the technical vetting form after accepting the report.
     *
     * @param propertyValue       property value (e.g. "25630000")
     * @param propertyVisitOption "Yes" or "No"
     * @param observation         general observation text
     * @param remarks             remarks text
     * @param legalIssues         legal issues text
     * @param marketability       marketability option (e.g. "Very good", "Poor")
     * @param marketabilityReason reason if Poor / Very Poor (empty string if not applicable)
     * @param titleClearance      title clearance option (e.g. "Clear")
     * @param titleRemarks        title clearance remarks
     */
    public void fillVettingForm(String propertyValue, String propertyVisitOption,
                                String observation, String remarks, String legalIssues,
                                String marketability, String marketabilityReason,
                                String titleClearance, String titleRemarks) {
        log.info("Filling technical vetting form...");

        // Property value
        page.getByPlaceholder("Enter value").click();
        page.getByPlaceholder("Enter value").fill(propertyValue);
        page.waitForTimeout(300);

        // Property visit Yes/No
        page.locator("div").filter(new Locator.FilterOptions()
                        .setHasText(Pattern.compile("^Property visitYes No Date Of Visit$")))
                .getByPlaceholder("Select", new Locator.GetByPlaceholderOptions().setExact(true)).click();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.LIST).locator("li")
                .filter(new Locator.FilterOptions().setHasText(propertyVisitOption)).click();
        page.waitForTimeout(500);

        // Date of visit (select a date from calendar)
        page.getByPlaceholder("Select date").click();
        page.waitForTimeout(500);
        page.locator("tr:nth-child(3) > td:nth-child(2) > div").first().click();
        page.waitForTimeout(500);

        // Observation (first Type here field)
        page.getByPlaceholder("Type here").first().click();
        page.getByPlaceholder("Type here").first().fill(observation);
        page.waitForTimeout(300);

        // Remarks (second Type here field)
        page.getByPlaceholder("Type here").nth(1).click();
        page.getByPlaceholder("Type here").nth(1).fill(remarks);
        page.waitForTimeout(300);

        // Legal issues (third Type here field)
        page.getByPlaceholder("Type here").nth(2).click();
        page.getByPlaceholder("Type here").nth(2).fill(legalIssues);
        page.waitForTimeout(300);

        // Marketability dropdown (4th Select placeholder)
        page.getByPlaceholder("Select", new Page.GetByPlaceholderOptions().setExact(true)).nth(3).click();
        page.waitForTimeout(1000);
        page.getByText(marketability, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        // Marketability reason (if Poor/Very Poor, fill the reason field)
        if (marketabilityReason != null && !marketabilityReason.isEmpty()) {
            page.locator("div").filter(new Locator.FilterOptions()
                            .setHasText(Pattern.compile("^Marketability Reason If Poor \\/ Very Poor$")))
                    .getByPlaceholder("Type here").fill(marketabilityReason);
            page.waitForTimeout(300);
        }

        // Title clearance dropdown (5th Select placeholder)
        page.getByPlaceholder("Select", new Page.GetByPlaceholderOptions().setExact(true)).nth(4).click();
        page.waitForTimeout(1000);
        page.getByText(titleClearance, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        // Title remarks (textarea at index 2)
        page.locator("textarea").nth(2).click();
        page.locator("textarea").nth(2).fill(titleRemarks);
        page.waitForTimeout(300);

        // Additional remarks (textarea at index 3)
        page.locator("textarea").nth(3).click();
        page.locator("textarea").nth(3).fill(remarks);
        page.waitForTimeout(300);

        log.info("Technical vetting form filled.");
    }

    /**
     * Fills the technical vetting form for Round 2 which has slightly different field indices.
     * Round 2 has additional "Type here" fields for marketability reason and title remarks.
     */
    public void fillVettingFormRound2(String propertyValue, String propertyVisitOption,
                                      String observation, String remarks, String legalIssues,
                                      String marketability, String marketabilityReason,
                                      String titleClearance, String titleRemarks) {
        log.info("Filling technical vetting form (Round 2)...");

        // Property value
        page.getByPlaceholder("Enter value").click();
        page.getByPlaceholder("Enter value").press("Control+a");
        page.getByPlaceholder("Enter value").fill(propertyValue);
        page.waitForTimeout(300);

        // Property visit Yes/No
        page.locator("div").filter(new Locator.FilterOptions()
                        .setHasText(Pattern.compile("^Property visitYes No Date Of Visit$")))
                .getByPlaceholder("Select", new Locator.GetByPlaceholderOptions().setExact(true)).click();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.LIST).locator("li")
                .filter(new Locator.FilterOptions().setHasText(propertyVisitOption)).click();
        page.waitForTimeout(500);

        // Date of visit
        page.getByPlaceholder("Select date").click();
        page.waitForTimeout(500);
        page.locator("tr:nth-child(3) > td:nth-child(2) > div").first().click();
        page.waitForTimeout(500);

        // Observation (first Type here)
        page.getByPlaceholder("Type here").first().click();
        page.getByPlaceholder("Type here").first().fill(observation);
        page.waitForTimeout(300);

        // Remarks (second Type here)
        page.getByPlaceholder("Type here").nth(1).click();
        page.getByPlaceholder("Type here").nth(1).fill(remarks);
        page.waitForTimeout(300);

        // Legal issues (third Type here)
        page.getByPlaceholder("Type here").nth(2).click();
        page.getByPlaceholder("Type here").nth(2).fill(legalIssues);
        page.waitForTimeout(300);

        // Marketability dropdown
        page.getByPlaceholder("Select", new Page.GetByPlaceholderOptions().setExact(true)).nth(3).click();
        page.waitForTimeout(500);
        page.getByText(marketability).click();
        page.waitForTimeout(500);

        // Marketability reason (4th Type here — present in Round 2)
        page.getByPlaceholder("Type here").nth(3).click();
        page.getByPlaceholder("Type here").nth(3).fill(
                marketabilityReason != null && !marketabilityReason.isEmpty() ? marketabilityReason : remarks);
        page.waitForTimeout(300);

        // Title clearance dropdown
        page.getByPlaceholder("Select", new Page.GetByPlaceholderOptions().setExact(true)).nth(4).click();
        page.waitForTimeout(1000);
        page.getByText(titleClearance, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        // Title remarks (5th Type here)
        page.getByPlaceholder("Type here").nth(4).click();
        page.getByPlaceholder("Type here").nth(4).fill(titleRemarks);
        page.waitForTimeout(300);

        log.info("Technical vetting form (Round 2) filled.");
    }

    /**
     * Submits the technical vetting form.
     */
    public void submitVettingForm() {
        log.info("Submitting vetting form...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForTimeout(3000);
        log.info("Vetting form submitted.");
    }

    /**
     * Verifies "Vetting details saved" success message.
     */
    public void verifyVettingSaved() {
        log.info("Verifying vetting details saved...");
        Locator successMsg = page.getByText("Vetting details saved");
        successMsg.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        log.info("Vetting details saved successfully.");
    }

    /**
     * Verifies "Vetting Clear" text appears after Round 2 submission.
     */
    public void verifyVettingClear() {
        log.info("Verifying Vetting Clear...");
        refreshSearch();
        page.getByText("Vetting Clear").nth(1).click();
        page.waitForTimeout(1000);
        log.info("Vetting Clear verified.");
    }
}
