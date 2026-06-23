package ui.stepDefinitions.dsa_secured;
import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.LeadDetailsPage;

public class LeadDetailsSteps extends BaseTest {

    @And("User completes adding LAP Lead Details")
    public void completeLeadDetails() {
            LeadDetailsPage leadDetailsPage = new LeadDetailsPage(BaseTest.getPage());

            // Fill out input text fields
            leadDetailsPage.fillName(TestDataProvider.get("dsa_secured.lead_details.name"));
            leadDetailsPage.fillPhoneNumber(TestDataProvider.get("dsa_secured.lead_details.phone_number"));
            leadDetailsPage.fillLoanAmount(TestDataProvider.get("dsa_secured.lead_details.loan_amount"));
            leadDetailsPage.fillTenure(TestDataProvider.get("dsa_secured.lead_details.tenure"));
            leadDetailsPage.fillRateOfInterest(TestDataProvider.get("dsa_secured.lead_details.rate_of_interest"));

            // Select values from form dropdown fields dynamically
            leadDetailsPage.selectEmploymentType(TestDataProvider.get("dsa_secured.lead_details.employment_type"));
            leadDetailsPage.selectInterestPaymentPreference(TestDataProvider.get("dsa_secured.lead_details.interest_payment_preference"));
            leadDetailsPage.selectLoanPurpose(TestDataProvider.get("dsa_secured.lead_details.loan_purpose"));

            // Click next layout transition button
            leadDetailsPage.clickNext();
        }
    }

