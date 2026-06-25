package ui.pages.dsa;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

import java.util.Map;

public class PartnerDetailsPage extends BaseTest {

    private final Page page;

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final String SAVE_AND_NEXT_BTN = "Save and Next";

    public PartnerDetailsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    private void selectCombobox(String name, String optionValue) {
        log.info("Selecting combobox '{}' with value: {}", name, optionValue);
        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName(name)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(optionValue)).click();
    }

    private void selectButtonDropdown(String name, String optionValue) {
        log.info("Selecting button dropdown '{}' with value: {}", name, optionValue);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(optionValue)).click();
    }

    public void fillMandatoryDetails(Map<String, String> details) {
        if (details.containsKey("Branch")) {
            selectCombobox("Branch", details.get("Branch"));
        }
        if (details.containsKey("Sales Manager")) {
            selectCombobox("Sales Manager", details.get("Sales Manager"));
        }
        if (details.containsKey("Scheme")) {
            selectButtonDropdown("Scheme", details.get("Scheme"));
        }
    }

    public void clickSaveAndNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(SAVE_AND_NEXT_BTN)).click();
        log.info("Clicked Save and Next button");
    }
}
