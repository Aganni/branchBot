package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

public class CoAppAadhaarPage extends BaseTest {
    private final Page page;

    private static final String INPUT_EMAIL           = "Enter email address";
    private static final String FATHER_NAME           = "Father Name *";
    private static final String MOTHER_NAME           = "Mother Name *";
    private static final String CATEGORY              = "Category *";
    private static final String RELIGION              = "Religion *";
    private static final String EDUCATION             = "Education *";
    private static final String SELECT_MARITAL_STATUS = "Select Marital Status";
    private static final String NATIONALITY           = "Nationality *";
    private static final String DISABILITY            = "Disability *";
    private static final String ADDRESS_L1            = "Current Address (Line 1)";
    private static final String ADDRESS_L2            = "Current Address Line 2 *";
    private static final String ADDRESS_PINCODE       = "Current Address Pincode *";
    private static final String ADDRESS_OWNERSHIP     = "Current Address Ownership *";

    public CoAppAadhaarPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }
    public void clickAddApplicant() throws InterruptedException {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Applicant")).click();
        log.info("Opened Add Applicant step form context.");
        Thread.sleep(500);
    }
    public void selectApplicantType(String applicantType) throws InterruptedException {
        page.getByLabel("Applicant Type").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(applicantType)).click();
        Thread.sleep(500);
    }
    public void selectType(String type) throws InterruptedException {
        page.getByLabel("Type *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(type).setExact(true)).click();
        Thread.sleep(500);
    }

    public void checkFormTypeOption() {
        page.getByLabel("Select if the type is Form").check();
    }

    public void verifyAadhaarOvd(String ovdType, String dob, String digits) {
        page.getByLabel("Other OVD *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ovdType)).click();
        page.waitForTimeout(100);
        // Date of Birth is a readonly MUI date-picker input; typing/fill fails actionability checks.
        // Open the calendar and navigate year -> month -> day instead.
        ui.Utils.Utils.selectDateFromMuiCalendar(page.getByLabel("Choose date"), dob);
        page.getByLabel("Aadhaar last 4 digits *").fill(digits);
        log.info("OVD structural verification requirements loaded.");
    }

    public void KYCDetails(String salutation, String name, String relationship, String phone, String email,
                                    String gender, String father, String mother, String category, String religion,
                                    String education, String maritalStatus, String nationality, String disability) {

        page.getByLabel("Salutation *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(salutation).setExact(true)).click();
        page.getByLabel("Name as per Other OVD *").fill(name);
        page.getByLabel("Relationship with applicant *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relationship)).click();
        page.getByLabel("Phone Number *").fill(phone);
        page.getByPlaceholder(INPUT_EMAIL).fill(email);
        page.getByPlaceholder("Select Gender").click();
        page.getByText(gender, new Page.GetByTextOptions().setExact(true)).click();
        page.getByLabel(FATHER_NAME).fill(father);
        page.getByLabel(MOTHER_NAME).fill(mother);
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
        log.info("Add all the KYC details for Aadhar non-financial co-applicant ");

        page.getByLabel(FATHER_NAME).click();
    }

    public void AddressDetails(String line1, String line2, String pincode, String ownership) {
        page.getByPlaceholder(ADDRESS_L1).fill(line1);
        page.getByLabel(ADDRESS_L2).fill(line2);
        page.getByLabel(ADDRESS_PINCODE).fill(pincode);
        page.getByLabel(ADDRESS_OWNERSHIP).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
    }
    public void checkAddressConsents() {
        page.getByLabel("Yes").first().check();
        page.getByLabel("Yes").nth(1).check();
        log.info("Address parameters configuration targets saved.");
    }

    public void AddObligation(String type, String financier, String emi, String accNum,
                                               String outstanding, String tenure, String obligate, String closureType) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Obligation")).click();

        page.getByLabel("Obligation Type *").click();
        page.getByText(type).click();
        page.getByLabel("Financier *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(financier)).click();
        page.getByLabel("EMI *").fill(emi);
        page.getByLabel("Account Number").fill(accNum);
        page.getByLabel("Out Standing").fill(outstanding);
        page.getByLabel("Remaining Tenure").fill(tenure);
        page.getByLabel("Obligate").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(obligate)).click();
        page.getByLabel("Closure Type").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(closureType)).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        log.info("Dynamic Financial Obligation records added.");
        page.waitForTimeout(2500);
    }

    public void submitInitialForm() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
    }

    public void processOtpVerification(String otpValue) throws InterruptedException {
        page.getByText("Give OTP Consent").first().click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get OTP")).click();

        Locator visibleOtpInputs = page.locator("input[id^=':r']:visible");
        for (int i = 0; i < otpValue.length(); i++) {
            char otpChar = otpValue.charAt(i);
            visibleOtpInputs.nth(i).fill(String.valueOf(otpChar));
        }
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        log.info("OTP verification layer confirmed.");
        Thread.sleep(1000);
    }

    public void clickNext() throws InterruptedException {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        Thread.sleep(1000);
    }
    }

