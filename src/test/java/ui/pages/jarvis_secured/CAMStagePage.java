package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

import java.util.regex.Pattern;

public class CAMStagePage extends BaseTest {

    private final Page page;

    public CAMStagePage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MOVE TO CREDIT REVIEW (triggers mandatory field validation)
    // ═══════════════════════════════════════════════════════════════════════════

    public void attemptMoveToCreditReview() {
        log.info("Attempting to move to Credit Review (will trigger mandatory field validation)...");

        // Ensure the page is fully loaded and the app form is in CAM stage
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);

        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(500);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(2000);
        page.getByText("Move to Credit Review").click();
        page.waitForTimeout(2000);
        log.info("Mandatory field validation triggered.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PRIMARY APPLICANT DETAILS — Aadhaar
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillAadhaarDetails(String lastFourDigits) {
        log.info("Filling Aadhaar last 4 digits: {}", lastFourDigits);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Primary Applicant Details")).click();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Enter Last 4 Digits of Aadhaar").click();
        page.getByPlaceholder("Enter Last 4 Digits of Aadhaar").fill(lastFourDigits);
        page.waitForTimeout(1000);
        log.info("Aadhaar last 4 digits entered.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  EMPLOYMENT DETAILS
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillEmploymentDetails(String employerCategory, String employeeId,
                                      String industrySector, String officeContact) {
        log.info("Filling Employment details...");
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Employment")).click();
        page.waitForTimeout(1000);

        page.getByPlaceholder("Select the Employer Category").click();
        page.getByText(employerCategory).click();
        page.waitForTimeout(500);

        page.getByPlaceholder("Enter the Employee ID").click();
        page.getByPlaceholder("Enter the Employee ID").fill(employeeId);

        page.getByPlaceholder("Select the Industry Sector").click();
        page.getByText(industrySector).click();
        page.waitForTimeout(500);

        page.getByPlaceholder("Enter Office Contact Number").click();
        page.getByPlaceholder("Enter Office Contact Number").fill(officeContact);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForTimeout(2000);
        log.info("Employment details submitted.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PERSONAL DETAILS
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillPersonalDetails(String residentialStatus) {
        log.info("Filling Personal details...");
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Personal")).click();
        page.waitForTimeout(1000);

        page.getByPlaceholder("Select the Residential Status").click();
        page.getByText(residentialStatus, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForTimeout(2000);
        log.info("Personal details submitted.");
        page.reload();
        page.waitForTimeout(1000);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SUBMIT PRIMARY APPLICANT & SELECT OVD
    // ═══════════════════════════════════════════════════════════════════════════

    public void submitPrimaryApplicantAndSelectOvd(String ovdType) {
        log.info("Submitting primary applicant details and selecting OVD: {}", ovdType);

        // Close/submit via the arrow button in the dialog
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("")).click();
        page.waitForTimeout(1000);

        page.getByRole(AriaRole.DIALOG).getByText("Submit Arrow Right icon").click();
        page.waitForTimeout(2000);

        // Select OVD type
        page.getByPlaceholder("Select the OVD").click();
        page.getByText(ovdType).click();
        page.waitForTimeout(500);

        // Confirm
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Primary applicant OVD selected and confirmed.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CO-APPLICANT NON-FINANCIAL DETAILS
    // ═══════════════════════════════════════════════════════════════════════════

    public void openCoApplicantDetails(String coApplicantName) {
        log.info("Opening CoApplicant details for: {}", coApplicantName);
        // Wait for any loading masks to disappear before interacting
        try {
            page.locator(".el-loading-mask").first()
                    .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(15000));
        } catch (Exception e) {
            log.info("No loading mask detected or already hidden.");
        }
        page.waitForTimeout(2000);
        //open the co-applicant details sections
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName(Pattern.compile("CoApplicant Details"))).click();
        page.waitForTimeout(2000);

        // Wait for co-applicant details to load — View buttons appear once loaded
        Locator viewButtons = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View"));
        viewButtons.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        page.waitForTimeout(1000);

        // Find all View buttons and identify which one belongs to the target co-applicant
        int viewCount = viewButtons.count();
        log.info("Found {} View buttons in CoApplicant section", viewCount);

        boolean found = false;
        for (int i = 0; i < viewCount; i++) {
            // Get the parent row of each View button and check if it contains the target name
            Locator parentRow = viewButtons.nth(i).locator("xpath=ancestor::div[contains(.,'" + coApplicantName + "')]").first();
            String rowText = parentRow.textContent();
            if (rowText != null && rowText.contains(coApplicantName)) {
                log.info("Found co-applicant '{}' at View button index: {}", coApplicantName, i);
                viewButtons.nth(i).click();
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("CoApplicant '" + coApplicantName + "' not found in the list.");
        }
        page.waitForTimeout(1000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);

        log.info("CoApplicant details opened for editing.");
    }

    public void submitCoApplicantAndSelectOvd(String ovdType) {
        log.info("Submitting co-applicant details and selecting OVD: {}", ovdType);
        // Submit the co-applicant edit dialog using the Submit button within the dialog
        page.getByRole(AriaRole.DIALOG)
                .locator("div.title", new Locator.LocatorOptions().setHasText("Submit")).last().click();
        page.waitForTimeout(2000);

        // Handle OVD selection
        page.getByPlaceholder("Select the OVD").click();
        page.getByText(ovdType).click();
        page.waitForTimeout(500);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
        page.waitForTimeout(2000);

        log.info("CoApplicant OVD selected.");
    }

    public void fillCoApplicantVoterIdAndVerify(String voterId) {
        log.info("Filling Voter ID: {}", voterId);

        page.getByPlaceholder("Enter voter id").click();
        page.getByPlaceholder("Enter voter id").fill(voterId);
        page.getByText("Verify").click();
        page.waitForTimeout(3000);

        log.info("Voter ID entered and verification triggered.");
    }

    public void fillCoApplicantLastNameAndResidentialStatus(String lastName, String residentialStatus) {
        log.info("Filling co-applicant last name [{}] and residential status [{}]", lastName, residentialStatus);

        page.getByPlaceholder("Enter the last name").click();
        page.getByPlaceholder("Enter the last name").fill(lastName);
        page.waitForTimeout(500);

        page.getByPlaceholder("Select the residential status").click();
        page.locator("li").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^" + residentialStatus + "$"))).click();
        page.waitForTimeout(500);

        log.info("Co-applicant personal details filled.");
    }

    public void submitCoApplicantFinalDetails() {
        log.info("Submitting co-applicant final details...");

        // Click the submit arrow icon in the dialog
        page.getByRole(AriaRole.DIALOG).locator("div")
                .filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Arrow Right icon$"))).click();
        page.waitForTimeout(2000);

        // Confirm
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        log.info("Co-applicant non-financial details submitted successfully.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CO-APPLICANT FINANCIAL DETAILS (Hannah)
    // ═══════════════════════════════════════════════════════════════════════════

    public void openFinancialCoApplicantDetails(String coApplicantName) {
        log.info("Opening Financial CoApplicant details for: {}", coApplicantName);
        // Wait for any loading masks to disappear
        try {
            page.locator(".el-loading-mask").first()
                    .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(15000));
        } catch (Exception e) {
            log.info("No loading mask detected or already hidden.");
        }
        page.waitForTimeout(2000);

        // Expand CoApplicant Details section
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName(Pattern.compile("CoApplicant Details"))).click();
        page.waitForTimeout(2000);

        // Wait for co-applicant details to load — View buttons appear once loaded
        Locator viewButtons = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View"));
        viewButtons.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        page.waitForTimeout(1000);

        // Find all View buttons and identify which one belongs to the target co-applicant
        int viewCount = viewButtons.count();
        log.info("Found {} View buttons in CoApplicant section", viewCount);

        boolean found = false;
        for (int i = 0; i < viewCount; i++) {
            Locator parentRow = viewButtons.nth(i).locator("xpath=ancestor::div[contains(.,'" + coApplicantName + "')]").first();
            String rowText = parentRow.textContent();
            if (rowText != null && rowText.contains(coApplicantName)) {
                log.info("Found financial co-applicant '{}' at View button index: {}", coApplicantName, i);
                viewButtons.nth(i).click();
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("Financial CoApplicant '" + coApplicantName + "' not found in the list.");
        }
        page.waitForTimeout(1000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);

        log.info("Financial CoApplicant details opened for editing.");
    }

    public void submitFinancialCoApplicantDialog() {
        log.info("Submitting financial co-applicant dialog...");
        page.getByRole(AriaRole.DIALOG).getByText("Submit").click();
        page.waitForTimeout(2000);
        log.info("Financial co-applicant dialog submitted.");
    }

    public void fillFinancialCoApplicantMandatoryFields(String aadhaarLast4, String residentialStatus,
                                                        String employerCategory, String employeeId,
                                                        String industrySector, String officeContact) {
        log.info("Filling financial co-applicant mandatory fields...");

        // Aadhaar last 4 digits
        page.getByPlaceholder("Enter aadhaar last 4 digits").click();
        page.getByPlaceholder("Enter aadhaar last 4 digits").fill(aadhaarLast4);
        page.waitForTimeout(500);

        // Residential status
        page.getByPlaceholder("Select the residential status").click();
        page.getByText(residentialStatus, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        // Employer category
        page.getByPlaceholder("Select the category").nth(1).click();
        page.locator("li").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^" + employerCategory + "$"))).click();
        page.waitForTimeout(500);

        // Employee ID
        page.getByPlaceholder("Enter the employee id").click();
        page.getByPlaceholder("Enter the employee id").fill(employeeId);
        page.waitForTimeout(500);

        // Industry sector
        page.getByPlaceholder("Select the industry sector").click();
        page.getByText(industrySector).click();
        page.waitForTimeout(500);

        // Office contact number
        page.getByPlaceholder("Enter office contact Number").click();
        page.getByPlaceholder("Enter office contact Number").fill(officeContact);
        page.waitForTimeout(500);

        log.info("Financial co-applicant mandatory fields filled.");
    }

    public void submitFinancialCoApplicantFinalDetails() {
        log.info("Submitting financial co-applicant final details...");
        // Click the submit FAB button to confirm
        page.locator("div:nth-child(29) > .el-col > .cs-fab > .info").click();
        page.waitForTimeout(2000);

        // Confirm if Yes button appears
        try {
            Locator yesButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes"));
            if (yesButton.isVisible()) {
                yesButton.click();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.waitForTimeout(3000);
            }
        } catch (Exception e) {
            log.info("No confirmation dialog appeared for financial co-applicant.");
        }

        log.info("Financial co-applicant details submitted successfully.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  RELOAD PAGE
    // ═══════════════════════════════════════════════════════════════════════════

    public void reloadPage() {
        log.info("Reloading appform page...");
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
    }
}
