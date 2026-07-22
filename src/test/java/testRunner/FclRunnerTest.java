package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * FCL test runner.
 * Uses combined step definitions with YAML-driven test data.
 *
 * Run with: mvn test -Denv=uat -Dcucumber.filter.tags="@FCL"
 */
@CucumberOptions(features = "features/fcl.feature", glue = {
                "hooks",
                "data",
                "ui.stepDefinitions.dsa",
                "ui.stepDefinitions.jarvis",
                "backend.stepDefinitions"
}, tags = "@FCL")
public class FclRunnerTest extends AbstractTestNGCucumberTests {
}
