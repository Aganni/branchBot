@TelePD-standalone
Feature: Standalone BlackPanther Tele PD and PD Visit Test

  Scenario: Complete Tele PD and PD Visit on BlackPanther for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User completes standalone Tele PD for app "e1cb1f73-1db5-43f8-9b50-1a6064e2bebf"
    And User completes PD Visit per-applicant with Jarvis uploads

  Scenario: Complete Property Visit on Jarvis for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User opens Jarvis and navigates to application "612ad8de-ed98-4cb3-8bd1-f8e70083b8c5"
    And User completes Property Visit on Jarvis

  Scenario: Complete Sales Gating for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User completes Sales Gating for app "78d8354a-1a07-41ee-afe6-2fe613fe76fb"

  Scenario: Complete Technical Vetting for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User opens Jarvis and navigates to application "78d8354a-1a07-41ee-afe6-2fe613fe76fb"
    And User completes Technical Vetting for the application

  Scenario: Complete CR Mandatory Details and move to Terms for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User opens Jarvis and navigates to application "78d8354a-1a07-41ee-afe6-2fe613fe76fb"
    And User fills mandatory details and moves to Credit Approval
    And User moves application from Credit Approval to Terms
