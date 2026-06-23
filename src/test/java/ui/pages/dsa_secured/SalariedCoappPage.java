package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import java.util.Map;

public class SalariedCoappPage extends BaseTest {
    private final Page page;

    public SalariedCoappPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Navigate to Co-Applicants and add a new applicant
    // ─────────────────────────────────────────────────────────────────────────
    public void clickAddApplicant() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("CO APPLICANTS")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ADD APPLICANT")).click();
        log.info("Clicked ADD APPLICANT on Co-Applicants page.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // KYC / Personal Details
    // ─────────────────────────────────────────────────────────────────────────
    public void fillPersonalDetails(Map<String, String> data) {
        log.info("Filling Co-Applicant personal details...");

        // Wait for form to mount
        page.getByLabel("PAN Number *").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(30000));

        // Applicant Type — native select, same pattern as PrimaryApplicantPage
        page.getByLabel("Applicant Type").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("applicant_type"))).click();

        // Co-applicant Type (Salaried / Self-employed etc.)
        page.getByLabel("Type *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("type")).setExact(true)).click();

        // PAN + Verify
        page.getByLabel("PAN Number *").click();
        page.getByLabel("PAN Number *").fill(data.get("pan"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).click();
        log.info("Clicked Verify for co-applicant PAN: {}", data.get("pan"));

        // Handle existing profile — click COPY PROFILE if banner appears
        Locator copyProfileBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("COPY PROFILE"));
        try {
            copyProfileBtn.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE).setTimeout(8000));
            copyProfileBtn.click();
            log.info("Existing co-applicant profile found — clicked COPY PROFILE.");
            page.waitForTimeout(2000);
        } catch (Exception e) {
            log.info("No existing profile for co-applicant — filling fields manually.");
            fillRemainingPersonalFields(data);
        }

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        log.info("Co-applicant personal details submitted.");
    }

    private void fillRemainingPersonalFields(Map<String, String> data) {
        // Relationship with primary applicant
        page.getByLabel("Relationship with applicant *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("relationship"))).click();

        page.getByLabel("Phone Number *").fill(data.get("phone"));
        page.getByPlaceholder("Enter email address").fill(data.get("email"));

        // Gender — rendered as a custom select with placeholder
        page.getByPlaceholder("Select Gender").click();
        page.getByText(data.get("gender"), new Page.GetByTextOptions().setExact(true)).click();

        page.getByLabel("Salutation *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("salutation")).setExact(true)).click();

        page.getByLabel("Father Name *").fill(data.get("father_name"));
        page.getByLabel("Mother Name *").fill(data.get("mother_name"));

        selectDropdown("Category *", data.get("category"));
        selectDropdown("Religion *", data.get("religion"));
        selectDropdown("Education *", data.get("education"));

        // Nationality — use .first() to avoid matching multiple elements
        page.getByLabel("Nationality *").first().click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("nationality"))).click();

        // Marital Status — rendered as a text input with placeholder, not a button
        page.getByPlaceholder("Select Marital Status").click();
        page.getByText(data.get("marital_status"), new Page.GetByTextOptions().setExact(true)).click();

        if ("Married".equalsIgnoreCase(data.get("marital_status"))) {
            page.getByLabel("Spouse Name").fill(data.get("spouse_name"));
        }

        selectDropdown("Disability *", data.get("disability"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Address Details
    // ─────────────────────────────────────────────────────────────────────────
    public void fillAddressDetails(Map<String, String> data) {
        log.info("Filling Co-Applicant address details...");

        page.getByPlaceholder("Current Address (Line 1)").fill(data.get("address_line1"));
        page.keyboard().press("Tab");

        selectDropdown("Current Address Ownership *", data.get("address_ownership"));

        // Same as current address radio buttons
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes")).first().click();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes")).nth(1).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        log.info("Co-applicant address details submitted.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Employment Details (Salaried)
    // ─────────────────────────────────────────────────────────────────────────
    public void fillEmploymentDetails(Map<String, String> data) {
        log.info("Filling Co-Applicant employment details...");

        selectDropdown("Employment Type", data.get("employment_type"));

        // Employer autocomplete
        page.getByPlaceholder("Search Your Company Here").fill(data.get("company_search"));
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("company_full_name"))).click();

        page.getByLabel("Total Monthly Income *").fill(data.get("monthly_income"));
        page.getByLabel("Office Email *").fill(data.get("office_email"));

        // Designation — use ID selector (consistent with PrimaryApplicantPage)
        page.locator("#designation").fill(data.get("designation"));

        page.getByLabel("Office Address Line 1 *").fill(data.get("office_line1"));
        page.getByLabel("Office Address Line 2 *").fill(data.get("office_line2"));
        page.getByLabel("Office Address Pincode *").fill(data.get("office_pin"));

        selectDropdown("Office Address Ownership *", data.get("office_ownership"));

        log.info("Co-applicant employment details filled.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Submit
    // ─────────────────────────────────────────────────────────────────────────
    public void clickSubmit() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForSelector("text=Linked individual added");
        log.info("Co-applicant submitted successfully.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper
    // ─────────────────────────────────────────────────────────────────────────
    private void selectDropdown(String buttonName, String value) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(buttonName)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(value)).click();
    }
}
