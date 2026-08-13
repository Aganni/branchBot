package backend.payload;

public class ApiPayload {

    public static String whitelistPanCardInMystique(String panCardValue, String panProfile) {

        return "{\n" +
                "    \"input\": {\n" +
                "        \"pan\": \"" + panCardValue + "\",\n" +
                "        \"consent\": \"Y\"\n" +
                "    },\n" +
                "    \"output\": {\n" +
                "        \"requestId\": \"d041f16d-902f-4ced-a365-427de20f2e78\",\n" +
                "        \"result\": {\n" +
                "            \"pan\": \"" + panCardValue + "\",\n" +
                "            \"name\": \""+panProfile+"\",\n" +
                "            \"firstName\": \"Shea\",\n" +
                "            \"middleName\": \"\",\n" +
                "            \"lastName\": \"Test\",\n" +
                "            \"gender\": \"F\",\n" +
                "            \"aadhaarLinked\": true,\n" +
                "            \"aadhaarMatch\": true,\n" +
                "            \"dob\": \"2004-02-23\",\n" +
                "            \"address\": {\n" +
                "                \"buildingName\": \"2/21 RAM WADI\",\n" +
                "                \"locality\": \"JOGESHWARI SOUTH\",\n" +
                "                \"streetName\": \"AGARWAL NGR CAVES RD\",\n" +
                "                \"pinCode\": \"400060\",\n" +
                "                \"city\": \"MUMBAI\",\n" +
                "                \"state\": \"MAHARASHTRA\",\n" +
                "                \"country\": \"\"\n" +
                "            },\n" +
                "            \"mobileNo\": null,\n" +
                "            \"emailId\": null,\n" +
                "            \"profileMatch\": [\n" +
                "                {\n" +
                "                    \"parameter\": \"address\",\n" +
                "                    \"matchScore\": 0.99,\n" +
                "                    \"matchResult\": true\n" +
                "                },\n" +
                "                {\n" +
                "                    \"parameter\": \"name\",\n" +
                "                    \"matchScore\": 1,\n" +
                "                    \"matchResult\": true\n" +
                "                },\n" +
                "                {\n" +
                "                    \"parameter\": \"dob\",\n" +
                "                    \"matchScore\": 1,\n" +
                "                    \"matchResult\": true\n" +
                "                }\n" +
                "            ],\n" +
                "            \"authorizedSignatory\": {\n" +
                "                \"pan\": \"\",\n" +
                "                \"name\": \"\",\n" +
                "                \"firstName\": \"\",\n" +
                "                \"middleName\": \"\",\n" +
                "                \"lastName\": \"\",\n" +
                "                \"gender\": \"\",\n" +
                "                \"aadhaarLinked\": null,\n" +
                "                \"dob\": \"\",\n" +
                "                \"mobileNo\": \"\",\n" +
                "                \"emailId\": \"\",\n" +
                "                \"address\": {\n" +
                "                    \"buildingName\": \"\",\n" +
                "                    \"locality\": \"\",\n" +
                "                    \"streetName\": \"\",\n" +
                "                    \"pinCode\": \"\",\n" +
                "                    \"city\": \"\",\n" +
                "                    \"state\": \"\",\n" +
                "                    \"country\": \"\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"status\": \"Active\",\n" +
                "            \"issueDate\": \"2016-06-28\",\n" +
                "            \"isSalaried\": true,\n" +
                "            \"isDirector\": false,\n" +
                "            \"isSoleProp\": false\n" +
                "        },\n" +
                "        \"statusCode\": 101\n" +
                "    }\n" +
                "}";
    }

    public static String getRepaymentDetailsPayload(String appFormId) {
        return "{\n" +
                "    \"appFormId\": \"" + appFormId + "\",\n" +
                "    \"repaymentMode\": \"NACH\",\n" +
                "    \"spdcNumber\": \"838384\",\n" +
                "    \"micrCode\": \"123456789\",\n" +
                "    \"repaymentCheques\": [\n" +
                "        {\n" +
                "            \"startChequeNumber\": \"886\",\n" +
                "            \"endChequeNumber\": \"889\",\n" +
                "            \"numberOfCheque\": \"2\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"totalPDCCheck\": 2,\n" +
                "    \"totalSPDCCheck\": 1,\n" +
                "    \"totalNumberOfCheque\": 0\n" +
                "}";
    }

    /**
     * Builds the Sales Gating (Deathlok addData) payload for a single applicant.
     *
     * @param appFormId     the appForm UUID
     * @param applicantId   the linkedIndividual ID from Shield
     * @param entityName    name of the applicant
     * @param applicantType e.g. "LinkedIndividual"
     */
    public static String getSalesGatingPayload(String appFormId, String applicantId, String entityName, String applicantType) {
        return "{\n" +
                "    \"userType\": \"SALES\",\n" +
                "    \"appFormId\": \"" + appFormId + "\",\n" +
                "    \"applicantId\": \"" + applicantId + "\",\n" +
                "    \"entityName\": \"" + entityName + "\",\n" +
                "    \"employmentType\": \"SALARIED\",\n" +
                "    \"applicantType\": \"" + applicantType + "\",\n" +
                "    \"status\": \"COMPLETED\",\n" +
                "    \"doneBy\": \"Automation\",\n" +
                "    \"documents\": [],\n" +
                "    \"visitType\": \"Residential\",\n" +
                "    \"address\": {\n" +
                "        \"CURRES\": \"2/21 RAM WADI JOGESHWARI EAST, AGARWAL NGR CAVES RD, 400060, MMB, MH\",\n" +
                "        \"PER\": \"2/21 RAM WADI JOGESHWARI EAST, AGARWAL NGR CAVES RD, 400060, MMB, MH\"\n" +
                "    },\n" +
                "    \"basicDetail\": {\n" +
                "        \"name\": \"Automation\"\n" +
                "    },\n" +
                "    \"recommendation\": \"Positive\",\n" +
                "    \"remarks\": \"Sales gating completed via automation\"\n" +
                "}";
    }
}
