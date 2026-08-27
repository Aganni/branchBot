package ui.pages.jarvis_secured_HLR;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import java.util.regex.Pattern;

public class FinancialCoApplicantPage extends BaseTest {
    private final Page page;
    public FinancialCoApplicantPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    //  OPEN CO-APPLICANT DETAILS
    public void openCoApplicantDetails(String coApplicantName) {
        log.info("Opening Financial CoApplicant details for: {}", coApplicantName);
        // Wait for any loading masks to disappear
        try {
            page.locator(".el-loading-mask").first()
                    .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(15000));
        } catch (Exception e) {
            log.info("No loading mask detected or already hidden.");
        }
        page.waitForTimeout(2000);

        // Expand CoApplicant Details section
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName(Pattern.compile("CoApplicant Details"))).click();
        page.waitForTimeout(2000);

        // Wait for co-applicant details to load — View buttons appear once loaded
        Locator viewButtons = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View"));
        viewButtons.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        page.waitForTimeout(1000);

        // Find the co-applicant by their h3 name heading, then click the View button in the same card
        Locator nameHeading = page.locator("h3").filter(new Locator.FilterOptions().setHasText(coApplicantName));
        int count = nameHeading.count();
        log.info("Found {} h3 elements with text '{}' in CoApplicant section", count, coApplicantName);

        if (count == 0) {
            throw new RuntimeException("Financial CoApplicant '" + coApplicantName + "' not found in the list.");
        }

        // Navigate from the h3 up to the details-card container, then find the View button within it
        nameHeading.first()
                .locator("xpath=ancestor::div[contains(@class,'details-card')]")
                .first()
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("View"))
                .click();
        log.info("Clicked View for financial co-applicant: {}", coApplicantName);
        page.waitForTimeout(1000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);

        log.info("Financial CoApplicant details opened for editing.");
    }

    //  SUBMIT DIALOG (triggers mandatory field validation)
  public void submitDialog() {
        log.info("Submitting financial co-applicant dialog...");
        // Scroll to the bottom Submit button in the dialog and click it
        Locator submitBtn = page.getByRole(AriaRole.DIALOG)
                .locator("div.title >> text=Submit").last();
        submitBtn.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        submitBtn.click();
        page.waitForTimeout(3000);
        log.info("Financial co-applicant dialog submitted.");
    }

    //  FILL MANDATORY FIELDS
    public void fillMandatoryFields(String aadhaarLast4, String residentialStatus,
                                    String employerCategory, String employeeId,
                                    String industrySector, String officeContact) {
        log.info("Filling financial co-applicant mandatory fields...");

        // Wait for the aadhaar field to be visible (indicates form is ready)
        page.getByPlaceholder("Enter aadhaar last 4 digits").waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));

        // Aadhaar last 4 digits
        page.getByPlaceholder("Enter aadhaar last 4 digits").click();
        page.getByPlaceholder("Enter aadhaar last 4 digits").fill(aadhaarLast4);
        page.waitForTimeout(500);

        // Residential status
        page.getByPlaceholder("Select the residential status").click();
        page.getByText(residentialStatus, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        // Employer category
        page.getByPlaceholder("Select the category").nth(1).click();
        page.locator("li").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^" + employerCategory + "$"))).click();
        page.waitForTimeout(500);

        // Employee ID
        page.getByPlaceholder("Enter the employee id").click();
        page.getByPlaceholder("Enter the employee id").fill(employeeId);
        page.waitForTimeout(500);

        // Industry sector
        page.getByPlaceholder("Select the industry sector").click();
        page.getByText(industrySector).click();
        page.waitForTimeout(500);

        // Office contact number
        page.getByPlaceholder("Enter office contact Number").click();
        page.getByPlaceholder("Enter office contact Number").fill(officeContact);
        page.waitForTimeout(500);

        log.info("Financial co-applicant mandatory fields filled.");
    }

    //  SUBMIT FINAL DETAILS
    public void submitFinalDetails() {
        log.info("Submitting financial co-applicant final details...");
        // Click the submit FAB button to confirm
        page.locator("div:nth-child(29) > .el-col > .cs-fab > .info").click();
        page.waitForTimeout(2000);
        // Confirm if Yes button appears
        try {
            Locator yesButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes"));
            if (yesButton.isVisible()) {
                yesButton.click();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.waitForTimeout(3000);
            }
        } catch (Exception e) {
            log.info("No confirmation dialog appeared for financial co-applicant.");
        }
        log.info("Financial co-applicant details submitted successfully.");
    }
}
