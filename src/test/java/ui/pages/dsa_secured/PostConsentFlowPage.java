package ui.pages.dsa_secured;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;
import java.nio.file.Paths;

public class PostConsentFlowPage extends BaseTest {
    private final Page page;

    public PostConsentFlowPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void bypassVerificationCheckpoints() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("NEXT")).click();
        log.info("Moved to Exposure Dedupe successful");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("next")).click();
        log.info("Moving to bureau output screen");
    }

    public void downloadBureauReport() throws InterruptedException {
        log.info("Attempting to capture and download generated Bureau reports.");
        Thread.sleep(20000);

        Download download = page.waitForDownload(() -> { page.waitForPopup(() -> {
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Download Report")).first().click();
            });
        });
        log.info("Report downloaded successfully: {}", download.suggestedFilename());
    }

    public void navigateToCollateralSection() throws InterruptedException {
        Thread.sleep(10000);
        getPage().locator("Bank Statement").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15000));
        getPage().waitForTimeout(2000);
        //page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Bank Statement")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save and Next")).click();
    }

    public void clickAddCollateral() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Collateral")).click();
    }

    public void selectOwners() {
        page.getByLabel("Collateral Owner Name *").click();
        page.getByText("Hannah Isaac").click();
        page.getByText("Noah johnson").click();
    }

    public void fillPropertyDemographics(String type, String status, String stage, String scheme) {
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(type)).click();

        page.getByLabel("Status *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(status)).click();

        page.getByLabel("Stage of Under Construction *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(stage)).click();

        page.getByLabel("Collateral Scheme *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(scheme)).click();
    }

    public void fillPropertyDimensionsAndAddress(String builtUp, String carpet, String pincode, String street, String landmark) {
        page.getByLabel("Square Feet").check();

        page.getByLabel("Build Up *").fill(builtUp);
        page.getByLabel("Carpet Area *").fill(carpet);
        page.getByLabel("Pincode *").fill(pincode);
        page.getByLabel("Street Name *").fill(street);
        page.getByLabel("Land Mark *").fill(landmark);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        log.info("Collateral property definitions added and saved.");
    }

    public void navigateToDocumentsTab() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Documents")).click();
    }

    /**
     * Bypasses unstable recorded dynamic hashes by leveraging structural input tags sequentially.
     */
    public void uploadDocumentRecord(String fileName, String docType, int inputIndex) {
        Locator fileInputElement = page.locator("input[type='file']").nth(inputIndex);
        fileInputElement.setInputFiles(Paths.get(fileName));

        // Locate and explicitly configure type configurations drop-down mappings
        page.getByLabel("Type").nth(inputIndex).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(docType)).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).nth(inputIndex).click();
        log.info("Successfully processed type attachment upload for: {}", docType);
    }

    public void clickNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
    }

    public void finalizeFeeCalculationAndLinkGeneration() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Calculate Login Fee")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Generate New Link")).click();
        log.info("Fee parameters configured and registration workflow initialization links sent.");
    }
}