package ui.pages.dsa_secured;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

import java.util.Map;

public class PartnerDetailsPage extends BaseTest {

    private final Page page;

    public PartnerDetailsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void fillMandatoryDetails(Map<String, String> details) {
        // 1. Program (label-based MUI select)
        String program = details.get("program");
        if (program != null && !program.isEmpty()) {
            log.info("Selecting program: {}", program);
            page.getByLabel("Program *").click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(program)).click();
        }

        // 2. Scheme (label-based MUI select)
        String scheme = details.get("Scheme");
        log.info("Selecting scheme: {}", scheme);
        page.getByLabel("Scheme *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(scheme).setExact(true)).click();

        // 3. Sub Product (label-based MUI select)
        String subProduct = details.get("subProduct");
        log.info("Selecting sub product: {}", subProduct);
        page.getByLabel("Sub Product *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(subProduct)).click();

        // 4. Branch (combobox with partial text fill for autocomplete)
        String branch = details.get("branch");
        String branchFullName = details.get("branchFullName");
        log.info("Selecting branch: {}", branchFullName);
        page.getByLabel("Branch *").click();
        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Branch")).fill(branch);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(branchFullName)).click();

        // 5. Sales Manager (label-based MUI select)
        String salesManager = details.get("sales Manager");
        log.info("Selecting sales manager: {}", salesManager);
        page.getByLabel("Sales Manager *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(salesManager)).click();

        log.info("Successfully populated all Partner configuration details.");
    }

    public void clickSaveAndNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        log.info("Clicked Next button");
    }
}
