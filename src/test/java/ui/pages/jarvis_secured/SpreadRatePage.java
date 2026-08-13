package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

/**
 * Page Object for Loan Requirements & Terms / Spread Rate section in the Docket Initiation stage.
 * Handles:
 *   - Opening Loan Requirements & Terms accordion
 *   - Editing and submitting spread rate for rate approval
 */
public class SpreadRatePage extends BaseTest {

    private final Page page;

    public SpreadRatePage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    /**
     * Attempts to move to Docket Initiation. Expects a "Please Initiate Rate approval" validation message.
     */
    public void attemptMoveToDocketInitiation() {
        log.info("Attempting Move to Docket Initiation (expecting rate approval validation)...");
        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to Docket Initiation").click();
        page.waitForTimeout(2000);
        log.info("Move to Docket Initiation attempted.");
    }

    /**
     * Clicks on the "Please Initiate Rate approval" validation message to navigate to rate section.
     */
    public void clickRateApprovalValidationMessage() {
        log.info("Clicking rate approval validation message...");
        page.getByText("Please Initiate Rate approval").click();
        page.waitForTimeout(2000);
        log.info("Navigated to Loan Requirements & Terms section.");
    }

    /**
     * Opens the Loan Requirements & Terms accordion section.
     */
    public void openLoanRequirementsSection() {
        log.info("Opening Loan Requirements & Terms section...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Loan Requirements & Terms")).click();
        page.waitForTimeout(1000);
        log.info("Loan Requirements & Terms section opened.");
    }

    /**
     * Clicks the Edit button to enable editing of spread rate.
     */
    public void clickEdit() {
        log.info("Clicking Edit on Loan Requirements & Terms...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);
        log.info("Loan Requirements & Terms in edit mode.");
    }

    /**
     * Fills in the spread rate value.
     *
     * @param spreadRate the spread rate value (e.g. "-3")
     */
    public void fillSpreadRate(String spreadRate) {
        log.info("Filling spread rate: {}", spreadRate);
        Locator spreadInput = page.getByPlaceholder("Enter the spread rate");
        spreadInput.click();
        spreadInput.fill(spreadRate);
        page.waitForTimeout(500);
        log.info("Spread rate filled: {}", spreadRate);
    }

    /**
     * Clicks Submit to save the spread rate and initiate rate approval.
     */
    public void submitSpreadRate() {
        log.info("Submitting spread rate...");
        page.getByText("Submit").click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        log.info("Spread rate submitted — rate approval initiated.");
    }

    /**
     * Completes the full spread rate flow:
     * Click validation message → Open section → Edit → Fill spread → Submit
     *
     * @param spreadRate the spread rate value (e.g. "-3")
     */
    public void completeSpreadRate(String spreadRate) {
        log.info("── Completing Spread Rate / Rate Approval ──");
        clickRateApprovalValidationMessage();
        openLoanRequirementsSection();
        clickEdit();
        fillSpreadRate(spreadRate);
        submitSpreadRate();
        log.info("Spread Rate completed successfully.");
    }
}
