package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "features/lap.feature", glue = {
        "hooks",
        "data",
        "ui.stepDefinitions.dsa_secured",
        "ui.stepDefinitions.jarvis",
        "backend.stepDefinitions"
}, tags = "@LAP")

public class LAPV2RunnerTest extends AbstractTestNGCucumberTests {
}

