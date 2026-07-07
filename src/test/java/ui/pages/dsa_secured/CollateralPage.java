package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import java.nio.file.Paths;

public class CollateralPage extends BaseTest {
    private final Page page;

    public CollateralPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void clickAddCollateral() {
        page.reload();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        log.info("Page refreshed before adding collateral.");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Collateral"))
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Collateral")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        log.info("Clicked + Add Collateral button.");
    }

    public void selectOwners(String owner1, String owner2) {
        page.getByLabel("Collateral Owner Name *").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(60000));
        page.getByLabel("Collateral Owner Name *").click();

        // Select first owner by text
        page.getByText(owner1).click();

        // Select second owner via checkbox
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(owner2))
                .getByRole(AriaRole.CHECKBOX).check();

        // Close the owner dropdown
        //page.locator("#menu-collateralType div").first().click();
        log.info("Selected collateral owners: {} and {}", owner1, owner2);

    }

    public void selectType(String type) {
        page.getByLabel("Type *", new Page.GetByLabelOptions().setExact(true)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(type)).click();
        log.info("Selected type: {}", type);
    }

    public void selectSubType(String subType) {
        page.getByLabel("Sub Type *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(subType)).click();
        log.info("Selected sub type: {}", subType);
    }

    public void selectStatus(String status) {
        page.getByLabel("Status *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(status)).click();
        log.info("Selected status: {}", status);
    }

    public void fillPropertyDetails(String stage, String scheme) {
        page.getByLabel("Stage of Under Construction *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(stage)).click();

        page.getByLabel("Collateral Scheme *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(scheme)).click();

        log.info("Property details filled — Stage: {}, Scheme: {}", stage, scheme);
    }

    public void fillPropertyDimensions(String builtUp, String carpet, String pincode, String street, String landmark) {
        page.getByLabel("Square Feet").check();

        page.getByLabel("Build Up *").click();
        page.getByLabel("Build Up *").fill(builtUp);

        page.getByLabel("Carpet Area *").click();
        page.getByLabel("Carpet Area *").fill(carpet);

        page.getByLabel("Pincode *").click();
        page.getByLabel("Pincode *").fill(pincode);

        page.getByLabel("Street Name *").click();
        page.getByLabel("Street Name *").fill(street);

        page.getByLabel("Land Mark *").click();
        page.getByLabel("Land Mark *").fill(landmark);

        log.info("Property dimensions filled — {}sqft built-up, {}sqft carpet, pincode: {}", builtUp, carpet, pincode);
    }

    public void clickSave() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        log.info("Clicked Save. Collateral details saved.");
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
        log.info("Uploaded document: {}", docType);
    }

    public void clickNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
    }

    public void finalizeFeeCalculationAndLinkGeneration() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Calculate Login Fee")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Generate New Link")).click();
        log.info("Fee calculations executed. Application link generated.");
    }
}
