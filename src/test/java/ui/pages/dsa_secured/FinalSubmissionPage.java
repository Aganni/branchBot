package ui.pages.dsa_secured;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

import java.util.regex.Pattern;

public class FinalSubmissionPage extends BaseTest {
    private final Page page;
    private final BrowserContext context;
    private Page groupsTab;

    private final String signInLinkText = "Sign in";
    private final String emailInputName = "Email or phone";
    private final String passwordInputName = "Enter your password";
    private final String nextButtonName = "Next";
    private final String groupsLinkName = "notification test";
    private final String trimmedContentLabel = "Show trimmed content";
    private final String verifyEmailLinkName = "Verify My Email";
    private final String verifyBtnName = "Verify";
    private static final String VERIFY_EMAIL_SUBJECT = "Verify your email & start your loan application";

    public FinalSubmissionPage(Page page, BrowserContext context) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
        this.context = context;
    }
    public void clickSubmit() {
        Locator submitBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit"));
        submitBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        submitBtn.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("Clicked Submit button.");
    }
    public void handleSwiftLoanPopup() {
        Locator swiftLoanText = page.getByText("Please add your swift Loan");
        swiftLoanText.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        swiftLoanText.click();
        page.waitForTimeout(1000);
        swiftLoanText.click();
        page.waitForTimeout(1000);
        page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForTimeout(500);
        Locator yesOption = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("Yes"));
        yesOption.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        yesOption.click();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("OK")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("Handled Swift Loan popup - selected Yes.");
    }

    // Step 3: Click Next and then Submit (triggers email verification validation)
    public void clickNextAndSubmit() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("Clicked Next.");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("Clicked Submit - validation message for email verification expected.");
    }

    // Step 4: Navigate to Primary Applicant section and trigger email verification
    public void triggerEmailVerification() {
        page.getByText("Primary Applicant's Email Id").click();
        page.waitForTimeout(1000);

        // Navigate to Primary Applicant section
        page.locator("div").filter(new Locator.FilterOptions()
                        .setHasText(Pattern.compile("^Primary Applicant$")))
                .locator("path").click();
        page.waitForTimeout(1000);

        // Click Resend Link to send verification email
        Locator resendLink = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Resend Link"));
        resendLink.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        resendLink.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);
        log.info("Clicked Resend Link for email verification.");
    }

    // Step 5: Open notification group, sign in, find verification email, and verify
    public void loginAndNavigateToGroups(String email, String password) {
        log.info("Opening Google Groups in a new tab...");
        this.groupsTab = context.newPage();
        // Land on the stable "My groups" page rather than deep-linking straight to the
        // notification-test thread list. Google's OAuth redirect doesn't reliably honor a deep
        // link as the post-sign-in destination (it can drop you on the generic Groups home
        // instead), which is why the group page never actually opened. Navigating into the
        // group by clicking its visible name is what actually lands on it.
        this.groupsTab.navigate("https://groups.google.com/my-groups");
        Locator signInBtn = groupsTab.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName(signInLinkText));
        if (signInBtn.isVisible()) {
            log.info("Sign-in required. Authenticating with Google");
            signInBtn.click();
            groupsTab.getByRole(AriaRole.TEXTBOX,
                    new Page.GetByRoleOptions().setName(emailInputName)).fill(email);
            groupsTab.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName(nextButtonName)).click();
            Locator pwdField = groupsTab.getByRole(AriaRole.TEXTBOX,
                    new Page.GetByRoleOptions().setName(passwordInputName));
            pwdField.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            pwdField.fill(password);
            groupsTab.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName(nextButtonName)).click();
        }

        log.info("Waiting for Google Groups to fully load");
        groupsTab.waitForLoadState(LoadState.LOAD);
        groupsTab.waitForTimeout(10000);

        // Open the "notification test" group by its visible name/link instead of assuming a URL
        // slug. Fall back to the direct deep-link URL if the link can't be found (e.g. the group
        // isn't listed on "My groups" for this account).
        Locator groupLink = groupsTab.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName(groupsLinkName));
        try {
            groupLink.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
            log.info("Opening '{}' group from My Groups list...", groupsLinkName);
            groupLink.first().click();
        } catch (PlaywrightException e) {
            log.info("'{}' link not found on My Groups page, falling back to direct group URL...", groupsLinkName);
            groupsTab.navigate("https://groups.google.com/a/creditsaison-in.com/g/notification-test");
        }
        groupsTab.waitForLoadState(LoadState.LOAD);
        groupsTab.waitForTimeout(5000);

        Locator verifyEmailSubject = groupsTab.getByText(VERIFY_EMAIL_SUBJECT).first();
        boolean emailFound = false;
        int maxAttempts = 6;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            log.info("Searching for '{}' email (attempt {}/{})", VERIFY_EMAIL_SUBJECT, attempt, maxAttempts);
            try {
                verifyEmailSubject.waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
                emailFound = true;
                break;
            } catch (PlaywrightException e) {
                log.info("Email not visible yet, reloading and retrying");
                groupsTab.reload();
                groupsTab.waitForLoadState(LoadState.LOAD);
                groupsTab.waitForTimeout(5000);
            }
        }
        if (!emailFound) {
            throw new RuntimeException("Timed out waiting for '" + VERIFY_EMAIL_SUBJECT
                    + "' email to appear after " + maxAttempts + " attempts.");
        }
        verifyEmailSubject.click();
        groupsTab.waitForTimeout(3000);
        log.info("Opened '{}' email thread.", VERIFY_EMAIL_SUBJECT);
    }

    // Step 6: Open the latest email, expand trimmed content, click Verify My Email, and verify
    public void openLatestEmailAndVerify() {
        if (this.groupsTab == null) {
            throw new IllegalStateException("Google Groups tab not initialized. Call loginAndNavigateToGroups() first.");
        }
        log.info("Scrolling to bottom of the email thread to reach the latest reply...");
        groupsTab.evaluate("window.scrollTo(0, document.body.scrollHeight)");
        groupsTab.waitForTimeout(2000);
        groupsTab.evaluate("window.scrollTo(0, document.body.scrollHeight)");
        groupsTab.waitForTimeout(2000);

        log.info("Looking for email content sections");
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
        // Scroll down within the last section to reveal the "Verify My Email" link
        latestEmail.locator(":scope >> *").last().scrollIntoViewIfNeeded();
        groupsTab.waitForTimeout(1000);
        // Find "Verify My Email" link
        log.info("Searching for 'Verify My Email' link in the last email...");
        Locator scopedLink = latestEmail.locator("a:has-text('Verify My Email')");
        final Locator verifyLink;
        if (scopedLink.count() == 0) {
            log.info("Link not found in last section, searching entire page...");
            verifyLink = groupsTab.locator("a:has-text('Verify My Email')").last();
        } else {
            verifyLink = scopedLink.last();
        }
        verifyLink.scrollIntoViewIfNeeded();
        groupsTab.waitForTimeout(500);

        // Click the link — it opens in a new tab
        log.info("Clicking 'Verify My Email' link...");
        Page verifyPage = groupsTab.waitForPopup(() -> {
            verifyLink.click();
        });
        verifyPage.waitForLoadState(LoadState.NETWORKIDLE);

        // Click the final Verify button on the verification page
        log.info("Clicking Verify button on verification page");
        Locator agreeBtn = verifyPage.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(verifyBtnName));
        agreeBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
        agreeBtn.click();
        verifyPage.waitForLoadState(LoadState.NETWORKIDLE);
        verifyPage.waitForTimeout(3000);
        log.info("Email verified successfully.");

        verifyPage.close();
        this.groupsTab.close();
    }

    // Step 7: Return to main page and navigate through all sections to final submit
    public void reloadAndSubmitApplication() {
        log.info("Returning to main DSA application page");
        page.bringToFront();
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Reloaded DSA portal page after email verification.");

        // Navigate through Primary Applicant
        Locator primaryApplicant = page.locator("span").filter(
                new Locator.FilterOptions().setHasText("Primary Applicant")).first();
        primaryApplicant.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        primaryApplicant.click();
        page.waitForTimeout(1000);

        // Close any backdrop/modal
        Locator backdrop = page.locator(".MuiBackdrop-root");
        if (backdrop.count() > 0 && backdrop.first().isVisible()) {
            backdrop.click();
            page.waitForTimeout(500);
        }

        // Navigate through Co Applicants
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Co Applicants")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);

        // Click NEXT (from Co Applicants to Dedupe)
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("NEXT")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);

        // Click next (from Dedupe to Bureau)
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("next")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);

        // Click Bank Statement
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Bank Statement")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);

        // Click Save and Next
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save and Next")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);

        // Click Documents
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Documents")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);

        // Click Next (from Documents to Fee Details/Login)
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);

        // Final Submit
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Application submitted successfully.");
    }
}
