package ui.stepDefinitions.dsa_secured_Entity;

import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.When;
import ui.pages.dsa_secured.DashboardPage;
import ui.pages.dsa_secured.LoginPage;
import static dynamicData.DynamicDataClass.get;

public class LoginSteps extends BaseTest {
    @When("User logs into DSA Portal for Secured Loan and initiates a LAP Entity Loan application")
    public void loginToDsaPortalForSecuredEntity() throws Exception {
        String url = BaseTest.initializeEnvironment("dsaPortalUrl");
        LoginPage loginPage = new LoginPage(BaseTest.getPage());
        loginPage.navigateToPortal(url);

        BaseTest.getCredentials("dsa");
        loginPage.clickExternalLogin();
        loginPage.loginWithEmailAndOtp(BaseTest.getUserEmail(), BaseTest.getOtp());
        loginPage.verifyRedirectedToDsaDashboard();

        String loanType = "Loan Against Property";
        DashboardPage dashboardPage = new DashboardPage(BaseTest.getPage());
        dashboardPage.initiateApplication(loanType);
    }
}
