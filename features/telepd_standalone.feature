@TelePD-standalone
Feature: Standalone BlackPanther Tele PD and PD Visit Test

  Scenario: Complete Tele PD and PD Visit on BlackPanther for a known application
    Given Initialize data for "normal" loan of "LAP"
    When User completes standalone Tele PD for app "00c123a6-95f6-4902-9b96-a130a7f7b364"
    And User completes PD Visit per-applicant with Jarvis uploads
