@LAP @Regression
Feature: Secured Loan Against Property loan End-to-End Workflow

  Scenario: Complete LAP flow from DSA PORTAL to Jarvis Welcome kit stage.
  # ── Data Initialization ──
    Given Initialize data for "normal" loan of "LAP"
  #   ── DSA Portal Flow ──
    When User logs into DSA Portal for Secured Loan and initiates a LAP Loan application
    Then User completes LAP Partner details
    And User completes adding LAP Lead Details
    Then User provides customer consent via OTP verification
    And User completes Primary Applicant details and proceeds
    And User adds Salaried Co-Applicant details and submits Appform
    #And User adds Entity Co-Applicant details and submits Appform
    #And User provides the consent for Entity applicant
    #And User adds Driving License Co-Applicant details and submits Appform
    #And User adds Passport Co-Applicant details and submits Appform
    #And User adds Aadhaar Co-Applicant details and submits Appform
    #And User navigates to Exposure dedupe and bureau output, downloads bureau report
    #And User Adds the property collateral details
    #And User uploads all required documents and proceeds
    #And User completes fee payment via sandbox payment gateway
    #And User completes final submission with email verification
