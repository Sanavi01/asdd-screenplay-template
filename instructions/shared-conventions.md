# Shared Conventions

Conventions that apply to both API and E2E automation in this template. All agents and all generated code must conform to these rules.

---

## 1. Package Structure

```
com.asdd.template
├── core
│   ├── hooks       — shared/abstract hooks
│   └── util        — shared constants
├── api
│   ├── hooks       — API test hooks
│   ├── model       — API request/response POJOs
│   ├── questions   — API Questions
│   ├── runners     — API JUnit runner
│   ├── stepdefinitions
│   ├── tasks       — API Tasks
│   └── util        — API-specific constants
└── e2e
    ├── hooks       — E2E test hooks
    ├── model       — E2E data models
    ├── pages       — Page Objects
    ├── questions   — E2E Questions
    ├── runners     — E2E JUnit runner
    ├── stepdefinitions
    ├── tasks       — E2E Tasks
    └── util        — E2E-specific constants
```

---

## 2. Class Naming

| Class Type | Convention | Examples |
|---|---|---|
| Task | `PascalCase VerbNoun` | `CreatePet`, `Login`, `NavigateTo`, `DeleteOrder` |
| Question | `PascalCase NounPhrase` | `ResponseStatusCode`, `CurrentUrl`, `PageTitle` |
| Page Object | `PascalCase + "Page"` | `LoginPage`, `DashboardPage`, `CheckoutPage` |
| Model/POJO | `PascalCase entity name` | `Pet`, `UserCredentials`, `ApiError` |
| Step Definitions | `PascalCase + "StepDefinitions"` | `PetStoreStepDefinitions`, `LoginStepDefinitions` |
| Runner | `PascalCase + "TestRunner"` | `ApiTestRunner`, `E2eTestRunner` |
| Hooks | `PascalCase + "Hooks"` | `ApiHooks`, `E2eHooks`, `BaseHooks` |
| Constants | `PascalCase + "Constants"` | `ApiConstants`, `E2eConstants`, `Constants` |

---

## 3. Method Naming

| Method Type | Convention | Examples |
|---|---|---|
| Static factory (Task) | Descriptive verb phrase | `with(pet)`, `withId(id)`, `forActor()` |
| Static factory (Question) | Noun phrase | `ofTheLastResponse()`, `ofTheBrowser()` |
| Step definition methods | `camelCase` matching step | `iCreateAPetWithName(...)` |
| Getters | `getFieldName()` | `getName()`, `getId()` |
| Builder setters | `setFieldName(T)` returning `this` | `setName(String name)` |

---

## 4. Field Naming

| Field Type | Convention | Example |
|---|---|---|
| Instance fields | `camelCase` | `private Pet currentPet;` |
| Constants | `UPPER_SNAKE_CASE` | `public static final String BASE_URL` |
| Task parameters | `final camelCase` | `private final long petId;` |
| Page targets | `UPPER_SNAKE_CASE` | `public static final Target LOGIN_BUTTON` |

---

## 5. Gherkin Tagging

| Tag | Usage |
|---|---|
| `@api` | All API scenarios |
| `@e2e` | All E2E scenarios |
| `@smoke` | Critical happy-path scenarios for smoke suite |
| `@regression` | Full regression scenarios |
| `@negative` | Negative/error case scenarios |
| `@wip` | Work in progress — not yet complete |
| Resource tags | Feature-specific (e.g., `@petstore`, `@login`) |

Run by tag:
```
./gradlew test -Dcucumber.filter.tags="@api and @smoke"
```

---

## 6. Import Rules

| What | Allowed Import |
|---|---|
| REST interactions | `net.serenitybdd.screenplay.rest.interactions.*` |
| REST ability | `net.serenitybdd.screenplay.rest.abilities.CallAnApi` |
| REST response | `net.serenitybdd.rest.SerenityRest` |
| WebDriver interactions | `net.serenitybdd.screenplay.actions.*` |
| WebDriver ability | `net.serenitybdd.screenplay.abilities.BrowseTheWeb` |
| Screenplay core | `net.serenitybdd.screenplay.*` |
| Cucumber annotations | `io.cucumber.java.en.*`, `io.cucumber.java.*` |
| JUnit Suite | `org.junit.platform.suite.api.*` |
| Assertions | `org.assertj.core.api.Assertions.*` |
| Logging | `org.slf4j.Logger`, `org.slf4j.LoggerFactory` |

Do NOT use:
- `junit.framework.*` (JUnit 4 legacy)
- `org.junit.Test` in step definitions
- Direct `org.openqa.selenium.*` in step definitions or tasks

---

## 7. Screenplay Responsibility Rules

| Class | Allowed | Forbidden |
|---|---|---|
| Task | `actor.attemptsTo(...)` | Assertions, `SerenityRest`, direct WebDriver |
| Question | Read-only state access | `actor.attemptsTo(...)`, assertions |
| Step Definition | Delegation to Tasks + Questions, assertions | Direct REST/WebDriver, `Thread.sleep` |
| Hook | Stage setup, ability assignment | Business logic, assertions |
| Page Object | `Target` definitions | Logic, Screenplay calls |
| Model | Data fields + getters/setters | Screenplay, test, or REST imports |

---

## 8. Code Style

- Indentation: 4 spaces (no tabs).
- Opening brace on same line.
- Blank line between methods.
- `final` for task constructor parameters.
- Prefer `List.of(...)` over `new ArrayList<>(Arrays.asList(...))`.
- Prefer method references over anonymous classes where readable.
- Do NOT add comments unless the code is non-obvious.
- `toString()` must be overridden on all model classes.

---

## 9. Feature File Conventions

- File name: `kebab-case.feature` (e.g., `petstore.feature`, `user-login.feature`).
- Feature title: `{Domain} - {Description}` (e.g., `Pet Store API - Pet Management`).
- Use `Background` for shared preconditions (max 3 steps).
- Use `Scenario Outline` + `Examples` for data-driven cases (min 2 rows to justify outline).
- All scenarios must have at least one `Then` assertion.
- No technical implementation details in step text.

---

## 10. Configuration Conventions

- Override base URLs via system properties: `-Dapi.base.url=https://...` or `-De2e.base.url=https://...`
- Environment-specific config in `serenity.conf` under `environments` block.
- Sensitive values (API keys, credentials) via system properties — never hardcoded.
