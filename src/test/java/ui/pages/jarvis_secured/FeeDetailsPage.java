package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

/**
 * Page Object for Fee Details section in the Docket Initiation stage.
 * Handles:
 *   - Opening Fee Details accordion
 *   - Editing and submitting processing fee percentage
 */
public class FeeDetailsPage extends BaseTest {

    private final Page page;

    public FeeDetailsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    /**
     * Attempts to move to Docket Initiation. Expects a "Please enter processing Fee" validation message.
     */
    public void attemptMoveToDocketInitiation() {
        log.info("Attempting Move to Docket Initiation (expecting fee validation)...");
        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to Docket Initiation").click();
        page.waitForTimeout(2000);
        log.info("Move to Docket Initiation attempted.");
    }

    /**
     * Clicks on the "Please enter processing Fee" validation message to navigate to fee section.
     */
    public void clickFeeValidationMessage() {
        log.info("Clicking fee validation message...");
        page.getByText("Please enter processing Fee").click();
        page.waitForTimeout(2000);
        log.info("Navigated to Fee Details section.");
    }

    /**
     * Opens the Fee Details accordion section.
     */
    public void openFeeDetailsSection() {
        log.info("Opening Fee Details section...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Fee Details")).click();
        page.waitForTimeout(1000);
        log.info("Fee Details section opened.");
    }

    /**
     * Clicks the Edit button to enable editing of fee details.
     */
    public void clickEdit() {
        log.info("Clicking Edit on Fee Details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);
        log.info("Fee Details in edit mode.");
    }

    /**
     * Fills in the processing fee percentage value.
     *
     * @param feePercentage the processing fee percentage (e.g. "1.50")
     */
    public void fillProcessingFeePercentage(String feePercentage) {
        log.info("Filling processing fee percentage: {}", feePercentage);
        Locator feeInput = page.getByPlaceholder("Enter the processing fee percentage");
        feeInput.click();
        feeInput.fill(feePercentage);
        page.waitForTimeout(500);
        log.info("Processing fee percentage filled: {}", feePercentage);
    }

    /**
     * Clicks Submit to save the fee details.
     * After submission, waits and reloads the page to ensure Application Actions is available.
     */
    public void submitFeeDetails() {
        log.info("Submitting fee details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        // Reload page after fee submission to ensure Application Actions is re-rendered
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Fee details submitted. Page reloaded.");
    }

    /**
     * Completes the full fee details flow:
     * Opens Fee Details → Edit → Fill percentage → Submit
     *
     * @param feePercentage the processing fee percentage (e.g. "1.50")
     */
    public void completeFeeDetails(String feePercentage) {
        log.info("── Completing Fee Details ──");
        clickFeeValidationMessage();
        openFeeDetailsSection();
        clickEdit();
        fillProcessingFeePercentage(feePercentage);
        submitFeeDetails();
        log.info("Fee Details completed successfully.");
    }
}
