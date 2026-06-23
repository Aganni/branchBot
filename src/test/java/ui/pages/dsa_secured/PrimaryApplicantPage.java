package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import java.util.Map;

public class PrimaryApplicantPage extends BaseTest {
    private final Page page;
    private static final String NEXT_BTN = "role=button[name='NEXT']";
    private static final String CO_APPLICANTS_BTN = "role=button[name='CO APPLICANTS']";

    public PrimaryApplicantPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void completeKycTab(Map<String, String> data) {
  

        // 1. Select Applicant Type
        page.getByLabel("Applicant Type").click();

        // FIX 1: Change role to OPTION so it registers the click event and dismisses the dropdown menu container
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Individual")).click();

        // Safeguard: Force close the overlay if it gets stuck to clean the viewport state
        page.keyboard().press("Escape");
        page.waitForTimeout(500);

        // 2. Interact with PAN Number
        // FIX 2: Use the exact underlying DOM selector path to remain immune to aria-hidden locks
        Locator panInput = page.locator("#panNumber");
        panInput.waitFor();
        panInput.click();
        panInput.fill(data.get("pan"));

        // 3. Press Enter to submit/trigger the profile check callback
        page.keyboard().press("Enter");
        log.info("Submitted PAN for verification: " + data.get("pan"));
        page.waitForTimeout(2000); // Wait for profile auto-fetch to resolve

        // 4. Email Verification Link
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email ID")).fill(data.get("email"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("VERIFY")).click();
        page.waitForSelector("text=Email+SMS with the consent link sent successfully");

        // 5. Personal Relationships
        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Father Name")).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("father_name"))).click();

        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Mother Name")).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("mother_name"))).click();

        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Spouse Name")).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("spouse_name"))).click();

        // Demographics & Background profile
        selectDropdown("Category", data.get("category"));
        selectDropdown("Religion", data.get("religion"));
        selectDropdown("Education", data.get("education"));
        selectDropdown("Marital Status", data.get("marital_status"));
        selectDropdown("Nationality", data.get("nationality"));
        selectDropdown("Disability", data.get("disability"));
        selectDropdown("Preferred Address", data.get("preferred_address"));
        selectDropdown("Related Interest?", data.get("related_interest"));
        selectDropdown("Related Party Control**", data.get("related_party_control"));

        page.click(NEXT_BTN);
        log.info("KYC sub-tab details submitted successfully.");
    }

    public void completeAddressesTab(Map<String, String> data) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Current Address (Line 1)*")).fill(data.get("line1"));
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Current Address Line 2*")).fill(data.get("line2"));
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Current Address Pincode*")).fill(data.get("pincode"));
        page.keyboard().press("Tab"); // Trigger city/state database lookup mapping

        selectDropdown("Current Address Ownership *", data.get("ownership"));

        // Toggle 'Same as Current Address' checkboxes
        page.locator("text=Same as Current Address").first().click();
        page.locator("text=Same as Current Address").last().click();

        page.click(NEXT_BTN);
        log.info("Address profiles configured and synchronized.");
    }

    public void completeObligationsTab(Map<String, String> data) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ADD OBLIGATION")).click();

        selectDropdown("Obligation Type *", data.get("type"));
        selectDropdown("Financier*", data.get("financier"));

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("EMI *")).fill(data.get("emi"));
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Remaining Tenure")).fill(data.get("tenure"));
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Account Number")).fill(data.get("account_number"));

        selectDropdown("Obligate", data.get("obligate"));
        selectDropdown("Closure Type *", data.get("closure_type"));

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SAVE")).click();
        page.click(NEXT_BTN);
        log.info("Active credit liabilities and obligations logged successfully.");
    }

    public void completeEmploymentTab(Map<String, String> data) {
        selectDropdown("Employment Type", data.get("type"));

        // Handle Autocomplete corporate lookup field
        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Employer*")).fill(data.get("employer"));
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("employer"))).click();

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Total Monthly Income *")).fill(data.get("income"));
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Office Email*")).fill(data.get("office_email"));

        selectDropdown("Designation*", data.get("designation"));

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Office Address Line 1*")).fill(data.get("line1"));
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Office Address Line 2*")).fill(data.get("line2"));
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Office Address Pincode *")).fill(data.get("pincode"));
        selectDropdown("Office Address Ownership *", data.get("ownership"));

        page.click(NEXT_BTN);
        log.info("Employment configuration and office address verified.");
    }

    public void completeReferencesTab(Map<String, String> data) {
        // Reference 1 Configuration
        page.locator("input[name*='references'][name*='name']").first().fill(data.get("ref1_name"));
        page.locator("input[name*='references'][name*='phone']").first().fill(data.get("ref1_phone"));
        page.locator("div[id*='references'][id*='relationship']").first().click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("ref1_rel"))).click();

        page.locator("input[name*='references'][name*='addressLine1']").first().fill(data.get("ref1_line1"));
        page.locator("input[name*='references'][name*='addressLine2']").first().fill(data.get("ref1_line2"));
        page.locator("input[name*='references'][name*='pincode']").first().fill(data.get("ref1_pin"));
        page.locator("div[id*='references'][id*='addressOwnership']").first().click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("ref1_own"))).click();

        // Reference 2 Configuration
        page.locator("input[name*='references'][name*='name']").last().fill(data.get("ref2_name"));
        page.locator("input[name*='references'][name*='phone']").last().fill(data.get("ref2_phone"));
        page.locator("div[id*='references'][id*='relationship']").last().click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("ref2_rel"))).click();

        page.locator("input[name*='references'][name*='addressLine1']").last().fill(data.get("ref2_line1"));
        page.locator("input[name*='references'][name*='addressLine2']").last().fill(data.get("ref2_line2"));
        page.locator("input[name*='references'][name*='pincode']").last().fill(data.get("ref2_pin"));
        page.locator("div[id*='references'][id*='addressOwnership']").last().click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("ref2_own"))).click();

        page.click(NEXT_BTN);
        log.info("Personal and professional references captured.");
    }

    public void completeBankDetailsTab(Map<String, String> data) {
        // Record 1 Allocation
        page.locator("input[id*='bankName']").first().fill(data.get("bank1_name"));
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("bank1_name"))).click();
        page.locator("input[id*='accountHolderName']").first().fill(data.get("bank1_acc_name"));
        page.locator("input[id*='accountNumber']").first().fill(data.get("bank1_acc_num"));
        page.locator("div[id*='accountType']").first().click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("bank1_type"))).click();
        page.locator("input[id*='ifscCode']").first().fill(data.get("bank1_ifsc"));
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("bank1_ifsc"))).click();

        // Record 2 Allocation
        page.locator("input[id*='bankName']").last().fill(data.get("bank2_name"));
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("bank2_name"))).click();
        page.locator("input[id*='accountHolderName']").last().fill(data.get("bank2_acc_name"));
        page.locator("input[id*='accountNumber']").last().fill(data.get("bank2_acc_num"));
        page.locator("div[id*='accountType']").last().click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("bank2_type"))).click();
        page.locator("input[id*='ifscCode']").last().fill(data.get("bank2_ifsc"));
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("bank2_ifsc"))).click();

        // Submit and transition to the next phase
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SUBMIT")).click();
        page.waitForSelector("text=Linked individual added");

        page.click(CO_APPLICANTS_BTN);
        log.info("Bank profiles submitted. Redirecting to Co-Applicant workflow stage.");
    }

    private void selectDropdown(String labelName, String value) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(labelName)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(value)).click();
        page.keyboard().press("Escape"); // Force close the active overlay drop-down view container
    }
}
