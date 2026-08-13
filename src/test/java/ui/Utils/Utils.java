package ui.Utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import dynamicData.DynamicDataClass;
import hooks.BaseTest;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static dynamicData.DynamicDataClass.get;

public class Utils extends BaseTest {

    public static void extractAndStorePartnerLoanId() {
        BaseTest.log.info("Waiting for URL to update with partnerLoanId...");

        BaseTest.getPage().waitForURL(Pattern.compile(".*partnerLoanId=.*"));

        String currentUrl = BaseTest.getPage().url();
        BaseTest.log.info("Current browser URL fetched by Playwright: {}", currentUrl);

        // Extract Partner Loan ID
        String partnerLoanId = null;
        Pattern pattern = Pattern.compile("partnerLoanId=([^&]+)");
        Matcher matcher = pattern.matcher(currentUrl);

        if (matcher.find()) {
            partnerLoanId = matcher.group(1);
            BaseTest.log.info("Successfully extracted partnerLoanId: {}", partnerLoanId);
        } else {
            throw new AssertionError("Failed to find 'partnerLoanId' in the URL: " + currentUrl);
        }

        get().setPartnerLoanId(partnerLoanId);
        DynamicDataClass.setValue("partnerLoanId", partnerLoanId);

        // Extract AppForm ID if present in URL
        Pattern appFormPattern = Pattern.compile("appFormId=([^&]+)");
        Matcher appFormMatcher = appFormPattern.matcher(currentUrl);
        if (appFormMatcher.find()) {
            String appFormId = appFormMatcher.group(1);
            get().setAppFormId(appFormId);
            DynamicDataClass.setValue("appFormId", appFormId);
            BaseTest.log.info("Successfully extracted appFormId: {}", appFormId);
        } else {
            // Try extracting from URL path (e.g., /application/{appFormId})
            Pattern pathPattern = Pattern.compile("/application/([a-f0-9\\-]+)");
            Matcher pathMatcher = pathPattern.matcher(currentUrl);
            if (pathMatcher.find()) {
                String appFormId = pathMatcher.group(1);
                get().setAppFormId(appFormId);
                DynamicDataClass.setValue("appFormId", appFormId);
                BaseTest.log.info("Successfully extracted appFormId from path: {}", appFormId);
            } else {
                BaseTest.log.warn("Could not find appFormId in URL: {}", currentUrl);
            }
        }
    }

    /**
     * Navigates an open Element UI DatePicker to select the exact target date.
     * @param targetDate The date string in "YYYY-MM-DD" format.
     */
    public static void selectDateFromElementUICalendar(String targetDate) {
        com.microsoft.playwright.Page page = BaseTest.getPage();
        String[] parts = targetDate.split("-");
        String targetYear = parts[0];
        int monthNum = Integer.parseInt(parts[1]);
        String targetMonth = java.time.Month.of(monthNum).getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);
        String targetDay = String.valueOf(Integer.parseInt(parts[2]));

        BaseTest.log.info("Navigating Element UI Calendar to: {} {} {}", targetYear, targetMonth, targetDay);

