package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * HLR (Home Loan Retail) test runner.
 * Uses HLR-specific step definitions (dsa_secured_HLR, Jarvis_secured_HLR)
 * which are intentionally separate from the LAP flow (dsa_secured, jarvis_Secure)
 * since the two products are expected to diverge over time.
 *
 * Run with: mvn test -Denv=uat -Dcucumber.filter.tags="@HLR"
 */
@CucumberOptions(features = "features/HLR.feature", glue = {
        "hooks",
        "data",
        "ui.stepDefinitions.dsa_secured_HLR",
        "ui.stepDefinitions.jarvis",
        "ui.stepDefinitions.Jarvis_secured_HLR",
        "backend.stepDefinitions"
}, tags = "@HLR")

public class HLRRunnerTest extends AbstractTestNGCucumberTests {
}
