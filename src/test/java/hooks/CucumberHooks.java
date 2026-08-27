package hooks;

import data.TestDataProvider;
import dynamicData.DynamicDataClass;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.Utils.ScreenshotUtil;

public class CucumberHooks {
    private static final Logger log = LogManager.getLogger(CucumberHooks.class);
    private static final java.util.concurrent.atomic.AtomicInteger videoCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  STARTING SCENARIO: {}", scenario.getName());
        log.info("═══════════════════════════════════════════════════════════════");
        BaseTest.startBrowserInstance();
    }

    @After
    public void afterScenario(Scenario scenario) {
        // Take screenshot on failure
        if (scenario.isFailed()) {
            try {
                ScreenshotUtil.saveScreenshot(BaseTest.getPage(),
                        "FAILED_" + scenario.getName().replaceAll("[^a-zA-Z0-9]", "_"),
                        "failures");
            } catch (Exception e) {
                log.warn("Could not capture failure screenshot: {}", e.getMessage());
            }
        }

        // Rename video to scenario name for easy identification.
        // Playwright only finalizes/flushes the .webm file to disk when the
        // BrowserContext closes, not when the Page closes. Closing only the page (as
        // before) meant the rename often raced against an unfinished file. Closing the
        // context here is safe even though teardownBrowserInstance() closes it again
        // later, since BrowserContext.close() is a no-op on an already-closed context.
        try {
            var page = BaseTest.getPage();
            if (page != null && page.video() != null) {
                var videoPath = page.video().path();
                if (videoPath != null) {
                    String safeName = scenario.getName().replaceAll("[^a-zA-Z0-9_\\-]", "_");
                    int videoNumber = videoCounter.incrementAndGet();
                    var renamedPath = videoPath.getParent().resolve(videoNumber + "_" + safeName + ".webm");

                    // Close the page's context (not just the page) so the video is
                    // guaranteed to be fully written before we try to move it.
                    var context = BaseTest.getContext();
                    try { page.close(); } catch (Exception ignore) { /* already closed */ }
                    try { context.close(); } catch (Exception ignore) { /* already closed */ }

                    java.nio.file.Files.move(videoPath, renamedPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    log.info("Video saved as: {}", renamedPath);
                }
            }
        } catch (Exception e) {
            log.warn("Could not rename video: {}", e.getMessage());
        }

        // Print execution summary
        printTestSummary(scenario);

        // Cleanup
        BaseTest.teardownBrowserInstance();
        BaseTest.cleanupStorageStates();
        TestDataProvider.cleanup();
    }

    private void printTestSummary(Scenario scenario) {
        String partnerLoanId = safeGet(() -> DynamicDataClass.get().getPartnerLoanId());
        String appFormId = safeGet(() -> String.valueOf(DynamicDataClass.getValue("appFormId")));
        String panCard = safeGet(() -> String.valueOf(DynamicDataClass.getValue("pan_card")));
        String mobileNumber = safeGet(() -> String.valueOf(DynamicDataClass.getValue("mobile_number")));

        log.info("");
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  TEST EXECUTION SUMMARY");
        log.info("  ─────────────────────────────────────────────────────────────");
        log.info("  Scenario        : {}", scenario.getName());
        log.info("  Status          : {}", scenario.getStatus());
        log.info("  ─────────────────────────────────────────────────────────────");
        log.info("  Partner Loan ID : {}", partnerLoanId);
        log.info("  AppForm ID      : {}", appFormId);
        log.info("  PAN Card        : {}", panCard);
        log.info("  Mobile Number   : {}", mobileNumber);
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("");
    }

    private String safeGet(java.util.function.Supplier<String> supplier) {
        try {
            String value = supplier.get();
            return (value == null || "null".equals(value)) ? "N/A" : value;
        } catch (Exception e) {
            return "N/A";
        }
    }
}
