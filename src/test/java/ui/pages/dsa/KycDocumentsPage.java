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
        if (page == null)
            throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void uploadMandatoryKycDocument() {
        log.info("On KYC Documents page - uploading mandatory document");

        // Click to expand the KYC Documents section
        page.getByText("KYC Documents*").click();
        page.waitForTimeout(1000);
        log.info("Expanded KYC Documents section");

        // Directly set file on the hidden input (Playwright allows this without
        // visibility)
        java.nio.file.Path absolutePath = Paths.get(SAMPLE_DOC_PATH).toAbsolutePath();
        log.info("Uploading document from: {}", absolutePath);
        page.locator(UPLOAD_INPUT).setInputFiles(absolutePath);
        log.info("Document uploaded via file input");

        // Wait for upload modal to appear
        page.waitForTimeout(2000);

        // Select document type as "Pan" in the modal (MUI Select component)
        page.locator("#docType").click();
        page.waitForTimeout(500);
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Pan"))
                .or(page.locator("li:has-text('Pan')"))
                .or(page.getByText("Pan", new Page.GetByTextOptions().setExact(true)))
                .first().click();
        log.info("Selected document type: Pan");

        // Save the document
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SAVE")).click();
        log.info("Clicked Save button");

        // Wait for the upload to complete before submitting
        page.waitForTimeout(25000);
    }

    public void submitDocuments() {
        Locator submitBtn = page.locator("button:has-text('SUBMIT')");
        submitBtn.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        submitBtn.click();
        log.info("Clicked Submit on KYC Documents page.");
    }
}
