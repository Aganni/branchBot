package ui.pages.jarvis_secured;

import backend.Utils.ApiClientUtils;
import backend.constants.Constants;
import backend.constants.Headers;
import backend.payload.ApiPayload;
import hooks.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * Page Object for the Sales Gating flow.
 * Handles:
 *   1. GET Shield API to fetch linkedIndividuals (applicant IDs and names)
 *   2. POST Deathlok addData API for each linkedIndividual to mark sales gating as COMPLETED
 */
public class SalesGatingPage extends BaseTest {

    private final String appFormId;

    public SalesGatingPage(String appFormId) {
        if (appFormId == null || appFormId.isEmpty()) {
            throw new IllegalArgumentException("appFormId cannot be null or empty");
        }
        this.appFormId = appFormId;
    }

    /**
     * Fetches linkedIndividuals from Shield API for the given appFormId.
     *
     * @return list of linkedIndividual maps containing id, entityName, etc.
     */
    public List<Map<String, Object>> fetchLinkedIndividuals() {
        String endpoint = String.format(Constants.SHIELD_APPFORM_ENDPOINT, appFormId);
        log.info("Fetching appForm data from Shield for App ID: [{}]", appFormId);

        try {
            Response response = ApiClientUtils.doGetWithBasicAuth(
                    Constants.SHIELD_BASE_URI, endpoint, Headers.BASIC_AUTH);

            int statusCode = response.getStatusCode();
            log.info("Shield API Response Status: {}", statusCode);

            Assert.assertEquals(statusCode, 200,
                    "Shield GET appForm API failed. Status: " + statusCode);

            JsonPath jsonPath = response.jsonPath();
            List<Map<String, Object>> linkedIndividuals = jsonPath.getList("linkedIndividuals");

            if (linkedIndividuals == null || linkedIndividuals.isEmpty()) {
                throw new AssertionError("No linkedIndividuals found in Shield response for appFormId: " + appFormId);
            }

            log.info("Found {} linkedIndividual(s) in Shield response.", linkedIndividuals.size());
            return linkedIndividuals;

        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch linkedIndividuals from Shield", e);
            Assert.fail("Exception during Shield GET: " + e.getMessage());
            return null;
        }
    }

    /**
     * Completes sales gating for a single applicant by calling Deathlok addData API.
     *
     * @param applicantId   the linkedIndividual ID from Shield
     * @param entityName    name of the applicant
     * @param applicantType e.g. "LinkedIndividual"
     */
    public void completeSalesGatingForApplicant(String applicantId, String entityName, String applicantType) {
        log.info("Completing sales gating for applicant: [{}] (ID: {})", entityName, applicantId);

        String payload = ApiPayload.getSalesGatingPayload(appFormId, applicantId, entityName, applicantType);

        try {
            Response response = ApiClientUtils.doPostWithBasicAuth(
                    Constants.DEATHLOK_BASE_URI, Constants.SALES_GATING_ADD_DATA_ENDPOINT,
                    payload, Headers.BASIC_AUTH);

            int statusCode = response.getStatusCode();
            log.info("Sales Gating API response for [{}]: Status={}, Body={}",
                    entityName, statusCode, response.getBody().asString());

            Assert.assertTrue(statusCode == 200 || statusCode == 201 || statusCode == 202,
                    "Sales Gating API failed for applicant " + entityName + ". Status: " + statusCode);

        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to complete Sales Gating for applicant: {}", entityName, e);
            Assert.fail("Exception during Sales Gating for " + entityName + ": " + e.getMessage());
        }
    }

    /**
     * Completes sales gating for all FINANCIAL linkedIndividuals in the appForm.
     * Fetches the list from Shield, filters to only financial applicants,
     * then calls addData for each one.
     */
    public void completeSalesGatingForAllApplicants() {
        log.info("Starting Sales Gating flow for appFormId: [{}]", appFormId);

        List<Map<String, Object>> linkedIndividuals = fetchLinkedIndividuals();

        int completedCount = 0;
        for (Map<String, Object> individual : linkedIndividuals) {
            // Skip non-financial applicants
            if (isNonFinancial(individual)) {
                String skipId = String.valueOf(individual.get("id"));
                log.info("Skipping non-financial applicant (ID: {})", skipId);
                continue;
            }

            String applicantId = String.valueOf(individual.get("id"));
            String entityName = getEntityName(individual, applicantId);
            String applicantType = "LinkedIndividual";

            completeSalesGatingForApplicant(applicantId, entityName, applicantType);
            completedCount++;
        }

        if (completedCount == 0) {
            log.warn("No financial applicants found. Completing for all linkedIndividuals as fallback.");
            for (Map<String, Object> individual : linkedIndividuals) {
                String applicantId = String.valueOf(individual.get("id"));
                String entityName = getEntityName(individual, applicantId);
                completeSalesGatingForApplicant(applicantId, entityName, "LinkedIndividual");
            }
        }

        log.info("Sales Gating completed for {} financial applicant(s).", completedCount);
    }

    /**
     * Checks if a linkedIndividual is non-financial.
     * Non-financial applicants are identified by coApplicantType or applicantType fields.
     */
    private boolean isNonFinancial(Map<String, Object> individual) {
        // Log all keys for debugging
        log.info("LinkedIndividual fields: {}", individual.keySet());

        // Check coApplicantType field
        String coApplicantType = getStringField(individual, "coApplicantType");
        if (coApplicantType != null && coApplicantType.toLowerCase().contains("non-financial")) {
            return true;
        }

        // Check applicantType field
        String applicantType = getStringField(individual, "applicantType");
        if (applicantType != null && applicantType.toLowerCase().contains("non-financial")) {
            return true;
        }

        // Check financialType field
        String financialType = getStringField(individual, "financialType");
        if (financialType != null && financialType.toLowerCase().contains("non")) {
            return true;
        }

        // Check isFinancial field
        Object isFinancial = individual.get("isFinancial");
        if (isFinancial != null && "false".equalsIgnoreCase(isFinancial.toString())) {
            return true;
        }

        return false;
    }

    /**
     * Extracts entity name from a linkedIndividual map, trying multiple fields.
     */
    private String getEntityName(Map<String, Object> individual, String applicantId) {
        // Try entityName first
        String name = getStringField(individual, "entityName");
        if (name != null) return name;

        // Try fullName
        name = getStringField(individual, "fullName");
        if (name != null) return name;

        // Try name
        name = getStringField(individual, "name");
        if (name != null) return name;

        // Build from firstName + lastName
        String firstName = individual.get("firstName") != null ? individual.get("firstName").toString().trim() : "";
        String lastName = individual.get("lastName") != null ? individual.get("lastName").toString().trim() : "";
        name = (firstName + " " + lastName).trim();
        if (!name.isEmpty()) return name;

        // Fallback
        return "Applicant_" + applicantId;
    }

    private String getStringField(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value != null && !value.toString().trim().isEmpty() && !"null".equals(value.toString())) {
            return value.toString().trim();
        }
        return null;
    }
}
