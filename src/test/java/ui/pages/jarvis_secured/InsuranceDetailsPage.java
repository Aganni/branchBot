package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

import java.util.regex.Pattern;

/**
 * Page Object for Insurance Details section in the Docket Initiation stage.
 * Handles:
 *   - General Insurance (life insurance for applicant)
 *   - Property Insurance / Collateral insurance
 *   - DOGH trigger for insurance
 *   - Final submission to complete insurance requirements
 */
public class InsuranceDetailsPage extends BaseTest {

    private final Page page;

    public InsuranceDetailsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  VALIDATION & NAVIGATION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Attempts to move to Docket Initiation. Expects a "Please add atleast 1 property" validation message.
     */
    public void attemptMoveToDocketInitiation() {
        log.info("Attempting Move to Docket Initiation (expecting property/insurance validation)...");
        // Wait for page to fully load — validation can take longer than usual
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(5000);
        page.evaluate("window.scrollTo(0, 0)");
        page.waitForTimeout(1000);

        // Wait for Application Actions to be visible and ready
        Locator appActions = page.getByPlaceholder("Application Actions");
        appActions.waitFor(new Locator.WaitForOptions().setTimeout(60000));
        appActions.click();
        page.waitForTimeout(2000);
        page.locator("li").filter(new Locator.FilterOptions().setHasText("Move to Docket Initiation")).click();
        page.waitForTimeout(2000);
        log.info("Move to Docket Initiation attempted.");
    }

