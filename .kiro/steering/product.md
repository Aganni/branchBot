# Product Context — branchBot

## Overview

branchBot is an end-to-end UI test automation framework for **Credit Saison India's** loan origination platforms:

- **DSA Portal** — Partner-facing loan intake application (unsecured and secured flows)
- **Jarvis** — Internal loan management system for credit operations teams

The framework validates complete loan lifecycle workflows from application intake through disbursal.

## Loan Products Under Test

### UBL — Unsecured Business Loan

<!-- TODO: Add business rules, eligibility criteria, and loan parameters -->

- **Intake**: DSA Portal → Partner details → Business details → PAN verification → Eligibility → QDE → Consent → Banking → DDE → KYC
- **Processing (Jarvis)**: Login Desk → CAM → Bureau → Documents → Credit Review → Credit Approval → Terms → Sanction → QC → Approval → Disbursal

### LAP — Loan Against Property (Secured)

<!-- TODO: Add business rules, collateral requirements, and loan parameters -->

- **Intake**: DSA Portal → Partner details → Lead details → Customer consent → Primary Applicant → Co-Applicants → Dedupe/Bureau → Collateral → Documents → Fee payment → Final submission
- **Processing (Jarvis)**: <!-- TODO: Document Jarvis-side LAP workflow stages -->

## Target Users

<!-- TODO: Define the personas who use each portal -->

- **DSA Partners** — <!-- TODO: Describe partner role and responsibilities -->
- **Credit Operations Team** — <!-- TODO: Describe internal team workflow -->
- **QC/Compliance Team** — <!-- TODO: Describe review and approval responsibilities -->

## Key Business Flows

### Cross-Portal Session Handoff

Applications originate in the DSA Portal and transition to Jarvis for internal processing. The framework uses browser storage state persistence to simulate this handoff without re-authentication.

### Consent & Verification

<!-- TODO: Document consent mechanisms (OTP, SQS bypass, e-sign) and when each is used -->

### Bureau & Dedupe

<!-- TODO: Document bureau report types (Commercial CIBIL) and dedupe resolution rules -->

### Document Management

<!-- TODO: Document required document types per loan product and OSV marking rules -->

### Fee & Payment

<!-- TODO: Document fee structure and sandbox payment gateway integration -->

## Environments

| Environment | Purpose |
|---|---|
| INT (Integration) | <!-- TODO: Describe integration environment usage --> |
| UAT | <!-- TODO: Describe UAT environment and access requirements --> |

## Business Rules & Constraints

<!-- TODO: Add key business rules that tests validate -->
<!-- Examples: -->
<!-- - Minimum/maximum loan amounts per product -->
<!-- - Required applicant age ranges -->
<!-- - Mandatory document checklists -->
<!-- - SLA timelines for stage transitions -->
<!-- - Approval authority matrices -->

## Test Data Strategy

- YAML-based test data in `src/test/resources/testdata/{product}/normal.yaml`
- Dynamic data generation for PAN whitelisting, phone numbers, etc.
- Bureau report fixtures stored as HTML in `testdata/bureau-reports/`

## Success Criteria

<!-- TODO: Define what "passing" means for each flow -->
<!-- - UBL: Application reaches disbursal stage -->
<!-- - LAP: Application reaches welcome kit stage -->

## Known Limitations & Constraints

<!-- TODO: Document known flaky areas, environment dependencies, or timing issues -->
