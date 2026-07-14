package ui.pages.dsa_secured_plp;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

import java.util.Map;

public class PartnerDetailsPage extends BaseTest {

    private final Page page;

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final String PARTNER_NAME = "#partnerName";
    private static final String SCHEME_SELECT_ID = "#scheme";
    private static final String SUB_PRODUCT_BTN = "#subProduct";
    private static final String BRANCH_INPUT_ID = "#branch";
    private static final String SALES_MANAGER_INPUT_ID = "#salesManager";

    private static final String SAVE_AND_NEXT_BTN = "button:has-text('NEXT')";

    public PartnerDetailsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    private void fillAutocomplete(String locator, String value) {
        log.info("Filling autocomplete '{}' with value: {}", locator, value);
        Locator input = page.locator(locator);
        input.click();
        input.clear();
        input.fill(value);

        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(value).setExact(true)).click();
    }

    private void selectDropdown(String locator, String value) {
        log.info("Selecting dropdown '{}' with value: {}", locator, value);
        page.locator(locator).click(); 
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(value).setExact(true)).click();
    }

    public void selectPartnerNameByOptionIndex(int optionIndex) {
        log.info("Selecting partner name via option index: {}", optionIndex);
        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("Partner Name")).click();
        page.locator("#partnerName-option-" + optionIndex).click();
    }

    public void fillMandatoryDetails(Map<String, String> details) {
        page.click(PARTNER_NAME);
        String index = details.get("partnerNameOptionIndex");
        page.click("#partnerName-option-" + index);
        log.info("Selecting partner name via option index: {}", index);

        // 2. Scheme Dropdown Allocation + Dropdown Dismissal
        page.click(SCHEME_SELECT_ID);
        page.click("role=option[name='" + details.get("Scheme") + "']");
        page.keyboard().press("Escape"); // Dismisses the Material UI overlay immediately

        // 3. Sub Product Field (Now clicks the active dropdown directly)
        page.click(SUB_PRODUCT_BTN);
        page.click("role=option[name='" + details.get("subProduct") + "']");
        page.keyboard().press("Escape");

        // 4. Branch Selection
        page.click(BRANCH_INPUT_ID);
        page.click("role=option[name*='" + details.get("branch") + "']");
        page.keyboard().press("Escape");

        // 5. Sales Manager Combo Routing
        page.click(SALES_MANAGER_INPUT_ID);
        page.fill(SALES_MANAGER_INPUT_ID, "");
        page.click("role=option[name*='" + details.get("sales Manager") + "']");
        log.info("Successfully populated all data-driven Partner configuration details.");
    }

    public void clickSaveAndNext() {
        page.locator(SAVE_AND_NEXT_BTN).click();
        log.info("Clicked SAVE AND NEXT button");
    }
}
