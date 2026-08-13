package hooks;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG Retry Analyzer - automatically retries failed tests.
 * Default: 1 retry (test runs max 2 times total).
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 1;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            System.out.println("[RETRY] Retrying failed test: " + result.getName() + " | Attempt: " + (retryCount + 1));
            return true;
        }
        return false;
    }
}
