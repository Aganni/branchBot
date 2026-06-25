package ui.pages.dsa;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

import java.nio.file.Paths;

public class KycDocumentsPage extends BaseTest {

    private final Page page;

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final String UPLOAD_INPUT = "#upload-button-bus2";
    private static final String SAMPLE_DOC_PATH = "src/test/resources/testdata/bank_statement.pdf";

    public KycDocumentsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void uploadMandatoryKycDocument() {
        log.info("Locating 'KYC Documents' section on the page");

        // Find the KYC Documents section and upload the file
        Locator section = page.getByText("KYC Documents");
        section.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        log.info("Found KYC Documents section");

        // Upload the PDF using absolute path
        java.nio.file.Path absolutePath = Paths.get(SAMPLE_DOC_PATH).toAbsolutePath();
        log.info("Uploading document from: {}", absolutePath);
        page.locator(UPLOAD_INPUT).setInputFiles(absolutePath);
        log.info("Document uploaded successfully");

        // Select document type as "Pan"
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Type")).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Pan")).click();
        log.info("Selected document type: Pan");

        // Save the document
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        log.info("Clicked Save button");
    }

    public void submitDocuments() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        log.info("Clicked Submit on KYC Documents page.");
    }
}