package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

public class PrimaryApplicantPage extends BaseTest {
    private final Page page;
    private static final String SELECT_APPLICANT_TYPE   = "#applicantType";
    private static final String APPLICANT_TYPE   = "li[data-value='Individual']";
    private static final String INPUT_EMAIL              = "Enter email address";
    private static final String LABEL_FATHER_NAME       = "Father Name *";
    private static final String LABEL_MOTHER_NAME       = "Mother Name *";
    private static final String LABEL_SPOUSE_NAME       = "Spouse Name";
    private static final String LABEL_CATEGORY          = "Category *";
    private static final String LABEL_RELIGION          = "Religion *";
    private static final String LABEL_EDUCATION         = "Education *";
    private static final String SELECT_MARITAL_STATUS   = "Select Marital Status";
    private static final String LABEL_NATIONALITY       = "Nationality *";
    private static final String LABEL_DISABILITY        = "Disability *";
    private static final String LABEL_PREFERRED_ADDRESS  = "Preferred Address *";
    private static final String LABEL_RELATED_INTEREST   = "Related Party Interest *";
    private static final String LABEL_RELATED_CONTROL    = "Related Party Control *";

    private static final String PLACEHOLDER_ADDRESS_L1  = "Current Address (Line 1)";
    private static final String LABEL_ADDRESS_L2        = "Current Address Line 2 *";
    private static final String LABEL_ADDRESS_PINCODE   = "Current Address Pincode *";
    private static final String LABEL_ADDRESS_OWNERSHIP = "Current Address Ownership *";

