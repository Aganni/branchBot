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

        // PAN Number
        if (data.containsKey("pan")) {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Pan Number")).fill(data.get("pan"));
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).click();
            log.info("Entered PAN: {} and clicked Verify", data.get("pan"));
        }

        // Phone
        if (data.containsKey("phone")) {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone No.")).fill(data.get("phone"));
            log.info("Entered Phone: {}", data.get("phone"));
        }

        // Email
        if (data.containsKey("email")) {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email")).fill(data.get("email"));
            log.info("Entered Email: {}", data.get("email"));
        }

        // NMC Registration Number
        if (data.containsKey("nmc_registration_number")) {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("NMC Registration Number")).fill(data.get("nmc_registration_number"));
            log.info("Entered NMC Registration Number: {}", data.get("nmc_registration_number"));
        }

        // Year of NMC Registration
        if (data.containsKey("year_of_nmc_registration")) {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Year of NMC Registration")).fill(data.get("year_of_nmc_registration"));
            log.info("Entered Year of NMC Registration: {}", data.get("year_of_nmc_registration"));
        }

        // Qualification
        if (data.containsKey("qualification")) {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Qualification")).click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("qualification"))).click();
            log.info("Selected Qualification: {}", data.get("qualification"));
        }

        // Medical Council
        if (data.containsKey("medical_council")) {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Medical Council")).click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(data.get("medical_council"))).click();
            log.info("Selected Medical Council: {}", data.get("medical_council"));
        }

        // Years of Experience
        if (data.containsKey("years_of_experience")) {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Years of Experience")).fill(data.get("years_of_experience"));
            log.info("Entered Years of Experience: {}", data.get("years_of_experience"));
        }

        // Save
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        log.info("Clicked Save on SEP Co-Applicant page");
    }

    public void clickSubmit() {
        page.locator("button:has-text('SUBMIT')").click();
        log.info("Clicked Submit on SEP Co-Applicant page");
    }
}
