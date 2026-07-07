package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.DocumentsPage;

public class DocumentsSteps extends BaseTest {

    @And("User clicks on Documents button")
    public void clickDocumentsButton() {
        DocumentsPage documentsPage = new DocumentsPage(BaseTest.getPage());
        documentsPage.clickDocumentsButton();
    }
    public void userUploadsRequiredDocuments() {
        DocumentsPage documentsPage = new DocumentsPage(BaseTest.getPage());

        // Upload Driving License
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.documents.driving_license_file"),
                "Driving License",
                0
        );

        // Upload Bank Statement
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.documents.bank_statement_file"),
                "Bank Statement",
                1
        );

        // Upload Pan
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.documents.pan_file"),
                "Pan",
                2
        );

        log.info("All documents uploaded successfully.");
    }
}
