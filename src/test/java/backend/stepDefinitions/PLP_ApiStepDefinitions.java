package backend.stepDefinitions;
import backend.Utils.ApiUtils;
import backend.Utils.DataGeneratorUtils;
import backend.constants.Constants;
import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import static dynamicData.DynamicDataClass.getValue;
import static dynamicData.DynamicDataClass.setValue;

public class PLP_ApiStepDefinitions extends BaseTest {

    @Given("User generates PLP test data and whitelists PAN in Mystique")
    public void generateDataAndWhitelistPan() {
        String profile = TestDataProvider.get("dsa.qde.pan_profile");

        setValue(Constants.MOBILE_NUMBER, DataGeneratorUtils.generateMobileNumber());
        log.info("Generated mobile: {}", getValue(Constants.MOBILE_NUMBER));

        setValue(Constants.PAN_CARD, DataGeneratorUtils.generatePanNumber());
        log.info("Generated PAN: {}", getValue(Constants.PAN_CARD));

        ApiUtils.updatePanInMystique(profile);
    }
    @And("User moves PLP to QC Approval stage")
    public void moveToQcApproval() {
        ApiUtils.moveAppFormToStage("QC_APPROVE");
    }
}

