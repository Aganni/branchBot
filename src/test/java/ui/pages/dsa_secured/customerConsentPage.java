package ui.pages.dsa_secured;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

public class customerConsentPage extends BaseTest {
    private final Page page;

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final String CONSENT_TEXT_ANCHOR = "text=We would require the customer's consent";
    private static final String VERIFIED_MESSAGE    = "text=Phone number verified, Please continue";

    // Stable partial selector matching any input element whose ID starts with ':r'
    private static final String OTP_INPUTS_SELECTOR = "input[id^=':r']";

    public customerConsentPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void clickSendOtp() {
        // 1. Wait for the heavy instructional text block to completely mount.
        page.waitForSelector(CONSENT_TEXT_ANCHOR);
        page.waitForTimeout(500); // Tiny stabilization pause to ensure event listeners are bound

        // 2. Click the actual 'Send otp' button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send otp")).click();
        log.info("Successfully clicked the 'Send otp' button.");
    }

    public void enterConsentOtp(String otp) {
        // 1. Target all inputs matching the dynamic layout pattern
        Locator dynamicInputs = page.locator(OTP_INPUTS_SELECTOR);

        // 2. Explicitly wait for the final box to render to ensure the entire block is hydrated
        dynamicInputs.last().waitFor();

        // 3. Dynamically calculate the start index to always pull the last 6 generated text boxes
        int totalInputs = dynamicInputs.count();
        int startIndex = totalInputs - otp.length();

        // 4. Sequentially populate each digit into its respective box
        for (int i = 0; i < otp.length(); i++) {
            String digit = String.valueOf(otp.charAt(i));
            dynamicInputs.nth(startIndex + i).fill(digit);
        }
        log.info("Successfully entered dynamic 6-digit verification consent OTP.");
    }

    public void verifyOtpSuccessAndProceed() {
        // Wait for backend validation callback status label text to show up in the view
        page.waitForSelector(VERIFIED_MESSAGE);
        log.info("UI confirmed status message: 'Phone number verified, Please continue'");

        // Click the enabled NEXT transition button navigation target
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("NEXT")).click();
        log.info("Clicked NEXT button to move to Primary Applicant Stage.");
    }
}