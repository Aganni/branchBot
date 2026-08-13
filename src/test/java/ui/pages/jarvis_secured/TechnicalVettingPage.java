package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

/**
 * Page Object for Technical Vetting interactions on the Jarvis main application page.
 * Handles:
 *   - Moving to Credit Approval (triggers technical vetting error)
 *   - Re-assigning the application
 *   - Triggering the verification process
 *   - Selecting TECHNICAL tab and triggering technical vetting
 *   - Opening FI/FCU Dashboard via "Go to Dashboard" popup
 *   - Handling error/success dialogs (Check Details, View TECHNICAL Details)
 */
public class TechnicalVettingPage extends BaseTest {

    private final Page page;

    public TechnicalVettingPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MOVE TO CREDIT APPROVAL (initial attempt — triggers reassign flow)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * First "Move to Credit Approval" click — just selects the action from dropdown.
     * This may show a stage-movement dialog or just a toast alert.
     * If a dialog appears, it will be cancelled before proceeding.
     */
    public void clickMoveToCreditApproval() {
        log.info("Clicking Move to Credit Approval (initial)...");
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to Credit Approval").click();
        page.waitForTimeout(3000);

        // If a dialog with "Cancel" button appeared, cancel it
        try {
            Locator cancelBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel"));
            if (cancelBtn.isVisible()) {
                cancelBtn.first().click();
                page.waitForTimeout(1000);
                log.info("Cancelled the Move to Credit Approval dialog.");
            }
        } catch (Exception e) {
            log.info("No dialog to cancel after Move to Credit Approval.");
        }

        log.info("Move to Credit Approval clicked.");
    }

