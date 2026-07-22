package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

/**
 * Page Object for KYC Verification in the Jarvis Secured portal.
 * Handles navigating to the Verification tab and resolving KYC for co-applicants.
 */
public class KycVerificationPage extends BaseTest {

    private final Page page;

    public KycVerificationPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  NAVIGATE TO VERIFICATION TAB
    // ═══════════════════════════════════════════════════════════════════════════

    public void navigateToVerificationTab() {
        log.info("Navigating to Verification tab...");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Verification")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Verification tab loaded.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CLICK EDIT TO OPEN KYC RESOLUTION
    // ═══════════════════════════════════════════════════════════════════════════

    public void clickEditForKycResolution() {
        log.info("Clicking Edit to open KYC resolution...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" edit")).click();
        page.waitForTimeout(2000);
        log.info("KYC resolution dialog opened.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  RESOLVE KYC
    // ═══════════════════════════════════════════════════════════════════════════

    public void resolveKyc() {
        log.info("Resolving KYC...");
        // Click the first Resolve button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Resolve")).click();
        page.waitForTimeout(1000);

        // Click the Resolve button inside the confirmation dialog
        page.getByLabel("Resolve Kyc")
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Resolve")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("KYC resolved successfully.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  NAVIGATE BACK TO APP FORM
    // ═══════════════════════════════════════════════════════════════════════════

    public void navigateBackToAppForm() {
        log.info("Navigating back to App Form...");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("App Form")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("App Form loaded.");
    }
}
