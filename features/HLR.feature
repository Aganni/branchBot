@HLR @Regression
Feature: Secured Home Loan Retail End-to-End Workflow

  Scenario: Complete HLR flow till DSA Portal.
  # ── Data Initialization ──
    Given Initialize data for "normal" loan of "HLR"
  #   ── DSA Portal Flow ──
    When User logs into DSA Portal for Secured Loan and initiates a HLR Loan application
    Then User completes HLR Partner details
    And User completes adding HLR Lead Details
    Then HLR User provides customer consent via OTP verification
    And HLR User completes Primary Applicant details and proceeds
    And HLR User adds Salaried Co-Applicant details and submits Appform
    #And HLR User adds Aadhaar Co-Applicant details and submits Appform
    And HLR User adds Entity Co-Applicant details and submits Appform
    And HLR User provides the consent for Entity applicant
    #And HLR User adds Driving License Co-Applicant details and submits Appform
    #And HLR User adds Passport Co-Applicant details and submits Appform
    And HLR User navigates to Exposure dedupe and bureau output, downloads bureau report
    And HLR User Adds the property collateral details
    And HLR User uploads all required documents and proceeds
    And HLR User completes fee payment via sandbox payment gateway
    And HLR User completes final submission with email verification

  # ── Jarvis Portal Flow ──
  Scenario: Complete HLR flow till Welcome KIT through Jarvis portal.
    Given Initialize data for "normal" loan of "HLR"
    When User switches to Jarvis and moves the HLR application to CAM
    And HLR User completes CAM stage primary applicant mandatory fields
    And HLR User completes non-financial co-applicant mandatory fields
    And HLR User completes financial co-applicant mandatory fields
    And HLR User completes entity co-applicant mandatory fields and moves to Credit Review
    And HLR User resolves KYC verification and reassigns application in Credit Review
    And HLR User fills collateral details in Credit Review
    And HLR User fills loan requirements and moves to Credit Approval
    And HLR User completes Tele PD Visit for all applicants
    And HLR User completes Sales Gating for the application
    And HLR User completes Technical Vetting for the application
    And HLR User fills mandatory details and moves to Credit Approval
    And HLR User moves application from Credit Approval to Terms
    And HLR User completes Fee Details for Docket Initiation
    And HLR User completes Spread Rate for Docket Initiation
    And HLR User completes Insurance Details for Docket Initiation
    And HLR User changes user role to SALES for DOGH access
    And HLR User triggers DOGH for insurance
    And HLR User moves application to Docket Initiation