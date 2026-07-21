package ui.pages.jarvis_secured;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;
public class LoanRequirementsPage extends BaseTest {
    private final Page page;
    public LoanRequirementsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // FILL LOAN REQUIREMENTS & TERMS
    public void fillLoanRequirements() {
        log.info("Filling Loan Requirements & Terms...");
        // Open Loan Requirements section
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Loan Requirements & Terms")).click();
        page.waitForTimeout(1000);
        // Click Edit
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);
        // Requested loan amount
        page.getByPlaceholder("Enter the requested amount").click();
        page.getByPlaceholder("Enter the requested amount").fill("50000000");
        page.waitForTimeout(500);
        // Sanction amount
        page.getByPlaceholder("Enter the amount").click();
        page.getByPlaceholder("Enter the amount").fill("19568400");
        page.waitForTimeout(500);
        // Tenure
        page.getByPlaceholder("Enter the tenure").click();
        page.getByPlaceholder("Enter the tenure").fill("240");
        page.waitForTimeout(500);
        // Requested tenure
        page.getByPlaceholder("Enter the requested tenure").click();
        page.getByPlaceholder("Enter the requested tenure").fill("360");
        page.waitForTimeout(500);
        // Save/Submit (click the save icon)
        page.locator(".el-col > .cs-fab > .info").click();
        page.waitForTimeout(2000);
        log.info("Loan Requirements & Terms filled and submitted.");
    }
}
