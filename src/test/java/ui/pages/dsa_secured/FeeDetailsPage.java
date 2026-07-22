package ui.pages.dsa_secured;

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
        Locator sandboxLink = page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("https://sandbox.assets.juspay"));
        sandboxLink.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(5000));

        Page paymentPage = page.waitForPopup(() -> {
            sandboxLink.click();
        });
        log.info("Payment gateway popup opened.");
        paymentPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        paymentPage.waitForTimeout(3000);
        paymentPage.locator("[id=\"\\31 0000111\"]").getByText("Netbanking").click();
        log.info("Selected Netbanking payment method.");
        paymentPage.waitForTimeout(1000);
        paymentPage.getByPlaceholder("Search banks").click();
        paymentPage.getByPlaceholder("Search banks").fill("test bank");
        paymentPage.waitForTimeout(1000);
        paymentPage.getByText("Test Bank").click();
        log.info("Selected Test Bank.");
        paymentPage.waitForTimeout(1000);
        Locator proceedBtn = paymentPage.getByText("proceed to pay ").nth(1);
        proceedBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        proceedBtn.click();
        log.info("Clicked Proceed to Pay.");
        paymentPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        paymentPage.waitForTimeout(5000);
        Locator otpInput = paymentPage.getByPlaceholder("Enter OTP");
        otpInput.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(3000));
        otpInput.click();
        otpInput.fill("111000");
        log.info("Entered OTP: 111000");
        paymentPage.waitForTimeout(1000);
        paymentPage.getByText("SUCCESS").click();
        log.info("Clicked SUCCESS.");
        paymentPage.waitForTimeout(2000);
        paymentPage.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit")).click();
        log.info("Clicked Submit on payment page.");
        paymentPage.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        paymentPage.waitForTimeout(10000);
        log.info("Payment completed successfully in sandbox.");
    }

    // After payment in sandbox popup, return focus to the original DSA portal page and proceed.
    public void navigateBackAndProceed() {
        // The original page (DSA portal) is still open — bring it back to focus
        page.bringToFront();
        page.reload();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Returned to DSA portal page.");

        // Click Next to move past Fee Details
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
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
