package ui.stepDefinitions.dsa_secured;

import hooks.BaseTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import ui.pages.dsa.DsaLapConsentPage;

public class DsaLapConsentSteps extends BaseTest {

    private DsaLapConsentPage consentPage;

    @Given("User is on the main loan application page")
    public void userIsOnMainLoanApplicationPage() {
        consentPage = new DsaLapConsentPage(BaseTest.getPage());
    }

    @When("User opens Google Groups in a new tab to log in with email {string} and password {string}")
    public void userOpensGoogleGroupsInANewTabAndLogsIn(String email, String password) {
        consentPage.loginAndNavigateToGroups(email, password);
    }

    @And("User locates the latest confirmation email to process verification consent")
    public void userLocatesLatestConfirmationEmailAndConsents() {
        consentPage.openLatestEmailAndConsent();
    }

    @Then("User returns to the front-facing main application dashboard")
    public void userReturnsToTheFrontFacingMainApplicationDashboard() {
        consentPage.returnToMainWorkspace();
    }
}
