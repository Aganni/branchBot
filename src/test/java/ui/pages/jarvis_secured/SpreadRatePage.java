package ui.pages.jarvis_secured;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;
public class SpreadRatePage extends BaseTest {
    private final Page page;

    public SpreadRatePage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }
    public void attemptMoveToDocketInitiation() {
        log.info("Attempting Move to Docket Initiation (expecting rate approval validation)...");
        page.evaluate("window.scrollTo(0, 0)");
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(2000);
        page.locator("li").filter(new Locator.FilterOptions().setHasText("Move to Docket Initiation")).click();
        page.waitForTimeout(2000);
        log.info("Move to Docket Initiation attempted.");
    }
public void clickRateApprovalValidationMessage() {
        log.info("Clicking rate approval validation message...");
        Locator validationMsg = page.getByText("Please capture insurance details before moving to next stage.");
        validationMsg.waitFor(new Locator.WaitForOptions().setTimeout(150000)); // 2.5 min timeout
        validationMsg.click();
        page.waitForTimeout(2000);
        log.info("Navigated to Loan Requirements & Terms section.");
    }
public void openLoanRequirementsSection() {
        log.info("Opening Loan Requirements & Terms section...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Loan Requirements & Terms")).click();
        page.waitForTimeout(1000);
        log.info("Loan Requirements & Terms section opened.");
    }
public void clickEdit() {
        log.info("Clicking Edit on Loan Requirements & Terms...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);
        log.info("Loan Requirements & Terms in edit mode.");
    }
public void fillSpreadRate(String spreadRate) {
        log.info("Filling spread rate: {}", spreadRate);
        Locator spreadInput = page.getByPlaceholder("Enter the spread rate");
        spreadInput.click();
        spreadInput.fill(spreadRate);
        page.waitForTimeout(500);
        log.info("Spread rate filled: {}", spreadRate);
    }
public void submitSpreadRate() {
        log.info("Submitting spread rate...");
        page.getByText("Submit").click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        log.info("Spread rate submitted — rate approval initiated.");
    }
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
