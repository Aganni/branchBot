package ui.pages.dsa;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import data.TestDataProvider;
import hooks.BaseTest;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class KycDocumentsPage extends BaseTest {

    private final Page page;

    private static final String SAMPLE_DOC_PATH = "src/test/resources/testdata/bank_statement.pdf";

    // File input indices (zero-based) for KYC Documents per LPC
    // Run in browser console to find: document.querySelectorAll('input[type="file"]').forEach((el, i) => console.log(i, el.id))
    private static final Map<String, List<Integer>> KYC_FILE_INPUT_INDICES = Map.of(
            "UBL", List.of(10),
            "FCL", List.of(2, 3, 15),
            "SEP", List.of()    // To be updated
    );

    // Document type to select per file input index (default is "Pan")
    // Key format: "LPC-index" → type name
    private static final Map<String, String> DOC_TYPE_OVERRIDES = Map.of(
            "FCL-3", "Aadhaarxml"
    );

    public KycDocumentsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void uploadAllMandatoryDocuments() {
        log.info("Uploading mandatory KYC documents by file input index");

        java.nio.file.Path absolutePath = Paths.get(SAMPLE_DOC_PATH).toAbsolutePath();
        String lpc = TestDataProvider.getLpc();

        List<Integer> indices = KYC_FILE_INPUT_INDICES.getOrDefault(lpc, List.of(10));
        log.info("Will upload to file input indices {} for LPC: {}", indices, lpc);

        for (int fileInputIndex : indices) {
            String docType = DOC_TYPE_OVERRIDES.getOrDefault(lpc + "-" + fileInputIndex, "Pan");
            uploadToFileInput(fileInputIndex, absolutePath, docType);
        }

        log.info("All mandatory KYC documents uploaded for LPC: {}", lpc);
    }

    private void uploadToFileInput(int fileInputIndex, java.nio.file.Path filePath, String docTypeName) {
        Locator fileInput = page.locator("input[type='file']").nth(fileInputIndex);

        log.info("Setting file on input[type='file'] at index {}", fileInputIndex);
        fileInput.setInputFiles(filePath);

        // Wait for upload modal
        page.waitForTimeout(2000);

        // Select document type
        Locator docType = page.locator("#docType");
        if (docType.isVisible()) {
            docType.click();
            page.waitForTimeout(500);
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(docTypeName))
                    .or(page.locator("li:has-text('" + docTypeName + "')"))
                    .first().click();
            log.info("Selected document type: {}", docTypeName);
        } else {
            log.error("docType dropdown not visible after upload at index {}!", fileInputIndex);
            throw new RuntimeException("Upload modal did not appear for file input at index " + fileInputIndex);
        }

        // Save
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.waitForTimeout(3000);
        log.info("Saved KYC document for file input index {}", fileInputIndex);
    }

    public void submitDocuments() {
        Locator submitBtn = page.locator("button:has-text('SUBMIT')");
        submitBtn.scrollIntoViewIfNeeded();
        submitBtn.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        submitBtn.click();
        log.info("Clicked Submit on KYC Documents page.");
    }
}
