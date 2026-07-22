package ui.pages.dsa_secured_plp;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

public class CoAppPassportPage extends BaseTest {
    private final Page page;

    // Locator Constants
    private static final String INPUT_EMAIL           = "Enter email address";
    private static final String FATHER_NAME           = "Father Name *";
    private static final String MOTHER_NAME           = "Mother Name *";
    private static final String SPOUSE_NAME           = "Spouse Name";
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

    public CoAppPassportPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void clickAddApplicant() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Applicant")).click();
        log.info("Opened Add Applicant flow context view.");
    }

    public void selectApplicantType(String applicantType) throws InterruptedException {
        Thread.sleep(3000);
        page.getByLabel("Applicant Type").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(applicantType)).click();
        Thread.sleep(3000);
    }

    public void selectType(String type) {
        page.getByLabel("Type *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(type).setExact(true)).click();
    }

    public void checkFormTypeOption() {
        page.getByLabel("Select if the type is Form").check();
    }

    public void PassportDetails(String ovdType, String dob, String fileNumber, String passportNumber, String expiryDate) throws InterruptedException {
        page.getByLabel("Other OVD *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ovdType)).click();
        page.getByLabel("Date of Birth *").fill(dob);
        page.getByLabel("Passport File Number *").fill(fileNumber);
        page.getByLabel("Passport Number *").fill(passportNumber);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).click();
        Thread.sleep(3000);
        page.getByLabel("Passport expiry date *").fill(expiryDate);
        log.info("Passport metadata parameters uploaded and verification initialized.");
    }

    public void KYCDetails(String relationship, String phone, String email, String gender, String father,
                               String mother, String category, String religion, String education,
                               String maritalStatus, String spouse, String nationality) {
        page.getByLabel("Relationship with applicant *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(relationship)).click();
        page.getByLabel("Phone Number *").fill(phone);
        page.getByPlaceholder(INPUT_EMAIL).fill(email);
        page.getByPlaceholder("Select Gender").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(gender).setExact(true)).click();
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
        if (maritalStatus.equalsIgnoreCase("Married")) {
            page.getByLabel(SPOUSE_NAME).fill(spouse);
        }
        page.getByLabel(NATIONALITY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(nationality)).click();
    }

    public void DisabilityDetails(String hasDisability, String typeOfDisability, String percentage) {
        page.getByLabel(DISABILITY).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(hasDisability)).click();
        if (hasDisability.equalsIgnoreCase("Yes")) {
            page.getByLabel("Type of disability *").click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(typeOfDisability)).click();
            page.getByLabel("% of disability *").fill(percentage);
        }
        log.info("Completed demographic core layout definitions profiles.");
    }

    public void AddressDetails(String line1, String line2, String pincode, String ownership) throws InterruptedException {
        page.getByPlaceholder(ADDRESS_L1).fill(line1);
        page.getByLabel(ADDRESS_L2).fill(line2);
        page.getByLabel(ADDRESS_PINCODE).fill(pincode);
        page.getByLabel(ADDRESS_OWNERSHIP).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        log.info("Populated Current Address criteria mapping configurations.");
        Thread.sleep(3000);
    }

    public void AddressConsents() throws InterruptedException{
        page.getByLabel("Yes").first().check();
        page.getByLabel("Yes").nth(1).check();
        Thread.sleep(3000);
    }

    public void FinancialObligation(String type, String financier, String emi) throws InterruptedException{
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Obligation")).click();
        page.getByLabel("Obligation Type *").click();
        page.getByText(type).click();

        page.getByLabel("Financier *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(financier)).click();

        page.getByLabel("EMI *").fill(emi);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        log.info("Added Obligation layout configuration details dynamically.");
        Thread.sleep(3000);
    }

    public void clickNext() throws InterruptedException{
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        Thread.sleep(3000);
    }

    public void submitForm() throws InterruptedException{
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        Thread.sleep(7000);
        log.info("Co-Applicant transaction record finalized and saved safely.");
    }
}
