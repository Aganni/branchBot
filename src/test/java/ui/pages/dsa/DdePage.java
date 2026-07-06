package ui.pages.dsa;

import hooks.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class DdePage extends BaseTest {

    private final Page page;

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final String SAVE_BTN = "button:has-text('Save')";
    private static final String SUBMIT_BTN = "button:has-text('Submit')";

    public DdePage(Page page) {
        if (page == null)
            throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void fillField(String fieldId, String value) {
        if (value == null || value.isEmpty())
            return;
        page.locator("#" + fieldId).fill(value);
        log.info("Filled field [{}] with value: {}", fieldId, value);
    }

    public void selectFromDropdown(String fieldId, String value) {
        if (value == null || value.isEmpty())
            return;
        Locator input = page.locator("#" + fieldId);
        input.click();
        input.fill(value);

        Locator option = page.getByRole(AriaRole.LISTBOX)
                .getByText(value, new Locator.GetByTextOptions().setExact(false)).first();
        option.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        option.click();
        log.info("Selected [{}] from dropdown [{}]", value, fieldId);
    }

    public void selectRadio(String groupName, String option) {
        page.locator("input[name='" + groupName + "'][value='" + option.toLowerCase() + "']").click();
        log.info("Selected [{}] for radio group [{}]", option, groupName);
    }

    public void selectRadixPopover(String labelText, String optionValue) {
        if (optionValue == null || optionValue.isEmpty())
            return;

        log.info("Selecting [{}] for Radix Popover [{}]", optionValue, labelText);

        String xpath = "//label[text()='" + labelText + "']/ancestor::div[1]//button";
        Locator popoverTrigger = page.locator(xpath).first();

        popoverTrigger.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        popoverTrigger.click();

        Locator option = page.locator("[role='option'], [data-radix-collection-item], button")
                .filter(new Locator.FilterOptions().setHasText(optionValue))
                .last();

        option.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        option.click();

        log.info("Successfully selected {}", optionValue);
    }

    public void selectPersonWithDisability(String value) {
        log.info("Selecting Person with Disability: {}", value);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Person with Disability")).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(value)).click();
        log.info("Selected Person with Disability: {}", value);
    }

    public void selectFirstDisabilityType() {
        log.info("Selecting first available Type of Disability option");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Type of Disability")).click();
        page.getByRole(AriaRole.OPTION).first().click();
        log.info("Selected first Type of Disability option");
    }

    public void fillRandomDisabilityPercentage() {
        int percentage = new java.util.Random().nextInt(100) + 1;
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Percentage of Disability"))
                .fill(String.valueOf(percentage));
        log.info("Filled Percentage of Disability: {}", percentage);
    }

    public void clickSave() {
        page.locator(SAVE_BTN).click();
        log.info("Clicked Save button");
    }

    public void fillCorrespondenceAddress(String partialAddress) {
        log.info("Filling Correspondence Address: {}", partialAddress);

        Locator addressInput = page.getByPlaceholder("Correspondence Address (Line 1)");

        addressInput.click();
        addressInput.fill(partialAddress);

        Locator suggestion = page.locator("div").filter(new Locator.FilterOptions().setHasText(partialAddress)).last();
        suggestion.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        suggestion.click();

        log.info("Address suggestion selected.");
    }

    public void fillShareHolding(String value) {
        if (value == null || value.isEmpty()) return;
        log.info("Filling Share Holding: {}", value);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Share Holding")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Share Holding")).fill(value);
        log.info("Filled Share Holding: {}", value);
    }

    public void fillCorrespondenceAddressManually(String line1, String line2, String pincode, String city, String state) {
        log.info("Filling Correspondence Address manually");

        Locator line1Input = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Correspondence Address (Line 1)"));
        line1Input.click();
        line1Input.fill(line1);
        line1Input.press("Tab");
        log.info("Filled Line 1: {}", line1);

        Locator line2Input = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Correspondence Address (Line 2)"));
        line2Input.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        if (line2 != null && !line2.isEmpty()) {
            line2Input.fill(line2);
            log.info("Filled Line 2: {}", line2);
        }

        Locator pincodeInput = page.getByPlaceholder("Correspondence Pincode");
        if (pincodeInput.count() > 0) {
            pincodeInput.fill(pincode);
            log.info("Filled Pincode: {}", pincode);
        }

        Locator cityInput = page.getByPlaceholder("Correspondence City");
        if (cityInput.count() > 0) {
            cityInput.fill(city);
            log.info("Filled City: {}", city);
        }

        Locator stateInput = page.getByPlaceholder("Correspondence State");
        if (stateInput.count() > 0) {
            stateInput.fill(state);
            log.info("Filled State: {}", state);
        }
    }

    public void selectEmail(String email) {
        log.info("Selecting Email: {}", email);

        Locator emailInput = page.getByPlaceholder("Select Email");
        emailInput.click();
        emailInput.fill(email);
    }

    public void selectValueFromMuiSelect(String fieldId, String value) {
        if (value == null || value.isEmpty())
            return;

        log.info("Opening MuiSelect dropdown [{}]", fieldId);

        Locator selectTrigger = page.locator("#" + fieldId);
        selectTrigger.click();

        log.info("Selecting option: {}", value);
        Locator option = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName(value).setExact(true));

        if (option.count() == 0) {
            option = page.locator("li[role='option']").filter(new Locator.FilterOptions().setHasText(value)).first();
        }

        option.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        option.click();

        log.info("Successfully selected [{}]", value);
    }

    public void clickSubmit() {
        page.locator(SUBMIT_BTN).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(SUBMIT_BTN).click();
        log.info("Clicked Submit button");
    }
}
