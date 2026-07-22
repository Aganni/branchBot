package ui.stepDefinitions.dsa;

import backend.constants.Constants;
import data.TestDataProvider;
import hooks.BaseTest;
import io.cucumber.java.en.*;
import ui.pages.dsa.SepCoApplicantPage;

import java.util.Map;

import static dynamicData.DynamicDataClass.getValue;

public class SepCoApplicantSteps extends BaseTest {

    @And("User fills SEP doctor co-applicant details")
    public void fillSepDoctorCoApplicant() {
        Map<String, String> data = TestDataProvider.getMap("dsa.co_applicant");
        // Override PAN with the Mystique-generated co-applicant PAN
        data.put("pan", (String) getValue(Constants.CO_APPLICANT_PAN));
        // Override phone with the generated mobile number
        data.put("phone", (String) getValue(Constants.MOBILE_NUMBER));

        SepCoApplicantPage page = new SepCoApplicantPage(BaseTest.getPage());
        page.fillDoctorCoApplicantDetails(data);
        page.clickSubmit();
    }
}
