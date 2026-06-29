package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;
import java.nio.file.Paths;

public class CollateralPage extends BaseTest {
    private final Page page;

    public CollateralPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void clickAddCollateral() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Collateral")).click();
        log.info("Initialized property collateral addition sub-form workflow.");
    }

    public void selectOwners() {
        page.getByLabel("Collateral Owner Name *").click();
        page.getByText("Hannah Isaac").click();
        page.getByText("Noah johnson").click();
        page.keyboard().press("Escape"); // Dismisses multi-select popover overlay securely
    }

    public void PropertyDemographics(String type, String status, String stage, String scheme) {
        selectDropdownOption("Property Type *", type);
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
        page.keyboard().press("Tab"); // Triggers auto city/state background resolution query
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

    // ── Reusable Dropdown Protection Helper ──────────────────────────────────
    private void selectDropdownOption(String labelSelector, String targetOption) {
        page.getByLabel(labelSelector).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(targetOption).setExact(true)).click();
        page.keyboard().press("Escape"); // Clears the invisible backdrop panel layer instantly
    }
}