package ui.stepDefinitions.dsa_secured;

import hooks.BaseTest;
import io.cucumber.java.en.And;
import ui.pages.dsa_secured.PostConsentFlowPage;
public class PostConsentFlowSteps extends BaseTest {

    @And("User navigates to Exposure dedupe and bureau output, downloads bureau report")
    public void processBureauAndDownload() throws Exception {
        PostConsentFlowPage flowPage = new PostConsentFlowPage(BaseTest.getPage());
        flowPage.movingToNextSatge();
        flowPage.downloadBureauReport();
        flowPage.BankStatement();
    }
}