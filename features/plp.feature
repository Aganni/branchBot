@PLP @Regression
Feature: Secured Prime LAP End-to-End Workflow

  Scenario: Complete PLP flow from DSA PORTAL to Jarvis Welcome kit stage.
  # ── Data Initialization ──
    Given Initialize data for "normal" loan of "PLP"

  #   ── DSA Portal Flow ──
    When User logs into DSA Portal for Secured Loan and initiates a PLP Loan application
    Then User completes PLP Partner details
    And User completes adding PLP Lead Details
    Then User provides PLP customer consent via OTP verification
    And User completes PLP Primary Applicant details and proceeds
    And User adds Salaried Co-Applicant details and submits PLP Appform
    And User adds Entity Co-Applicant details and submits PLP Appform
    And User provides the consent for PLP Entity applicant
    #And User adds Aadhaar Co-Applicant details and submits PLP Appform
    #And User adds Driving License Co-Applicant details and submits PLP Appform
    #And User adds Passport Co-Applicant details and submits PLP Appform
    And User navigates to PLP Exposure dedupe and bureau output, downloads bureau report
    And User Adds the PLP property collateral details
    And User uploads all required PLP documents and proceeds
    And User completes PLP fee payment via sandbox payment gateway
    And User completes PLP final submission with email verification
