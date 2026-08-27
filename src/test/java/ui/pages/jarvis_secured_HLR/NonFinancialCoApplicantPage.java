package ui.pages.jarvis_secured_HLR;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import java.util.regex.Pattern;

public class NonFinancialCoApplicantPage extends BaseTest {
    private final Page page;
    public NonFinancialCoApplicantPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    //  OPEN CO-APPLICANT DETAILS
    public void openCoApplicantDetails(String coApplicantName) {
        log.info("Opening Non-Financial CoApplicant details for: {}", coApplicantName);
        // Wait for any loading masks to disappear before interacting
        try {
            page.locator(".el-loading-mask").first()
                    .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(15000));
        } catch (Exception e) {
            log.info("No loading mask detected or already hidden.");
        }
        page.waitForTimeout(2000);

        // Open the co-applicant details section
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
            throw new RuntimeException("Non-Financial CoApplicant '" + coApplicantName + "' not found in the list.");
        }

        // Navigate from the h3 up to the details-card container, then find the View button within it
        nameHeading.first()
                .locator("xpath=ancestor::div[contains(@class,'details-card')]")
                .first()
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("View"))
                .click();
        log.info("Clicked View for non-financial co-applicant: {}", coApplicantName);
        page.waitForTimeout(1000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);

        log.info("Non-Financial CoApplicant details opened for editing.");
    }

    //  SUBMIT & SELECT OVD
    public void submitAndSelectOvd(String ovdType) {
        log.info("Submitting non-financial co-applicant details and selecting OVD: {}", ovdType);
        // Scroll to the bottom Submit button in the dialog and click it
        Locator submitBtn = page.getByRole(AriaRole.DIALOG)
                .locator("div.title >> text=Submit").last();
        submitBtn.scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        submitBtn.click();
        page.waitForTimeout(3000);

        // Handle OVD selection
        page.getByPlaceholder("Select the OVD").click();
        page.getByText(ovdType).click();
        page.waitForTimeout(500);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
        page.waitForTimeout(2000);
        log.info("Non-Financial CoApplicant OVD selected.");
    }

    //  FILL VOTER ID & VERIFY
    public void fillVoterIdAndVerify(String voterId) {
        log.info("Filling Voter ID: {}", voterId);
        page.getByPlaceholder("Enter voter id").click();
        page.getByPlaceholder("Enter voter id").fill(voterId);
        page.getByText("Verify").click();
        page.waitForTimeout(5000);
        log.info("Voter ID entered and verification triggered.");
    }

    //  FILL LAST NAME & RESIDENTIAL STATUS
    public void fillLastNameAndResidentialStatus(String lastName, String residentialStatus) {
        page.getByPlaceholder("Enter the last name").click();
        page.getByPlaceholder("Enter the last name").fill(lastName);
        page.waitForTimeout(3500);
        page.getByPlaceholder("Select the residential status").click();
        page.locator("li").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^" + residentialStatus + "$"))).click();
        page.waitForTimeout(500);
        log.info("Filled the co-applicant last name [{}] and residential status [{}]", lastName, residentialStatus);
    }

    //  SUBMIT FINAL DETAILS
    public void submitFinalDetails() {
        // Click the submit arrow icon in the dialog
        page.getByRole(AriaRole.DIALOG).locator("div")
                .filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Arrow Right icon$"))).click();
        page.waitForTimeout(2000);
        // Confirm
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Non-financial co-applicant details submitted successfully.");
    }
}
