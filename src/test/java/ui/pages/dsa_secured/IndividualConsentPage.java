package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

public class IndividualConsentPage extends BaseTest {
    private final Page page;

    public IndividualConsentPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }
    public void verifyCoApplicantsConsent(int count, String otpValue) {
        for (int i = 0; i < count; i++) {
            log.info("Processing OTP consent authorization workflow step {} of {}", (i + 1), count);

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Give OTP Consent")).first().click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get OTP")).click();

            Locator visibleOtpInputs = page.locator("input[id^=':r']:visible");

            for (int j = 0;  j < otpValue.length();  j++) {
                char otpChar = otpValue.charAt(j);
                visibleOtpInputs.nth(j).fill(String.valueOf(otpChar));
            }
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
            page.waitForLoadState();
        }
        log.info("Successfully authorized required co-applicant profile accounts confirmation steps.");
    }

    public void clickNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("NEXT")).click();
        log.info("Navigated forward past Co-Applicant dashboard layout segment view.");
    }
}