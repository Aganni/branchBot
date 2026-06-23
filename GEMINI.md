# branchBot - Jarvis & DSA Automation Framework

## Project Overview
branchBot is a specialized automation framework designed for end-to-end testing of the Jarvis and DSA (Direct Selling Agent) portals. It handles complex financial workflows, including loan application intake, eligibility checks, credit appraisal, and disbursal.

### Core Technologies
- **Language:** Java 21
- **UI Automation:** Playwright (v1.47+)
- **API Automation:** Rest-Assured
- **BDD Framework:** Cucumber (v7.14+)
- **Test Runner:** TestNG
- **State Management:** ThreadLocal-based session and data isolation
- **Data Serialization:** Jackson (YAML/JSON)

---

## 🏗 Architecture & Design Patterns

### 1. Page Object Model (POM)
- **BasePage (`src/test/java/core/BasePage.java`):** An abstract base class providing common UI utilities (navigation, waits, click/fill wrappers, Element UI dropdown handlers).
- **Page Objects:** Encapsulate locators and actions. New pages should extend `BasePage`.

### 2. ThreadLocal Lifecycle Management
- **BaseTest (`src/test/java/hooks/BaseTest.java`):** Manages `Playwright`, `Browser`, `BrowserContext`, and `Page` instances using `ThreadLocal`. This ensures complete isolation for parallel test execution.

### 3. Context Bridge (Portal Switching)
The framework supports seamless transitions between different portals (e.g., from DSA to Jarvis) without re-logging in where session sharing is applicable:
- `BaseTest.saveStorageState()`: Persists cookies and localStorage to a JSON file.
- `BaseTest.createContextFromState(path)`: Initializes a new context using the saved state.
- `BaseTest.switchToJarvisPortal()`: A high-level helper for the DSA -> Jarvis handoff.

### 4. Dynamic Data & Session Management
- **DynamicDataClass:** Provides a thread-safe map (`TestSessionData`) to store and retrieve data generated during a test run (e.g., `appFormId`, `partnerLoanId`).
- **TestDataProvider:** Loads static test data from YAML files located in `src/test/resources/testdata/`.

---

## 🚀 Building and Running

### Prerequisites
- **JDK 21**
- **Maven**

### Key Commands
```bash
# Clean and compile
mvn clean compile

# Run tests via Maven (Default env: int)
mvn test -Denv=uat -Dcucumber.filter.tags="@Regression"

# Run specific TestNG suite
mvn test -DsuiteXmlFile=branchBotTestNG.xml
```

---

## 🛠 Development Conventions

### 1. Creating New Page Objects
- Extend `BasePage`.
- Pass the `Page` instance via the constructor.
- Use private static final Strings for locators.
- Return `this` or the next Page Object to support method chaining where appropriate.

### 2. Writing Step Definitions
- Keep step definitions lean; delegate logic to Page Objects or Utils.
- Use `DynamicDataClass.setValue/getValue` for sharing state between steps.
- Leverage `BaseTest.getPage()` to access the current thread's Playwright page.

### 3. Configuration & Environments
- Properties are managed in `src/test/resources/properties/`.
- Use the `-Denv` flag to switch between `int` and `uat`.
- Credentials should be maintained in `*Credentials.properties` files (ensure these are not committed if they contain sensitive data).

### 4. Error Handling & Debugging
- Screenshots are automatically captured on failure via `CucumberHooks`.
- Logs are managed via Log4j2; check `target/logs/` or console output.
- Use `page.pause()` for interactive debugging during local runs.

---

## 📁 Directory Structure Highlights
- `features/`: Gherkin feature files.
- `src/test/java/ui/pages/`: Page Object implementations.
- `src/test/java/ui/stepDefinitions/`: UI-related step definitions.
- `src/test/java/backend/`: API utilities and step definitions.
- `src/test/java/hooks/`: Setup/Teardown logic.
- `src/test/resources/testdata/`: YAML-based test data configurations.
