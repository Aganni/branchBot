package ui.pages.dsa_secured_plp;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

import java.util.Map;

public class PartnerDetailsPage extends BaseTest {

    private final Page page;

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final String PARTNER_NAME = "#partnerName";

    public PartnerDetailsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void selectPartnerNameByOptionIndex(int optionIndex) {
        log.info("Selecting partner name via option index: {}", optionIndex);
        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Partner Name")).click();
        page.locator("#partnerName-option-" + optionIndex).click();
    }

    public void fillMandatoryDetails(Map<String, String> details) {
        // 1. Partner Name (option-index based select)
        //    If the field is already disabled (auto-populated based on login context),
        //    skip the selection — the partner is pre-assigned.
        boolean partnerDisabled = page.locator(PARTNER_NAME).isDisabled();
        if (partnerDisabled) {
            log.info("Partner name field is pre-filled and disabled, skipping selection.");
        } else {
            page.click(PARTNER_NAME);
            String index = details.get("partnerNameOptionIndex");
            page.click("#partnerName-option-" + index);
            log.info("Selecting partner name via option index: {}", index);
        }

        // 2. Program (label-based MUI select). Scheme/Sub Product options are
        // dependent on Program being selected first, so this must run before them.
        String program = details.get("program");
        if (program != null && !program.isEmpty()) {
            log.info("Selecting program: {}", program);
            page.getByLabel("Program *").click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(program)).click();
        }

        // 3. Scheme (label-based MUI select)
        String scheme = details.get("Scheme");
        log.info("Selecting scheme: {}", scheme);
        page.getByLabel("Scheme *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(scheme).setExact(true)).click();

        // 4. Sub Product (label-based MUI select)
        String subProduct = details.get("subProduct");
        log.info("Selecting sub product: {}", subProduct);
        page.getByLabel("Sub Product *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(subProduct)).click();

        // 5. Branch (combobox with partial text fill for autocomplete)
        String branch = details.get("branch");
        String branchFullName = details.get("branchFullName");
        log.info("Selecting branch: {}", branchFullName != null ? branchFullName : branch);
        page.getByLabel("Branch *").click();
        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Branch")).fill(branch);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(branchFullName != null ? branchFullName : branch)).click();

        // 6. Sales Manager (label-based MUI select)
        String salesManager = details.get("sales Manager");
        log.info("Selecting sales manager: {}", salesManager);
        page.getByLabel("Sales Manager *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(salesManager)).click();

        log.info("Successfully populated all data-driven Partner configuration details.");
    }

    public void clickSaveAndNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        log.info("Clicked Next button");
    }
}
