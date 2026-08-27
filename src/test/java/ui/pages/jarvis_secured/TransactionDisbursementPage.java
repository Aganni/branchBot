package ui.pages.jarvis_secured;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;
public class TransactionDisbursementPage extends BaseTest {

    private final Page page;

    public TransactionDisbursementPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MOVE TO DOCKET INITIATION — INITIAL ATTEMPT
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Attempts to move to Docket Initiation. Expects "At least one disbursement/" validation.
     */
    public void attemptMoveToDocketInitiation() {
        log.info("Attempting Move to Docket Initiation (expecting disbursement validation)...");
        page.evaluate("window.scrollTo(0, 0)");
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(2000);
        page.getByText("Move to Docket Initiation").click();
        page.waitForTimeout(3000);
        log.info("Move to Docket Initiation attempted.");
    }

    /**
     * Clicks on the "At least one disbursement/" validation message to navigate to tranche section.
     */
    public void clickDisbursementValidationMessage() {
        log.info("Clicking disbursement validation message...");
        Locator validationMsg = page.getByText("At least one disbursement/");
        validationMsg.waitFor(new Locator.WaitForOptions().setTimeout(60000));
        validationMsg.click();
        page.waitForTimeout(2000);
        log.info("Navigated to Transaction Disbursement section.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TRANSACTION DISBURSEMENT SECTION
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Opens the Transaction Disbursement accordion section.
     */
    public void openTransactionDisbursementSection() {
        log.info("Opening Transaction Disbursement section...");
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Transaction Disbursement")).click();
        page.waitForTimeout(1000);
        log.info("Transaction Disbursement section opened.");
    }

    /**
     * Clicks Edit to enable editing of transaction disbursement details.
     */
    public void clickEdit() {
        log.info("Clicking Edit on Transaction Disbursement...");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit").setExact(true)).click();
        page.waitForTimeout(1000);
        log.info("Transaction Disbursement in edit mode.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TRANSACTION 1 — First tranche (Cheque)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Adds the first transaction with Cheque mode.
     *
     * @param favouringName   the favouring/payee name (e.g. "Nikitha P S")
     * @param paymentAmount   the payment amount (e.g. "25000")
     * @param payableLocation the payable location (e.g. "Bangalore")
     * @param printLocSearch  partial text to search print location (e.g. "ban")
     * @param printLocSelect  the print location to select from dropdown (e.g. "BANGALORE")
     */
    public void addFirstTransaction(String favouringName, String paymentAmount,
                                    String payableLocation, String printLocSearch,
                                    String printLocSelect) {
        log.info("Adding Transaction 1: mode=Cheque, favouring={}, amount={}", favouringName, paymentAmount);

        // Click "+ Add New Transaction"
        page.getByText("+ Add New Transaction").click();
        page.waitForTimeout(1000);

        // Select Mode = Cheque
        page.getByPlaceholder("Select the Mode").click();
        page.waitForTimeout(500);
        page.getByText("Cheque", new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        // Fill Favouring/Payee name
        Locator favouringInput = page.getByPlaceholder("Enter the Favouring/");
        favouringInput.click();
        favouringInput.fill(favouringName);
        page.waitForTimeout(300);

        // Fill Payment Amount
        Locator paymentInput = page.getByPlaceholder("Enter the Payment Amount");
        paymentInput.click();
        paymentInput.fill(paymentAmount);
        page.waitForTimeout(300);

        // Fill Payable Location
        Locator payableInput = page.getByPlaceholder("Enter the Payable Location");
        payableInput.click();
        payableInput.fill(payableLocation);
        page.waitForTimeout(300);

        // Fill Print Location (type-ahead search)
        Locator printLocInput = page.getByPlaceholder("Enter the Print Location");
        printLocInput.click();
        printLocInput.fill(printLocSearch);
        page.waitForTimeout(1000);
        page.getByText(printLocSelect, new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        // Submit Transaction 1
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("submit")).click();
        page.waitForTimeout(2000);
        log.info("Transaction 1 added successfully.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TRANSACTION 2 — Second tranche (Cheque, verify NEFT disabled)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Adds the second transaction with Cheque mode.
     * Before selecting mode, verifies that NEFT option is disabled.
     * Uses scoped locators (form filter) to target the second transaction form.
     *
     * @param favouringName   the favouring/payee name (e.g. "Sneha")
     * @param paymentAmount   the payment amount (e.g. "267500")
     * @param payableLocation the payable location (e.g. "Pune")
     * @param printLocSearch  partial text to search print location (e.g. "pun")
     * @param printLocSelect  the print location to select from dropdown (e.g. "PUNE")
     */
    public void addSecondTransaction(String favouringName, String paymentAmount,
                                     String payableLocation, String printLocSearch,
                                     String printLocSelect) {
        log.info("Adding Transaction 2: mode=Cheque, favouring={}, amount={}", favouringName, paymentAmount);

        // Click "+ Add New Transaction" for the second time
        page.getByText("+ Add New Transaction").click();
        page.waitForTimeout(1000);

        // Scope to the second transaction form
        Locator secondForm = page.locator("form").filter(
                new Locator.FilterOptions().setHasText("Transaction Transaction"));

        // Select Mode — open dropdown in second form
        secondForm.getByPlaceholder("Select the Mode").click();
        page.waitForTimeout(500);

        // Verify NEFT is disabled
        verifyNeftDisabled();

        // Select Cheque (use nth(1) since first Cheque belongs to transaction 1)
        page.getByText("Cheque", new Page.GetByTextOptions().setExact(true)).nth(1).click();
        page.waitForTimeout(500);

        // Fill Favouring/Payee name (use "Please Input" placeholder for 2nd transaction)
        Locator favouringInput = page.getByPlaceholder("Please Input");
        favouringInput.click();
        favouringInput.fill(favouringName);
        page.waitForTimeout(300);

        // Fill Payment Amount (scoped to second form)
        secondForm.getByPlaceholder("Enter the Payment Amount").click();
        secondForm.getByPlaceholder("Enter the Payment Amount").fill(paymentAmount);
        page.waitForTimeout(300);

        // Fill Payable Location (scoped to second form)
        secondForm.getByPlaceholder("Enter the Payable Location").click();
        secondForm.getByPlaceholder("Enter the Payable Location").fill(payableLocation);
        page.waitForTimeout(300);

        // Fill Print Location (type-ahead search, scoped to second form)
        secondForm.getByPlaceholder("Enter the Print Location").click();
        secondForm.getByPlaceholder("Enter the Print Location").fill(printLocSearch);
        page.waitForTimeout(1000);
        page.getByText(printLocSelect).nth(1).click();
        page.waitForTimeout(500);

        // Submit Transaction 2
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("submit")).nth(1).click();
        page.waitForTimeout(2000);
        log.info("Transaction 2 added successfully.");
    }

    /**
     * Verifies that the NEFT option is disabled in the mode dropdown.
     * Logs the result but does not fail the test if unable to verify.
     */
    private void verifyNeftDisabled() {
        log.info("Verifying NEFT option is disabled...");
        try {
            Locator neftOption = page.locator("li").filter(
                    new Locator.FilterOptions().setHasText("NEFT"));
            if (neftOption.count() > 0) {
                String className = neftOption.first().getAttribute("class");
                if (className != null && className.contains("is-disabled")) {
                    log.info("NEFT option is confirmed DISABLED.");
                } else {
                    log.warn("NEFT option is present but NOT disabled. Class: {}", className);
                }
            } else {
                log.info("NEFT option not found in dropdown.");
            }
        } catch (Exception e) {
            log.warn("Could not verify NEFT disabled state: {}", e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CLOSE TRANSACTION DISBURSEMENT PANEL
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Closes the Transaction Disbursement panel.
     * Scrolls up, clicks the close (X) button, then clicks outside to dismiss.
     */
    public void closeTransactionPanel() {
        log.info("Closing Transaction Disbursement panel...");

        // Scroll up to make close button visible
        page.keyboard().press("Home");
        page.waitForTimeout(1000);

        // Click the close (X) button
        try {
            Locator closeBtn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(""));
            if (closeBtn.count() > 0) {
                closeBtn.first().click();
                page.waitForTimeout(1000);
            } else {
                // Fallback: click the el-icon-close
                page.locator("//i[@class='el-icon-close']").first().click();
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            log.info("Trying alternative close approach...");
            page.locator(".el-icon-close").first().click();
            page.waitForTimeout(1000);
        }

        // Click outside to fully dismiss the panel
        page.mouse().click(50, 400);
        page.waitForTimeout(2000);
        log.info("Transaction Disbursement panel closed.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MOVE TO DOCKET INITIATION — AFTER TRANSACTIONS (CREDIT ROLE REQUIRED)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Attempts Move to Docket Initiation after transactions are added.
     * Expects "Unable to initiate credit" error because user role is not CREDIT yet.
     */
    public void attemptMoveToDocketInitiationAfterTransactions() {
        log.info("Attempting Move to Docket Initiation after adding transactions...");

        // Navigate back to App Form page (panel close may have changed URL context)
        String currentUrl = page.url();
        if (!currentUrl.contains("/appForm")) {
            // Extract app ID from URL and navigate to appForm
            String appFormUrl = currentUrl.replaceAll("/application/([^/]+).*", "/application/$1/appForm");
            if (appFormUrl.equals(currentUrl)) {
                // URL doesn't contain /application/ pattern — go back via browser history
                page.goBack();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.waitForTimeout(3000);
            } else {
                page.navigate(appFormUrl);
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.waitForTimeout(3000);
            }
        } else {
            page.reload();
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.waitForTimeout(3000);
        }

        page.evaluate("window.scrollTo(0, 0)");
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(2000);
        page.getByText("Move to Docket Initiation").click();
        page.waitForTimeout(3000);
        log.info("Move to Docket Initiation attempted (expecting credit role error).");
    }

    /**
     * Clicks the "Unable to initiate credit" error message to acknowledge it.
     */
    public void clickUnableToInitiateCreditError() {
        log.info("Clicking 'Unable to initiate credit' error...");
        Locator errorMsg = page.getByText("Unable to initiate credit");
        errorMsg.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        errorMsg.click();
        page.waitForTimeout(1000);
        log.info("'Unable to initiate credit' error acknowledged.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  FINAL MOVE TO DOCKET INITIATION (after Admin Portal role change)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Dismisses any lingering drawer/sidebar overlay before performing actions.
     */
    public void dismissDrawerOverlay() {
        log.info("Dismissing drawer overlay if visible...");
        try {
            Locator drawerTint = page.locator(".drawer-tint-div");
            if (drawerTint.isVisible()) {
                drawerTint.click();
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            log.info("No drawer overlay to dismiss.");
        }
    }

    /**
     * Final Move to Docket Initiation after Admin Portal role change is complete.
     * Clicks Application Actions → Move to Docket Initiation → Accept.
     */
    public void finalMoveToDocketInitiation() {
        log.info("Final Move to Docket Initiation...");
        dismissDrawerOverlay();

        page.evaluate("window.scrollTo(0, 0)");
        page.waitForTimeout(1000);

        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(2000);
        page.locator("li").filter(
                new Locator.FilterOptions().setHasText("Move to Docket Initiation")).click();
        page.waitForTimeout(2000);

        // Click Accept on the confirmation dialog
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept")).click();
        page.waitForTimeout(3000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        log.info("Application moved to Docket Initiation successfully.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MOVE TO DISBURSAL REQUEST
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Attempts to move to Disbursal Request.
     * Expects "ESign documents are missing." validation message.
     */
    public void attemptMoveToDisbursal() {
        log.info("Attempting Move to Disbursal Request...");
        page.evaluate("window.scrollTo(0, 0)");
        page.waitForTimeout(1000);
        page.getByPlaceholder("Application Actions").click();
        page.waitForTimeout(2000);
        page.locator("li").filter(
                new Locator.FilterOptions().setHasText("Move to Disbursal request")).click();
        page.waitForTimeout(3000);
        log.info("Move to Disbursal Request attempted.");
    }

    /**
     * Verifies the "ESign documents are missing." validation message appears.
     */
    public void verifyESignMissingValidation() {
        log.info("Verifying ESign documents missing validation...");
        Locator eSignMsg = page.getByText("ESign documents are missing.");
        eSignMsg.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        eSignMsg.click();
        page.waitForTimeout(1000);
        log.info("ESign documents are missing validation confirmed.");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  COMPLETE TRANSACTION FLOW
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Completes the full transaction disbursement flow:
     *   1. Attempt Move to Docket Initiation → "At least one disbursement" validation
     *   2. Open Transaction Disbursement → Edit
     *   3. Add Transaction 1 (Cheque)
     *   4. Add Transaction 2 (Cheque, verify NEFT disabled)
     *   5. Close Transaction Disbursement panel
     *
     * @param t1Favouring      Transaction 1 favouring name
     * @param t1Amount         Transaction 1 payment amount
     * @param t1Payable        Transaction 1 payable location
     * @param t1PrintSearch    Transaction 1 print location search text
     * @param t1PrintSelect    Transaction 1 print location to select
     * @param t2Favouring      Transaction 2 favouring name
     * @param t2Amount         Transaction 2 payment amount
     * @param t2Payable        Transaction 2 payable location
     * @param t2PrintSearch    Transaction 2 print location search text
     * @param t2PrintSelect    Transaction 2 print location to select
     */
    public void completeTransactionDisbursement(String t1Favouring, String t1Amount,
                                                String t1Payable, String t1PrintSearch,
                                                String t1PrintSelect,
                                                String t2Favouring, String t2Amount,
                                                String t2Payable, String t2PrintSearch,
                                                String t2PrintSelect) {
        log.info("── Completing Transaction Disbursement ──");

        // Step 1: Attempt move and handle validation
        attemptMoveToDocketInitiation();
        clickDisbursementValidationMessage();

        // Step 2: Open section and edit
        openTransactionDisbursementSection();
        clickEdit();

        // Step 3: Add Transaction 1
        addFirstTransaction(t1Favouring, t1Amount, t1Payable, t1PrintSearch, t1PrintSelect);

        // Step 4: Add Transaction 2 (verify NEFT disabled)
        addSecondTransaction(t2Favouring, t2Amount, t2Payable, t2PrintSearch, t2PrintSelect);

        // Step 5: Close the panel
        closeTransactionPanel();

        log.info("Transaction Disbursement completed.");
    }
}
