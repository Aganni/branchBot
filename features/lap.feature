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
    #And User adds Driving License Co-Applicant details and submits Appform
    #And User adds Passport Co-Applicant details and submits Appform
    And User adds Entity Co-Applicant details and submits Appform
    Given User navigates to Google Groups and signs in
    When User opens the "notification test" group dashboard
    And User refreshes conversations and opens the verification email
    And User processes the verification link inside the email popup
    Then User navigates to the DSA Portal using the active Partner Loan ID
    And User clicks next to resume the application workflow
    And User adds Aadhaar Co-Applicant details and submits Appform
    #And User completes OTP consent verification for individual applicants
    And User navigates to Exposure dedupe and bureau output, downloads bureau report
    And User Adds the property collateral details
    And User uploads required verification documents and generates link
