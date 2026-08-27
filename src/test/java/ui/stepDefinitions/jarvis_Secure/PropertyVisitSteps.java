package ui.stepDefinitions.jarvis_Secure;
import com.microsoft.playwright.Page;
import data.TestDataProvider;
import hooks.BaseTest;
import ui.pages.jarvis_secured.PropertyVisitPage;

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

        // Select Visit Done By users.
        // The number of users configured can vary by product/test data (e.g. LAP defines
        // 3, HLR defines 4), so collect only the ones actually present instead of assuming
        // a fixed count of 4.
        java.util.List<String> visitDoneByUsers = new java.util.ArrayList<>();
        for (int i = 1; ; i++) {
            String user = TestDataProvider.getOrDefault(PVT + "visit_done_by_user_" + i, null);
            if (user == null) {
                break;
            }
            visitDoneByUsers.add(user);
        }
        pvPage.selectVisitDoneByUsers(
                TestDataProvider.get(PVT + "visit_done_by_search"),
                visitDoneByUsers.toArray(new String[0]));

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

        // Post-submit: Navigate to Appform and attempt Move to Credit Approval to verify validation message (triggers Physical PD completion requirement)
        pvPage.navigateToAppformAndAttemptStageMove();
    }
}
