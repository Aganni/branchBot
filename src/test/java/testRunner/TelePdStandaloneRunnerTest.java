package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "features/telepd_standalone.feature", glue = {
        "hooks",
        "data",
        "ui.stepDefinitions.jarvis_Secure"
}, tags = "@TelePD-standalone")

public class TelePdStandaloneRunnerTest extends AbstractTestNGCucumberTests {
}
