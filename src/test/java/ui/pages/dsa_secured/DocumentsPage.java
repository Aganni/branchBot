package ui.pages.dsa_secured;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.BaseTest;

import java.nio.file.Paths;
import java.util.regex.Pattern;

public class DocumentsPage extends BaseTest {
    private final Page page;

    public DocumentsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void clickDocumentsButton() {
        page.reload();
        page.waitForTimeout(2000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Documents"))
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(30000));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Documents")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
        log.info("Clicked Documents button.");
    }

    public void uploadDocument(String filePath, String documentType, int inputIndex) {
        page.waitForTimeout(2000);

        // Ensure DOM is fresh and stable before locating elements
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);

        // Step 1: Re-locate the Upload button fresh from current DOM
        Locator uploadButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Upload")).nth(inputIndex);
        uploadButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        uploadButton.scrollIntoViewIfNeeded();
        page.waitForTimeout(500);

        // Step 2: Handle the file picker popup using FileChooser API
        FileChooser fileChooser = page.waitForFileChooser(() -> {
            uploadButton.click();
        });
        fileChooser.setFiles(Paths.get(filePath));
        log.info("Selected file via file picker: {}", filePath);

        // Step 3: Wait for the Type dropdown to become visible after file selection
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        Locator typeDropdown = page.locator("#docType");
        typeDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        typeDropdown.click();
        page.waitForTimeout(1000);

        // Step 4: Select the document type option from the dropdown listbox
        Locator option = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName(documentType).setExact(true));
        option.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        option.click();
        log.info("Selected document type: {}", documentType);
        page.waitForTimeout(500);

        // Step 5: Click Save and wait for upload to complete + page reload
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Saved document: {} with type: {}", filePath, documentType);
    }

    public void uploadApplicationForm(String filePath) {
        page.waitForTimeout(2000);
        // Reload page to get fresh DOM after previous uploads
        page.reload();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        // Locate the Application Form section fresh from the current DOM
        Locator appFormSection = page.locator("div").filter(new Locator.FilterOptions().setHasText(
                Pattern.compile("^Application FormApplication Form\\*UploadExisting Loan SheetUpload$")));

        // Scroll into view and click upload button
        Locator uploadButton = appFormSection.getByRole(AriaRole.BUTTON).first();
        uploadButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        uploadButton.scrollIntoViewIfNeeded();
        page.waitForTimeout(500);

        // Handle the file picker popup
        FileChooser fileChooser = page.waitForFileChooser(() -> {
            uploadButton.click();
        });
        fileChooser.setFiles(Paths.get(filePath));
        log.info("Selected Application Form file via file picker: {}", filePath);

        // Wait for the Type dropdown to become visible
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        Locator typeDropdown = page.locator("#docType");
        typeDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15000));
        typeDropdown.click();
        page.waitForTimeout(1000);

        // Select Application Form type
        Locator option = page.getByRole(AriaRole.OPTION,
                new Page.GetByRoleOptions().setName("Application Form").setExact(true));
        option.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        option.click();
        page.waitForTimeout(5000);

        // Save and wait for upload to finish
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
        log.info("Saved Application Form document.");
    }

    public void clickNext() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Next")).click();
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        log.info("Clicked Next button.");
        page.waitForTimeout(3000);
    }
}
