package ui.stepDefinitions.jarvis;

import dynamicData.DynamicDataClass;
import hooks.BaseTest;
import io.cucumber.java.en.*;
import ui.pages.jarvis.AppFormPage.ApplicationPage;
import ui.pages.jarvis.LoginPage;

import java.io.FileInputStream;
import java.util.Properties;

public class QcSteps extends BaseTest {

    private String getCredentialProperty(String key) throws Exception {
        String propertyFile = System.getProperty("user.dir") + "/src/test/resources/properties/" + environment + "Credentials.properties";
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(propertyFile)) {
            properties.load(inputStream);
        }
        String value = properties.getProperty(key);
        if (value == null) throw new RuntimeException("Property '" + key + "' not found in " + propertyFile);
        return value;
    }

    @And("User logs into Jarvis as {string} and opens the application")
    public void loginAsUserAndOpenApp(String role) throws Exception {
        String email;
        String password;

        // Read credentials from properties file
        if ("maker".equals(role)) {
            email = getCredentialProperty("makerUserEmail");
            password = getCredentialProperty("makerUserPassword");
        } else {
            email = getCredentialProperty("checkerUserEmail");
            password = getCredentialProperty("checkerUserPassword");
        }

        log.info("Opening new context and logging in as {} ({})", role, email);

        // Close old context and create a fresh browser context + page
        teardownBrowserInstance();
        startBrowserInstance();

        // Navigate to Jarvis login page
        String jarvisUrl = initializeEnvironment("jarvisUrl");
        getPage().navigate(jarvisUrl);
        getPage().waitForTimeout(2000);

        // Login with the role-specific credentials
        setUserEmail(email);
        setUserPassWord(password);
        LoginPage loginPage = new LoginPage(getPage());
        loginPage.login();

        // Navigate directly to the appForm URL
        String appFormId = (String) DynamicDataClass.getValue("appFormId");
        log.info("Opening appForm: {}", appFormId);
        String appUrl = jarvisUrl.replace("/dashboard", "") + "/application/" + appFormId + "/appForm";
        getPage().navigate(appUrl);
        getPage().waitForTimeout(5000);
        log.info("Opened appForm as {} user", role);
    }

    @And("User reassigns appForm to {string} user")
    public void reassignAppFormToUser(String role) throws Exception {
        String email;
        if ("maker".equals(role)) {
            email = getCredentialProperty("makerUserEmail");
        } else {
            email = getCredentialProperty("checkerUserEmail");
        }

        log.info("Reassigning appForm to {} ({})", role, email);
        ApplicationPage appPage = new ApplicationPage(getPage());
        appPage.reassignToUser(email);
    }

    @And("User selects {string} from Application Actions")
    public void selectApplicationAction(String actionName) {
        log.info("Selecting action: {}", actionName);
        ApplicationPage appPage = new ApplicationPage(getPage());
        appPage.selectApplicationActionAndAccept(actionName, "Moving_AppFrom");
    }
}
