package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import ui.Utils.ScreenshotUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginDeskPage extends BaseTest {

    private final Page page;

    // ── Login Locators ───────────────────────────────────────────────────────
    private static final String EMAIL_INPUT_LABEL = "Email or phone";
    private static final String PASSWORD_INPUT_LABEL = "Enter your password";
    private static final String NEXT_TEXT = "Next";

    // ── Dashboard / Search Locators ──────────────────────────────────────────
    private static final String SEARCH_TYPE_DROPDOWN = ".search-by-filter .el-input__inner";
    private static final String SEARCH_INPUT = ".search-by-filter input[type='text']:not([readonly])";
    private static final String FIRST_ROW_APP_ID_LINK = "table tbody tr:first-child td .app-id p, " +
            "table tbody tr:first-child [class*='app-id'], " +
            "table tbody tr:first-child td:nth-child(2) p";
    private static final String APPLICATION_SIDEBAR_LINK = "a[href='/application']";

    // ── Application Actions Locators ─────────────────────────────────────────
    private static final String ACTIONS_DROPDOWN = "input[placeholder='Application Actions'], input[placeholder='moveToNextStage']";
    private static final String REASSIGN_MODAL = ".assign-container";
    private static final String REASSIGN_BUTTON = ".assign-btn:has-text('ReAssign')";

    private static final Pattern PARTNER_LOAN_ID_PATTERN = Pattern.compile("partnerLoanId=([^&]+)");

    public LoginDeskPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  LOGIN
    // ═══════════════════════════════════════════════════════════════════════════

    public void login() {
        log.info("Starting Jarvis Secured Login process...");

        page.getByLabel(EMAIL_INPUT_LABEL).last().fill(getUserEmail());
        page.getByText(NEXT_TEXT).click();

        page.getByLabel(PASSWORD_INPUT_LABEL).fill(getUserPassword());
        page.getByText(NEXT_TEXT).last().click();

        log.info("Jarvis Secured Login submitted.");

        page.waitForTimeout(10000);
        assertThat(page).hasTitle("jarvis");
        log.info("Successfully landed on Jarvis Dashboard (Secured).");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  URL CAPTURE
    // ═══════════════════════════════════════════════════════════════════════════

    public String capturePartnerLoanIdFromUrl() {
        String currentUrl = page.url();
        Matcher matcher = PARTNER_LOAN_ID_PATTERN.matcher(currentUrl);

        if (!matcher.find()) {
            throw new RuntimeException("Could not find partnerLoanId in current URL: " + currentUrl);
        }

        String partnerLoanId = matcher.group(1);
        log.info("Captured Partner Loan ID from URL: {}", partnerLoanId);
        return partnerLoanId;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  NAVIGATION & SEARCH
    // ═══════════════════════════════════════════════════════════════════════════

    public void navigateToApplicationTab() {
        log.info("Navigating to Application tab in Jarvis sidebar (Secured)...");
        page.locator(APPLICATION_SIDEBAR_LINK).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        log.info("Application tab loaded.");
    }

    public void searchByPartnerLid(String partnerLoanId) {
        log.info("Searching Jarvis application by Partner LID: {}", partnerLoanId);

        Locator dropdownTrigger = page.locator(SEARCH_TYPE_DROPDOWN).first();
        dropdownTrigger.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        dropdownTrigger.click();

        String optionXpath = "//li[contains(@class,'el-select-dropdown__item')]//span[text()='Partner LID']";
        page.locator(optionXpath).click();
        log.info("Selected 'Partner LID' from search criteria dropdown.");

        Locator searchInput = page.locator(SEARCH_INPUT).last();
        searchInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        searchInput.fill(partnerLoanId);

        log.info("Triggering search...");
        page.keyboard().press("Enter");

        Locator loadingMask = page.locator(".el-loading-mask").first();
        try {
            loadingMask.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(10000));
        } catch (Exception e) {
            log.info("No loading mask detected or it disappeared instantly.");
        }

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1500);
        log.info("Search results loaded.");
    }

    public String getAppFormIdFromFirstRow() {
        log.info("Extracting App ID from the first row...");

        Locator appIdLocator = page.locator("table tbody tr:first-child .app-id p.app-id-ellipse").first();
        appIdLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        String appId = appIdLocator.innerText().trim();
        if (appId.isEmpty()) {
            throw new RuntimeException("App ID was found but the text was empty!");
        }

        log.info("Captured App ID from first row: {}", appId);
        return appId;
    }

    public void openFirstApplication() {
        log.info("Opening the first application in the list...");
        Locator firstRow = page.locator(FIRST_ROW_APP_ID_LINK).first();
        firstRow.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        firstRow.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Application form opened successfully.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  APPLICATION ACTIONS
    // ═══════════════════════════════════════════════════════════════════════════

    public void reassignApplication(String level, String assigneeEmail, String scenarioName) {
        log.info("Reassigning application to level [{}], user [{}]", level, assigneeEmail);

        selectActionFromDropdown("ReAssign");
        page.waitForTimeout(1000);

        // Wait for ReAssign modal
        Locator reassignModal = page.locator(REASSIGN_MODAL);
        reassignModal.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        log.info("ReAssign modal appeared.");

        selectLevelAndAssignee(level, assigneeEmail);

        // Click ReAssign button
        Locator reassignBtn = page.locator(REASSIGN_BUTTON);
        reassignBtn.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        reassignBtn.click();
        log.info("Clicked ReAssign button.");

        // Wait for success notification
        Locator successNotification = page.locator(".el-notification__title")
                .filter(new Locator.FilterOptions().setHasText("Success"));
        try {
            successNotification.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            log.info("ReAssign successful — notification received.");
        } catch (Exception e) {
            log.error("ReAssign may have failed — no success notification received.");
            ScreenshotUtil.saveScreenshot(page, "ReAssign_NoSuccess", scenarioName);
            throw new AssertionError("ReAssign failed: no success notification appeared.", e);
        }

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
    }

    public void moveToCam(String scenarioName) {
        log.info("Moving application to CAM stage...");

        selectActionFromDropdown("Move to CAM");
        handleOptionalFIWarning();
        handleActionResponse("Move to CAM", scenarioName);

        log.info("Application moved to CAM stage successfully.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PRIVATE HELPERS
    // ═══════════════════════════════════════════════════════════════════════════

    private void selectActionFromDropdown(String actionName) {
        log.info("Selecting Application Action: [{}]", actionName);

        Locator actionsDropdown = page.locator(ACTIONS_DROPDOWN);
        actionsDropdown.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        actionsDropdown.first().click(new Locator.ClickOptions().setForce(true));

        Locator actionOption = page.locator("li.el-select-dropdown__item:visible")
                .filter(new Locator.FilterOptions().setHasText(actionName))
                .first();
        actionOption.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        actionOption.click(new Locator.ClickOptions().setForce(true));
        log.info("Successfully clicked action: [{}]", actionName);
    }

    private void selectLevelAndAssignee(String level, String assigneeEmail) {
        log.info("Selecting Level: {}", level);
        Locator levelInput = page.locator("//label[text()='Level']/following-sibling::div//input").first();
        levelInput.click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(500);

        Locator levelOption = page.locator("li.el-select-dropdown__item:visible")
                .filter(new Locator.FilterOptions().setHasText(level)).first();
        levelOption.click(new Locator.ClickOptions().setForce(true));

        log.info("Searching and selecting Assignee: {}", assigneeEmail);
        Locator assignInput = page.locator("//label[text()='Assigned to']/following-sibling::div//input").first();
        assignInput.click(new Locator.ClickOptions().setForce(true));

        String searchTerm = assigneeEmail.split("@")[0].split("\\.")[0];
        page.keyboard().type(searchTerm);
        page.waitForTimeout(1000);

        Locator assigneeOption = page.locator("li.el-select-dropdown__item:visible")
                .filter(new Locator.FilterOptions().setHasText(assigneeEmail)).first();
        assigneeOption.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assigneeOption.click(new Locator.ClickOptions().setForce(true));
        log.info("Assignee [{}] selected.", assigneeEmail);
    }

    private void handleActionResponse(String actionName, String scenarioName) {
        log.info("Waiting for system response after clicking action...");

        Locator errorToast = page.locator(".el-notification__title")
                .filter(new Locator.FilterOptions().setHasText("Authorization Error"));
        Locator acceptModal = page.locator(".el-dialog__body .accept-btn");
        Locator successToast = page.locator(".el-notification__title")
                .filter(new Locator.FilterOptions().setHasText("Success"));

        try {
            acceptModal.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));

            log.info("Confirmation modal appeared. Clicking Accept...");
            acceptModal.click();

            successToast.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            log.info("Success notification verified. Action '{}' completed.", actionName);

            page.waitForLoadState(LoadState.NETWORKIDLE);

        } catch (Exception e) {
            if (errorToast.isVisible()) {
                String errorMsg = page.locator(".el-notification__content").first().innerText();
                log.error("Authorization Error blocked the workflow: {}", errorMsg);
                ScreenshotUtil.saveScreenshot(page, "AuthError_" + actionName.replace(" ", ""), scenarioName);
                throw new AssertionError("Failed to move workflow. Authorization Error: " + errorMsg);
            } else {
                log.error("Unknown error occurred after clicking Application Action: {}", actionName);
                ScreenshotUtil.saveScreenshot(page, "UnknownError_" + actionName.replace(" ", ""), scenarioName);
                throw new RuntimeException("Expected confirmation modal did not appear for action: " + actionName, e);
            }
        }
    }

    private void handleOptionalFIWarning() {
        Locator fiWarningBox = page.locator(".el-message-box")
                .filter(new Locator.FilterOptions().setHasText("No FI has been triggered"));

        try {
            fiWarningBox.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(3000));
            log.info("FI Warning popup appeared. Clicking 'Yes'.");

            Locator yesBtn = fiWarningBox.locator(".el-message-box__btns button.el-button--primary")
                    .filter(new Locator.FilterOptions().setHasText("Yes"))
                    .first();
            yesBtn.click(new Locator.ClickOptions().setForce(true));
            page.waitForTimeout(1000);
        } catch (Exception e) {
            log.info("No FI warning popup detected. Proceeding.");
        }
    }
}
