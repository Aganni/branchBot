package ui.pages.dsa_secured;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

public class FeeDetailsPage extends BaseTest {
    private final Page page;

    public FeeDetailsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }
    public void clickCalculateLoginFee() {
        Locator calculateBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Calculate Login Fee"));
        calculateBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        calculateBtn.click();
        log.info("Clicked Calculate Login Fee button.");
        page.waitForTimeout(2500);
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
    }
    public void clickGenerateNewLink() {
        Locator generateLinkBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Generate New Link"));
        generateLinkBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        generateLinkBtn.click();
        log.info("Clicked Generate New Link button.");
        page.waitForTimeout(2000);
    }

    public void completePaymentInSandbox() {
        // Wait for the Razorpay payment link to appear after generating
        Locator paymentLink = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("https://rzp.io"));
        paymentLink.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        log.info("Razorpay payment link visible.");

        // Click the link — opens Razorpay checkout in a popup
        Page razorpayPage = page.waitForPopup(() -> {
            paymentLink.click();
        });
        log.info("Razorpay payment popup opened.");
        razorpayPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        razorpayPage.waitForTimeout(5000);

        // Inside the Razorpay popup — interact with the checkout iframe
        var checkoutFrame = razorpayPage.locator("#checkout-parent iframe").contentFrame();

        // Click the pay/proceed button
        checkoutFrame.getByTestId("bottom-cta-button").click();
        razorpayPage.waitForTimeout(2000);

        // Select Netbanking
        checkoutFrame.getByTestId("netbanking").getByTestId("instrument-stack")
                .locator("div").first().click();
        razorpayPage.waitForTimeout(1000);

        // Select HDFC bank — this opens a bank authentication popup
        Page bankPage = razorpayPage.waitForPopup(() -> {
            checkoutFrame.getByRole(AriaRole.BUTTON,
                    new com.microsoft.playwright.FrameLocator.GetByRoleOptions().setName("HDFC HDFC")).click();
        });
        log.info("Bank authentication popup opened.");
        bankPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        bankPage.waitForTimeout(3000);

        // Click Success on the bank page
        bankPage.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Success")).click();
        log.info("Clicked Success on bank page.");

        // Bank page closes itself after success — wait on the Razorpay page instead
        // Use try-catch since razorpayPage may also close after payment completes
        try {
            razorpayPage.waitForTimeout(8000);
        } catch (Exception e) {
            log.info("Razorpay page closed after payment completion.");
        }
        log.info("Payment completed successfully via Razorpay.");
    }

    // After payment in Razorpay popup, return focus to the original DSA portal page and proceed.
    public void navigateBackAndProceed() {
        // The original page (DSA portal) is still open — bring it back to focus
        page.bringToFront();
        page.reload();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);
        log.info("Returned to DSA portal page.");

        // Click Next to move past Fee Details
        Locator nextBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next"));
        nextBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        nextBtn.click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("Clicked Next after fee payment.");
    }
    public void confirmPaymentReceived() {
        Locator receivedText = page.getByText("RECEIVED", new Page.GetByTextOptions().setExact(true));
        receivedText.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        receivedText.click();
        log.info("Selected RECEIVED status.");
        page.waitForTimeout(1000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Submitted payment confirmation.");
    }
}
