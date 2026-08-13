package ui.pages.jarvis_secured;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;

/**
 * Page Object for Insurance Details section in the Docket Initiation stage.
 * Handles:
 *   - General Insurance (life insurance for applicant)
 *   - Property Insurance / Collateral insurance
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
        page.getByPlaceholder("Application Actions").scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(1000);
        page.getByText("Move to Docket Initiation").click();
        page.waitForTimeout(2000);
        log.info("Move to Docket Initiation attempted.");
    }

    /**
     * Clicks on the "Please add atleast 1 property" validation message to navigate to insurance section.
     */
    public void clickPropertyValidationMessage() {
        log.info("Clicking property validation message...");
        page.getByText("Please add atleast 1 property").click();
        page.waitForTimeout(2000);
        log.info("Navigated to Insurance Details section.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  GENERAL INSURANCE
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Opens the Insurance Details section (accordion with "No" status).
     */
    public void openInsuranceDetailsSection() {
        log.info("Opening Insurance Details section...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Insurance Details No")).click();
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

    /**
     * Opens the General Insurance form by clicking the "General Insurance" button/accordion.
     */
    public void openGeneralInsuranceForm() {
        log.info("Opening General Insurance form...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("General Insurance ")).click();
        page.waitForTimeout(1000);
        log.info("General Insurance form opened.");
    }

    /**
     * Selects an insurance provider from the dropdown.
     *
     * @param provider the insurance provider name (e.g. "ICICI Lombard")
     */
    public void selectInsuranceProvider(String provider) {
        log.info("Selecting insurance provider: {}", provider);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Insurance Provider")).click();
        page.waitForTimeout(500);
        page.getByText(provider).nth(1).click();
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
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Title")).click();
        page.waitForTimeout(300);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Select a policy holder name")).click();
        page.waitForTimeout(500);
        page.getByText(holderName).nth(3).click();
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
        Locator sumInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Policy sum insured"));
        sumInput.click();
        sumInput.fill(sumInsured);
        page.waitForTimeout(300);
        log.info("Policy sum insured filled: {}", sumInsured);
    }

    /**
     * Selects the policy tenure from the dropdown.
     *
     * @param tenure the policy tenure (e.g. "3 years")
     */
    public void selectPolicyTenure(String tenure) {
        log.info("Selecting policy tenure: {}", tenure);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Policy Tenure")).click();
        page.waitForTimeout(500);
        page.locator("li").filter(new Locator.FilterOptions().setHasText(tenure)).nth(2).click();
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
     * Selects a nominee name from the dropdown.
     *
     * @param nomineeName the nominee name (e.g. "Noah johnson")
     */
    public void selectNomineeName(String nomineeName) {
        log.info("Selecting nominee: {}", nomineeName);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Select a nominee name")).click();
        page.waitForTimeout(500);
        page.getByText(nomineeName).nth(4).click();
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

    /**
     * Clicks Update to save the general insurance details.
     */
    public void clickUpdate() {
        log.info("Clicking Update to save insurance details...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update").setExact(true)).click();
        page.waitForTimeout(2000);
        log.info("Insurance details updated.");
    }

    /**
     * Clicks SUBMIT to finalize the general insurance entry.
     */
    public void clickSubmit() {
        log.info("Clicking SUBMIT...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SUBMIT")).click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        log.info("Insurance submitted.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  PROPERTY INSURANCE / COLLATERAL
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Opens the Property Insurance Collateral section.
     */
    public void openPropertyInsuranceCollateralSection() {
        log.info("Opening Property Insurance Collateral section...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Property Insurance Collateral")).click();
        page.waitForTimeout(1000);
        log.info("Property Insurance Collateral section opened.");
    }

    /**
     * Clicks on the "Property insurance is" validation/info message.
     */
    public void clickPropertyInsuranceMessage() {
        log.info("Clicking Property insurance message...");
        page.getByText("Property insurance is").click();
        page.waitForTimeout(1000);
        log.info("Property insurance message clicked.");
    }

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
     * Fills in the property insurance premium for the collateral.
     *
     * @param premium the insurance premium value (e.g. "6840")
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
     * Clicks Update to save property insurance details.
     */
    public void clickPropertyUpdate() {
        log.info("Clicking Update for property insurance...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update").setExact(true)).click();
        page.waitForTimeout(2000);
        log.info("Property insurance updated.");
    }

    /**
     * Clicks SUBMIT for property insurance collateral.
     */
    public void submitPropertyInsurance() {
        log.info("Submitting property insurance...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SUBMIT")).click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        log.info("Property insurance submitted.");
    }

    /**
     * Final submit for the entire General Insurance entry after property insurance is done.
     */
    public void finalSubmitGeneralInsurance() {
        log.info("Final submit for General Insurance entry...");
        // Click on the General Insurance accordion to expand/view the final state
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("General Insurance Hannah (")).click();
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SUBMIT")).click();
        page.waitForTimeout(2000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(1000);
        log.info("General Insurance final submission completed.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  DOGH TRIGGER
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Triggers DOGH (Obtain physical documents) for the General Insurance entry.
     * Flow: Open Insurance Details → Edit → General Insurance Hannah → See more details → Trigger DOGH → SUBMIT
     * Prerequisite: User must have SALES department/designation assigned via Admin Portal.
     */
    public void triggerDOGH() {
        log.info("── Triggering DOGH for Insurance ──");

        // Open Insurance Details section
        log.info("Opening Insurance Details section for DOGH...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Insurance Details Insurance")).click();
        page.waitForTimeout(1000);

        // Click Edit
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit")).click();
        page.waitForTimeout(1000);

        // Open General Insurance Hannah accordion
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("General Insurance Hannah (")).click();
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
        page.waitForTimeout(1000);

        log.info("DOGH triggered successfully for General Insurance.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  COMPLETE FLOW
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Completes the full insurance details flow including general and property insurance.
     *
     * @param provider         insurance provider (e.g. "ICICI Lombard")
     * @param holderName       policy holder name (e.g. "Hannah Isaac")
     * @param sumInsured       sum insured (e.g. "250000")
     * @param tenure           policy tenure (e.g. "3 years")
     * @param premium          general insurance premium (e.g. "25000")
     * @param nomineeName      nominee name (e.g. "Noah johnson")
     * @param relationship     nominee relationship (e.g. "brother")
     * @param propertyPremium  property insurance premium (e.g. "6840")
     */
    public void completeInsuranceDetails(String provider, String holderName, String sumInsured,
                                         String tenure, String premium, String nomineeName,
                                         String relationship, String propertyPremium) {
        log.info("── Completing Insurance Details ──");

        // Step 1: Handle validation message and open section
        clickPropertyValidationMessage();
        openInsuranceDetailsSection();
        clickEdit();

        // Step 2: Fill General Insurance
        openGeneralInsuranceForm();
        selectInsuranceProvider(provider);
        selectPolicyHolderName(holderName);
        fillPolicySumInsured(sumInsured);
        selectPolicyTenure(tenure);
        fillInsurancePremium(premium);
        selectNomineeName(nomineeName);
        fillNomineeRelationship(relationship);
        clickUpdate();
        clickSubmit();

        // Step 3: Fill Property Insurance / Collateral
        openPropertyInsuranceCollateralSection();
        clickPropertyInsuranceMessage();
        openCollateralSection();
        fillPropertyInsurancePremium(propertyPremium);
        clickPropertyUpdate();
        submitPropertyInsurance();

        // Step 4: Final submission
        finalSubmitGeneralInsurance();

        // Step 5: Click outside to close the insurance panel/modal
        closeInsurancePanel();

        log.info("Insurance Details completed successfully.");
    }

    /**
     * Closes the insurance panel by clicking the close (X) button
     * and then clicking outside the insurance section.
     */
    public void closeInsurancePanel() {
        log.info("Closing insurance panel — clicking close button then clicking outside...");
        page.locator("//i[@class='el-icon-close']").click();
        page.waitForTimeout(1000);
        page.mouse().click(50, 400);
        page.waitForTimeout(2000);
        log.info("Insurance panel closed.");
    }
}
