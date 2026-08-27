package ui.pages.jarvis_secured_HLR;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
public class CRMandatoryDetailsPage extends BaseTest {
    private final Page page;
    public CRMandatoryDetailsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // PRIMARY APPLICANT — EMPLOYMENT DETAILS
    public void openPrimaryApplicantDetails() {
        log.info("Opening Primary Applicant Details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Primary Applicant Details")).click();
        page.waitForTimeout(500);
        log.info("Primary Applicant Details opened.");
    }
    public void clickEmploymentTab() {
        log.info("Clicking Employment tab...");
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Employment")).click();
        page.waitForTimeout(500);
        log.info("Employment tab active.");
    }
    public void fillTotalWorkExperience(String years) {
        log.info("Filling Total Work Experience: {}", years);
        page.getByPlaceholder("Enter Total Work Experience").click();
        page.getByPlaceholder("Enter Total Work Experience").fill(years);
        page.waitForTimeout(300);
    }
    public void fillTotalYearsInCurrent(String years) {
        log.info("Filling Total Years in Current: {}", years);
        page.getByPlaceholder("Enter Total Years in Current").click();
        page.getByPlaceholder("Enter Total Years in Current").fill(years);
        page.waitForTimeout(300);
    }

    //Clicks the Submit button to save Primary Applicant details.
     public void submitPrimaryApplicant() {
        log.info("Submitting Primary Applicant details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForTimeout(1000);
        // Wait for "Individual updated" confirmation
        try {
            page.getByText("Individual updated").waitFor(new Locator.WaitForOptions().setTimeout(2000));
            log.info("Primary Applicant updated successfully.");
        } catch (Exception e) {
            log.info("No 'Individual updated' toast seen, continuing...");
        }
        // Dismiss notification if visible
        dismissNotification();
    }

    public void closeModal() {
        log.info("Closing modal...");
        page.locator("button.close-btn.is-circle").click();
        page.waitForTimeout(1000);
        // Wait for the dialog wrapper to disappear
        try {
            page.locator(".el-dialog__wrapper").first()
                    .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(1000));
        } catch (Exception e) {
            log.info("Dialog wrapper still present, waiting additional time...");
            page.waitForTimeout(2000);
        }
        log.info("Modal closed.");
    }

    // FINANCIAL CO-APPLICANT — WORK EXPERIENCE DETAILS
    public void openCoApplicantDetails() {
        log.info("Opening CoApplicant Details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("CoApplicant Details 20.00%")).click();
        page.waitForTimeout(2000);
        log.info("CoApplicant Details opened.");
    }
    public void viewFinancialCoApplicant() {
        log.info("Opening financial co-applicant (Hannah) view...");
        // Try multiple ancestor levels to find the container holding both h3 and View button
        Locator viewBtn = page.locator("xpath=(//h3[normalize-space()='Hannah'])[1]/ancestor::div[2]//button[normalize-space()='View']");
        if (viewBtn.count() == 0) {
            viewBtn = page.locator("xpath=(//h3[normalize-space()='Hannah'])[1]/ancestor::div[3]//button[normalize-space()='View']");
        }
        if (viewBtn.count() == 0) {
            viewBtn = page.locator("xpath=(//h3[normalize-space()='Hannah'])[1]/ancestor::div[4]//button[normalize-space()='View']");
        }
        viewBtn.first().click();
        page.waitForTimeout(2000);
        log.info("Financial co-applicant (Hannah) view opened.");
    }

    //Clicks the EDIT button on the co-applicant details modal to enter edit mode.
    public void clickEditOnCoApplicantModal() {
        log.info("Clicking EDIT on co-applicant details modal...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("EDIT")).click();
        page.waitForTimeout(2000);
        log.info("Edit mode active on co-applicant modal.");
    }
    public void openCoApplicantEmploymentSection() {
        log.info("Opening co-applicant employment section...");
        page.locator("div:nth-child(29) > .el-col > .cs-fab > .info").click();
        page.waitForTimeout(1000);
        log.info("Employment section opened.");
    }
    public void fillCoApplicantWorkExp(String years) {
        log.info("Filling co-applicant work exp: {}", years);
        page.getByPlaceholder("Enter work exp").click();
        page.getByPlaceholder("Enter work exp").fill(years);
        page.waitForTimeout(300);
    }

    //Fills the total years in current for co-applicant.
    public void fillCoApplicantYearsInCurrent(String years) {
        log.info("Filling co-applicant total years in current: {}", years);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        Locator field = page.getByPlaceholder("Enter total years in current");
        field.scrollIntoViewIfNeeded();
        page.waitForTimeout(500);
        field.click();
        field.fill(years);
        page.waitForTimeout(300);
    }

    //Saves the co-applicant employment details by clicking the info icon again.
    public void saveCoApplicantEmployment() {
        log.info("Saving co-applicant employment details...");
        page.locator("div:nth-child(29) > .el-col > .cs-fab > .info").click();
        page.waitForTimeout(2000);
        log.info("Co-applicant employment saved.");
    }

    // ENTITY CO-APPLICANT — INDUSTRY/REVENUE DETAILS
    public void openAndEditEntityCoApplicant() {
        log.info("Opening entity co-applicant (Amazon.com Inc.)...");
        // First open the CoApplicant Details dialog
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("CoApplicant Details 20.00%")).click();
        page.waitForTimeout(2000);
        // Scroll down within the dialog to see Amazon.com Inc.
        page.locator(".el-dialog__body").last().evaluate("el => el.scrollTop = el.scrollHeight");
        page.waitForTimeout(1000);

        // Click View button for Amazon.com Inc. — try multiple ancestor levels
        Locator viewBtn = page.locator("xpath=(//h3[normalize-space()='Amazon.com Inc.'])[1]/ancestor::div[2]//button[normalize-space()='View']");
        if (viewBtn.count() == 0) {
            viewBtn = page.locator("xpath=(//h3[normalize-space()='Amazon.com Inc.'])[1]/ancestor::div[3]//button[normalize-space()='View']");
        }
        if (viewBtn.count() == 0) {
            viewBtn = page.locator("xpath=(//h3[normalize-space()='Amazon.com Inc.'])[1]/ancestor::div[4]//button[normalize-space()='View']");
        }
        viewBtn.first().click();
        page.waitForTimeout(2000);

        // Click EDIT on the Amazon details modal
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("EDIT")).click();
        page.waitForTimeout(2000);
        log.info("Entity co-applicant (Amazon.com Inc.) edit mode active.");
    }

    public void openEntityDetailsSection() {
        log.info("Opening entity details section...");
        page.locator("div:nth-child(22) > .el-col > .cs-fab > .info").click();
        page.waitForTimeout(1000);
        log.info("Entity details section opened.");
    }

    public void selectIndustrySector(String sector) {
        log.info("Selecting industry sector: {}", sector);
        page.getByPlaceholder("Select the industry sector").click();
        page.waitForTimeout(500);
        page.locator("li").filter(new Locator.FilterOptions().setHasText(sector)).click();
        page.waitForTimeout(500);
        log.info("Industry sector selected: {}", sector);
    }

    public void selectProductCategory(String category) {
        log.info("Selecting product category: {}", category);
        page.getByPlaceholder("Select the product category").click();
        page.waitForTimeout(500);
        page.getByText(category).click();
        page.waitForTimeout(500);
        log.info("Product category selected: {}", category);
    }

    //Fills the revenue field.
    public void fillRevenue(String revenue) {
        log.info("Filling revenue: {}", revenue);
        page.getByPlaceholder("Enter the revenue").click();
        page.getByPlaceholder("Enter the revenue").fill(revenue);
        page.waitForTimeout(300);
    }

    //Clicks Verify, then Submit in the dialog to save entity details. After submission, refreshes the page.
    public void verifyAndSubmitEntity() {
        log.info("Verifying and submitting entity details...");
        page.getByText("Verify").click();
        page.waitForTimeout(2000);
        page.getByRole(AriaRole.DIALOG).getByText("Submit").click();
        page.waitForTimeout(3000);
        log.info("Entity details submitted.");
        // Refresh the page after submission
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Page refreshed after entity submission.");
    }

    //Navigates to the entity co-applicant (Amazon.com Inc.) section.
    public void openEntityCoApplicant() {
        log.info("Opening entity co-applicant (Amazon.com Inc.)...");
        page.locator("[id=\"Co-Applicant\\ Details\"] div")
                .filter(new Locator.FilterOptions().setHasText("Amazon.com Inc. Details Edit")).nth(1).click();
        page.waitForTimeout(2000);
        log.info("Entity co-applicant section opened.");
    }

    //STAGE MOVEMENTS
    public void moveToCreditApproval(String level, String userEmail) {
        log.info("Moving to Credit Approval with level [{}], user [{}]...", level, userEmail);
        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to Credit Approval").click();
        page.waitForTimeout(2000);

        page.getByPlaceholder("All Level").click();
        page.waitForTimeout(500);
        page.locator("li").filter(new Locator.FilterOptions().setHasText(level)).click();
        page.waitForTimeout(500);

        page.getByPlaceholder("User email id").click();
        page.waitForTimeout(500);
        page.getByText(userEmail).click();
        page.waitForTimeout(500);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept")).click();
        page.waitForTimeout(3000);
        // Wait for success confirmations
        try {
            page.getByText("Workflow updated").waitFor(new Locator.WaitForOptions().setTimeout(10000));
            log.info("Workflow updated confirmation received.");
        } catch (Exception e) {
            log.info("No 'Workflow updated' toast seen.");
        }
        try {
            page.getByText("Assigned successfully").waitFor(new Locator.WaitForOptions().setTimeout(5000));
            log.info("Assigned successfully confirmation received.");
        } catch (Exception e) {
            log.info("No 'Assigned successfully' toast seen.");
        }
        log.info("Moved to Credit Approval.");
    }

    //Navigates to the Credit Approval stage by clicking the status circle.
    public void navigateToCreditApprovalStage() {
        log.info("Navigating to Credit Approval stage...");
        page.waitForTimeout(2000);
        page.locator("div:nth-child(6) > .status-circle > .status-dim").click();
        page.waitForTimeout(1000);
        page.getByText("Credit Approval", new Page.GetByTextOptions().setExact(true)).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("On Credit Approval stage.");
    }

    //Attempts to move to Terms stage. First attempt triggers Reg Check validation.
    public void attemptMoveToTerms() {
        log.info("Attempting Move to Terms...");
        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to Terms").click();
        page.waitForTimeout(3000);
        log.info("Move to Terms attempted.");
    }

    //Resolves the Regulatory Check by clicking on the Reg. Check link. Just visiting the Reg Check tab resolves it.
    public void resolveRegCheck() {
        log.info("Resolving Regulatory Check...");
        // Click "Resolve Regulatory check" message or "Reg. Check" link
        try {
            page.getByText("Resolve Regulatory check").waitFor(new Locator.WaitForOptions().setTimeout(5000));
            log.info("Reg Check validation message detected.");
        } catch (Exception e) {
            log.info("No explicit Reg Check message, proceeding to click Reg. Check link.");
        }
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Reg. Check")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Reg. Check tab visited — resolved.");

        // Go back to App Form
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("App Form")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Back on App Form tab.");
    }

    public void moveToTerms() {
        log.info("Moving to Terms stage...");
        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.locator("li").filter(new Locator.FilterOptions().setHasText("Move to Terms")).click();
        page.waitForTimeout(2000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept")).click();
        page.waitForTimeout(3000);

        // Wait for success confirmation
        try {
            page.getByText("Workflow updated").waitFor(new Locator.WaitForOptions().setTimeout(10000));
            log.info("Workflow updated — moved to Terms.");
        } catch (Exception e) {
            log.info("No 'Workflow updated' toast seen after Terms move.");
        }
        log.info("Application moved to Terms stage.");
    }

    // UTILITIES
    private void dismissNotification() {
        try {
            Locator closeBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(""));
            if (closeBtn.first().isVisible()) {
                closeBtn.first().click();
                page.waitForTimeout(500);
            }
        } catch (Exception e) {
            // No notification to dismiss
        }
    }
}
