# Technology Stack — branchBot

## Language & Runtime

- **Java 21** — Leverages modern features (records, switch expressions, sealed classes)
- **Maven** — Build tool, dependency management, and test lifecycle (`pom.xml`)

## Browser Automation

- **Playwright for Java 1.47** — Chromium-based browser automation
  - Headless mode configurable; defaults to headed for local dev
  - Video recording to `target/videos/`
  - Storage state persistence for cross-portal session handoff

## Test Frameworks

- **Cucumber 7.14** — BDD test definitions in Gherkin (`.feature` files in `features/`)
  - `cucumber-java` for step definitions
  - `cucumber-testng` for TestNG integration
- **TestNG 7.5** — Test runner with parallel execution support
  - Suite config in `branchBotTestNG.xml`
  - `parallel="methods"` with configurable `thread-count`

## Reporting

- **ExtentReports 5.1.1** — Rich HTML test reports
- **extentreports-cucumber7-adapter 1.14.0** — Bridges Cucumber lifecycle into Extent

## API & Backend Testing

- **Rest-Assured 5.4** — REST API interaction and assertions
- **Spring Web 6.1.8** — HTTP utilities (RestTemplate, URI builders)
- **AWS SDK v2 (SQS 2.24.0)** — Queue interaction for async test workflows (consent bypass, etc.)

## Data & Serialization

- **Jackson 2.19** — JSON and YAML parsing (`jackson-databind`, `jackson-dataformat-yaml`)
- **JSON Simple 1.1.1** — Lightweight JSON handling
- **JsonPath 2.8.0** — JSON path queries on API responses

## Utilities

- **Lombok 1.18.38** — Boilerplate reduction (getters, builders, logging)
- **Awaitility 4.2.1** — Polling-based async wait conditions in tests
- **Log4j2 2.23.1** — Structured logging (config in `src/test/resources/log4j2.xml`)
- **SLF4J 2.0.13 + Logback 1.5.6** — Logging facade

## Architecture & Patterns

- **Page Object Model (POM)** — Each page/portal screen is a dedicated class under `ui/pages/`
  - `BasePage` abstract class provides shared interaction utilities (click, fill, dropdown, screenshot)
  - Constructor-based dependency injection of Playwright `Page` instance
- **ThreadLocal State Management** — `BaseTest` wraps Playwright lifecycle in `ThreadLocal` for parallel safety
- **Context Bridge** — Storage state saved/loaded to switch between DSA Portal and Jarvis without re-authentication
- **Step Definitions** — Organized by portal: `dsa/`, `dsa_secured/`, `jarvis/`, `jarvis_Secure/`
- **Backend utilities** — `backend/` package for API helpers, SQS utilities, payload builders

## Project Layout

```
branchBot/
├── features/               # Gherkin feature files (ubl.feature, lap.feature)
├── src/test/java/
│   ├── hooks/              # BaseTest (Playwright lifecycle), CucumberHooks
│   ├── core/               # BasePage (abstract page utilities)
│   ├── ui/
│   │   ├── pages/          # Page Objects (jarvis/, dsa/, dsa_secured/)
│   │   ├── stepDefinitions/ # Cucumber step defs by portal
│   │   ├── Utils/          # UI utilities, screenshot helper
│   │   └── constants/      # UI constants
│   ├── backend/            # API/SQS utilities, step definitions, payloads
│   ├── data/               # TestDataProvider, DataInitializationSteps
│   ├── dynamicData/        # Runtime test session data
│   └── testRunner/         # Cucumber runner classes (UBL, LAP)
├── src/test/resources/
│   ├── properties/         # Environment configs (int.properties, uat.properties, credentials)
│   ├── testdata/           # YAML test data (UBL/normal.yaml, LAP/normal.yaml), fixtures
│   └── log4j2.xml
├── branchBotTestNG.xml     # TestNG suite definition
└── pom.xml                 # Maven project descriptor
```

## Environment Configuration

- Environments: `int` (integration), `uat`
- Selected via `-Denv=<env>` system property (defaults to `int`)
- Properties files: `src/test/resources/properties/{env}.properties` and `{env}Credentials.properties`
- URLs, credentials, and API keys loaded at runtime from properties

## Build & Run

```bash
# Compile
mvn clean compile

# Run full suite
mvn test

# Run with specific environment
mvn test -Denv=uat
```
