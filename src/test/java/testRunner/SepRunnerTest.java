package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * SEP test runner.
 * Uses combined step definitions with YAML-driven test data.
 *
 * Run with: mvn test -Denv=uat -Dcucumber.filter.tags="@SEP"
 */
@CucumberOptions(features = "features/sep.feature", glue = {
                "hooks",
                "data",
                "ui.stepDefinitions.dsa",
                "ui.stepDefinitions.jarvis",
                "backend.stepDefinitions"
}, tags = "@SEP")
public class SepRunnerTest extends AbstractTestNGCucumberTests {
}
