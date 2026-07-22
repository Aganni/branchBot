package ui.pages.dsa;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hooks.BaseTest;

import java.util.Map;

public class SepCoApplicantPage extends BaseTest {

    private final Page page;

    public SepCoApplicantPage(Page page) {
        if (page == null) throw new IllegalArgumentException("Page instance cannot be null");
        this.page = page;
    }

    public void fillDoctorCoApplicantDetails(Map<String, String> data) {
        log.info("Filling SEP doctor co-applicant details");

        fillTextbox("Pan Number", data.get("pan"));
        clickButton("Verify");

        fillTextbox("Phone No.", data.get("phone"));
        fillTextbox("Email", data.get("email"));
        fillTextbox("NMC Registration Number", data.get("nmc_registration_number"));
        fillTextbox("Year of NMC Registration", data.get("year_of_nmc_registration"));

        selectDropdown("Qualification", data.get("qualification"));
        selectDropdown("Medical Council", data.get("medical_council"));

        fillTextbox("Years of Experience", data.get("years_of_experience"));

        clickButton("Save");
        log.info("Saved SEP co-applicant details");
    }

    public void clickSubmit() {
        page.locator("button:has-text('SUBMIT')").click();
        log.info("Clicked Submit on SEP Co-Applicant page");
    }

    // ── Private Helpers ──────────────────────────────────────────────────────

    private void fillTextbox(String name, String value) {
        if (value == null || value.isEmpty()) return;
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(name)).fill(value);
        log.info("Filled '{}': {}", name, value);
    }

    private void selectDropdown(String name, String value) {
        if (value == null || value.isEmpty()) return;
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name)).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(value)).click();
        log.info("Selected '{}': {}", name, value);
    }

    private void clickButton(String name) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name)).click();
    }
}
