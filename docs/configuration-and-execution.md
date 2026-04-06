# Configuration and Execution

How to configure environments, run tests, and interpret reports.

---

## Prerequisites

| Tool | Minimum Version | Notes |
|---|---|---|
| Java | 17 | Set `JAVA_HOME` to JDK 17 |
| Gradle | 8.5 | Use the included `gradlew` wrapper |
| Chrome | Latest stable | Required for E2E tests |
| ChromeDriver | Must match Chrome version | Auto-managed by Serenity WebDriver manager |

---

## Initial Setup

```bash
# Clone the repository
git clone <repo-url>
cd asdd-screenplay-template

# Verify Java version
java -version   # Must be 17+

# Run a build to download dependencies
./gradlew compileTestJava
```

---

## Configuration Files

### `serenity.conf`

Located at `src/test/resources/serenity.conf`.

```hocon
serenity {
  project.name = "ASDD Screenplay Template"
  test.root = "com.asdd.template"

  webdriver {
    driver = chrome
    auto.manage.downloads = true
  }
}

environments {
  default {
    webdriver.base.url = "https://the-internet.herokuapp.com"
    api.base.url = "https://petstore.swagger.io/v2"
  }
}
```

**Key properties**:
| Property | Description |
|---|---|
| `serenity.project.name` | Appears in the HTML report header |
| `serenity.test.root` | Base package for test discovery |
| `serenity.webdriver.driver` | Browser: `chrome`, `firefox`, `edge` |
| `webdriver.base.url` | Default URL for `Open.url(url)` in E2E tasks |
| `api.base.url` | Default REST base URL |

---

### `ApiConstants.java`

Override the API base URL at runtime:
```bash
./gradlew test -Dapi.base.url=https://my-staging-api.example.com
```

### `E2eConstants.java`

Override the E2E base URL at runtime:
```bash
./gradlew test -De2e.base.url=https://staging.myapp.example.com
```

---

## Running Tests

### Run all tests
```bash
./gradlew test
```

### Run only API tests
```bash
./gradlew test -Dcucumber.filter.tags="@api"
```

### Run only E2E tests
```bash
./gradlew test -Dcucumber.filter.tags="@e2e"
```

### Run smoke tests only
```bash
./gradlew test -Dcucumber.filter.tags="@smoke"
```

### Run a specific feature tag
```bash
./gradlew test -Dcucumber.filter.tags="@petstore"
./gradlew test -Dcucumber.filter.tags="@login"
```

### Run with a custom base URL
```bash
./gradlew test -Dcucumber.filter.tags="@api" -Dapi.base.url=https://my-api.example.com
```

### Run in headless mode (CI)
```bash
./gradlew test -Dwebdriver.chrome.arguments="--headless,--no-sandbox,--disable-dev-shm-usage"
```

---

## Test Reports

After a test run, Serenity generates reports in `build/site/serenity/`.

### Opening the Report
```bash
# The aggregate task runs automatically after test
# Open the report directly
open build/site/serenity/index.html   # macOS
xdg-open build/site/serenity/index.html  # Linux
start build/site/serenity/index.html  # Windows
```

### Report Contents

| Section | Description |
|---|---|
| Test Results | Pass/fail/skip counts and percentages |
| Scenario Results | Per-scenario results with steps expanded |
| Requirements Coverage | Requirements vs. test mapping |
| History | Trend charts across multiple runs |
| Features | Breakdown by feature tag |

### Configured Report Type

In `build.gradle`:
```groovy
serenity {
    reports = ['single-page-html']
}
```

Change to `['html', 'json', 'junit']` for additional formats.

---

## CI/CD Integration

### GitHub Actions Example

```yaml
name: Test Suite

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Install Chrome
        uses: browser-actions/setup-chrome@latest

      - name: Run API tests
        run: ./gradlew test -Dcucumber.filter.tags="@api"

      - name: Upload Serenity Report
        uses: actions/upload-artifact@v4
        with:
          name: serenity-report
          path: build/site/serenity/
```

---

## Parallel Execution

Cucumber parallel execution is supported via the JUnit Platform Suite runner. Enable it in `serenity.conf`:

```hocon
serenity {
  parallel {
    batches = 4
  }
}
```

Or via Gradle:
```groovy
test {
    maxParallelForks = 4
}
```

**Note**: Ensure step definition classes use instance fields (not static) for cross-step state — static fields cause race conditions in parallel execution.

---

## Extending the Template

### Adding a New API Domain

1. Create the package: `com.asdd.template.{domain}/`
2. Create sub-packages: `tasks`, `questions`, `model`, `stepdefinitions`, `hooks`, `util`, `runners`
3. Add a `{Domain}Constants.java` in `util`
4. Add a `{Domain}Hooks.java` in `hooks` — extends `BaseHooks`, sets up ability
5. Add a `{Domain}TestRunner.java` in `runners` — configure glue path and features path
6. Add feature files in `src/test/resources/features/{domain}/`
7. Run the ASDD pipeline with the new spec to generate tasks, questions, models, and step defs

### Adding a New E2E Page

1. Create `{PageName}Page.java` in `e2e/pages/`
2. Add `Target` constants for each interactive element
3. Create or extend tasks that use the new page's targets
4. Add step definitions that delegate to the new tasks

---

## Troubleshooting

| Problem | Solution |
|---|---|
| `ChromeDriver not found` | Serenity auto-manages it; ensure Chrome is installed |
| `ClassNotFoundException` for runner | Check `@SelectClasspathResource` path matches `features/api` or `features/e2e` |
| `StepDefinitionBody` error | Ensure glue package in `@ConfigurationParameter` is correct |
| Tests not discovered | Verify tag in scenario matches the filter tag |
| REST call fails with 500 | PetStore is a public demo — retry or check API status |
| Serenity report empty | Ensure `aggregate` task ran — it runs automatically via `finalizedBy` |