    private static final String PLACEHOLDER_COMPANY     = "Search Your Company Here";
    private static final String LABEL_MONTHLY_INCOME    = "Total Monthly Income *";
    private static final String LABEL_OFFICE_EMAIL      = "Office Email *";
    private static final String LABEL_DESIGNATION       = "Designation *";
    private static final String LABEL_OFFICE_L1         = "Office Address Line 1 *";
    private static final String LABEL_OFFICE_L2         = "Office Address Line 2 *";
    private static final String LABEL_OFFICE_PINCODE    = "Office Address Pincode *";
    private static final String LABEL_OFFICE_OWNERSHIP  = "Office Address Ownership *";
    public PrimaryApplicantPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }
    public void handlePageInitRefresh() {
        page.waitForTimeout(2000);
        page.reload();
        log.info("Refreshed Primary Applicant Page for initialization.");
    }
    public void selectApplicantType(String applicantType, String individualText) {
        Locator dropdown = page.locator(SELECT_APPLICANT_TYPE);
        dropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        dropdown.click();
        log.info("Opened Applicant Type dropdown.");
        page.waitForTimeout(500);
        Locator option = page.locator("li:has-text('" + individualText + "')").first();
        option.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        option.click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(750);
        log.info("Successfully selected Applicant Type: {}", individualText);
    }
    public void verifyPanNumber(String pan) {
        Locator panInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("PAN Number"));
        panInput.click();
        panInput.fill(pan);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).nth(0).click();
        log.info("Submitted and verified PAN: {}", pan);
    }

    public void verifyEmailAddress(String email) {
        page.getByPlaceholder(INPUT_EMAIL).fill(email);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).nth(1).click();
        log.info("Submitted and verified Email: {}", email);
    }

    public void fillFamilyDetails(String fatherName, String motherName, String spouseName) {
        page.getByLabel(LABEL_FATHER_NAME).fill(fatherName);
        page.getByLabel(LABEL_MOTHER_NAME).fill(motherName);
        page.getByLabel(LABEL_SPOUSE_NAME).fill(spouseName);
        log.info("Filled Family Details -> Father: {}, Mother: {}, Spouse: {}", fatherName, motherName, spouseName);
    }

    public void selectBackgroundProfile(String category, String religion, String education, String maritalStatus, String nationality, String disability) {
        page.getByLabel(LABEL_CATEGORY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(category)).click();

        page.getByLabel(LABEL_RELIGION).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(religion)).click();

        page.getByLabel(LABEL_EDUCATION).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(education)).click();

        page.getByPlaceholder(SELECT_MARITAL_STATUS).click();
        page.getByText(maritalStatus).click();

        page.getByLabel(LABEL_NATIONALITY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(nationality)).click();

        page.getByLabel(LABEL_DISABILITY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(disability)).click();

        log.info("Selected Background Profile Options successfully.");
    }

    public void selectInternalDeclarations(String preferredAddress, String relatedInterest, String relatedControl) {
        page.getByLabel(LABEL_PREFERRED_ADDRESS).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(preferredAddress)).click();

        page.getByLabel(LABEL_RELATED_INTEREST).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relatedInterest)).click();

        page.getByLabel(LABEL_RELATED_CONTROL).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relatedControl)).click();

        log.info("Selected Internal Declarations and Preferred Address.");
    }

    // ── Address Details Actions ──────────────────────────────────────────────
    public void fillCurrentAddress(String line1, String line2, String pincode, String ownership) {
        page.getByPlaceholder(PLACEHOLDER_ADDRESS_L1).fill(line1);
        page.getByLabel(LABEL_ADDRESS_L2).fill(line2);
        page.getByLabel(LABEL_ADDRESS_PINCODE).fill(pincode);
        page.getByLabel(LABEL_ADDRESS_OWNERSHIP).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        log.info("Filled Current Address Details.");
    }

    public void checkAddressConsents() {
        page.getByLabel("Yes").first().check();
        page.getByLabel("Yes").nth(1).check();
        log.info("Checked Address Consent checkboxes.");
    }

    // ── Financial Obligations Actions ────────────────────────────────────────
    public void addFinancialObligation(String type, String financier, String emi) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Obligation")).click();
        page.getByLabel("Obligation Type *").click();
        page.getByText(type).click();

        page.getByLabel("Financier *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(financier)).click();

        page.getByLabel("EMI *").fill(emi);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        log.info("Added Obligation -> Type: {}, Financier: {}, EMI: {}", type, financier, emi);
    }

    // ── Employment Details Actions ───────────────────────────────────────────
    public void fillEmploymentProfile(String type, String employer, String income, String officeEmail, String designation) {
        page.getByLabel("Employment Type").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(type)).click();

        page.getByPlaceholder(PLACEHOLDER_COMPANY).fill(employer);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(employer)).click();

        page.getByLabel(LABEL_MONTHLY_INCOME).fill(income);
        page.getByLabel(LABEL_OFFICE_EMAIL).fill(officeEmail);
        page.getByLabel(LABEL_DESIGNATION).fill(designation);
        log.info("Filled Employment Profile Information for employer: {}", employer);
    }

    public void fillOfficeAddress(String line1, String line2, String pincode, String ownership) {
        page.getByLabel(LABEL_OFFICE_L1).fill(line1);
        page.getByLabel(LABEL_OFFICE_L2).fill(line2);
        page.getByLabel(LABEL_OFFICE_PINCODE).fill(pincode);
        page.getByLabel(LABEL_OFFICE_OWNERSHIP).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        log.info("Filled Office Address Fields.");
    }

    // ── References Actions ───────────────────────────────────────────────────
    public void fillReference1(String name, String phone, String relation, String line1, String line2, String pin, String ownership) {
        page.locator("input[name=\"ref1Name\"]").fill(name);
        page.locator("#ref1Phone").fill(phone);
        page.locator("#ref1Relation").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relation)).click();
        page.locator("textarea[name=\"ref1AddressLine1\"]").fill(line1);
        page.locator("textarea[name=\"ref1AddressLine2\"]").fill(line2);
        page.locator("#ref1AddressPincode").fill(pin);
        page.locator("#ref1AddressOwnership").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        log.info("Filled Reference 1 Data: {}", name);
    }

    public void fillReference2(String name, String phone, String relation, String line1, String line2, String pin, String ownership) {
        page.locator("input[name=\"ref2Name\"]").fill(name);
        page.locator("#ref2Phone").fill(phone);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Relationship \u200B").setExact(true)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relation)).click();
        page.locator("textarea[name=\"ref2AddressLine1\"]").fill(line1);
        page.locator("textarea[name=\"ref2AddressLine2\"]").fill(line2);
        page.locator("#ref2AddressPincode").fill(pin);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Address Ownership \u200B").setExact(true)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        log.info("Filled Reference 2 Data: {}", name);
    }

    // ── Bank Details Actions ─────────────────────────────────────────────────
    public void fillDisbursalBankDetails(String bankName, String holderName, String accNum, String accType, String ifsc) {
        page.locator("#disbursalBankName").fill(bankName);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(bankName)).click();
        page.locator("#disbursalHolderName").fill(holderName);
        page.locator("#disbursalAccountNumber").fill(accNum);
        page.locator("#disbursalType").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(accType)).click();
        page.locator("#disbursalIfscCode").fill(ifsc);
        log.info("Filled Disbursal Bank Details.");
    }

    public void fillCollectionsBankDetails(String bankName, String holderName, String accNum, String accType, String ifsc) {
        page.locator("#collectionsBankName").fill(bankName);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(bankName)).click();
        page.locator("#collectionsHolderName").fill(holderName);
        page.locator("#collectionsAccountNumber").fill(accNum);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Account Type \u200B").setExact(true)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(accType)).click();
        page.locator("#collectionsIfscCode").fill(ifsc);
        log.info("Filled Collections Bank Details.");
    }

    // ── Navigation Elements ──────────────────────────────────────────────────
    public void clickNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        log.info("Clicked 'Next' to shift layouts.");
    }

    public void submitAndNavigateToCoApplicants() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Co Applicants")).click();
        log.info("Form Submitted successfully. Routed onto Co-Applicants page context.");
    }
}