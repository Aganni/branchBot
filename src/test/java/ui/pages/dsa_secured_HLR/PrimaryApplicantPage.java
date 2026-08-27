package ui.pages.dsa_secured_HLR;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

public class PrimaryApplicantPage extends BaseTest {
    private final Page page;
    private static final String SELECT_APPLICANT_TYPE   = "#applicantType";
    private static final String APPLICANT_TYPE   = "li[data-value='Individual']";
    private static final String INPUT_EMAIL              = "Enter email address";
    private static final String FATHER_NAME       = "Father Name *";
    private static final String MOTHER_NAME       = "Mother Name *";
    private static final String SPOUSE_NAME       = "Spouse Name";
    private static final String CATEGORY          = "Category *";
    private static final String RELIGION          = "Religion *";
    private static final String EDUCATION         = "Education *";
    private static final String SELECT_MARITAL_STATUS   = "Select Marital Status";
    private static final String NATIONALITY       = "Nationality *";
    private static final String DISABILITY        = "Disability *";
    private static final String PREFERRED_ADDRESS  = "Preferred Address *";
    private static final String RELATED_INTEREST   = "Related Party Interest *";
    private static final String RELATED_CONTROL    = "Related Party Control *";
    private static final String ADDRESS_L1  = "Current Address (Line 1)";
    private static final String ADDRESS_L2        = "Current Address Line 2 *";
    private static final String ADDRESS_PINCODE   = "Current Address Pincode *";
    private static final String ADDRESS_OWNERSHIP = "Current Address Ownership *";
    private static final String COMPANY     = "Search Your Company Here";
    private static final String MONTHLY_INCOME    = "Total Monthly Income *";
    private static final String OFFICE_EMAIL      = "Office Email *";
    private static final String DESIGNATION       = "Designation *";
    private static final String OFFICE_L1         = "Office Address Line 1 *";
    private static final String OFFICE_L2         = "Office Address Line 2 *";
    private static final String OFFICE_PINCODE    = "Office Address Pincode *";
    private static final String OFFICE_OWNERSHIP  = "Office Address Ownership *";
    public PrimaryApplicantPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }
    public void selectApplicantType(String applicantType, String individualText) throws InterruptedException {
        page.reload();
        page.locator(SELECT_APPLICANT_TYPE).click();
        log.info("Opened Applicant Type dropdown.");
        Locator dropdownListbox = page.locator("//li[text()='Individual']");
        Thread.sleep(800);
        dropdownListbox.dblclick();
        page.waitForLoadState();
        Thread.sleep(800);
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
        page.getByLabel(FATHER_NAME).fill(fatherName);
        page.getByLabel(MOTHER_NAME).fill(motherName);
        page.getByLabel(SPOUSE_NAME).fill(spouseName);
        log.info("Filled Family Details -> Father: {}, Mother: {}, Spouse: {}", fatherName, motherName, spouseName);
    }

    public void selectBackgroundProfile(String category, String religion, String education, String maritalStatus, String nationality, String disability) {
        page.getByLabel(CATEGORY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(category)).click();

        page.getByLabel(RELIGION).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(religion)).click();

        page.getByLabel(EDUCATION).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(education)).click();

        page.getByPlaceholder(SELECT_MARITAL_STATUS).click();
        page.getByText(maritalStatus).click();

        page.getByLabel(NATIONALITY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(nationality)).click();

        page.getByLabel(DISABILITY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(disability)).click();

        log.info("Selected Background Profile Options successfully.");
    }

    public void selectInternalDeclarations(String preferredAddress, String relatedInterest, String relatedControl) {
        page.getByLabel(PREFERRED_ADDRESS).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(preferredAddress)).click();

        page.getByLabel(RELATED_INTEREST).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relatedInterest)).click();

        page.getByLabel(RELATED_CONTROL).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relatedControl)).click();

        log.info("Selected Internal Declarations and Preferred Address.");
    }

    // ── Address Details Actions ──────────────────────────────────────────────
    public void fillCurrentAddress(String line1, String line2, String pincode, String ownership) {
        page.getByPlaceholder(ADDRESS_L1).fill(line1);
        page.getByLabel(ADDRESS_L2).fill(line2);
        page.getByLabel(ADDRESS_PINCODE).fill(pincode);
        page.getByLabel(ADDRESS_OWNERSHIP).click();
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

        page.getByPlaceholder(COMPANY).fill(employer);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(employer)).click();

        page.getByLabel(MONTHLY_INCOME).fill(income);
        page.getByLabel(OFFICE_EMAIL).fill(officeEmail);
        page.getByLabel(DESIGNATION).fill(designation);
        log.info("Filled Employment Profile Information for employer: {}", employer);
    }

    public void fillOfficeAddress(String line1, String line2, String pincode, String ownership) {
        page.getByLabel(OFFICE_L1).fill(line1);
        page.getByLabel(OFFICE_L2).fill(line2);
        page.getByLabel(OFFICE_PINCODE).fill(pincode);
        page.getByLabel(OFFICE_OWNERSHIP).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        log.info("Filled Office Address Fields.");
        page.getByLabel(OFFICE_L2).click();  //Click outside to enable 'Next' button. Hence clicked office address 2 field.
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
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Relationship").setExact(true)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relation)).click();
        page.locator("textarea[name=\"ref2AddressLine1\"]").fill(line1);
        page.locator("textarea[name=\"ref2AddressLine2\"]").fill(line2);
        page.locator("#ref2AddressPincode").fill(pin);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Address Ownership").setExact(true)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        log.info("Filled Reference 2 Data: {}", name);
        page.locator("textarea[name=\"ref2AddressLine1\"]").click();  //Click outside to enable 'Next' button. Hence click the ref2AddressLine1 field.
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
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Account Type").setExact(true)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(accType)).click();
        page.locator("#collectionsIfscCode").fill(ifsc);
        log.info("Filled Collections Bank Details.");
    }

    // ── Navigation Elements ──────────────────────────────────────────────────
    public void clickNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        log.info("Clicked 'Next' to shift layouts.");
    }

    public void submitAndNavigateToCoApplicants() throws InterruptedException {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Co Applicants")).click();
        log.info("Form Submitted successfully. Routed onto Co-Applicants page context.");
        page.reload();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
    }
}