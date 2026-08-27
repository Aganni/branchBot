package ui.stepDefinitions.jarvis_Secure;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.jarvis_secured.CreditRoleAssignmentPage;
import ui.pages.jarvis_secured.TransactionDisbursementPage;

import java.util.Arrays;
import java.util.List;

/**
 * Step definitions for the Tranche / Transaction Disbursement flow in Jarvis.
 * Single step covers:
 *   1. Transaction Disbursement — Add 2 tranches (Cheque mode, verify NEFT disabled on 2nd)
 *   2. Attempt Move to Docket Initiation — triggers "Unable to initiate credit" error
 *   3. Admin Portal — Change user role to CREDIT with product code, stages, hierarchy mapping
 *   4. Final Move to Docket Initiation after role change
 */
public class TrancheDisbursementSteps extends BaseTest {

    private static final String TD = "dsa_secured.jarvis_secured.transaction_disbursement.";

    @And("User completes Transaction Disbursement and moves to Docket Initiation")
    public void completeTransactionDisbursementAndMoveToDocketInitiation() {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  Tranche — Full Transaction Disbursement → Docket Initiation");
        log.info("═══════════════════════════════════════════════════════════════");

        Page jarvisPage = BaseTest.getPage();
        TransactionDisbursementPage tranchePage = new TransactionDisbursementPage(jarvisPage);

        // ──────────────────────────────────────────────────────────────────────
        //  PHASE 1: Add 2 Transactions
        // ──────────────────────────────────────────────────────────────────────
        log.info("── Phase 1: Transaction Disbursement ──");

        // Load transaction data from YAML
        String t1Favouring = TestDataProvider.get(TD + "transaction_1.favouring_name");
        String t1Amount = TestDataProvider.get(TD + "transaction_1.payment_amount");
        String t1Payable = TestDataProvider.get(TD + "transaction_1.payable_location");
        String t1PrintSearch = TestDataProvider.get(TD + "transaction_1.print_location_search");
        String t1PrintSelect = TestDataProvider.get(TD + "transaction_1.print_location_select");

        String t2Favouring = TestDataProvider.get(TD + "transaction_2.favouring_name");
        String t2Amount = TestDataProvider.get(TD + "transaction_2.payment_amount");
        String t2Payable = TestDataProvider.get(TD + "transaction_2.payable_location");
        String t2PrintSearch = TestDataProvider.get(TD + "transaction_2.print_location_search");
        String t2PrintSelect = TestDataProvider.get(TD + "transaction_2.print_location_select");

        log.info("Transaction 1: favouring={}, amount={}", t1Favouring, t1Amount);
        log.info("Transaction 2: favouring={}, amount={}", t2Favouring, t2Amount);

        // Complete the transaction disbursement (adds both tranches + closes panel)
        tranchePage.completeTransactionDisbursement(
                t1Favouring, t1Amount, t1Payable, t1PrintSearch, t1PrintSelect,
                t2Favouring, t2Amount, t2Payable, t2PrintSearch, t2PrintSelect);

        log.info("Phase 1 complete: Both transactions added.");

        // ──────────────────────────────────────────────────────────────────────
        //  PHASE 2: Attempt Move → "Unable to initiate credit" error
        // ──────────────────────────────────────────────────────────────────────
        log.info("── Phase 2: Attempt Move to Docket Initiation (expect credit error) ──");

        tranchePage.attemptMoveToDocketInitiationAfterTransactions();
        tranchePage.clickUnableToInitiateCreditError();

        log.info("Phase 2 complete: 'Unable to initiate credit' error handled.");

        // ──────────────────────────────────────────────────────────────────────
        //  PHASE 3: Admin Portal — CREDIT Role + Hierarchy Mapping
        // ──────────────────────────────────────────────────────────────────────
        log.info("── Phase 3: Admin Portal — Credit Role Assignment ──");

        String userSearchText = TestDataProvider.get(TD + "admin_portal.user_search_text");
        String department = TestDataProvider.get(TD + "admin_portal.department");
        String productCodeSearch = TestDataProvider.get(TD + "admin_portal.product_code_search");
        String productCode = TestDataProvider.get(TD + "admin_portal.product_code");
        String designation = TestDataProvider.get(TD + "admin_portal.designation");
        String approvalAmount = TestDataProvider.get(TD + "admin_portal.approval_amount");
        String subProduct = TestDataProvider.get(TD + "admin_portal.sub_product");

        List<String> stages = Arrays.asList(
                TestDataProvider.get(TD + "admin_portal.stage_1"),
                TestDataProvider.get(TD + "admin_portal.stage_2"),
                TestDataProvider.get(TD + "admin_portal.stage_3"),
                TestDataProvider.get(TD + "admin_portal.stage_4"),
                TestDataProvider.get(TD + "admin_portal.stage_5"),
                TestDataProvider.get(TD + "admin_portal.stage_6")
        );

        String hierarchyDept = TestDataProvider.get(TD + "admin_portal.hierarchy_department");
        String hierarchyLpc = TestDataProvider.get(TD + "admin_portal.hierarchy_lpc");
        String hierarchyUserSearch = TestDataProvider.get(TD + "admin_portal.hierarchy_user_search");
        String hierarchyUser = TestDataProvider.get(TD + "admin_portal.hierarchy_user");
        String hierarchyMgrSearch = TestDataProvider.get(TD + "admin_portal.hierarchy_manager_search");
        String hierarchyManager = TestDataProvider.get(TD + "admin_portal.hierarchy_manager");

        // Open Admin Portal in a new tab
        BrowserContext context = jarvisPage.context();
        Page adminPage = context.newPage();
        CreditRoleAssignmentPage creditRolePage = new CreditRoleAssignmentPage(adminPage);

        creditRolePage.completeCreditRoleAssignment(
                userSearchText, department, productCodeSearch, productCode, designation,
                approvalAmount, stages, subProduct,
                hierarchyDept, hierarchyLpc, hierarchyUserSearch,
                hierarchyUser, hierarchyMgrSearch, hierarchyManager);

        // Close admin tab and switch back to main Jarvis page
        adminPage.close();
        jarvisPage.bringToFront();
        jarvisPage.waitForTimeout(1000);

        log.info("Phase 3 complete: Credit role assigned and hierarchy mapped.");

        // ──────────────────────────────────────────────────────────────────────
        //  PHASE 4: Final Move to Docket Initiation
        // ──────────────────────────────────────────────────────────────────────
        log.info("── Phase 4: Final Move to Docket Initiation ──");

        tranchePage.dismissDrawerOverlay();
        tranchePage.finalMoveToDocketInitiation();

        log.info("Phase 4 complete: Application moved to Docket Initiation.");
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  Tranche Disbursement → Docket Initiation COMPLETE");
        log.info("═══════════════════════════════════════════════════════════════");
    }

    @And("User attempts Move to Disbursal Request and verifies ESign validation")
    public void attemptMoveToDisbursal() {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("  Tranche — Move to Disbursal Request (ESign Validation)");
        log.info("═══════════════════════════════════════════════════════════════");

        Page jarvisPage = BaseTest.getPage();
        TransactionDisbursementPage tranchePage = new TransactionDisbursementPage(jarvisPage);

        // Reload to reflect Docket Initiation stage
        jarvisPage.reload();
        jarvisPage.waitForTimeout(5000);

        // Attempt move to Disbursal Request
        tranchePage.attemptMoveToDisbursal();
        tranchePage.verifyESignMissingValidation();

        log.info("ESign documents missing validation verified. Disbursal Request blocked as expected.");
    }
}
