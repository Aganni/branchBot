package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.DocumentsPage;

public class DocumentsSteps extends BaseTest {

    @And("User uploads all required documents and proceeds")
    public void userUploadsAllRequiredDocumentsAndProceeds() {
        DocumentsPage documentsPage = new DocumentsPage(BaseTest.getPage());
        documentsPage.clickDocumentsButton();
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.document_attachments.Bank Statement"), "Bank Statement", 0
        );
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.document_attachments.aadhaar_file_1"), "Aadhaar", 1
        );
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.document_attachments.pan_file_1"), "Pan", 2
        );
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.document_attachments.itr_file"), "Itr", 3
        );
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.document_attachments.photo_file_1"), "Photo", 4
        );
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.document_attachments.aadhaar_file_2"), "Aadhaar", 5
        );
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.document_attachments.bank_statement_file_1"), "Bank Statement", 6
        );
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.document_attachments.pan_file_2"), "Pan", 7
        );
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.document_attachments.form_file"), "Form 16", 8
        );
        documentsPage.uploadDocument(
                TestDataProvider.get("dsa_secured.document_attachments.photo_file_2"), "Photo", 9
        );
//        documentsPage.uploadDocument(
//                TestDataProvider.get("dsa_secured.document_attachments.driving_license_file"), "Driving License", 10
//        );
//        documentsPage.uploadDocument(
//                TestDataProvider.get("dsa_secured.document_attachments.bank_statement_file_2"), "Bank Statement", 11
//        );
//        documentsPage.uploadDocument(
//                TestDataProvider.get("dsa_secured.document_attachments.aadhaar_file_3"), "Aadhaar", 12
//        );
//        documentsPage.uploadDocument(
//                TestDataProvider.get("dsa_secured.document_attachments.gst_return_file"), "Gst Return", 13
//        );
//        documentsPage.uploadDocument(
//                TestDataProvider.get("dsa_secured.document_attachments.photo_file_3"), "Photo", 14
//        );
        documentsPage.uploadApplicationForm(
                TestDataProvider.get("dsa_secured.document_attachments.application_form_file")
        );
        log.info("All 15 documents uploaded successfully.");
        documentsPage.clickNext();
    }
}
