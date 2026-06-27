package ui.pages.dsa_secured;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

public class CoAppDLPage extends BaseTest {
    private final Page page;
    private static final String INPUT_EMAIL          = "Enter email address";
    private static final String FATHER_NAME          = "Father Name *";
    private static final String MOTHER_NAME          = "Mother Name *";
    private static final String CATEGORY             = "Category *";
    private static final String RELIGION             = "Religion *";
    private static final String EDUCATION            = "Education *";
    private static final String SELECT_MARITAL_STATUS= "Select Marital Status";
    private static final String NATIONALITY          = "Nationality *";
    private static final String DISABILITY           = "Disability *";
    private static final String ADDRESS_L2           = "Current Address Line 2 *";
    private static final String ADDRESS_PINCODE      = "Current Address Pincode *";
    private static final String ADDRESS_OWNERSHIP    = "Current Address Ownership *";
    public CoAppDLPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }
    public void clickAddApplicant() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Applicant")).click();
        log.info("Clicked on Add Applicant window.");
    }
    public void selectApplicantType(String applicantType) throws InterruptedException {
        //applicant_type: "Individual" --Declared in Normal.yaml file
        page.getByLabel("Applicant Type").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(applicantType)).click();
        Thread.sleep(1000);
    }
    public void selectType(String type) {
        //type: "Non-Financial" --Declared in Normal.yaml file
        page.getByLabel("Type *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(type).setExact(true)).click();
    }
    public void checkFormTypeOption() {
        page.getByLabel("Select if the type is Form").check();
    }

    public void verifyDrivingLicense(String ovdType, String dob, String dlNumber, String dlExpiry) throws InterruptedException {
        //ovd_type: "Driving License" --Declared in Normal.yaml file
        page.getByLabel("Other OVD *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ovdType)).click();
        page.getByLabel("Date of Birth *").click();
        page.getByLabel("Date of Birth *").fill(dob);
        page.getByLabel("Driving License Number *").click();
        page.getByLabel("Driving License Number *").fill(dlNumber);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).click();
        Thread.sleep(2000);

        page.getByLabel("Driving License Expiry Date *").click();
        page.getByLabel("Driving License Expiry Date *").fill(dlExpiry);
        log.info("Driving License identification data verification triggered.");
    }

    public void fillKYCDetails(String relationship, String phone, String email, String gender, String father,
                               String mother, String category, String religion, String education,
                               String maritalStatus, String nationality, String disability) {
        page.getByLabel("Relationship with applicant *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relationship)).click();
        page.getByLabel("Phone Number *").fill(phone);
        page.getByPlaceholder(INPUT_EMAIL).fill(email);
        page.getByPlaceholder("Select Gender").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(gender)).click();
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
        log.info("Co-Applicant background profile configurations completed.");
    }

    public void fillAddressDetails(String line1, String line2, String pincode, String ownership) {
        page.getByPlaceholder("Current Address (Line 1)").fill(line1);
        page.getByLabel(ADDRESS_L2).fill(line2);
        page.getByLabel(ADDRESS_PINCODE).fill(pincode);
        page.getByLabel(ADDRESS_OWNERSHIP).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
    }

    public void checkAddressConsents() {
        page.getByLabel("Yes").first().check();
        page.getByLabel("Yes").nth(1).check();
    }

    public void submitInitialForm() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
    }

    public void processOtpVerification(String otpValue) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Give OTP Consent")).nth(1).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get OTP")).click();

        // Dynamically parses and maps characters to sequence layout fields safely
        for (int i = 0; i < otpValue.length(); i++) {
            char otpChar = otpValue.charAt(i);
            page.locator("input[id*='r6']").nth(i).fill(String.valueOf(otpChar));
        }
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        log.info("OTP verification criteria completed successfully.");
    }
    public void clickNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
    }
    public void clickFinalNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("NEXT")).click();
        log.info("Moved past current validation layout.");
    }
}