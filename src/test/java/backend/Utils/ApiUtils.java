package backend.Utils;

import backend.constants.Constants;
import backend.constants.Headers;
import backend.payload.ApiPayload;
import hooks.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import static dynamicData.DynamicDataClass.get;
import static dynamicData.DynamicDataClass.getValue;

public class ApiUtils extends BaseTest {

    /**
     * Whitelists a PAN in Mystique with the given profile.
     * Single method for both primary and co-applicant PAN whitelist.
     *
     * @param panValue   the PAN number to whitelist
     * @param panProfile the profile name to associate (e.g. "Shea Test", "Alex Doctor")
     */
    public static void whitelistPanInMystique(String panValue, String panProfile) {
        String payload = ApiPayload.whitelistPanCardInMystique(panValue, panProfile);
        get().setApiPayload(payload);

        try {
            Response response = ApiClientUtils.doPostKyc(Constants.MYSTIQUE_BASE_URI, Constants.WHITELIST_PANCARD_IN_MYSTIQUE, payload);

            Assert.assertEquals(response.getStatusCode(), 200, "Mystique PAN Whitelist API failed for PAN: " + panValue);

            JsonPath jsonPath = response.jsonPath();
            String expectedMessage = "Added a new kyc of type pancard with id- " + panValue;
            Assert.assertEquals(jsonPath.getString("message"), expectedMessage, "API Response message mismatch for PAN: " + panValue);

        } catch (Exception e) {
            log.error("Failed to whitelist PAN [{}] in Mystique", panValue, e);
            Assert.fail("Exception during Mystique PAN Whitelist for " + panValue + ": " + e.getMessage());
        }
    }

    public static void moveAppFormToStage(String stage) {
        String appId = getValue("appFormId").toString();

        if (appId == null || appId.isEmpty()) {
            throw new AssertionError("Cannot move to stage: appFormId is missing in TestSessionData.");
        }

        String endpoint = String.format(Constants.START_PROCESS_ENDPOINT, stage, appId);

        try {
            log.info("Triggering Nebula API to move App ID: [{}] to Stage: [{}]", appId, stage);

            Response response = ApiClientUtils.doPostEmptyBodyWithBasicAuth(
                    Constants.NEBULA_BASE_URI, endpoint, Headers.BASIC_AUTH
            );

            int statusCode = response.getStatusCode();
            log.info("API Response Status Code: {}", statusCode);
            log.info("API Response Body: {}", response.getBody().asString());

            Assert.assertTrue(statusCode == 200 || statusCode == 204,
                    "Failed to move app to stage " + stage + ". API returned status: " + statusCode);

        } catch (Exception e) {
            log.error("Failed to execute Nebula start-process API", e);
            Assert.fail("Exception during moving app to stage " + stage + ": " + e.getMessage());
        }
    }

    public static void updateRepaymentDetails() {
        String appId = getValue("appFormId").toString();

        if (appId == null || appId.isEmpty()) {
            throw new AssertionError("Cannot update repayment details: appFormId is missing in TestSessionData.");
        }

        String endpoint = String.format(Constants.REPAYMENT_DETAILS_ENDPOINT, appId);
        String payload = ApiPayload.getRepaymentDetailsPayload(appId);

        try {
            log.info("Triggering Lannister API to update Repayment Details for App ID: [{}]", appId);

            Response response = ApiClientUtils.doPostWithBasicAuth(Constants.LANNISTER_BASE_URI, endpoint, payload, Headers.BASIC_AUTH);

            int statusCode = response.getStatusCode();
            log.info("API Response Status Code: {}", statusCode);
            log.info("API Response Body: {}", response.getBody().asString());

            Assert.assertTrue(statusCode == 200 || statusCode == 201,
                    "Failed to update repayment details. API returned status: " + statusCode);

        } catch (Exception e) {
            log.error("Failed to execute Lannister repayment details API", e);
            Assert.fail("Exception during repayment details API: " + e.getMessage());
        }
    }
}
