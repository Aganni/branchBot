package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

/**
 * Page Object for Program validation in the CAM stage of the Jarvis Secured portal.
 * Handles selecting the program under Partner Details and performing stage movement.
 */
public class ProgramValidationPage extends BaseTest {

    private final Page page;

    public ProgramValidationPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  OPEN PARTNER DETAILS & EDIT
    // ═══════════════════════════════════════════════════════════════════════════

    public void openPartnerDetailsForEdit() {
        log.info("Opening Partner Details for editing...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Partner Details")).click();
        page.waitForTimeout(1000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);

        log.info("Partner Details opened for editing.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SELECT PROGRAM
    // ═══════════════════════════════════════════════════════════════════════════

    public void selectProgram(String programName) {
        log.info("Selecting program: {}", programName);
        page.getByPlaceholder("Select the Program").click();
        page.waitForTimeout(500);

        page.locator("li").filter(new Locator.FilterOptions().setHasText(programName)).click();
        page.waitForTimeout(1000);

        log.info("Program '{}' selected.", programName);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SUBMIT PARTNER DETAILS
    // ═══════════════════════════════════════════════════════════════════════════

    public void submitPartnerDetails() {
        log.info("Submitting Partner Details...");
        page.locator("[id=\"DSA\\ Details\"]").getByText("Submit Arrow Right icon").click();
        page.waitForTimeout(2000);
        log.info("Partner Details submitted.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  COLLAPSE PARTNER DETAILS SECTION
    // ═══════════════════════════════════════════════════════════════════════════

    public void collapsePartnerDetails() {
        log.info("Collapsing Partner Details section...");
        try {
            page.locator("[id=\"DSA\\ Details\"] div")
                    .filter(new Locator.FilterOptions().setHasText("Partner Details Edit Partner"))
                    .nth(1).click();
            page.waitForTimeout(1000);
        } catch (Exception e) {
            log.info("Could not collapse Partner Details section: {}", e.getMessage());
        }
        log.info("Partner Details section collapsed.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MOVE TO CREDIT REVIEW
    // ═══════════════════════════════════════════════════════════════════════════

    public void moveToCreditReview() {
        log.info("Moving application to Credit Review after program validation...");
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);

        page.locator("li").filter(new Locator.FilterOptions().setHasText("Move to Credit Review")).click();
        page.waitForTimeout(2000);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Application moved to Credit Review.");
    }
}
