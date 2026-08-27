package ui.stepDefinitions.Jarvis_secured_HLR;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured_HLR.CollateralDetailsPage;
public class CollateralDetailsSteps extends BaseTest {
    @And("HLR User fills collateral details in Credit Review")
    public void fillCollateralDetails() throws Exception {
        CollateralDetailsPage collateralPage = new CollateralDetailsPage(BaseTest.getPage());
        // Fill Collateral Details (CERSAI)
        collateralPage.fillCollateralDetails();
        log.info("Collateral details filled successfully.");
    }
}
