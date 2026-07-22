package ui.pages.jarvis_secured;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import java.util.regex.Pattern;
public class EntityCoApplicantPage extends BaseTest {
    private final Page page;
    public EntityCoApplicantPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    //  OPEN ENTITY CO-APPLICANT DETAILS
    public void openCoApplicantDetails(String coApplicantName) {
        log.info("Opening Entity CoApplicant details for: {}", coApplicantName);
        try {
            page.locator(".el-loading-mask").first()
                    .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(15000));
        } catch (Exception e) {
            log.info("No loading mask detected or already hidden.");
        }
        page.waitForTimeout(2000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName(Pattern.compile("CoApplicant Details"))).click();
        page.waitForTimeout(2000);

        Locator viewButtons = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View"));
        viewButtons.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        page.waitForTimeout(1000);
        Locator nameHeading = page.locator("h3").filter(new Locator.FilterOptions().setHasText(coApplicantName));
        int count = nameHeading.count();
        log.info("Found {} h3 elements with text '{}' in CoApplicant section", count, coApplicantName);

        if (count == 0) {
            throw new RuntimeException("Entity CoApplicant '" + coApplicantName + "' not found in the list.");
        }

        // Navigate from the h3 up to the details-card container, then find the View button
        nameHeading.first()
                .locator("xpath=ancestor::div[contains(@class,'details-card')]")
                .first()
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("View"))
                .click();
        log.info("Clicked View for entity co-applicant: {}", coApplicantName);
        page.waitForTimeout(1000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);

        log.info("Entity CoApplicant details opened for editing.");
    }

    // SUBMIT DIALOG (triggers mandatory field validation)
    public void submitDialog() {
        log.info("Submitting entity co-applicant dialog...");
        // Scroll to the bottom Submit button in the dialog and click it
        Locator submitBtn = page.getByRole(AriaRole.DIALOG)
                .locator("div.title >> text=Submit").last();
        submitBtn.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        submitBtn.click();
        page.waitForTimeout(3000);
        log.info("Entity co-applicant dialog submitted.");
    }

    // CLICK ARROW RIGHT ICON (navigate to next section)
    public void clickArrowRightIcon() {
        log.info("Clicking Arrow Right icon to navigate to next section...");
        page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("Arrow Right icon"))
                .getByRole(AriaRole.IMG).click();
        page.waitForTimeout(2000);
        log.info("Navigated to next section.");
    }

    // FILL OPERATIONAL DATE
    public void fillOperationalDate(String year, String month, String day) {
        log.info("Filling operational date: {}-{}-{}", year, month, day);
        page.getByPlaceholder("Pick the operational date").click();
        page.waitForTimeout(500);
        // Click on year header to open year picker
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName(Pattern.compile("\\d{4}"))).click();
        page.waitForTimeout(500);
        // Navigate to the correct year using Previous Year button. Click Previous Year until we find the target year
        for (int i = 0; i < 10; i++) {
            try {
                Locator yearOption = page.getByText(year, new Page.GetByTextOptions().setExact(true));
                if (yearOption.isVisible()) {
                    yearOption.click();
                    break;
                }
            } catch (Exception e) {
                // Year not visible yet
            }
            page.getByLabel("Previous Year").click();
            page.waitForTimeout(300);
        }
        page.waitForTimeout(500);

        // Select month
        page.getByText(month, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);
        // Select day
        page.getByText(day, new Page.GetByTextOptions().setExact(true)).first().click();
        page.waitForTimeout(1000);
        log.info("Operational date filled.");
    }

    //  FILL ENTITY SHAREHOLDING
    public void fillEntityShareholding(String shareholding) {
        log.info("Filling entity shareholding: {}", shareholding);
        page.getByPlaceholder("Enter the entity shareholding").click();
        page.getByPlaceholder("Enter the entity shareholding").fill(shareholding);
        page.waitForTimeout(500);
        log.info("Entity shareholding filled.");
    }

    //  SUBMIT FINAL DETAILS
    public void submitFinalDetails() {
        log.info("Submitting entity co-applicant final details...");
        // Click the Submit button in the dialog
        Locator submitBtn = page.getByRole(AriaRole.DIALOG)
                .locator("div.title >> text=Submit").last();
        submitBtn.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        submitBtn.click();
        page.waitForTimeout(2000);

        // Click the close/collapse button
        try {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("")).click();
            page.waitForTimeout(1000);
        } catch (Exception e) {
            log.info("No close button found after entity submit.");
        }

        log.info("Entity co-applicant details submitted successfully.");
        page.reload();
    }

    //  REASSIGN APPLICATION TO TENJIN
    public void reassignToTenjin(String level, String assigneeEmail) {
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

        // Dismiss any lingering notification that could block subsequent clicks
        try {
            Locator notification = page.locator(".el-notification");
            if (notification.count() > 0 && notification.first().isVisible()) {
                notification.first().locator(".el-notification__closeBtn").click();
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            log.info("No notification to dismiss after reassign.");
        }

        log.info("Application re-assigned to Tenjin successfully.");
    }

    //  MOVE TO CREDIT REVIEW
    public void moveToCreditReview() {
        log.info("Moving application to Credit Review...");
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to Credit Review").click();
        page.waitForTimeout(2000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Application moved to Credit Review.");
    }
}