        com.microsoft.playwright.Locator activeCalendar = page.locator(".el-picker-panel:visible").last();
        activeCalendar.waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE).setTimeout(5000));

        // 1. Click Year Header to open Year Selection
        com.microsoft.playwright.Locator yearHeaderBtn = activeCalendar.locator(".el-date-picker__header-label").first();
        yearHeaderBtn.click();
        page.waitForTimeout(500);

        // 2. Navigate Decades until Target Year is visible
        com.microsoft.playwright.Locator yearTable = activeCalendar.locator(".el-year-table:visible").last();
        com.microsoft.playwright.Locator prevDecadeBtn = activeCalendar.locator("button.el-icon-d-arrow-left").first();
        com.microsoft.playwright.Locator nextDecadeBtn = activeCalendar.locator("button.el-icon-d-arrow-right").first();
        
        boolean yearFound = false;
        int attempts = 0;
        while (!yearFound && attempts < 20) {
            com.microsoft.playwright.Locator targetYearCell = yearTable.locator("a.cell").filter(new com.microsoft.playwright.Locator.FilterOptions().setHasText(targetYear)).first();
            if (targetYearCell.isVisible()) {
                targetYearCell.click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
                yearFound = true;
            } else {
                String firstVisibleYear = yearTable.locator("a.cell").first().innerText().trim();
                if (Integer.parseInt(targetYear) < Integer.parseInt(firstVisibleYear)) {
                    prevDecadeBtn.click();
                } else {
                    nextDecadeBtn.click();
                }
                page.waitForTimeout(300);
            }
            attempts++;
        }
        
        if (!yearFound) {
            throw new RuntimeException("Could not find the target year " + targetYear + " in the calendar.");
        }
        page.waitForTimeout(500);

        // 3. Select Month
        com.microsoft.playwright.Locator monthTable = activeCalendar.locator(".el-month-table:visible").last();
        com.microsoft.playwright.Locator targetMonthCell = monthTable.locator("a.cell").filter(new com.microsoft.playwright.Locator.FilterOptions().setHasText(targetMonth)).first();
        targetMonthCell.click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
        page.waitForTimeout(500);

        // 4. Select Day
        com.microsoft.playwright.Locator dateTable = activeCalendar.locator(".el-date-table:visible").last();
        com.microsoft.playwright.Locator targetDayCell = dateTable.locator("xpath=.//td[contains(@class, 'available')]/div/span[normalize-space(text())='" + targetDay + "']").first();
        targetDayCell.click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));
        page.waitForTimeout(500);
    }

    private static final Pattern MUI_CALENDAR_HEADER_PATTERN = Pattern.compile(
            "^(January|February|March|April|May|June|July|August|September|October|November|December) \\d{4}$");

    /**
     * Navigates an open MUI X DatePicker to select the exact target date.
     * Opens the calendar via {@code calendarTrigger} (e.g. the "Choose date" icon button),
     * switches to the year view, picks the target year, walks month-by-month to the
     * target month, then clicks the target day cell.
     * <p>
     * Mirrors the interaction recorded via Playwright codegen: clicking the header label
     * (e.g. "July 2026") opens the year view, clicking a year button (e.g. "1994") returns
     * to the day view, and "Next month"/"Previous month" buttons step through months before
     * the target day gridcell is clicked.
     *
     * @param calendarTrigger the locator that opens the calendar popup when clicked
     *                        (typically {@code page.getByLabel("Choose date")}).
     * @param targetDateDDMMYYYY the date string in "dd/MM/yyyy" format (e.g. "24/12/1995").
     */
    public static void selectDateFromMuiCalendar(Locator calendarTrigger, String targetDateDDMMYYYY) {
        Page page = BaseTest.getPage();
        String[] parts = targetDateDDMMYYYY.split("/");
        String targetDay = String.valueOf(Integer.parseInt(parts[0]));
        int targetMonthNum = Integer.parseInt(parts[1]);
        int targetYearNum = Integer.parseInt(parts[2]);
        YearMonth targetYearMonth = YearMonth.of(targetYearNum, targetMonthNum);

        BaseTest.log.info("Navigating MUI calendar to: {}", targetDateDDMMYYYY);

        // 1. Open the calendar popup
        calendarTrigger.click();
        page.waitForTimeout(300);

        Locator headerLabel = page.getByText(MUI_CALENDAR_HEADER_PATTERN);
        headerLabel.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5_000));

        // 2. Click the header label (e.g. "July 2026") to switch into Year view
        waitForSingleMatch(headerLabel, page).click();
        page.waitForTimeout(300);

        // 3. Pick the target year from the year list
        Locator yearButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(String.valueOf(targetYearNum)).setExact(true)).first();
        yearButton.scrollIntoViewIfNeeded();
        yearButton.click();

        // 4. Walk from the month now displayed to the target month.
        // MUI animates a slide transition between months (and between year/day views), which
        // briefly leaves both the outgoing and incoming month grids in the DOM at once. Polling
        // for a single stable match avoids reading a stale header or clicking a duplicate cell.
        DateTimeFormatter headerFormatter = DateTimeFormatter.ofPattern("MMMM uuuu", Locale.ENGLISH);
        int safety = 0;
        while (safety < 24) {
            String currentText = readStableText(headerLabel, page);
            YearMonth currentYearMonth;
            try {
                currentYearMonth = YearMonth.parse(currentText, headerFormatter);
            } catch (Exception e) {
                throw new RuntimeException("Could not parse MUI calendar header text: '" + currentText + "'", e);
            }

            if (currentYearMonth.equals(targetYearMonth)) {
                break;
            }

            String navLabel = currentYearMonth.isBefore(targetYearMonth) ? "Next month" : "Previous month";
            page.getByLabel(navLabel).click();
            safety++;
        }

        if (safety >= 24) {
            throw new RuntimeException("Could not navigate MUI calendar to target month: " + targetYearMonth);
        }

        // 5. Select the target day, resolving to a single stable gridcell first.
        Locator dayCell = page.getByRole(AriaRole.GRIDCELL, new Page.GetByRoleOptions().setName(targetDay).setExact(true));
        waitForSingleMatch(dayCell, page).click();
        page.waitForTimeout(300);
    }

    /**
     * Polls a locator until it resolves to exactly one element (waiting out MUI's slide
     * transition, which briefly duplicates outgoing/incoming calendar nodes), then returns a
     * single-element locator to act on. Falls back to the most recently rendered match
     * ({@code last()}) if the count never settles to 1 within the polling budget.
     */
    private static Locator waitForSingleMatch(Locator locator, Page page) {
        for (int attempt = 0; attempt < 15; attempt++) {
            if (locator.count() == 1) {
                return locator.first();
            }
            page.waitForTimeout(150);
        }
        return locator.last();
    }

    /**
     * Reads text from a locator that may be transiently duplicated during an MUI animation,
     * returning only once a single matching element is present.
     */
    private static String readStableText(Locator locator, Page page) {
        return waitForSingleMatch(locator, page).innerText().trim();
    }
}
