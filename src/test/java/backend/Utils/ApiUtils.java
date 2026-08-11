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

    /**
     * Completes the Sales Gating flow by:
     * 1. GET Shield API to fetch linkedIndividuals (applicant IDs and names)
     * 2. POST Deathlok addData API for each linkedIndividual to mark sales gating as COMPLETED
     */
    public static void completeSalesGating() {
        String appId = getValue("appFormId").toString();

        if (appId == null || appId.isEmpty()) {
            throw new AssertionError("Cannot complete sales gating: appFormId is missing in TestSessionData.");
        }

        try {
            // Step 1: GET Shield API to fetch appForm data with linkedIndividuals
            String shieldEndpoint = String.format(Constants.SHIELD_APPFORM_ENDPOINT, appId);
            log.info("Fetching appForm data from Shield for App ID: [{}]", appId);

            Response shieldResponse = ApiClientUtils.doGetWithBasicAuth(
                    Constants.SHIELD_BASE_URI, shieldEndpoint, Headers.BASIC_AUTH);

            int shieldStatus = shieldResponse.getStatusCode();
            Assert.assertEquals(shieldStatus, 200,
                    "Shield GET appForm API failed. Status: " + shieldStatus);

            JsonPath shieldJson = shieldResponse.jsonPath();

            // Step 2: Extract linkedIndividuals and call addData for each
            java.util.List<java.util.Map<String, Object>> linkedIndividuals = shieldJson.getList("linkedIndividuals");

            if (linkedIndividuals == null || linkedIndividuals.isEmpty()) {
                throw new AssertionError("No linkedIndividuals found in Shield response for appFormId: " + appId);
            }

            log.info("Found {} linkedIndividual(s) in Shield response.", linkedIndividuals.size());

            for (java.util.Map<String, Object> individual : linkedIndividuals) {
                String applicantId = String.valueOf(individual.get("id"));
                String entityName = (String) individual.get("entityName");
                String applicantType = "LinkedIndividual";

                log.info("Completing sales gating for applicant: [{}] (ID: {})", entityName, applicantId);

                String payload = ApiPayload.getSalesGatingPayload(appId, applicantId, entityName, applicantType);

                Response addDataResponse = ApiClientUtils.doPostWithBasicAuth(
                        Constants.DEATHLOK_BASE_URI, Constants.SALES_GATING_ADD_DATA_ENDPOINT,
                        payload, Headers.BASIC_AUTH);

                int addDataStatus = addDataResponse.getStatusCode();
                log.info("Sales Gating API response for [{}]: Status={}, Body={}",
                        entityName, addDataStatus, addDataResponse.getBody().asString());

                Assert.assertTrue(addDataStatus == 200 || addDataStatus == 201 || addDataStatus == 202,
                        "Sales Gating API failed for applicant " + entityName + ". Status: " + addDataStatus);
            }

            log.info("Sales Gating completed successfully for all {} linkedIndividual(s).", linkedIndividuals.size());

        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to complete Sales Gating flow", e);
            Assert.fail("Exception during Sales Gating: " + e.getMessage());
        }
    }
}
