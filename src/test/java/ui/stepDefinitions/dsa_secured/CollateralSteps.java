package ui.stepDefinitions.dsa_secured;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.CollateralPage;

public class CollateralSteps extends BaseTest {

    @And("User Adds the property collateral details")
    public void configureCollateralDetails() {
        CollateralPage page = new CollateralPage(BaseTest.getPage());

        // 1. Click + Add Collateral
        page.clickAddCollateral();

        // 2. Select collateral owners (multi-select with checkboxes)
        page.selectOwners(
                TestDataProvider.get("dsa_secured.collateral_details.owner1"),
                TestDataProvider.get("dsa_secured.collateral_details.owner2")
        );

        // 3. Select property type (e.g., "Flat")
        page.selectPropertyType(
                TestDataProvider.get("dsa_secured.collateral_details.type")
        );

        // 4. Fill property details — status, stage, scheme
        page.fillPropertyDetails(
                TestDataProvider.get("dsa_secured.collateral_details.status"),
                TestDataProvider.get("dsa_secured.collateral_details.construction_stage"),
                TestDataProvider.get("dsa_secured.collateral_details.scheme")
        );

        // 5. Fill dimensions and address
        page.fillPropertyDimensions(
                TestDataProvider.get("dsa_secured.collateral_details.area_built_up"),
                TestDataProvider.get("dsa_secured.collateral_details.area_carpet"),
                TestDataProvider.get("dsa_secured.collateral_details.pincode"),
                TestDataProvider.get("dsa_secured.collateral_details.street"),
                TestDataProvider.get("dsa_secured.collateral_details.landmark")
        );

        // 6. Save
        page.clickSave();
    }

    @And("User uploads required verification documents and generates link")
    public void uploadDocumentsAndGenerateLink() {
        CollateralPage collateralPage = new CollateralPage(BaseTest.getPage());
        collateralPage.navigateToDocumentsTab();

        collateralPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured.document_attachments.driving_license_file"), "Driving License", 0);
        collateralPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured.document_attachments.bank_statement_file_1"), "Bank Statement", 1);
        collateralPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured.document_attachments.bank_statement_file_2"), "Bank Statement", 2);
        collateralPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured.document_attachments.aadhaar_file"), "Aadhaar", 3);

        collateralPage.clickNext();
        collateralPage.finalizeFeeCalculationAndLinkGeneration();
    }
}
