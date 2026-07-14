package ui.pages.dsa_secured_plp;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityConsentPage {
    private static final Logger log = LogManager.getLogger(EntityConsentPage.class);

    private final Page mainPage;
    private Page groupsTab;

    private final String signInLinkText = "Sign in";
    private final String emailInputName = "Email or phone";
    private final String passwordInputName = "Enter your password";
    private final String nextButtonName = "Next";
    private final String groupsLinkName = "notification test";
    //private final String uniqueLinkName = "Verify your email & start your loan application";
    private final String uniqueLinkName = "Unique link to provide consent for your loan application | Credit Saison India" ;
    private final String trimmedContentLabel = "Show trimmed content";
    private final String clickHereName = "Verify My Email";
    private final String verifyBtnName = "Verify";

    public EntityConsentPage(Page page)
    {
        this.mainPage = page;
    }

    public void clickRetriggerConsent() {
        log.info("Clicking Re-trigger Consent button to send email notification...");
        Locator retriggerBtn = mainPage.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Re-trigger Consent"));
        retriggerBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        retriggerBtn.click();
        mainPage.waitForTimeout(5000);
        log.info("Re-trigger Consent clicked. Email notification dispatched.");
    }

    public void loginAndNavigateToGroups(String email, String password) {
        log.info("Opening Google Groups in a new tab...");
        this.groupsTab = mainPage.context().newPage();
        this.groupsTab.navigate("https://groups.google.com/a/creditsaison-in.com/g/notification-test");
        Locator signInBtn = groupsTab.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(signInLinkText));
        if (signInBtn.isVisible()) {
            log.info("Sign-in required. Authenticating with Google");
            signInBtn.click();
            groupsTab.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(emailInputName)).fill(email);
            groupsTab.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(nextButtonName)).click();
            Locator pwdField = groupsTab.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(passwordInputName));
            pwdField.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            pwdField.fill(password);
            groupsTab.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(nextButtonName)).click();
        }
        log.info("Waiting for Google Groups to fully load");
        groupsTab.waitForTimeout(5000);
        groupsTab.waitForLoadState(LoadState.DOMCONTENTLOADED);
        // Click on the notification-test group link
        Locator groupLink = groupsTab.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(groupsLinkName)).nth(0);
        groupLink.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
        groupLink.click();
        log.info("notification test group");
        // Open the latest (most recent) email with the consent verification subject
        groupsTab.waitForLoadState(LoadState.DOMCONTENTLOADED);
        Locator emailLink = groupsTab.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(uniqueLinkName)).last();
        emailLink.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
        emailLink.click();
        log.info("Opened latest consent verification email.");
    }

    public void openLatestEmailAndConsent() {
        if (this.groupsTab == null) {
            throw new IllegalStateException("Google Groups tab not initialized. Call loginAndNavigateToGroups() first.");
        }
        log.info("Scrolling to bottom of the email thread to reach the latest reply...");
        groupsTab.evaluate("window.scrollTo(0, document.body.scrollHeight)");
        groupsTab.waitForTimeout(2000);
        groupsTab.evaluate("window.scrollTo(0, document.body.scrollHeight)");
        groupsTab.waitForTimeout(2000);
        log.info("Looking for email content sections...");
        Locator emailSections = groupsTab.locator("section.BkrUxb");
        emailSections.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        Locator latestEmail = emailSections.last();
        latestEmail.scrollIntoViewIfNeeded();
        groupsTab.waitForTimeout(1000);
        Locator trimmedBtn = latestEmail.getByLabel(trimmedContentLabel);
        if (trimmedBtn.isVisible() && "false".equals(trimmedBtn.getAttribute("aria-expanded"))) {
            log.info("Expanding trimmed email content...");
            trimmedBtn.click();
            groupsTab.waitForTimeout(2000);
        }
        // Scroll down again within the last section to reveal the "Verify My Email" link
        latestEmail.locator(":scope >> *").last().scrollIntoViewIfNeeded();
        groupsTab.waitForTimeout(1000);
        log.info("Searching for 'Unique link to provide consent for your loan application | Credit Saison India' ");
        Locator scopedLink = latestEmail.locator("a:has-text('Verify My Email')");
        final Locator verifyLink;
        if (scopedLink.count() == 0) {
            log.info("Link not found in last section, searching entire page...");
            verifyLink = groupsTab.locator("a:has-text('Verify Email and Continue')").last();
        } else {
            verifyLink = scopedLink.last();
        }
        verifyLink.scrollIntoViewIfNeeded();
        groupsTab.waitForTimeout(500);
        // Click the link — it opens in a new tab
        log.info("Clicking 'Unique link to provide consent for your loan application | Credit Saison India' ");
        Page consentTab = groupsTab.waitForPopup(() -> {
            verifyLink.click();
        });
        consentTab.waitForLoadState(LoadState.NETWORKIDLE);

        // Click the final Verify button on the consent verification page
        log.info("Clicking Verify button on consent page...");
        Locator agreeBtn = consentTab.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(verifyBtnName));
        agreeBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
        agreeBtn.click();

        // Wait for success confirmation
        String successMessage = "Verification Complete!";
        consentTab.getByText(successMessage).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(60000));
        log.info("Entity consent verification completed successfully.");
        consentTab.close();
        this.groupsTab.close();
    }

    //Returns focus to the main DSA application page and reloads to reflect consent status. Verifies that entity consent is received after reload.
    public void returnToMainWorkspace() {
        log.info("Returning to main DSA application page...");
        mainPage.bringToFront();
        mainPage.reload();
        mainPage.waitForLoadState(LoadState.NETWORKIDLE);
        // Verify consent status updated — multiple applicants may show "Consent Received", use first()
        mainPage.getByText("Consent Received").first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(30000));
        log.info("Entity consent status confirmed: Consent Received.");
    }
}
