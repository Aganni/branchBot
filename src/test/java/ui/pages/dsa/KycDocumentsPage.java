package ui.pages.dsa;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

import java.nio.file.Paths;

public class KycDocumentsPage extends BaseTest {

    private final Page page;

    private static final String SAMPLE_DOC_PATH = "src/test/resources/testdata/bank_statement.pdf";

    public KycDocumentsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void uploadAllMandatoryDocuments() {
        log.info("Uploading documents for all mandatory KYC Documents sections");

        java.nio.file.Path absolutePath = Paths.get(SAMPLE_DOC_PATH).toAbsolutePath();

        // Find all <p> tags that contain "KYC Documents" and end with "*"
        Locator kycLabels = page.locator("p:has-text('KYC Documents')").filter(new Locator.FilterOptions().setHasText("*"));
        int count = kycLabels.count();
        log.info("Found {} KYC Documents labels (p tags) on the page", count);

        int uploaded = 0;
        for (int i = 0; i < count; i++) {
            Locator label = kycLabels.nth(i);

            if (!label.isVisible()) {
                log.info("Skipping KYC Documents #{} - not visible", i + 1);
                continue;
            }

            // Find the UPLOAD link in the parent div (sibling of this p tag)
            Locator parentDiv = label.locator("xpath=./ancestor::div[1]");
            Locator uploadLink = parentDiv.locator("a:has-text('UPLOAD')");

            if (uploadLink.count() == 0) {
                // Try going one level up
                parentDiv = label.locator("xpath=./ancestor::div[2]");
                uploadLink = parentDiv.locator("a:has-text('UPLOAD')");
            }

            if (uploadLink.count() == 0) {
                log.info("No UPLOAD link found for KYC Documents #{}, skipping", i + 1);
                continue;
            }

            log.info("Uploading to KYC Documents section #{}", i + 1);

            page.onFileChooser(fileChooser -> {
                fileChooser.setFiles(absolutePath);
            });

            uploadLink.first().scrollIntoViewIfNeeded();
            uploadLink.first().click();
            log.info("Clicked UPLOAD for section #{}", i + 1);

            // Wait for upload modal
            page.waitForTimeout(2000);

            // Select document type as "Pan"
            Locator docType = page.locator("#docType");
            if (docType.isVisible()) {
                docType.click();
                page.waitForTimeout(500);
                page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Pan"))
                        .or(page.locator("li:has-text('Pan')"))
                        .first().click();
                log.info("Selected document type: Pan");
            }

            // Save
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SAVE")).click();
            log.info("Saved document for section #{}", i + 1);

            page.waitForTimeout(3000);
            uploaded++;
        }

        log.info("Uploaded documents for {} KYC Documents sections", uploaded);
    }

    public void submitDocuments() {
        Locator submitBtn = page.locator("button:has-text('SUBMIT')");
        submitBtn.scrollIntoViewIfNeeded();
        submitBtn.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        submitBtn.click();
        log.info("Clicked Submit on KYC Documents page.");
    }
}
