package ui.pages.dsa_secured_plp;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;


public class CoAppEntityPage extends BaseTest {
    private final Page page;

    // Locator Constants
    private static final String COMPANY_PAN         = "Company PAN *";
    private static final String UDYAM  = "Enter or select UDYAM";
    private static final String REGISTRATION_DATE   = "Date of Registration";
    private static final String BUSINESS_TYPE       = "Business type *";
    private static final String PHONE   = "Select...";
    private static final String EMAIL   = "Email ID";
    private static final String ADDR_L1 = "Operating Office Address Line";
    private static final String ADDR_L2 = "Operating Office Address Line";
    private static final String ADDR_PINCODE        = "Operating Office Address Pincode *";
    private static final String ADDR_OWNERSHIP      = "Operating Office Address Ownership *";

    public CoAppEntityPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void clickAddApplicant() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Applicant")).click();
        log.info("Opened Add Applicant step wizard context.");
    }

    public void selectApplicantType(String applicantType) {
        page.getByLabel("Applicant Type").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(applicantType)).click();
    }

    public void verifyCompanyPan(String pan) throws InterruptedException {
        Thread.sleep(5000);
        page.getByLabel("Company PAN *").fill(pan);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).click();
        Thread.sleep(5000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue to fetch details")).click();
        log.info("Company PAN verified and background details fetched.");
    }

    public void verifyUdyamDetails(String udyam) throws InterruptedException {
        page.getByPlaceholder(UDYAM).fill(udyam);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).nth(2).click();
        Thread.sleep(5000);
        log.info("UDYAM Registration registration credentials verified.");
    }

    public void EntityProfileDetails(String regDate, String bizType, String phone, String email) {
        page.getByPlaceholder(REGISTRATION_DATE).fill(regDate);
        page.getByLabel(BUSINESS_TYPE).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(bizType)).click();
        page.getByPlaceholder(PHONE).fill(phone);
        page.getByPlaceholder(EMAIL).fill(email);
    }

    public void OperatingAddressDetails(String line1, String line2, String pincode, String ownership) {
        page.getByPlaceholder(ADDR_L1).fill(line1);
        page.getByLabel(ADDR_L2).fill(line2);
        page.getByLabel(ADDR_PINCODE).fill(pincode);

        page.getByLabel(ADDR_OWNERSHIP).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(ownership)).click();
        log.info("Operating Office Address details populated.");
        page.waitForTimeout(2500);
        //Click outside to enable 'Next' button. Click ADDR_L2_LABEL field.
        page.getByLabel(ADDR_L1).click();
        page.waitForTimeout(2500);
    }

    public void submitForm(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        log.info("Entity form configuration saved and layout processing finalized.");
      page.waitForTimeout(5000);
    }

    public void clickSendEmail() {
        log.info("Clicking Re-trigger Consent button for Entity Email");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Re-trigger Consent")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);

    }
}

