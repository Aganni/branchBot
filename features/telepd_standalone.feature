@TelePD-standalone
Feature: Standalone BlackPanther Tele PD and PD Visit Test

  Scenario: Complete Tele PD and PD Visit on BlackPanther for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User completes standalone Tele PD for app "d6495f62-61ac-4bf9-96c9-ccfbdce8f4ee"
    And User completes PD Visit per-applicant with Jarvis uploads

  Scenario: Complete Property Visit on Jarvis for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User opens Jarvis and navigates to application "d6495f62-61ac-4bf9-96c9-ccfbdce8f4ee"
    And User completes Property Visit on Jarvis

  Scenario: Complete Sales Gating for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User completes Sales Gating for app "b36fcdd1-622b-4e01-b131-026475a4608d"

  Scenario: Complete Technical Vetting for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User opens Jarvis and navigates to application "b36fcdd1-622b-4e01-b131-026475a4608d"
    And User completes Technical Vetting for the application

  Scenario: Complete CR Mandatory Details and move to Terms for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User opens Jarvis and navigates to application "b36fcdd1-622b-4e01-b131-026475a4608d"
    And User fills mandatory details and moves to Credit Approval
    And User moves application from Credit Approval to Terms
    And User completes Fee Details for Docket Initiation
    And User completes Spread Rate for Docket Initiation
    And User completes Insurance Details for Docket Initiation
    And User changes user role to SALES for DOGH access
    And User triggers DOGH for insurance
    And User moves application to Docket Initiation

  Scenario: Complete Transaction Disbursement and Docket Initiation for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User opens Jarvis and navigates to application "5f0e29fc-827e-42a3-983b-fc53a25a32dc"
    And User completes Transaction Disbursement and moves to Docket Initiation
    And User attempts Move to Disbursal Request and verifies ESign validation