    /**
     * Clicks on the "Please add atleast 1 property" validation message to navigate to insurance section.
     */
    public void clickPropertyValidationMessage() {
        log.info("Clicking property validation message...");
        Locator validationMsg = page.getByText("Please add atleast 1 property");
        validationMsg.waitFor(new Locator.WaitForOptions().setTimeout(150000)); // 2.5 min timeout
        validationMsg.click();
        page.waitForTimeout(2000);
        log.info("Navigated to Insurance Details section.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  INSURANCE DETAILS SECTION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Opens the Insurance Details section accordion.
     */
    public void openInsuranceDetailsSection() {
        log.info("Opening Insurance Details section...");
        // The accordion button's accessible name can vary (e.g. "Insurance Details Insurance",
        // "Insurance Details Not Initiated", etc.) depending on the current status badge.
        // Use a text-based locator with the section heading text instead.
        Locator insuranceBtn = page.locator("button").filter(
                new Locator.FilterOptions().setHasText("Insurance Details"));
        insuranceBtn.first().scrollIntoViewIfNeeded();
        insuranceBtn.first().click();
        page.waitForTimeout(1000);
        log.info("Insurance Details section opened.");
    }

    /**
     * Clicks Edit to enable editing of insurance details.
     */
    public void clickEdit() {
        log.info("Clicking Edit on Insurance Details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);
        log.info("Insurance Details in edit mode.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  GENERAL INSURANCE
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Selects an insurance provider from the dropdown.
     *
     * @param provider the insurance provider name (e.g. "ICICI Lombard")
     */
    public void selectInsuranceProvider(String provider) {
        log.info("Selecting insurance provider: {}", provider);
        page.getByPlaceholder("Insurance Provider").click();
        page.waitForTimeout(500);
        page.locator("span").filter(new Locator.FilterOptions()
                .setHasText(Pattern.compile("^" + provider + "$"))).click();
        page.waitForTimeout(500);
        log.info("Insurance provider selected: {}", provider);
    }

    /**
     * Selects a policy holder name from the dropdown.
     *
     * @param holderName the policy holder name (e.g. "Hannah Isaac")
     */
    public void selectPolicyHolderName(String holderName) {
        log.info("Selecting policy holder: {}", holderName);
        page.getByPlaceholder("Select a policy holder name").click();
        page.waitForTimeout(500);
        page.getByText(holderName).nth(1).click();
        page.waitForTimeout(500);
        log.info("Policy holder selected: {}", holderName);
    }

    /**
     * Fills in the policy sum insured amount.
     *
     * @param sumInsured the sum insured value (e.g. "250000")
     */
    public void fillPolicySumInsured(String sumInsured) {
        log.info("Filling policy sum insured: {}", sumInsured);
        Locator sumInput = page.getByPlaceholder("Policy sum insured");
        sumInput.click();
        sumInput.fill(sumInsured);
        page.waitForTimeout(300);
        log.info("Policy sum insured filled: {}", sumInsured);
    }

    /**
     * Selects the policy tenure from the dropdown.
     *
     * @param tenure the policy tenure (e.g. "1 year")
     */
    public void selectPolicyTenure(String tenure) {
        log.info("Selecting policy tenure: {}", tenure);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Policy Tenure")).click();
        page.waitForTimeout(500);
        page.getByText(tenure).nth(1).click();
        page.waitForTimeout(500);
        log.info("Policy tenure selected: {}", tenure);
    }

    /**
     * Fills in the general insurance premium amount.
     *
     * @param premium the insurance premium value (e.g. "25000")
     */
    public void fillInsurancePremium(String premium) {
        log.info("Filling insurance premium: {}", premium);
        Locator premiumInput = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Insurance Premium").setExact(true));
        premiumInput.click();
        premiumInput.fill(premium);
        page.waitForTimeout(300);
        log.info("Insurance premium filled: {}", premium);
    }

    /**
     * Clicks Update to save the insurance details. First attempt may show
     * "Please enter mandatory fields" validation for nominee.
     */
    public void clickUpdate() {
        log.info("Clicking Update to save insurance details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update").setExact(true)).click();
        page.waitForTimeout(2000);
        log.info("Insurance details updated.");
    }

    /**
     * Clicks the "Please enter mandatory fields" validation message (shown after first Update
     * when nominee is missing).
     */
    public void clickMandatoryFieldsValidation() {
        log.info("Clicking 'Please enter mandatory fields' validation...");
        page.getByText("Please enter mandatory fields").click();
        page.waitForTimeout(1000);
        log.info("Mandatory fields validation acknowledged.");
    }

    /**
     * Selects a nominee name from the dropdown.
     *
     * @param nomineeName the nominee name (e.g. "loganathan Sharma")
     */
    public void selectNomineeName(String nomineeName) {
        log.info("Selecting nominee: {}", nomineeName);
        page.getByPlaceholder("Select a nominee name").click();
        page.waitForTimeout(500);
        page.locator("li").filter(new Locator.FilterOptions().setHasText(nomineeName)).nth(1).click();
        page.waitForTimeout(500);
        log.info("Nominee selected: {}", nomineeName);
    }

    /**
     * Fills in the nominee relationship.
     *
     * @param relationship the relationship (e.g. "brother")
     */
    public void fillNomineeRelationship(String relationship) {
        log.info("Filling nominee relationship: {}", relationship);
        page.getByPlaceholder("Enter Relationship with").click();
        page.waitForTimeout(500);
        page.getByText(relationship).click();
        page.waitForTimeout(500);
        log.info("Nominee relationship filled: {}", relationship);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PROPERTY INSURANCE / COLLATERAL
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Opens the Collateral 1 section to fill property insurance details.
     */
    public void openCollateralSection() {
        log.info("Opening Collateral 1 section...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Collateral 1 Not triggered")).click();
        page.waitForTimeout(1000);
        log.info("Collateral 1 section opened.");
    }

    /**
     * Clicks the property insurance checkbox to enable the premium input field.
     * The checkbox must be checked before the "Insurance Premium" input becomes editable.
     */
    public void enablePropertyInsuranceCheckbox() {
        log.info("Clicking property insurance checkbox to enable premium input...");
        Locator checkbox = page.locator("(//span[@class='el-checkbox__inner'])[1]");
        checkbox.scrollIntoViewIfNeeded();
        checkbox.click();
        page.waitForTimeout(1000);
        log.info("Property insurance checkbox enabled.");
    }

    /**
     * Fills in the property insurance premium for the collateral.
     * Note: The property insurance checkbox must be checked first via
     * {@link #enablePropertyInsuranceCheckbox()} to enable this input.
     *
     * @param premium the insurance premium value (e.g. "84000")
     */
    public void fillPropertyInsurancePremium(String premium) {
        log.info("Filling property insurance premium: {}", premium);
        Locator premiumInput = page.getByPlaceholder("Insurance Premium",
                new Page.GetByPlaceholderOptions().setExact(true));
        premiumInput.click();
        premiumInput.fill(premium);
        page.waitForTimeout(300);
        log.info("Property insurance premium filled: {}", premium);
    }

    /**
     * Clicks Update for property insurance.
     */
    public void clickPropertyUpdate() {
        log.info("Clicking Update for property insurance...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update").setExact(true)).click();
        page.waitForTimeout(2000);
        log.info("Property insurance updated.");
    }

    /**
     * Clicks SUBMIT to finalize (triggers "Please trigger DOGH for all" validation).
     */
    public void clickSubmit() {
        log.info("Clicking SUBMIT...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SUBMIT")).click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        log.info("Insurance submitted.");
    }

    /**
     * Clicks the "Please trigger DOGH for all" validation message.
     */
    public void clickTriggerDoghValidation() {
        log.info("Clicking 'Please trigger DOGH for all' validation...");
        page.getByText("Please trigger DOGH for all").click();
        page.waitForTimeout(1000);
        log.info("DOGH validation acknowledged.");
    }

    /**
     * Closes the insurance panel by scrolling up to the close (X) button,
     * clicking it, then clicking outside to dismiss the panel.
     */
    public void closeInsurancePanel() {
        log.info("Closing insurance panel...");
        // Scroll up inside the panel to make the close button visible
        page.keyboard().press("Home");
        page.waitForTimeout(1000);

        // Click the close (X) button
        page.locator("//i[@class='el-icon-close']").click();
        page.waitForTimeout(1000);

        // Click outside to fully dismiss the panel
        page.mouse().click(50, 400);
        page.waitForTimeout(2000);
        log.info("Insurance panel closed.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  DOGH TRIGGER
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Triggers DOGH (Obtain physical documents) for the General Insurance entry.
     * Flow: Open Insurance Details → Edit → See more details → Trigger DOGH → SUBMIT → Close panel
     * Prerequisite: User must have SALES department/designation assigned via Admin Portal.
     */
    public void triggerDOGH() {
        log.info("── Triggering DOGH for Insurance ──");

        // Open Insurance Details section
        page.locator("button").filter(
                new Locator.FilterOptions().setHasText("Insurance Details")).first().click();
        page.waitForTimeout(1000);

        // Click Edit
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);

        // Click "See more details" to expand the details view
        page.getByText("See more details").click();
        page.waitForTimeout(1000);

        // Click "Trigger DOGH" CTA
        page.getByText("Trigger DOGH").click();
        page.waitForTimeout(2000);

        // Submit to confirm DOGH trigger
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SUBMIT")).click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        // Close the insurance panel — scroll up to find the close button, then click outside
        closeInsurancePanel();

        log.info("DOGH triggered successfully for General Insurance.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  COMPLETE FLOW
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Completes the full insurance details flow including general and property insurance.
     * After this method, the "Please trigger DOGH for all" validation has been shown and
     * the insurance panel is closed, ready for Admin Portal role change and DOGH trigger.
     *
     * @param provider         insurance provider (e.g. "ICICI Lombard")
     * @param holderName       policy holder name (e.g. "Hannah Isaac")
     * @param sumInsured       sum insured (e.g. "250000")
     * @param tenure           policy tenure (e.g. "1 year")
     * @param premium          general insurance premium (e.g. "25000")
     * @param nomineeName      nominee name (e.g. "loganathan Sharma")
     * @param relationship     nominee relationship (e.g. "brother")
     * @param propertyPremium  property insurance premium (e.g. "84000")
     */
    public void completeInsuranceDetails(String provider, String holderName, String sumInsured,
                                         String tenure, String premium, String nomineeName,
                                         String relationship, String propertyPremium) {
        log.info("── Completing Insurance Details ──");

        // Step 1: Click validation message and open section
        clickPropertyValidationMessage();
        openInsuranceDetailsSection();
        clickEdit();

        // Step 2: Fill General Insurance fields
        selectInsuranceProvider(provider);
        selectPolicyHolderName(holderName);
        fillPolicySumInsured(sumInsured);
        selectPolicyTenure(tenure);
        fillInsurancePremium(premium);

        // Step 3: First Update → triggers mandatory fields validation for nominee
        clickUpdate();
        clickMandatoryFieldsValidation();

        // Step 4: Fill nominee details and Update again
        selectNomineeName(nomineeName);
        fillNomineeRelationship(relationship);
        clickUpdate();

        // Step 5: Fill Property Insurance / Collateral
        openCollateralSection();
        enablePropertyInsuranceCheckbox();
        fillPropertyInsurancePremium(propertyPremium);
        clickPropertyUpdate();

        // Step 6: Submit → triggers "Please trigger DOGH for all" validation
        clickSubmit();
        clickTriggerDoghValidation();

        // Step 7: Close the insurance panel (cross button + click outside)
        closeInsurancePanel();

        log.info("Insurance Details completed. Ready for DOGH trigger after role change.");
    }
}