    /**
     * Dismisses the alert that appears after the initial Move to Credit Approval.
     */
    public void dismissAlert() {
        log.info("Dismissing alert...");
        try {
            Locator alert = page.getByRole(AriaRole.ALERT).locator("div").nth(2);
            if (alert.isVisible()) {
                alert.click();
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            log.info("No alert to dismiss.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  RE-ASSIGN APPLICATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Re-assigns the application to a specific level and user via Application Actions.
     * The Re-Assign dialog shows Level + User fields.
     */
    public void reassignApplication(String level, String userEmail) {
        log.info("Re-assigning application to level [{}], user [{}]", level, userEmail);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Re-Assign").click();
        page.waitForTimeout(2000);

        // Select Level — use last() to target the Re-Assign dialog (not any leftover dialog)
        page.locator(".el-form-item__content > .el-select > .el-input").last().click();
        page.waitForTimeout(500);

        // Click User field to dismiss level dropdown, then re-open and select
        page.getByPlaceholder("User email id").last().click();
        page.waitForTimeout(500);
        page.locator(".el-form-item__content > .el-select > .el-input").last().click();
        page.waitForTimeout(500);
        page.getByText(level).click();
        page.waitForTimeout(1000);

        // Select User
        page.getByPlaceholder("User email id").last().click();
        page.waitForTimeout(500);
        page.getByText(userEmail).click();
        page.waitForTimeout(500);

        // Click ReAssign button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ReAssign")).click();
        page.waitForTimeout(3000);

        // Dismiss alert
        dismissAlert();
        log.info("Application re-assigned.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MOVE TO CREDIT APPROVAL (with level/user dialog — triggers vetting error)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Attempts to move the application to Credit Approval via the full dialog.
     * Navigates to App Form tab, scrolls right to find Application Actions,
     * selects Move to Credit Approval, fills level/user and clicks Accept.
     * This triggers the "Technical vetting should be..." error until vetting is complete.
     */
    public void attemptMoveToCreditApproval(String level, String userEmail) {
        log.info("Attempting Move to Credit Approval with level [{}], user [{}]...", level, userEmail);

        // Ensure we're on App Form tab and page is loaded
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        // Navigate to App Form tab if not already there
        try {
            Locator appFormTab = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("App Form"));
            if (appFormTab.isVisible()) {
                appFormTab.click();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.waitForTimeout(3000);
            }
        } catch (Exception e) {
            log.info("Already on App Form tab or tab not found.");
        }

        // Scroll right to make Application Actions visible
        // Use keyboard End key or scroll the page container to the right
        page.keyboard().press("End");
        page.waitForTimeout(2000);

        // Scroll Application Actions into view and click
        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(2000);
        page.locator("li").filter(new Locator.FilterOptions().setHasText("Move to Credit Approval")).click();
        page.waitForTimeout(3000);

        // Fill level and user in the dialog
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
        log.info("Move to Credit Approval attempted.");
    }

    /**
     * Verifies that the "Technical vetting should be" error message is displayed.
     */
    public void verifyTechnicalVettingError() {
        log.info("Verifying technical vetting error message...");
        Locator errorMsg = page.getByText("Technical vetting should be");
        errorMsg.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        log.info("Technical vetting error message verified.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TRIGGER VERIFICATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks the "Trigger Verification" button on the error dialog.
     */
    public void clickTriggerVerification() {
        log.info("Clicking Trigger Verification...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Trigger Verification")).click();
        page.waitForTimeout(2000);
        log.info("Trigger Verification clicked.");
    }

    /**
     * Selects the TECHNICAL tab and triggers technical vetting by selecting
     * a collateral checkbox and clicking TRIGGER TECHNICAL.
     */
    public void selectTechnicalAndTrigger() {
        log.info("Selecting TECHNICAL tab and triggering...");
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("TECHNICAL")).click();
        page.waitForTimeout(1000);

        // Select the collateral checkbox
        page.locator("label span").nth(1).click();
        page.waitForTimeout(500);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("TRIGGER TECHNICAL")).click();
        page.waitForTimeout(2000);
        log.info("TECHNICAL triggered.");
    }

    /**
     * Clicks "Go to Dashboard" which opens the FI/FCU Dashboard in a popup.
     * Returns the new popup Page instance.
     */
    public Page openFiDashboard() {
        log.info("Opening FI/FCU Dashboard via 'Go to Dashboard'...");
        Page popup = page.waitForPopup(() -> {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Go to Dashboard")).click();
        });
        popup.waitForLoadState(LoadState.NETWORKIDLE);
        popup.waitForTimeout(3000);
        log.info("FI/FCU Dashboard popup opened.");
        return popup;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ERROR DIALOG HANDLING (Round 2)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Clicks "Check Details" button on the error dialog (for round 2).
     */
    public void clickCheckDetails() {
        log.info("Clicking Check Details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Check Details")).click();
        page.waitForTimeout(2000);
    }

    /**
     * After Check Details, selects TECHNICAL tab and clicks RE-TRIGGER 2nd TECHNICAL,
     * then confirms with Re-Initiate.
     */
    public void retriggerSecondTechnical() {
        log.info("Re-triggering 2nd TECHNICAL...");
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("TECHNICAL")).click();
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("RE-TRIGGER 2nd TECHNICAL")).click();
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Re-Initiate")).click();
        page.waitForTimeout(3000);
        log.info("2nd TECHNICAL re-triggered.");
    }

    /**
     * Closes any visible dialog/notification (Close button).
     */
    public void closeDialog() {
        log.info("Closing dialog...");
        try {
            Locator closeBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close"));
            if (closeBtn.isVisible()) {
                closeBtn.click();
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            log.info("No Close button found, continuing.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  FINAL MOVE TO CREDIT APPROVAL (after both rounds complete)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Final Move to Credit Approval after technical vetting is complete.
     * Uses the simple dropdown selection (no level/user dialog needed as it's already assigned).
     */
    public void finalMoveToCreditApproval(String level, String userEmail) {
        log.info("Final Move to Credit Approval...");
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to Credit Approval").click();
        page.waitForTimeout(1000);

        page.getByPlaceholder("All Level").click();
        page.waitForTimeout(500);
        page.getByText(level).click();
        page.waitForTimeout(500);

        page.getByPlaceholder("User email id").click();
        page.waitForTimeout(500);
        page.getByText(userEmail).click();
        page.waitForTimeout(500);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept")).click();
        page.waitForTimeout(3000);
        log.info("Final Move to Credit Approval completed.");
    }
}
