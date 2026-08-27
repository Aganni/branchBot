package ui.pages.dsa_secured_Entity;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

public class DashboardPage extends BaseTest {
    
    private final Page page;
    
    // ── Locators ─────────────────────────────────────────────────────────────
    private static final String LOAN_TYPE_DROPDOWN = ".ant-select-selector";
    private static final String DROPDOWN_PLACEHOLDER = "Choose Loan Type";
    private static final String SELECT_ITEM_OPTION = ".ant-select-item-option";
    private static final String ADD_APPLICATION_BTN_TEXT = "Add Application";

    public DashboardPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }
    public void initiateApplication(String loanType) {
        log.info("Attempting to initiate application for loan type: {}", loanType);

        page.locator(LOAN_TYPE_DROPDOWN)
                .filter(new Locator.FilterOptions().setHasText(DROPDOWN_PLACEHOLDER))
                .click();

        // Ant Design uses a virtual-scroll dropdown, so options below the fold are not
        // rendered in the DOM until the popup container is scrolled. We scroll inside
        // the dropdown popup until the target option becomes visible and clickable.
        Locator dropdownPopup = page.locator(".ant-select-dropdown .rc-virtual-list-holder");
        Locator loanTypeOption = page.locator(SELECT_ITEM_OPTION)
                .filter(new Locator.FilterOptions().setHasText(loanType));

        int maxScrollAttempts = 10;
        for (int i = 0; i < maxScrollAttempts; i++) {
            if (loanTypeOption.isVisible()) break;
            dropdownPopup.evaluate("el => el.scrollTop += 100");
            page.waitForTimeout(300);
        }

        loanTypeOption.click();

        page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName(ADD_APPLICATION_BTN_TEXT))
                .click();

        log.info("Successfully selected {} and clicked {} button", loanType, ADD_APPLICATION_BTN_TEXT);
    }
}
