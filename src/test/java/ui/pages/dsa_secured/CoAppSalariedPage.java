package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import data.TestDataProvider;
import hooks.BaseTest;

public class CoAppSalariedPage extends BaseTest {
    private final Page page;
    private static final String INPUT_EMAIL          = "Enter email address";
    private static final String FATHER_NAME          = "Father Name *";
    private static final String MOTHER_NAME          = "Mother Name *";
    private static final String CATEGORY             = "Category *";
    private static final String RELIGION             = "Religion *";
    private static final String EDUCATION            = "Education *";
    private static final String NATIONALITY          = "Nationality *";
    private static final String DISABILITY           = "Disability *";
    private static final String ADDRESS_L1           = "Current Address (Line 1)";
    private static final String ADDRESS_L2           = "Current Address (Line 2)";
    private static final String ADDRESS_OWNERSHIP    = "Current Address Ownership *";
    private static final String COMPANY               = "Search Your Company Here";
    private static final String MONTHLY_INCOME        = "Total Monthly Income *";
    private static final String OFFICE_EMAIL          = "Office Email *";
    private static final String DESIGNATION           = "Designation *";
    private static final String OFFICE_L1             = "Office Address Line 1 *";
    private static final String OFFICE_L2             = "Office Address Line 2 *";
    private static final String OFFICE_PINCODE        = "Office Address Pincode *";
    private static final String OFFICE_OWNERSHIP      = "Office Address Ownership *";

    public CoAppSalariedPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }
    public void clickAddApplicant() throws InterruptedException {
        Thread.sleep(5000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Applicant")).click();
        log.info("Clicked on Add Applicant entry window.");
        Thread.sleep(5000);
    }
    public void selectApplicantType(String applicantType) throws InterruptedException {
        page.getByLabel("Applicant Type").click();
        log.info("Clicked on Add Applicant Type CTA.");
        //applicant_type: "Individual" - which was declared in normal.yaml file
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(applicantType)).click();
        Thread.sleep(2000);
    }

    public void selectType(String type) {
        page.locator("//div[@id='type']").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(type).setExact(true)).click();
    }

    public void verifyPanNumber(String pan) {
        page.getByLabel("PAN Number *").click();
        page.getByLabel("PAN Number *").fill(pan);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).click();
        log.info("Verified Co-Applicant PAN: {}", pan);
    }

    public void KYCDetails(String relationship, String phone, String email, String gender,
                           String father, String mother, String category, String religion,
                           String education, String nationality, String disability) throws InterruptedException{
        page.getByLabel("Relationship with applicant *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relationship)).click();

        page.getByLabel("Phone Number *").click();
        page.getByLabel("Phone Number *").fill(phone);

        page.getByPlaceholder(INPUT_EMAIL).click();
        page.getByPlaceholder(INPUT_EMAIL).fill(email);

        page.getByPlaceholder("Select Gender").click();
        page.getByText(gender).click();
        page.getByLabel(FATHER_NAME).fill(father);
        page.getByLabel(MOTHER_NAME).fill(mother);

        page.getByLabel(CATEGORY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(category)).click();

        page.getByLabel(RELIGION).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(religion)).click();

        page.getByLabel(EDUCATION).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(education)).click();

        page.getByPlaceholder("Select Marital Status").click();

        page.getByLabel(NATIONALITY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(nationality)).click();

        page.getByLabel(DISABILITY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(disability)).click();
        log.info("Completed Co-Applicant dynamic layout demographics entries.");
        Thread.sleep(4000);
        //Click outside to enable 'Next' button. Click parent field.
        page.getByLabel(FATHER_NAME).click();
    }

    public void AddressDetails(String line1, String ownership) throws InterruptedException{
        page.getByPlaceholder(ADDRESS_L1).click();
        page.getByText(line1).click(); // Matching recorded pattern
        //page.getByPlaceholder(ADDRESS_L2).click();
        //page.getByText(line1).click();
        page.getByLabel(ADDRESS_OWNERSHIP).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        Thread.sleep(5000);
    }

    public void checkAddressConsents() {
        page.getByLabel("Yes").first().check();
        page.getByLabel("Yes").nth(1).check();
    }

    public void EmploymentDetails(String type, String company, String income, String email, String designation, String line1, String line2, String pincode, String ownership) throws InterruptedException {
        page.getByLabel("Employment Type").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(type)).click();
        page.getByPlaceholder(COMPANY).click();
        page.getByPlaceholder(COMPANY).fill(company);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(company)).click();

        page.getByLabel(MONTHLY_INCOME).fill(income);
        page.getByLabel(OFFICE_EMAIL).fill(email);
        page.getByLabel(DESIGNATION).fill(designation);
        page.getByLabel(OFFICE_L1).fill(line1);
        page.getByLabel(OFFICE_L2).fill(line2);
        page.getByLabel(OFFICE_PINCODE).fill(pincode);
        page.getByLabel(OFFICE_OWNERSHIP).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        log.info("Filled Corporate Employment Address Configurations.");
        Thread.sleep(2000);

        //Click outside to enable 'Next' button. Click office address 1 field.
        page.getByLabel(OFFICE_L1).click();
    }
    public void clickNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
    }
    public void submitForm() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        log.info("Co-Applicant details finalized and layout submitted.");
    }

    public void processOtpVerification(String otpValue) {
        page.getByText("Give OTP Consent").first().click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get OTP")).click();

        // Targets active dynamic input grids securely by fetching components visible on the viewport
        Locator visibleOtpInputs = page.locator("input[id^=':r']:visible");
        for (int i = 0; i < otpValue.length(); i++) {
            char otpChar = otpValue.charAt(i);
            visibleOtpInputs.nth(i).fill(String.valueOf(otpChar));
        }

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        log.info("OTP verification layer confirmed.");
    }

}
