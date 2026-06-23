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

public class LAP_ApiStepDefinitions extends BaseTest {


    @Given("User generates test data and whitelists PAN in Mystique")
    public void generateDataAndWhitelistPan() {
        String profile = TestDataProvider.get("dsa.qde.pan_profile");

        setValue(Constants.MOBILE_NUMBER, DataGeneratorUtils.generateMobileNumber());
        log.info("Generated mobile: {}", getValue(Constants.MOBILE_NUMBER));

        setValue(Constants.PAN_CARD, DataGeneratorUtils.generatePanNumber());
        log.info("Generated PAN: {}", getValue(Constants.PAN_CARD));

        ApiUtils.updatePanInMystique(profile);
    }
    @And("User moves LAP to QC Approval stage") // Changed text here
    public void moveToQcApproval() {
        ApiUtils.moveAppFormToStage("QC_APPROVE");
    }
}
