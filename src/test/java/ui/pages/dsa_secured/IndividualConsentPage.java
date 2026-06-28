package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;


public class IndividualConsentPage extends BaseTest {
    private final Page page;

    private static final String VALIDATION_MSG = "Consent for all co-applicant is mandatory before moving to next stage";
    public IndividualConsentPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void verifyIndividualApplicantsConsent(String otpValue) throws InterruptedException {
        // Collect all matching active consent trigger components
        Locator consentButtons = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Give OTP Consent"));
        Thread.sleep(5000);
        int totalApplicants = consentButtons.count();

        log.info("Detected {} individual applicant(s) requiring consent verification.", totalApplicants);

        for (int i = 0; i < totalApplicants; i++) {
            log.info("Processing consent confirmation step {} of {}.", (i + 1), totalApplicants);
            // Always target the first remaining button since the layout updates on submission
            consentButtons.first().click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get OTP")).click();
            // Secure locator strategy targeting only the active modal input fields
            Locator visibleOtpInputs = page.locator("input[id^=':r']:visible");

            for (int j = 0; j < otpValue.length(); j++) {
                char otpChar = otpValue.charAt(j);
                visibleOtpInputs.nth(j).fill(String.valueOf(otpChar));
            }

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
            page.waitForLoadState();
        }
            log.info("Successfully completed OTP verification workflows for all available individual profiles.");
            Thread.sleep(3000);
    }

        public void verifyMandatoryConsentValidation() {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("NEXT")).click();
            log.info("Clicked NEXT button to verify mandatory form validations.");

            Locator validationBanner = page.getByText(VALIDATION_MSG);
            validationBanner.waitFor(new Locator.WaitForOptions().setTimeout(5000));

            if (!validationBanner.isVisible()) {
                throw new AssertionError("Expected validation banner '" + VALIDATION_MSG + "' was not visible on screen!");
            }
            log.info("Validation verified successfully: '{}' is displayed.", VALIDATION_MSG);
        }

        public void clickReTriggerEntityConsent() {
            Locator reTriggerButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Re-trigger Consent"));
            reTriggerButton.click();
            log.info("Successfully clicked on 'Re-trigger Consent' button for the Entity applicant.");
        }
}
