package ui.pages.jarvis_secured;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import hooks.BaseTest;
import java.util.regex.Pattern;
public class CollateralDetailsPage extends BaseTest {
    private final Page page;
    public CollateralDetailsPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    // FILL COLLATERAL DETAILS
    public void fillCollateralDetails() {
        // Scroll to the Collateral Details section
        Locator collateralSection = page.locator("[id='Collateral Details'], #Collateral\\ Details").first();
        collateralSection.scrollIntoViewIfNeeded();
        page.waitForTimeout(2000);
        // Click the section button to expand it
        collateralSection.locator("button.appform-card").first().click();
        page.waitForTimeout(3000);
        // Mortgage Type - click the 3rd Select placeholder dropdown
        page.locator("xpath=(//input[@placeholder='Select'])[3]").click();
        page.waitForTimeout(500);
        page.getByText("Equitable Mortgage").click();
        page.waitForTimeout(500);
        // Seller Name
        Locator sellerNameField = page.locator("xpath=//div[@class='el-col el-col-24']//input[@type='text']");
        sellerNameField.scrollIntoViewIfNeeded();
        page.waitForTimeout(500);
        sellerNameField.click();
        page.waitForTimeout(500);
        sellerNameField.fill("");
        page.keyboard().type("Sadie Luke");
        page.waitForTimeout(500);
        // Construction Completion Year
        page.getByPlaceholder("Enter the Construction Completion Year").click();
        page.getByPlaceholder("Enter the Construction Completion Year").fill("2002");
        page.waitForTimeout(500);
        // Built-up area
        page.locator("div:nth-child(14) > div > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").first().click();
        page.locator("div:nth-child(14) > div > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").first().fill("1200");
        page.waitForTimeout(500);
        // Cost of construction per sqFt/Mtr
        page.getByPlaceholder("Cost of const per sqFt/Mtr").click();
        page.getByPlaceholder("Cost of const per sqFt/Mtr").fill("1200");
        page.waitForTimeout(500);
        // Primary agency
        page.locator("div:nth-child(16) > div > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").first().click();
        page.locator("div:nth-child(16) > div > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").first().fill("primary agecny 1");
        page.waitForTimeout(500);
        // Primary valuation amount
        page.locator("div:nth-child(16) > div:nth-child(2) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").click();
        page.locator("div:nth-child(16) > div:nth-child(2) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").fill("102000000");
        page.waitForTimeout(500);
        // Secondary valuation amount
        page.locator("div:nth-child(18) > div:nth-child(2) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").click();
        page.locator("div:nth-child(18) > div:nth-child(2) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").fill("102000000");
        page.waitForTimeout(500);
        // Nature of property
        page.locator("div:nth-child(20) > div > .el-form-item > .el-form-item__content > .el-select > .el-input > .el-input__inner").first().click();
        page.locator("li").filter(new Locator.FilterOptions().setHasText("Land & Buildings")).click();
        page.waitForTimeout(500);
        // Charge type
        page.locator("div:nth-child(20) > div:nth-child(2) > .el-form-item > .el-form-item__content > .el-select > .el-input > .el-input__inner").click();
        page.getByText("Primary - First Charge").click();
        page.waitForTimeout(500);
        // CERSAI match status
        page.locator("div:nth-child(21) > div > .el-form-item > .el-form-item__content > .el-select > .el-input > .el-input__inner").first().click();
        page.getByText("Match Found", new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        // Property usage
        page.locator("div:nth-child(21) > div:nth-child(2) > .el-form-item > .el-form-item__content > .el-select > .el-input > .el-input__inner").click();
        page.getByText("Civic", new Page.GetByTextOptions().setExact(true)).click();
        page.waitForTimeout(500);

        // Shop number
        page.locator("div:nth-child(24) > div:nth-child(3) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").click();
        page.locator("div:nth-child(24) > div:nth-child(3) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").fill("shop numer");
        page.waitForTimeout(500);

        // Building
        page.locator("div:nth-child(24) > div:nth-child(4) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").click();
        page.locator("div:nth-child(24) > div:nth-child(4) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").fill("building");
        page.waitForTimeout(500);

        // Pincode
        page.locator("div:nth-child(7) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").click();
        page.locator("div:nth-child(7) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").fill("560093");
        page.waitForTimeout(500);

        // Survey number
        page.locator("div:nth-child(8) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").click();
        page.locator("div:nth-child(8) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").fill("12/34");
        page.waitForTimeout(500);

        // Village/Locality
        page.locator("div:nth-child(11) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").click();
        page.locator("div:nth-child(11) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").fill("huddi");
        page.waitForTimeout(500);

        // District
        page.getByPlaceholder("Enter District").click();
        page.getByPlaceholder("Enter District").fill("bangalore");
        page.waitForTimeout(500);

        // Submit collateral
        page.getByLabel("Collateral").getByText("Submit Arrow Right icon").click();
        page.waitForTimeout(3000);

        // Reload to clear notification and refresh the page
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        log.info("Collateral Details filled and submitted.");
    }

    // SECONDARY COLLATERAL VALIDATION
    // Fills secondary validation fields when "Sanction loan amount" error appears.
    public void fillSecondaryCollateralValidation() {
        log.info("Filling secondary collateral validation fields...");

        // Open Collateral Details section
        Locator collateralSection = page.locator("[id='Collateral Details'], #Collateral\\ Details").first();
        collateralSection.scrollIntoViewIfNeeded();
        page.waitForTimeout(2000);
        collateralSection.locator("button.appform-card").first().click();
        page.waitForTimeout(3000);

        // Secondary agency name
        page.locator("div:nth-child(17) > div > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").first().click();
        page.locator("div:nth-child(17) > div > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").first().fill("secondary validation");
        page.waitForTimeout(500);

        // Secondary valuation amount
        page.locator("div:nth-child(17) > div:nth-child(2) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").click();
        page.locator("div:nth-child(17) > div:nth-child(2) > .el-form-item > .el-form-item__content > .el-input > .el-input__inner").fill("256800000");
        page.waitForTimeout(500);

        // Submit collateral again
        page.getByLabel("Collateral").getByText("Submit Arrow Right icon").click();
        page.waitForTimeout(5000);

        // Reload to clear notifications
        page.reload();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        log.info("Secondary collateral validation fields filled and submitted.");
    }
}
