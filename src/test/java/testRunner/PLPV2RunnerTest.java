package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "features/plp.feature", glue = {
        "hooks",
        "data",
        "ui.stepDefinitions.dsa_secured",
        "ui.stepDefinitions.jarvis",
        "backend.stepDefinitions"
}, tags = "@PLP")

public class PLPV2RunnerTest extends AbstractTestNGCucumberTests {
}
