package ui.stepDefinitions.jarvis_Secure;

import com.microsoft.playwright.Page;
import data.TestDataProvider;
import hooks.BaseTest;
import ui.pages.jarvis_secured.PropertyVisitPage;

/**
 * Step definitions for Property Visit flow in Jarvis.
 * Called after PD Visit + References/Income are complete.
 *
 * Flow:
 *   1. Navigate to PD & Property Visit → Property Visit tab
 *   2. Select applicants and owner type
 *   3. Fill all Property Visit Details (grid fields)
 *   4. Select Visit Done By users
 *   5. Save
 *   6. Upload images (Photographs + Business Photographs)
 *   7. Submit
 */
public class PropertyVisitSteps extends BaseTest {
    private static final String PVT = "dsa_secured.jarvis_secured.property_visit.";
    public void completePropertyVisit(Page jarvisPage) {
        log.info("Starting Property Visit flow in Jarvis...");

        PropertyVisitPage pvPage = new PropertyVisitPage(jarvisPage);

        // Navigate to Property Visit tab
        pvPage.navigateToPropertyVisitViaUrl();

        // Enter Edit mode
        pvPage.clickEdit();

        // Select owner type and applicants
        pvPage.selectOwnerType("Existing");
        pvPage.selectApplicants(
                TestDataProvider.get(PVT + "applicant_1"),
                TestDataProvider.get(PVT + "applicant_2"));

        // Fill Property Visit Details (grid fields)
        pvPage.fillPropertyAddress(TestDataProvider.get(PVT + "property_address"));
        pvPage.selectTypeOfProperty(TestDataProvider.get(PVT + "type_of_property"));
        pvPage.selectTypesOfCollateral(TestDataProvider.get(PVT + "types_of_collateral"));
        pvPage.fillPropertyValue(TestDataProvider.get(PVT + "property_value_as_per_customer"));
        pvPage.fillPropertyArea(TestDataProvider.get(PVT + "property_area"));
        pvPage.selectWithinGeoLimits(TestDataProvider.get(PVT + "within_geo_limits"));
        pvPage.fillSpokeName(TestDataProvider.get(PVT + "spoke_name"));
        pvPage.fillContactNoPersonMet(TestDataProvider.get(PVT + "contact_no_person_met"));
        pvPage.fillAgeOfProperty(TestDataProvider.get(PVT + "age_of_property"));
        pvPage.fillTotalNoOfUnits(TestDataProvider.get(PVT + "total_no_of_units"));
        pvPage.fillVacantUnits(TestDataProvider.get(PVT + "vacant_units"));
        pvPage.selectAccessRoadToProperty(
                TestDataProvider.get(PVT + "access_road_to_property_1"),
                TestDataProvider.get(PVT + "access_road_to_property_2"));
        pvPage.selectHabitation(TestDataProvider.get(PVT + "habitation"));
        pvPage.fillHabitationValue(TestDataProvider.get(PVT + "habitation_value"));
        pvPage.fillAgencyPropertyVisitDoneBy(TestDataProvider.get(PVT + "agency_property_visit_done_by"));
        pvPage.fillRateReferenceVicinity(TestDataProvider.get(PVT + "rate_reference_vicinity"));
        pvPage.selectAgencyPropertyVisitFeedback(TestDataProvider.get(PVT + "agency_property_visit_feedback"));
        pvPage.fillDeviationsRemarks(TestDataProvider.get(PVT + "deviations_remarks"));
        pvPage.fillTechnicalVendorRemarks(TestDataProvider.get(PVT + "technical_vendor_remarks"));

        // Select Visit Done By users
        pvPage.selectVisitDoneByUsers(
                TestDataProvider.get(PVT + "visit_done_by_search"),
                TestDataProvider.get(PVT + "visit_done_by_user_1"),
                TestDataProvider.get(PVT + "visit_done_by_user_2"),
                TestDataProvider.get(PVT + "visit_done_by_user_3"),
                TestDataProvider.get(PVT + "visit_done_by_user_4"));

        // Save the form details
        pvPage.clickSave();
        log.info("Property Visit details saved.");

        // Upload images (Photographs + Business Photographs)
        String[] imagePaths = {
                TestDataProvider.get(PVT + "image_path_1"),
                TestDataProvider.get(PVT + "image_path_2"),
                TestDataProvider.get(PVT + "image_path_3"),
                TestDataProvider.get(PVT + "image_path_4")
        };
        pvPage.uploadPropertyVisitImages(imagePaths);

        // Submit the form
        pvPage.clickSubmit();
        log.info("Property Visit flow completed successfully.");

        // Post-submit: Navigate to Appform and attempt Move to Credit Approval
        // to verify validation message (triggers Physical PD completion requirement)
        pvPage.navigateToAppformAndAttemptStageMove();
    }
}
