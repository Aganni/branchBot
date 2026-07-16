package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import ui.Utils.ScreenshotUtil;
public class LoginDeskPage extends BaseTest {

    private final Page page;
    public LoginDeskPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void login() {
        log.info("Starting Jarvis Secured Login process...");
        page.getByLabel("Email or phone").fill(getUserEmail());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        page.getByLabel("Enter your password").click();
        page.getByLabel("Enter your password").fill(getUserPassword());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        page.waitForTimeout(10000);
        log.info("Jarvis Secured Login submitted.");
    }

    //  NAVIGATION & SEARCH
    public void navigateToApplicationTab() {
        log.info("Navigating to Application tab...");
        page.locator("a[href='/application']").click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("Application tab loaded.");
    }
    public void filterByProductAndStatus(String product, String status) {
        log.info("Filtering by Product [{}] and Status [{}]", product, status);
        page.getByPlaceholder("Products").click();
        page.getByPlaceholder("Products").fill(product.toLowerCase());
        page.getByRole(AriaRole.LISTITEM).getByText(product).click();
        page.waitForTimeout(1000);
        page.getByPlaceholder("All Status").click();
        page.getByPlaceholder("All Status").fill(status.toLowerCase());
        page.getByText(status).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("Filters applied.");
    }
    public void openApplicationByName(String applicantName) {
        log.info("Opening application: {}", applicantName);
        page.getByText(applicantName).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Application opened.");
    }
    public void openFirstApplication() {
        log.info("Opening the first application in the list...");
        Locator firstRow = page.locator("table tbody tr:first-child td .app-id p, " +
                "table tbody tr:first-child [class*='app-id'], " +
                "table tbody tr:first-child td:nth-child(2) p").first();
        firstRow.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        firstRow.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Application opened.");
    }

    //  APPLICATION ACTIONS
    public void reassignApplication(String level, String assigneeEmail, String scenarioName) {
        log.info("Reassigning application to level [{}], user [{}]", level, assigneeEmail);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Re-Assign").click();
        page.waitForTimeout(1000);
        // Select Level
        page.getByPlaceholder("All Level").click();
        page.locator("li").filter(new Locator.FilterOptions().setHasText(level)).click();
        page.waitForTimeout(500);
        // Select User
        page.getByPlaceholder("User email id").click();
        page.waitForTimeout(1000);
        page.getByText(assigneeEmail).click();
        page.waitForTimeout(500);
        // Click ReAssign button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ReAssign")).click();
        log.info("Clicked ReAssign button.");
        // Wait for success and close
        page.waitForTimeout(3000);
        try {
            Locator closeBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close"));
            if (closeBtn.isVisible()) {
                closeBtn.click();
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            log.info("No Close button detected, proceeding.");
        }
        page.waitForLoadState(LoadState.NETWORKIDLE);
        log.info("Application re-assigned successfully.");
    }

    public void moveToCam(String scenarioName) {
        log.info("Moving application to CAM stage...");
        page.waitForTimeout(5000);
        try {
            Locator notification = page.locator(".el-notification");
            if (notification.count() > 0 && notification.first().isVisible()) {
                notification.first().locator(".el-notification__closeBtn").click();
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            log.info("No notification to dismiss.");
        }

        // Reload and wait for page to settle
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        // Click Application Actions (Move to CAM dropdown on the right)
        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(500);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to CAM").click();
        page.waitForTimeout(2000);

        // Click Accept on the confirmation dialog
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept")).click();

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Application moved to CAM stage successfully.");
    }
}
