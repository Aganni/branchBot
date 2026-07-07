package ui.pages.dsa_secured;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

public class customerConsentPage extends BaseTest {
    private final Page page;
    private static final String CONSENT_TEXT_ANCHOR = "text=We would require the customer's consent";
    private static final String VERIFIED_MESSAGE    = "text=Phone number verified, Please continue";

    // Stable partial selector matching any input element whose ID starts with ':r'
    private static final String OTP_INPUTS_SELECTOR = "input[id^=':r']";
    public customerConsentPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void clickSendOtp() {
        page.waitForSelector(CONSENT_TEXT_ANCHOR);
        page.waitForTimeout(500);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send otp")).click();
        log.info("Successfully clicked the 'Send otp' button.");
    }

    public void enterConsentOtp(String otp) {
        Locator dynamicInputs = page.locator(OTP_INPUTS_SELECTOR);
        dynamicInputs.last().waitFor();
        int totalInputs = dynamicInputs.count();
        int startIndex = totalInputs - otp.length();
        for (int i = 0; i < otp.length(); i++) {
            String digit = String.valueOf(otp.charAt(i));
            dynamicInputs.nth(startIndex + i).fill(digit);
        }
        log.info("Successfully entered dynamic 6-digit verification consent OTP.");
    }

    public void verifyOtpSuccessAndProceed() {
        page.waitForSelector(VERIFIED_MESSAGE);
        log.info("UI confirmed status message: 'Phone number verified, Please continue'");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("NEXT")).click();
        log.info("Clicked NEXT button to move to Primary Applicant Stage.");
    }
}