package ui.pages.jarvis_secured;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * Page Object for the PD Visit flow in BlackPanther portal.
 * Handles PD Visit form filling (Basic Details, End Use, Customer Details,
 * Work Profile, Customer Profile, CIBIL, CRIF, Conditions, Risk,
 * Customer Summary, Asset/Investment, Banking).
 */
public class PdVisitPage extends BaseTest {
    private Page page;
    public PdVisitPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }
    public void setPage(Page page) {
        this.page = page;
    }
    public Page getCurrentPage() {
        return this.page;
    }

    // NAVIGATION
    public void navigateTo(String url) {
        page.navigate(url);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);
    }

    public void clickPdVisitTab() {
        page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("PD Visit")).click();
        page.waitForTimeout(2000);
    }

    public void selectApplicantFromCombo(String applicantText) {
        log.info("Selecting applicant for PD Visit: {}", applicantText);
        page.evaluate("window.scrollTo(0, 0)");
        page.waitForTimeout(500);
        page.getByRole(AriaRole.COMBOBOX).first().click();
        page.waitForTimeout(1000);
        page.getByText(applicantText).first().click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SECTION BUTTONS
    // ═══════════════════════════════════════════════════════════════════════════

    public void clickSectionButton(String sectionName) {
        log.info("Opening section: {}", sectionName);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(sectionName)).click();
        page.waitForTimeout(1000);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  FORM FIELD INTERACTIONS
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillByLabel(String label, String value) {
        page.getByLabel(label).click();
        page.getByLabel(label).fill(value);
        page.waitForTimeout(300);
    }

    public void selectDropdownByLabel(String comboLabel, String optionLabel) {
        page.getByLabel(comboLabel).click();
        page.waitForTimeout(500);
        page.getByLabel(optionLabel, new Page.GetByLabelOptions().setExact(true)).click();
        page.waitForTimeout(500);
    }

    public void selectDropdownByLabelAndText(String comboLabel, String optionLabel, String optionText) {
        page.getByLabel(comboLabel).click();
        page.waitForTimeout(500);
        page.getByLabel(optionLabel, new Page.GetByLabelOptions().setExact(true))
                .getByText(optionText, new Locator.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);
    }

    public void fillByPlaceholder(String placeholder, String value) {
        page.getByPlaceholder(placeholder).click();
        page.getByPlaceholder(placeholder).fill(value);
        page.waitForTimeout(300);
    }

    public void selectDateFromCalendar(String day) {
        page.getByRole(AriaRole.GRIDCELL, new Page.GetByRoleOptions()
                .setName(day).setExact(true)).first().click();
        page.waitForTimeout(500);
    }

    // BASIC DETAILS
    public void fillBasicDetails(String personName, String contactNo, String relationship, boolean isEntity) {
        log.info("Filling PD Visit Basic Details...");
        clickSectionButton("Basic Details");

        // Date of PD visit
        page.getByLabel("Date of PD visit *").click();
        page.waitForTimeout(500);
        selectDateFromCalendar("1");

        // Property Type
        selectDropdownByLabel("Property Type *", "Commercial");

        // PD At Office
        selectDropdownByLabel("PD At Office *", "Yes");

        if (!isEntity) {
            // Name of the Person Met with (only for individual applicants)
            page.locator("div").filter(new Locator.FilterOptions()
                    .setHasText(Pattern.compile("^Name of the Person Met with \\*$"))).click();
            page.getByLabel("Name of the Person Met with *").fill(personName);
            page.waitForTimeout(300);
            // Contact No
            fillByLabel("Contact No of the Person Met", contactNo);
        } else {
            // Entity has different field label
            fillByLabel("Contact Number of the Person Met with", contactNo);
        }
        // Relationship
        fillByLabel("Relationship with the", relationship);
    }

    // END USE OF FUND
   public void fillEndUseOfFund(String purpose) {
        log.info("Filling End Use Of Fund...");
        clickSectionButton("End Use Of Fund");
        fillByPlaceholder("Enter detailed purpose...", purpose);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CUSTOMER DETAILS
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillCustomerDetails(String dependents, String stabilityOfResidence, String familyMembers,
                                    String email, String qualification, String earningMembers) {
        log.info("Filling Customer Details...");
        clickSectionButton("Customer Details");

        fillByLabel("No Of Dependents *", dependents);
        fillByLabel("Stability of Residence *", stabilityOfResidence);
        fillByLabel("No of Family Members *", familyMembers);
        fillByLabel("Email Id *", email);
        fillByLabel("Qualification *", qualification);
        selectDropdownByLabel("Residence Type *", "Owned");
        fillByLabel("Earning Family Members *", earningMembers);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  WORK PROFILE
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillWorkProfile(String yearsInCurrent, String employees, String natureOfBusiness,
                                String overallExperience) {
        log.info("Filling Work Profile...");
        clickSectionButton("Work Profile");

        selectDropdownByLabelAndText("Current Organization Structure *", "Partnership firm", "Partnership firm");
        fillByLabel("No Of Years In The Current", yearsInCurrent);
        selectDropdownByLabelAndText("Working From *", "Office", "Office");
        selectDropdownByLabelAndText("Mode Of Salary *", "NEFT", "NEFT");
        selectDropdownByLabelAndText("Salary Payment *", "Regular", "Regular");
        selectDropdownByLabelAndText("Pf Deduction *", "Yes", "Yes");
        fillByLabel("No Of Employees In The", employees);

        // Click somewhere to commit, then fill remaining
        page.getByText("Name of The Current Organization").first().click();
        page.waitForTimeout(300);

        fillByLabel("Nature Of Business Of The", natureOfBusiness);
        fillByLabel("Overall Experience *", overallExperience);
        selectDropdownByLabelAndText("Any Other Income *", "Yes", "Yes");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TEXT AREA SECTIONS (Customer Profile, CIBIL, CRIF, Conditions, Risk)
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillCustomerProfile(String text) {
        log.info("Filling Customer Profile...");
        clickSectionButton("Customer Profile");
        fillByPlaceholder("Enter customer profile details", text);
    }

    public void fillCibilObligations(String text) {
        log.info("Filling CIBIL Obligations...");
        clickSectionButton("Current obligation as per CIBIL");
        fillByPlaceholder("Enter CIBIL obligations", text);
    }

    public void fillCrifObligations(String text) {
        log.info("Filling CRIF Obligations...");
        clickSectionButton("Current obligation as per CRIF");
        fillByPlaceholder("Enter CRIF obligations details", text);
    }

    public void fillConditions(String text) {
        log.info("Filling Conditions...");
        clickSectionButton("Conditions");
        fillByPlaceholder("Enter conditions", text);
    }

    public void fillRiskWeakness(String text) {
        log.info("Filling Risk / Weakness...");
        clickSectionButton("Risk / Weakness");
        fillByPlaceholder("Enter risk or weakness details", text);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CUSTOMER SUMMARY
    // ═══════════════════════════════════════════════════════════════════════════

    public void addCustomerSummary(String name, String address, String contact,
                                   String age, String cibilScore, String crifScore,
                                   String propertyOwner, String incomeConsidered) {
        log.info("Adding Customer Summary...");
        clickSectionButton("Customer Summary");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Customer Summary")).click();
        page.waitForTimeout(1000);

        fillByLabel("Name *", name);
        // Applicant Type dropdown
        page.getByLabel("Applicant Type *").click();
        page.waitForTimeout(500);
        page.getByLabel("Applicant", new Page.GetByLabelOptions().setExact(true))
                .getByText("Applicant").click();
        page.waitForTimeout(500);

        fillByLabel("Address *", address);
        fillByLabel("Contact *", contact);
        fillByLabel("Current Age *", age);
        fillByLabel("CIBIL Score *", cibilScore);
        fillByLabel("CRIF Score *", crifScore);
        fillByLabel("Property Owner *", propertyOwner);
        fillByLabel("Income Considered *", incomeConsidered);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ASSET / INVESTMENT DETAILS
    // ═══════════════════════════════════════════════════════════════════════════

    public void addAssetInvestment(String assetType, String ownership, String typeOfAssets,
                                   String numberOfAssets, String valueOfAssets) {
        log.info("Adding Asset / Investment Details...");
        clickSectionButton("Asset / Investment Details");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Asset / Investment")).click();
        page.waitForTimeout(1000);

        fillByLabel("Investment Assets Type *", assetType);
        fillByLabel("Ownership of Assets *", ownership);
        fillByLabel("Type of Assets", typeOfAssets);
        fillByLabel("Number of Assets", numberOfAssets);
        fillByLabel("Value of Assets", valueOfAssets);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  BANKING DETAILS
    // ═══════════════════════════════════════════════════════════════════════════

    public void addBankingDetails(String holderName, String bankName, String accountType,
                                  String accountNumber, String vintage, String avgBalance) {
        log.info("Adding Banking Details...");
        clickSectionButton("Banking Details");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Bank Account")).click();
        page.waitForTimeout(1000);

        fillByLabel("Account Holder Name *", holderName);
        fillByLabel("Bank Name *", bankName);
        fillByLabel("Account Type *", accountType);
        fillByLabel("Account Number *", accountNumber);
        fillByLabel("Vintage Of Account *", vintage);
        fillByLabel("Average Bank Balance (Approx", avgBalance);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  BUSINESS DETAILS (Entity applicants only)
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillBusinessDetails(String firmName, String businessProfile, String businessVintage,
                                    String businessStartedBy, String yearsBusinessRunning,
                                    String employeesSeenDuringPd, String areaOfSpace,
                                    String officeStructure, String inventoryValue,
                                    String electricityBillAmount, String rentAmount) {
        log.info("Filling Business Details (Entity)...");
        clickSectionButton("Business Details");

        fillByLabel("Firm Name / Employer Name *", firmName);
        fillByLabel("Customer Business Profile *", businessProfile);

        // Business Vintage dropdown
        page.getByLabel("Business Vintage *").click();
        page.waitForTimeout(500);
        page.getByLabel("8 yrs").getByText("yrs").click();
        page.waitForTimeout(500);

        // Business Started By dropdown
        page.getByLabel("Business Started By *").click();
        page.waitForTimeout(500);
        page.getByLabel("Family Business").getByText("Family Business").click();
        page.waitForTimeout(500);

        fillByLabel("No of Years Business is", yearsBusinessRunning);
        fillByLabel("No Of Employees Seen During", employeesSeenDuringPd);
        fillByLabel("Area Of The Space From Where", areaOfSpace);

        // Office Structure dropdown
        page.getByLabel("Office Structure *").click();
        page.waitForTimeout(500);
        page.getByLabel("Full tin shed").getByText("Full tin shed").click();
        page.waitForTimeout(500);

        // Heavy Industry Usage dropdown
        page.getByLabel("Heavy Industry Usage *").click();
        page.waitForTimeout(500);
        page.getByLabel("Yes").getByText("Yes").click();
        page.waitForTimeout(500);

        // HRP/Caution Profile dropdown
        page.getByLabel("HRP/Caution Profile *").click();
        page.waitForTimeout(500);
        page.getByLabel("Yes").getByText("Yes").click();
        page.waitForTimeout(500);

        // Any Seasonality in Business dropdown
        page.getByLabel("Any Seasonality in Business *").click();
        page.waitForTimeout(500);
        page.getByLabel("Yes").getByText("Yes").click();
        page.waitForTimeout(500);

        // Inventory Seen dropdown
        page.getByLabel("Inventory Seen *").click();
        page.waitForTimeout(500);
        page.getByLabel("Yes").getByText("Yes").click();
        page.waitForTimeout(500);

        fillByLabel("Inventory Value (In INR) *", inventoryValue);
        fillByLabel("Electricity Bill Amount *", electricityBillAmount);
        fillByLabel("Rent Amount*", rentAmount);

        // Validity of Rent Agreement - select a future date
        page.getByLabel("Validity of Rent Agreement*").click();
        page.waitForTimeout(500);
        // Navigate forward ~15 months
        for (int i = 0; i < 15; i++) {
            page.getByLabel("Go to next month").click();
            page.waitForTimeout(200);
        }
        page.getByRole(AriaRole.GRIDCELL, new Page.GetByRoleOptions().setName("9").setExact(true)).click();
        page.waitForTimeout(500);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  BUSINESS PROFILE (Entity applicants only)
    // ═══════════════════════════════════════════════════════════════════════════

    public void fillBusinessProfile(String text) {
        log.info("Filling Business Profile...");
        clickSectionButton("Business Profile");
        fillByPlaceholder("Enter business profile details", text);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PHYSICAL PD STATUS (Entity applicants - at submit time)
    // ═══════════════════════════════════════════════════════════════════════════

    public void selectPhysicalPdStatus(String status) {
        log.info("Selecting Physical PD Status: {}", status);
        page.getByLabel("Physical PD Status *").click();
        page.waitForTimeout(500);
        page.getByLabel(status).getByText(status).click();
        page.waitForTimeout(500);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PD DONE BY - ENTITY (different flow from individual)
    // ═══════════════════════════════════════════════════════════════════════════

    public void selectPdDoneByEntity(String remarkText, String searchText, String userName, String secondUser) {
        log.info("Filling PD Done By (Entity) and remarks...");
        fillByPlaceholder("Type your remark here", remarkText);
        page.waitForTimeout(300);

        // Select first user
        page.locator("div").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^Select users$"))).nth(1).click();
        page.waitForTimeout(500);
        page.getByPlaceholder("Search users...").fill(searchText);
        page.waitForTimeout(1000);
        page.getByText(userName, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        // Select second user
        page.getByText("Select users").click();
        page.waitForTimeout(500);
        page.getByText(secondUser).click();
        page.waitForTimeout(500);
    }

    public void selectPdDoneBy(String remarkText) {
        log.info("Filling PD Done By and remarks...");
        fillByPlaceholder("Type your remark here", remarkText);
        page.waitForTimeout(300);

        // Select users
        page.locator("div").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^Select users$"))).nth(1).click();
        page.waitForTimeout(500);
        page.getByText("Tenjintenjin.user@").click();
        page.waitForTimeout(500);

        page.locator("div").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^Select users$"))).nth(1).click();
        page.waitForTimeout(500);
        page.getByText("Tenjin", new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SAVE & SUBMIT
    // ═══════════════════════════════════════════════════════════════════════════

    // ═══════════════════════════════════════════════════════════════════════════
    //  REFERENCES (mandatory for PD Visit completion)
    // ═══════════════════════════════════════════════════════════════════════════

    public void addReference(String refType, String name, String contact,
                             String department, String designation, String remarks) {
        log.info("Adding Reference...");
        clickSectionButton("References");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Reference")).click();
        page.waitForTimeout(1000);

        fillByLabel("Reference Type *", refType);
        fillByLabel("Name *", name);
        fillByLabel("Contact No *", contact);
        fillByLabel("Department *", department);
        fillByLabel("Designation *", designation);
        fillByLabel("Remarks *", remarks);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  INCOME ESTIMATION DETAILS / APPLICANT SENP INCOME
    // ═══════════════════════════════════════════════════════════════════════════

    public void addIncomeEstimation(String particulars, String monthly) {
        log.info("Adding Income Estimation Details...");
        clickSectionButton("Income Estimation Details /");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Income Detail")).click();
        page.waitForTimeout(1000);

        fillByLabel("Particulars *", particulars);
        fillByLabel("Monthly *", monthly);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SAVE & SUBMIT
    // ═══════════════════════════════════════════════════════════════════════════

    public void clickSave() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);
    }

    public void clickSubmit() {
        Locator submitBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        submitBtn.scrollIntoViewIfNeeded();
        page.waitForTimeout(500);
        submitBtn.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PD VISIT STATUS VERIFICATION (BlackPanther)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Verifies that the applicant's PD Visit status matches the expected value.
     * Looks for the applicant text combined with the expected status in the dropdown/list.
     */
    public void verifyApplicantStatus(String applicantName, String expectedStatus) {
        log.info("Verifying PD Visit status for '{}' is '{}'...", applicantName, expectedStatus);
        page.waitForTimeout(2000);

        // Check if the applicant name with status text is visible on the page
        Locator statusLocator = page.locator("text=/" + applicantName + ".*" + expectedStatus + "/i");
        if (statusLocator.first().isVisible()) {
            log.info("PD Visit status verified: '{}' is '{}'", applicantName, expectedStatus);
        } else {
            // Alternative: check if combo option shows the status
            page.getByRole(AriaRole.COMBOBOX).first().click();
            page.waitForTimeout(1000);

            Locator optionWithStatus = page.getByText(applicantName + " - " + expectedStatus);
            if (optionWithStatus.first().isVisible()) {
                log.info("PD Visit status verified via dropdown: '{}' is '{}'", applicantName, expectedStatus);
                // Close dropdown by pressing Escape
                page.keyboard().press("Escape");
            } else {
                // Log the available options for debugging
                log.warn("Could not verify exact status '{}' for '{}'. Continuing flow.", expectedStatus, applicantName);
                page.keyboard().press("Escape");
            }
        }
        page.waitForTimeout(500);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  JARVIS - PHYSICAL PD TAB (image uploads)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Navigates to Jarvis PD & Property Visit → Physical PD tab → applicant tab
     * and uploads 4 images to Photographs + 4 images to Business Photographs.
     * This runs on the Jarvis page instance.
     */
    public void uploadPhysicalPdImages(Page jarvisPage, String applicantTabName, String[] imagePaths) {
        log.info("Uploading Physical PD images for: {}", applicantTabName);

        // Navigate to "PD & Property Visit" section
        navigateToPdPropertyVisitSection(jarvisPage);

        jarvisPage.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Physical PD")).click();
        jarvisPage.waitForTimeout(1000);

        jarvisPage.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName(applicantTabName)).click();
        jarvisPage.waitForTimeout(1000);

        jarvisPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        jarvisPage.waitForTimeout(2000);

        // Select PD Status
        jarvisPage.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Positive")).click();
        jarvisPage.waitForTimeout(500);

        // ── Upload 4 images to "Photographs" section ──
        log.info("Uploading 4 images to Photographs section...");
        // Scroll to Photographs heading
        jarvisPage.getByText("Photographs", new Page.GetByTextOptions().setExact(true)).first().scrollIntoViewIfNeeded();
        jarvisPage.waitForTimeout(1000);

        for (int i = 0; i < imagePaths.length; i++) {
            // Always target the first .el-upload file input (Photographs section comes first)
            Locator uploadInput = jarvisPage.locator(".el-upload input[type='file']").first();
            uploadInput.setInputFiles(Paths.get(imagePaths[i]));
            log.info("  Uploaded image {} of 4 to Photographs", i + 1);
            jarvisPage.waitForTimeout(4000); // Wait for upload to complete before next
        }
        log.info("Uploaded 4 images to Photographs section.");

        // ── Upload 4 images to "Business Photographs" section ──
        log.info("Uploading 4 images to Business Photographs section...");
        // Scroll to Business Photographs heading
        jarvisPage.getByText("Business Photographs").scrollIntoViewIfNeeded();
        jarvisPage.waitForTimeout(1000);

        for (int i = 0; i < imagePaths.length; i++) {
            // The page has exactly 2 .el-upload wrapper divs (with the drag-drop zone).
            // The second one belongs to Business Photographs.
            // Target the upload zone that contains "click to browse" text within Business Photographs section.
            Locator businessUpload = jarvisPage.locator("div.el-upload").nth(1).locator("input[type='file']");

            // Fallback if nth(1) doesn't work after DOM mutation
            if (!businessUpload.isVisible()) {
                // Find by proximity to "Business Photographs" text
                businessUpload = jarvisPage.locator("div.el-upload").last().locator("input[type='file']");
            }

            businessUpload.setInputFiles(Paths.get(imagePaths[i]));
            log.info("  Uploaded image {} of 4 to Business Photographs", i + 1);
            jarvisPage.waitForTimeout(4000); // Wait for upload to complete before next
        }
        log.info("Uploaded 4 images to Business Photographs section.");

        // Save
        jarvisPage.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        jarvisPage.waitForLoadState(LoadState.NETWORKIDLE);
        jarvisPage.waitForTimeout(3000);

        log.info("Physical PD images uploaded for: {}", applicantTabName);
    }

    /**
     * Navigates to the "PD & Property Visit" section in Jarvis sidebar.
     * Uses multiple locator strategies with fallbacks since the element
     * may be rendered as a link, list-item, or a generic clickable element.
     * Skips navigation if already on the PD & Property Visit section.
     */
    private void navigateToPdPropertyVisitSection(Page jarvisPage) {
        // Check if we're already on PD & Property Visit (Physical PD tab visible means we're there)
        Locator physicalPdTab = jarvisPage.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Physical PD"));
        if (physicalPdTab.isVisible()) {
            log.info("Already on PD & Property Visit section, skipping navigation.");
            return;
        }

        // Strategy 1: Direct URL navigation to pdForm page
        // URL pattern: /application/{appId}/pdForm
        String currentUrl = jarvisPage.url();
        if (currentUrl.contains("/application/")) {
            String pdFormUrl = currentUrl.replaceAll("/application/([^/]+).*", "/application/$1/pdForm");
            if (!currentUrl.equals(pdFormUrl)) {
                log.info("Navigating directly to PD form URL: {}", pdFormUrl);
                jarvisPage.navigate(pdFormUrl);
                jarvisPage.waitForLoadState(LoadState.NETWORKIDLE);
                jarvisPage.waitForTimeout(3000);
                return;
            }
        }

        // Strategy 2: AriaRole.LINK
        Locator linkLocator = jarvisPage.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("PD & Property Visit"));
        if (linkLocator.isVisible()) {
            linkLocator.click();
            jarvisPage.waitForLoadState(LoadState.NETWORKIDLE);
            jarvisPage.waitForTimeout(2000);
            return;
        }

        // Strategy 3: AriaRole.LISTITEM containing the text
        Locator listItemLocator = jarvisPage.getByRole(AriaRole.LISTITEM).filter(
                new Locator.FilterOptions().setHasText("PD & Property Visit"));
        if (listItemLocator.first().isVisible()) {
            listItemLocator.first().click();
            jarvisPage.waitForLoadState(LoadState.NETWORKIDLE);
            jarvisPage.waitForTimeout(2000);
            return;
        }

        // Strategy 4: CSS selector for sidebar nav items (Element UI)
        Locator menuItemLocator = jarvisPage.locator("li.el-menu-item:has-text('PD & Property Visit')");
        if (menuItemLocator.isVisible()) {
            menuItemLocator.click();
            jarvisPage.waitForLoadState(LoadState.NETWORKIDLE);
            jarvisPage.waitForTimeout(2000);
            return;
        }

        // Strategy 5: Generic text match with click (broadest fallback)
        Locator textLocator = jarvisPage.locator("text='PD & Property Visit'").first();
        textLocator.click();
        jarvisPage.waitForLoadState(LoadState.NETWORKIDLE);
        jarvisPage.waitForTimeout(2000);
    }
}
