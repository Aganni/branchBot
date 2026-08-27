package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;
import ui.Utils.Utils;

public class CoAppDLPage extends BaseTest {
    private final Page page;
    private static final String INPUT_EMAIL = "Enter email address";
    private static final String FATHER_NAME = "Father Name *";
    private static final String MOTHER_NAME = "Mother Name *";
    private static final String CATEGORY = "Category *";
    private static final String RELIGION = "Religion *";
    private static final String EDUCATION = "Education *";
    private static final String SELECT_MARITAL_STATUS = "Select Marital Status";
    private static final String SELECT_NATIONALITY = "Select Nationality";
    private static final String DISABILITY = "Disability *";
    private static final String ADDRESS_L2 = "Current Address Line 2 *";
    private static final String ADDRESS_PINCODE = "Current Address Pincode *";
    private static final String ADDRESS_OWNERSHIP = "Current Address Ownership *";
    private static final String AADHAR_LAST_FOUR = "Aadhar Number (Last Four";

    public CoAppDLPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void clickAddApplicant() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Applicant")).click();
        log.info("Clicked on Add Applicant window.");
    }

    public void selectApplicantType(String applicantType) throws InterruptedException {
        page.getByLabel("Applicant Type").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(applicantType)).click();
        Thread.sleep(1000);
    }

    public void selectType(String type) {
        page.getByLabel("Type *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(type).setExact(true)).click();
    }

    public void checkFormTypeOption() {
        page.getByLabel("Select if the type is Form").check();
    }

    public void DrivingLicense(String ovdType, String dob, String dlNumber, String dlExpiry) throws InterruptedException {
        // Select OVD type
        page.getByLabel("Other OVD *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ovdType)).click();

        // DOB - click calendar icon, then pick year > month > day
        String dobNormalized = dob.replace("-", "/");
        Locator dobCalendarTrigger = page.getByLabel("Choose date").first();
        Utils.selectDateFromMuiCalendar(dobCalendarTrigger, dobNormalized);

        // Fill DL number and verify
        page.getByLabel("Driving License Number *").click();
        page.getByLabel("Driving License Number *").fill(dlNumber);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).click();
        Thread.sleep(4000);

        // DL Expiry Date - click calendar icon, then pick year > month > day
        String expiryNormalized = dlExpiry.replace("-", "/");
        page.getByLabel("Driving License Expiry Date *").click();
        Locator expiryCalendarTrigger = page.getByLabel("Choose date", new Page.GetByLabelOptions().setExact(true));
        Utils.selectDateFromMuiCalendar(expiryCalendarTrigger, expiryNormalized);

        log.info("Driving License identification data verification triggered.");
    }

    public void fillAadharLastFour(String aadharLastFour) {
        page.getByLabel(AADHAR_LAST_FOUR).fill(aadharLastFour);
        log.info("Filled Aadhar last four digits.");
    }

    public void KYCDetails(String relationship, String phone, String email, String gender, String father,
                           String mother, String category, String religion, String education,
                           String maritalStatus, String nationality, String disability) throws InterruptedException {

        // Relationship with applicant
        page.getByLabel("Relationship with applicant *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relationship)).click();

        // Phone & Email
        page.getByLabel("Phone Number *").click();
        page.getByLabel("Phone Number *").fill(phone);
        page.getByPlaceholder(INPUT_EMAIL).click();
        page.getByPlaceholder(INPUT_EMAIL).fill(email);

        // Gender
        page.getByPlaceholder("Select Gender").click();
        page.getByText(gender, new Page.GetByTextOptions().setExact(true)).click();

        // Family details
        page.getByLabel(FATHER_NAME).click();
        page.getByLabel(FATHER_NAME).fill(father);
        page.getByLabel(MOTHER_NAME).click();
        page.getByLabel(MOTHER_NAME).fill(mother);

        // Background profile
        page.getByLabel(CATEGORY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(category)).click();
        page.getByLabel(RELIGION).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(religion)).click();
        page.getByLabel(EDUCATION).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(education)).click();

        // Nationality - uses placeholder locator
        page.getByPlaceholder(SELECT_NATIONALITY).click();
        page.getByText(nationality).click();

        // Marital Status
        page.getByPlaceholder(SELECT_MARITAL_STATUS).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(maritalStatus)).click();

        // Disability
        page.getByLabel(DISABILITY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(disability)).click();

        log.info("Co-Applicant background profile configurations completed.");
        Thread.sleep(3000);
        // Click outside to dismiss any dropdown and enable 'Next' button
        page.locator(".ant-spin-nested-loading").click();
    }

    public void AddressDetails(String line1, String line2, String pincode, String ownership) throws InterruptedException {
        page.getByPlaceholder("Current Address (Line 1)").click();
        page.getByPlaceholder("Current Address (Line 1)").fill(line1);
        page.locator(".MuiGrid-root > .MuiFormControl-root > .MuiInputBase-root").first().click();
        page.getByLabel(ADDRESS_L2).fill(line2);
        page.getByLabel(ADDRESS_PINCODE).click();
        page.getByLabel(ADDRESS_PINCODE).fill(pincode);
        page.getByLabel(ADDRESS_OWNERSHIP).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        Thread.sleep(3000);
    }

    public void checkAddressConsents() throws InterruptedException {
        page.getByLabel("Yes").first().check();
        page.getByLabel("Yes").nth(1).check();
        Thread.sleep(3000);
    }

    public void clickNext() throws InterruptedException {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        Thread.sleep(3000);
    }

    public void submitForm() throws InterruptedException {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        log.info("Form configuration completed and submitted.");
        Thread.sleep(5000);
    }

    public void processOtpVerification(String otpValue) {
        page.getByText("Give OTP Consent").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get OTP")).click();

        // Fill OTP digits into visible input fields
        Locator visibleOtpInputs = page.locator("input[id^=':r']:visible");
        for (int i = 0; i < otpValue.length(); i++) {
            char otpChar = otpValue.charAt(i);
            visibleOtpInputs.nth(i).fill(String.valueOf(otpChar));
        }

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        log.info("OTP verification layer confirmed.");
    }
}
