package ui.stepDefinitions.dsa_secured_HLR;

import hooks.BaseTest;
import io.cucumber.java.en.When;
import ui.pages.dsa_secured.DashboardPage;
import ui.pages.dsa_secured.LoginPage;

public class LoginSteps extends BaseTest {
    @When("User logs into DSA Portal for Secured Loan and initiates a HLR Loan application")
    public void loginToDsaPortalForSecured() throws Exception {
        String url = BaseTest.initializeEnvironment("dsaPortalUrl");
        LoginPage loginPage = new LoginPage(BaseTest.getPage());
        loginPage.navigateToPortal(url);

        BaseTest.getCredentials("dsa");
        loginPage.clickExternalLogin();
        loginPage.loginWithEmailAndOtp(BaseTest.getUserEmail(), BaseTest.getOtp());
        loginPage.verifyRedirectedToDsaDashboard();

        String loanType = "Home Loan Retail";
        DashboardPage dashboardPage = new DashboardPage(BaseTest.getPage());
        dashboardPage.initiateApplication(loanType);
    }
}
