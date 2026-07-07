package ui.pages.dsa_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

import java.nio.file.Paths;

public class DocumentsPage extends BaseTest {
    private final Page page;

    public DocumentsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void clickDocumentsButton() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Documents"))
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Documents")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        log.info("Clicked Documents button.");
    }

    public void uploadDocument(String filePath, String documentType, int inputIndex) {
        // Click the file input and upload the file
        Locator fileInput = page.locator("input[type='file']").nth(inputIndex);
        fileInput.setInputFiles(Paths.get(filePath));
        log.info("Uploaded file: {}", filePath);

        // Select the document type from dropdown
        page.getByLabel("Type").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(documentType).setExact(true)).click();
        log.info("Selected document type: {}", documentType);

        // Click Save for this document entry
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        log.info("Saved document: {} with type: {}", filePath, documentType);
    }

    public void clickAddMoreDocument() {
        // Clicks the add more button (last file input area) to add another document upload slot
        Locator addMoreButton = page.locator("input[type='file']").last();
        addMoreButton.click();
        log.info("Clicked to add another document upload slot.");
    }
}
