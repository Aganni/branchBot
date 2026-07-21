@LAP @Regression
Feature: Secured Loan Against Property loan End-to-End Workflow

  Scenario: Complete LAP flow till DSA Portal.
  # ── Data Initialization ──
    Given Initialize data for "normal" loan of "LAP"
  #   ── DSA Portal Flow ──
    When User logs into DSA Portal for Secured Loan and initiates a LAP Loan application
    Then User completes LAP Partner details
    And User completes adding LAP Lead Details
    Then User provides customer consent via OTP verification
    And User completes Primary Applicant details and proceeds
    And User adds Salaried Co-Applicant details and submits Appform
    And User adds Entity Co-Applicant details and submits Appform
    And User provides the consent for Entity applicant
    And User adds Aadhaar Co-Applicant details and submits Appform
    #And User adds Driving License Co-Applicant details and submits Appform
    #And User adds Passport Co-Applicant details and submits Appform
    And User navigates to Exposure dedupe and bureau output, downloads bureau report
    And User Adds the property collateral details
    And User uploads all required documents and proceeds
    And User completes fee payment via sandbox payment gateway
    And User completes final submission with email verification

  # ── Jarvis Portal Flow ──
  Scenario: Complete LAP flow till Welcome KIT through Jarvis portal.
    Given Initialize data for "normal" loan of "LAP"
    When User switches to Jarvis and moves the LAP application to CAM
    And User completes CAM stage primary applicant mandatory fields
    And User completes non-financial co-applicant mandatory fields
    And User completes financial co-applicant mandatory fields
    And User completes entity co-applicant mandatory fields and moves to Credit Review
    And User completes program validation and moves to Credit Review
    And User completes Credit Review stage and moves to Credit Approval
