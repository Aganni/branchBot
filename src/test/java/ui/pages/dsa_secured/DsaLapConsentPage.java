package ui.pages.dsa_secured;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DsaLapConsentPage {
    private static final Logger log = LogManager.getLogger(DsaLapConsentPage.class);

    private final Page mainPage;
    private Page groupsTab;

    // Configuration Selectors mapped from Python strings
    private final String signInLinkText = "Sign in";
    private final String emailInputName = "Email or phone";
    private final String passwordInputName = "Enter your password";
    private final String nextButtonName = "Next";
    private final String groupsLinkName = "notification test";
    private final String uniqueLinkName = "Verify your email & start your loan application";
    private final String trimmedContentLabel = "Show trimmed content";
    private final String clickHereName = "Verify My Email";
    private final String verifyBtnName = "Verify";

    public DsaLapConsentPage(Page page) {
        this.mainPage = page;
    }

    /**
     * Replaces Step 2 from Python.
     * Opens Google Groups in a NEW separate browser context tab and performs login actions.
     */
    public void loginAndNavigateToGroups(String email, String password) {
        log.info("Opening Google Groups workflow in a fresh secondary tab...");

        // Spawn a parallel isolated tab via the shared primary parent context
        this.groupsTab = mainPage.context().newPage();
        this.groupsTab.navigate("https://groups.google.com/a/creditsaison-in.com/g/notification-test");

        Locator signInBtn = groupsTab.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(signInLinkText));

        if (signInBtn.isVisible()) {
            log.info("Google Authentication barrier detected. Executing account sign-in sequence...");
            signInBtn.click();

            groupsTab.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(emailInputName)).fill(email);
            groupsTab.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(nextButtonName)).click();

            Locator pwdField = groupsTab.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(passwordInputName));
            pwdField.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            pwdField.fill(password);

            groupsTab.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(nextButtonName)).click();
        }

        log.info("Waiting for security redirections to settle down...");
        groupsTab.waitForLoadState(LoadState.NETWORKIDLE);
        groupsTab.waitForTimeout(5000);

        groupsTab.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(groupsLinkName)).click();
        groupsTab.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(uniqueLinkName)).first().click();
    }

    /**
     * Replaces Step 3 & 4 from Python.
     * Targets dynamic array elements, un-trims hidden DOM content, tracks popups, and executes the final verification click.
     */
    public void openLatestEmailAndConsent() {
        if (this.groupsTab == null) {
            throw new IllegalStateException("Google Groups tab was not initialized. Execute step initialization sequence first.");
        }

        log.info("Scanning for mail sections to extract the latest communication...");
        Locator emailSections = groupsTab.locator("section.BkrUxb");
        emailSections.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        Locator latestEmail = emailSections.last();
        latestEmail.scrollIntoViewIfNeeded();

        Locator trimmedBtn = latestEmail.getByLabel(trimmedContentLabel);
        if (trimmedBtn.isVisible() && "false".equals(trimmedBtn.getAttribute("aria-expanded"))) {
            log.info("Expanding trimmed layout contents within the target email container...");
            trimmedBtn.click();
        }

        log.info("Intercepting the generated nested popup consent tab redirection...");
        Page consentTab = groupsTab.waitForPopup(() -> {
            latestEmail.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(clickHereName)).click();
        });

        consentTab.waitForLoadState(LoadState.NETWORKIDLE);

        log.info("Executing final authorization sequence inside the Verification application wrapper...");
        Locator agreeBtn = consentTab.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(verifyBtnName));
        agreeBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        agreeBtn.click();

        // Safety assertion block matching python timeout limit configuration
        String successMessage = "Verification Complete!";
        consentTab.getByText(successMessage).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(60000));

        log.info("Consent verification processed successfully. Terminating context helper tabs.");
        consentTab.close();
        this.groupsTab.close();
    }

    /**
     * Replaces Step 5 from Python.
     * Focuses execution thread back onto the active core automation workspace tab.
     */
    public void returnToMainWorkspace() {
        log.info("Bringing the core automation parent context view back to the front window profile...");
        mainPage.bringToFront();
        mainPage.reload(); // Proactive page asset processing loop
    }
}
