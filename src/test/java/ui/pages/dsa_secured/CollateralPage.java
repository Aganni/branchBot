package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class CollateralPage extends BaseTest {
    private final Page page;

    public CollateralPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void clickAddCollateral() {
        log.info("Navigating away from Bank Statement...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save and Next").setExact(true)).click();
        log.info("Triggering proactive page reload to prevent layout freeze...");
        page.reload();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Collateral")).click();
        log.info("Initialized property collateral addition sub-form workflow.");
    }

    public void selectOwners() {
        Locator ownerDropdown = page.getByLabel(Pattern.compile("Collateral Owner Name", Pattern.CASE_INSENSITIVE));
        try {
            ownerDropdown.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5000));
        } catch (com.microsoft.playwright.TimeoutError e) {
            log.warn("Form field missing! Detecting potential dead click. Retrying interaction on '+ Add Collateral' button...");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Collateral")).click();
            ownerDropdown.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(15000));
        }
        ownerDropdown.click();
        page.waitForSelector("[role='listbox'], .MuiPopover-root",
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        page.getByText("Hannah Isaac").click();
        page.getByText("Noah johnson").click();
        page.keyboard().press("Escape");
        log.info("Successfully selected collateral property owners.");
    }
    public void PropertyDemographics(String type, String sub_type, String status, String stage, String scheme) {
        selectDropdownOption("Type *", type);
        selectDropdownOption("Sub Type *", sub_type);
        selectDropdownOption("Status *", status);
        selectDropdownOption("Stage of Under Construction *", stage);
        selectDropdownOption("Collateral Scheme *", scheme);
        log.info("Property background profiles and scheme specifications saved.");
    }

    public void PropertyDimensions(String builtUp, String carpet, String pincode, String street, String landmark) {
        page.getByLabel("Square Feet").check();
        page.getByLabel("Build Up *").fill(builtUp);
        page.getByLabel("Carpet Area *").fill(carpet);
        page.getByLabel("Pincode *").fill(pincode);
        page.keyboard().press("Tab");

        page.getByLabel("Street Name *").fill(street);
        page.getByLabel("Land Mark *").fill(landmark);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        log.info("Collateral property dimensions and address parameters saved.");
    }

    public void navigateToDocumentsTab() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Documents")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
    }

    public void uploadDocumentRecord(String fileName, String docType, int inputIndex) {
        Locator fileInputElement = page.locator("input[type='file']").nth(inputIndex);
        fileInputElement.setInputFiles(Paths.get(fileName));

        page.getByLabel("Type").nth(inputIndex).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(docType).setExact(true)).click();
        page.keyboard().press("Escape");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).nth(inputIndex).click();
        log.info("Successfully processed type attachment upload for: {}", docType);
    }

    public void clickNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
    }

    public void finalizeFeeCalculationAndLinkGeneration() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Calculate Login Fee")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Generate New Link")).click();
        log.info("Fee calculations executed successfully. Application link generation completed.");
    }

    // ── Optimized Dropdown Picker Helper Method ──────────────────────────────
    private void selectDropdownOption(String labelSelector, String targetOption) {

        page.getByLabel(labelSelector, new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForSelector("[role='listbox'], .MuiPopover-root",
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));

        // 3. FIX: Replaced getByRole with a highly resilient attribute lookup + case-insensitive text filter.
        // This cleanly targets items matching "Commercial" or "MH_Gunthewari (within MC)" regardless of layout structure or padding spaces.
        Locator optionItem = page.locator("[role='option'], .MuiMenuItem-root")
                .filter(new Locator.FilterOptions().setHasText(Pattern.compile(targetOption.trim(), Pattern.CASE_INSENSITIVE)))
                .first();

        optionItem.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        optionItem.click();

        // 4. Safely clear the active focus
        page.keyboard().press("Escape");
    }
}