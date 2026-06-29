package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.CollateralPage;

public class CollateralSteps {
    @And("User Adds the property collateral details")

    public void configureCollateralDetails() throws Exception {
        CollateralPage flowPage = new CollateralPage(BaseTest.getPage());
        flowPage.clickAddCollateral();
        flowPage.selectOwners();

        flowPage.PropertyDemographics(
                TestDataProvider.get("dsa_secured.collateral_details.property_type"),
                TestDataProvider.get("dsa_secured.collateral_details.status"),
                TestDataProvider.get("dsa_secured.collateral_details.construction_stage"),
                TestDataProvider.get("dsa_secured.collateral_details.scheme")
        );

        flowPage.PropertyDimensions(
                TestDataProvider.get("dsa_secured.collateral_details.area_built_up"),
                TestDataProvider.get("dsa_secured.collateral_details.area_carpet"),
                TestDataProvider.get("dsa_secured.collateral_details.pincode"),
                TestDataProvider.get("dsa_secured.collateral_details.street"),
                TestDataProvider.get("dsa_secured.collateral_details.landmark")
        );
    }

    @And("User uploads required verification documents and generates link")
    public void uploadDocumentsAndGenerateLink() throws Exception {
        CollateralPage flowPage = new CollateralPage(BaseTest.getPage());
        flowPage.navigateToDocumentsTab();

        flowPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured.document_attachments.driving_license_file"), "Driving License", 0);
        flowPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured.document_attachments.bank_statement_file_1"), "Bank Statement", 1);
        flowPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured.document_attachments.bank_statement_file_2"), "Bank Statement", 2);
        flowPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured.document_attachments.aadhaar_file"), "Aadhaar", 3);

        flowPage.clickNext();
        flowPage.finalizeFeeCalculationAndLinkGeneration();
    }
}
