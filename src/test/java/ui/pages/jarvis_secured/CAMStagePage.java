package ui.pages.jarvis_secured;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

public class CAMStagePage extends BaseTest {

    private final Page page;

    public CAMStagePage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MOVE TO CREDIT REVIEW (triggers mandatory field validation)
    // ═══════════════════════════════════════════════════════════════════════════

    public void attemptMoveToCreditReview() {
        log.info("Attempting to move to Credit Review (will trigger mandatory field validation)...");
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);
        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(500);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(2000);
        page.getByText("Move to Credit Review").click();
        page.waitForTimeout(2000);
        log.info("Mandatory field validation triggered.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PRIMARY APPLICANT DETAILS — Aadhaar
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillAadhaarDetails(String lastFourDigits) {
        log.info("Filling Aadhaar last 4 digits: {}", lastFourDigits);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Primary Applicant Details")).click();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Enter Last 4 Digits of Aadhaar").click();
        page.getByPlaceholder("Enter Last 4 Digits of Aadhaar").fill(lastFourDigits);
        page.waitForTimeout(1000);
        log.info("Aadhaar last 4 digits entered.");
    }

    //  EMPLOYMENT DETAILS
    public void fillEmploymentDetails(String employerCategory, String employeeId,
                                      String industrySector, String officeContact) {
        log.info("Filling Employment details...");
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Employment")).click();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Select the Employer Category").click();
        page.getByText(employerCategory).click();
        page.waitForTimeout(500);
        page.getByPlaceholder("Enter the Employee ID").click();
        page.getByPlaceholder("Enter the Employee ID").fill(employeeId);
        page.getByPlaceholder("Select the Industry Sector").click();
        page.getByText(industrySector).click();
        page.waitForTimeout(500);
        page.getByPlaceholder("Enter Office Contact Number").click();
        page.getByPlaceholder("Enter Office Contact Number").fill(officeContact);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForTimeout(2000);
        log.info("Employment details submitted.");
    }

    //  PERSONAL DETAILS
    public void fillPersonalDetails(String residentialStatus) {
        log.info("Filling Personal details...");
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Personal")).click();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Select the Residential Status").click();
        page.getByText(residentialStatus, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForTimeout(2000);
        log.info("Personal details submitted.");
        page.reload();
        page.waitForTimeout(1000);
    }

    //  SUBMIT PRIMARY APPLICANT & SELECT OVD
    public void submitPrimaryApplicantAndSelectOvd(String ovdType) {
        log.info("Submitting primary applicant details and selecting OVD: {}", ovdType);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("")).click();
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.DIALOG).getByText("Submit Arrow Right icon").click();
        page.waitForTimeout(2000);
        page.getByPlaceholder("Select the OVD").click();
        page.getByText(ovdType).click();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Primary applicant OVD selected and confirmed.");
    }

    //  RELOAD PAGE
    public void reloadPage() {
        log.info("Reloading appform page");
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
    }
}
