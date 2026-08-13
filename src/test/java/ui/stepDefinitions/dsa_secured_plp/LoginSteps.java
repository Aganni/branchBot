package ui.stepDefinitions.dsa_secured_plp;

import hooks.BaseTest;
import io.cucumber.java.en.When;
import ui.pages.dsa_secured_plp.DashboardPage;
import ui.pages.dsa_secured_plp.LoginPage;

public class LoginSteps extends BaseTest {
    @When("User logs into DSA Portal for Secured Loan and initiates a PLP Loan application")
    public void loginToDsaPortalForSecured() throws Exception {
        String url = BaseTest.initializeEnvironment("dsaPortalUrl");
        LoginPage loginPage = new LoginPage(BaseTest.getPage());
        loginPage.navigateToPortal(url);

        BaseTest.getCredentials("dsa");
        loginPage.clickExternalLogin();
        loginPage.loginWithEmailAndOtp(BaseTest.getUserEmail(), BaseTest.getOtp());
        loginPage.verifyRedirectedToDsaDashboard();

        //String loanType = TestDataProvider.get("dsa.business_details.loan_type");
        String loanType = "Prime LAP";
        DashboardPage dashboardPage = new DashboardPage(BaseTest.getPage());
        dashboardPage.initiateApplication(loanType);
    }
}
