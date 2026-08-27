package ui.stepDefinitions.dsa_secured_HLR;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.CollateralPage;

public class CollateralSteps extends BaseTest {

    @And("HLR User Adds the property collateral details")
    public void configureCollateralDetails() {
        CollateralPage page = new CollateralPage(BaseTest.getPage());

        // 1. Click + Add Collateral
        page.clickAddCollateral();

        // 2. Select collateral owners
        page.selectOwners(
                TestDataProvider.get("dsa_secured.collateral_details.owner1"),
                TestDataProvider.get("dsa_secured.collateral_details.owner2")
        );

        // 3. Select Type (e.g., "Commercial")
        page.selectType(
                TestDataProvider.get("dsa_secured.collateral_details.type")
        );

        // 4. Select Sub Type (e.g., "Shop")
        page.selectSubType(
                TestDataProvider.get("dsa_secured.collateral_details.sub_type")
        );

        // 5. Select Status (e.g., "Rented")
        page.selectStatus(
                TestDataProvider.get("dsa_secured.collateral_details.status")
        );

        // 6. Fill property details — stage, scheme
        page.fillPropertyDetails(
                TestDataProvider.get("dsa_secured.collateral_details.construction_stage"),
                TestDataProvider.get("dsa_secured.collateral_details.scheme")
        );

        // 7. Fill dimensions and address
        page.fillPropertyDimensions(
                TestDataProvider.get("dsa_secured.collateral_details.area_built_up"),
                TestDataProvider.get("dsa_secured.collateral_details.area_carpet"),
                TestDataProvider.get("dsa_secured.collateral_details.pincode"),
                TestDataProvider.get("dsa_secured.collateral_details.street"),
                TestDataProvider.get("dsa_secured.collateral_details.landmark")
        );

        // 8. Save
        page.clickSave();
    }
}
