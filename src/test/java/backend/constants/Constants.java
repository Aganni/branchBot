package backend.constants;

public interface Constants {

    String UAT = "uat";
    String PAN_CARD = "pan_card";
    String CO_APPLICANT_PAN = "co_applicant_pan";
    String MOBILE_NUMBER = "mobile_number";
    String BUSINESS_NAME = "business_name";

    // Mystique Api keys
    String MYSTIQUE_BASE_URI = "mystiqueUri";
    String WHITELIST_PANCARD_IN_MYSTIQUE = "/addKyc?kycType=pancard";
    String KYC_TYPE = "kycType";
    String PANCARD = "pancard";

    // Nebula API keys
    String NEBULA_BASE_URI = "nebulaUri";
    String START_PROCESS_ENDPOINT = "/api/v1/UBL/%s/start-process/%s";

    // Lannister API keys
    String LANNISTER_BASE_URI = "lannisterUri";
    String REPAYMENT_DETAILS_ENDPOINT = "/api/v1/repaymentdetail/appForm/%s";

    // Shield API keys
    String SHIELD_BASE_URI = "shieldUri";
    String SHIELD_APPFORM_ENDPOINT = "/api/v1/appForm/%s";

    // Deathlok API keys (Sales Gating)
    String DEATHLOK_BASE_URI = "deathlokUri";
    String SALES_GATING_ADD_DATA_ENDPOINT = "/api/v1/addData";

}
