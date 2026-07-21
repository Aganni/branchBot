package ui.pages.jarvis_secured;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
public class ProgramValidationPage extends BaseTest {
    private final Page page;
    public ProgramValidationPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    //  OPEN PARTNER DETAILS & EDIT
    public void openPartnerDetailsForEdit() {
        log.info("Opening Partner Details for editing...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Partner Details")).click();
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);
        log.info("Partner Details opened for editing.");
    }

    //  SELECT PROGRAM
    public void selectProgram(String programName) {
        log.info("Selecting program: {}", programName);
        page.getByPlaceholder("Select the Program").click();
        page.waitForTimeout(500);
        page.locator("li").filter(new Locator.FilterOptions().setHasText(programName)).click();
        page.waitForTimeout(1000);
        log.info("Program '{}' selected.", programName);
    }

    //  SUBMIT PARTNER DETAILS
    public void submitPartnerDetails() {
        log.info("Submitting Partner Details...");
        page.locator("[id=\"DSA\\ Details\"]").getByText("Submit Arrow Right icon").click();
        page.waitForTimeout(2000);
        log.info("Partner Details submitted.");
        page.reload();
    }

    //  COLLAPSE PARTNER DETAILS SECTION
   public void collapsePartnerDetails() {
        log.info("Skipping collapse — page reload in moveToCreditReview will reset the view.");
    }

    //  MOVE TO CREDIT REVIEW
    public void moveToCreditReview() {
        log.info("Moving application to Credit Review after program validation...");

        // Reload to clear any lingering dropdown/overlay state
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        // Dismiss any notification that might block the dropdown
        try {
            Locator notification = page.locator(".el-notification");
            if (notification.count() > 0 && notification.first().isVisible()) {
                notification.first().locator(".el-notification__closeBtn").click();
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            log.info("No notification to dismiss.");
        }

        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(500);
        page.getByPlaceholder("Application Actions").click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(1000);
        page.locator("li").filter(new Locator.FilterOptions().setHasText("Move to Credit Review")).click();
        page.waitForTimeout(2000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Application moved to Credit Review.");
    }
}
