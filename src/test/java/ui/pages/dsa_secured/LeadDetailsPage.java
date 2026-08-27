package ui.pages.dsa_secured;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

public class LeadDetailsPage extends BaseTest {
    private final Page page;

    // ── Button / Dropdown Target Labels ──────────────────────────────────────
    private static final String BTN_EMPLOYMENT_TYPE     = "Employment Type";
    private static final String BTN_INTEREST_PREFERENCE = "Interest Payment Preference";
    private static final String COMBO_LOAN_PURPOSE      = "Loan Purpose";
    private static final String BTN_NEXT                = "Create Lead";

    // ── Next Page Indicator (Customer Consent step) ────────────────────────
    private static final String CONSENT_PAGE_TEXT = "We would require the customer's consent";

    public LeadDetailsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void fillName(String name) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name")).fill(name);
        log.info("Filled Name: {}", name);
    }

    public void fillPhoneNumber(String phone) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number")).fill(phone);
        log.info("Filled Phone Number: {}", phone);
    }

    public void clearPhoneNumber() {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number")).clear();
        log.info("Cleared Phone Number field.");
    }

    public void fillLoanAmount(String amount) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Loan Amount")).fill(amount);
        log.info("Filled Loan Amount: {}", amount);
    }

    public void fillTenure(String tenure) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Tenure")).fill(tenure);
        log.info("Filled Tenure: {}", tenure);
    }

    public void fillRateOfInterest(String roi) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Rate of Interest")).fill(roi);
        log.info("Filled Rate of Interest: {}", roi);
    }

    public void selectEmploymentType(String employmentType) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(BTN_EMPLOYMENT_TYPE)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(employmentType)).click();
        page.keyboard().press("Escape");
        log.info("Selected Employment Type: {}", employmentType);
    }

    public void selectInterestPaymentPreference(String preference) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(BTN_INTEREST_PREFERENCE)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(preference)).click();
        page.keyboard().press("Escape");
        log.info("Selected Interest Payment Preference: {}", preference);
    }

    public void selectLoanPurpose(String purpose) {
        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName(COMBO_LOAN_PURPOSE)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(purpose)).click();
        page.keyboard().press("Escape");
        log.info("Selected Loan Purpose: {}", purpose);
    }

    public void clickCreateLead() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(BTN_NEXT)).click();
        log.info("Clicked Create Lead button.");
    }

    /**
     * Checks whether the page successfully transitioned to the Customer Consent step
     * after clicking Create Lead. Waits up to 5 seconds for the consent page text to appear.
     *
     * @return true if the consent page loaded (lead created successfully), false otherwise
     */
    public boolean isLeadCreatedSuccessfully() {
        try {
            page.waitForSelector("text=" + CONSENT_PAGE_TEXT,
                    new Page.WaitForSelectorOptions().setTimeout(5000));
            return true;
        } catch (Exception e) {
            log.warn("Consent page did not load — phone number likely already has an application.");
            return false;
        }
    }
}
