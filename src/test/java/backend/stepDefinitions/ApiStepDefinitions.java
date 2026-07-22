package backend.stepDefinitions;

import backend.Utils.ApiUtils;
import backend.Utils.DataGeneratorUtils;
import backend.constants.Constants;
import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.*;

import static dynamicData.DynamicDataClass.*;

public class ApiStepDefinitions extends BaseTest {

    @Given("User generates test data and whitelists PAN in Mystique")
    public void generateDataAndWhitelistPan() {
        String profile = TestDataProvider.get("dsa.qde.pan_profile");

        setValue(Constants.MOBILE_NUMBER, DataGeneratorUtils.generateMobileNumber());
        log.info("Generated mobile: {}", getValue(Constants.MOBILE_NUMBER));

        setValue(Constants.PAN_CARD, DataGeneratorUtils.generatePanNumber());
        log.info("Generated PAN: {}", getValue(Constants.PAN_CARD));

        ApiUtils.whitelistPanInMystique((String) getValue(Constants.PAN_CARD), profile);
    }

    @And("User generates a random business name")
    public void generateBusinessName() {
        setValue(Constants.BUSINESS_NAME, DataGeneratorUtils.generateBusinessName());
        log.info("Generated Business Name: {}", getValue(Constants.BUSINESS_NAME));
    }

    @And("User generates co-applicant PAN and whitelists in Mystique")
    public void generateCoApplicantPan() {
        String coApplicantProfile = TestDataProvider.get("dsa.co_applicant.pan_profile");

        setValue(Constants.CO_APPLICANT_PAN, DataGeneratorUtils.generatePanNumber());
        log.info("Generated Co-Applicant PAN: {}", getValue(Constants.CO_APPLICANT_PAN));

        ApiUtils.whitelistPanInMystique((String) getValue(Constants.CO_APPLICANT_PAN), coApplicantProfile);
    }

    @And("User moves to QC Approval stage")
    public void moveToQcApproval() {
        ApiUtils.moveAppFormToStage("QC_APPROVE");
    }
}
