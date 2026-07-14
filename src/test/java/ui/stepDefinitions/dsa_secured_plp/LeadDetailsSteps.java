package ui.stepDefinitions.dsa_secured_plp;
import data.TestDataProvider;
import dynamicData.DynamicDataClass;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured_plp.LeadDetailsPage;

public class LeadDetailsSteps extends BaseTest {

    @And("User completes adding PLP Lead Details")
    public void completeLeadDetails() {
            LeadDetailsPage leadDetailsPage = new LeadDetailsPage(BaseTest.getPage());

            // Fill out input text fields
            leadDetailsPage.fillName(TestDataProvider.get("dsa_secured_plp.lead_details.name"));
            String phoneNumber = TestDataProvider.get("dsa_secured_plp.lead_details.phone_number");
            leadDetailsPage.fillPhoneNumber(phoneNumber);
            DynamicDataClass.setValue("mobile_number", phoneNumber);
            leadDetailsPage.fillLoanAmount(TestDataProvider.get("dsa_secured_plp.lead_details.loan_amount"));
            leadDetailsPage.fillTenure(TestDataProvider.get("dsa_secured_plp.lead_details.tenure"));
            leadDetailsPage.fillRateOfInterest(TestDataProvider.get("dsa_secured_plp.lead_details.rate_of_interest"));

            // Select values from form dropdown fields dynamically
            leadDetailsPage.selectEmploymentType(TestDataProvider.get("dsa_secured_plp.lead_details.employment_type"));
            leadDetailsPage.selectInterestPaymentPreference(TestDataProvider.get("dsa_secured_plp.lead_details.interest_payment_preference"));
            leadDetailsPage.selectLoanPurpose(TestDataProvider.get("dsa_secured_plp.lead_details.loan_purpose"));

            // Click next layout transition button
            leadDetailsPage.clickNext();
        }
    }

