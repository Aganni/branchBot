package ui.stepDefinitions.dsa_secured_plp;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured_plp.CollateralPage;

public class CollateralSteps extends BaseTest {

    @And("User Adds the PLP property collateral details")
    public void configureCollateralDetails() {
        CollateralPage page = new CollateralPage(BaseTest.getPage());

        // 1. Click + Add Collateral
        page.clickAddCollateral();

        // 2. Select collateral owners
        page.selectOwners(
                TestDataProvider.get("dsa_secured_plp.collateral_details.owner1"),
                TestDataProvider.get("dsa_secured_plp.collateral_details.owner2")
        );

        // 3. Select Type (e.g., "Commercial")
        page.selectType(
                TestDataProvider.get("dsa_secured_plp.collateral_details.type")
        );

        // 4. Select Sub Type (e.g., "Shop")
        page.selectSubType(
                TestDataProvider.get("dsa_secured_plp.collateral_details.sub_type")
        );

        // 5. Select Status (e.g., "Rented")
        page.selectStatus(
                TestDataProvider.get("dsa_secured_plp.collateral_details.status")
        );

        // 6. Fill property details — stage, scheme
        page.fillPropertyDetails(
                TestDataProvider.get("dsa_secured_plp.collateral_details.construction_stage"),
                TestDataProvider.get("dsa_secured_plp.collateral_details.scheme")
        );

        // 7. Fill dimensions and address
        page.fillPropertyDimensions(
                TestDataProvider.get("dsa_secured_plp.collateral_details.area_built_up"),
                TestDataProvider.get("dsa_secured_plp.collateral_details.area_carpet"),
                TestDataProvider.get("dsa_secured_plp.collateral_details.pincode"),
                TestDataProvider.get("dsa_secured_plp.collateral_details.street"),
                TestDataProvider.get("dsa_secured_plp.collateral_details.landmark")
        );

        // 8. Save
        page.clickSave();
    }

//    @And("User uploads required verification documents and generates link")
//    public void uploadDocumentsAndGenerateLink() {
//        CollateralPage collateralPage = new CollateralPage(BaseTest.getPage());
//        collateralPage.navigateToDocumentsTab();
//
//        collateralPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured_plp.document_attachments.driving_license_file"), "Driving License", 0);
//        collateralPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured_plp.document_attachments.bank_statement_file_1"), "Bank Statement", 1);
//        collateralPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured_plp.document_attachments.bank_statement_file_2"), "Bank Statement", 2);
//        collateralPage.uploadDocumentRecord(TestDataProvider.get("dsa_secured_plp.document_attachments.aadhaar_file"), "Aadhaar", 3);
//
//        collateralPage.clickNext();
//        collateralPage.finalizeFeeCalculationAndLinkGeneration();
//    }
}
