# ASDD Screenplay Template

> **Agentic Spec-Driven Development** — A production-ready template for API and E2E test automation using Java 17, Serenity BDD 5.x, Screenplay Pattern, and Cucumber 7.x. Includes a full AI-agent orchestration layer that generates test code from specifications.

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/projects/jdk/17/)
[![Serenity BDD](https://img.shields.io/badge/Serenity-5.3.7-blue)](https://serenity-bdd.info/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.20.1-green)](https://cucumber.io/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-purple)](https://gradle.org/)
[![CI](https://github.com/Sanavi01/asdd-screenplay-template/actions/workflows/ci.yml/badge.svg)](https://github.com/Sanavi01/asdd-screenplay-template/actions/workflows/ci.yml)

---

## What is ASDD?

**Agentic Spec-Driven Development (ASDD)** is an approach to test automation where AI agents collaborate to transform specifications into production-ready test code. Instead of manually writing boilerplate, you:

1. Write a specification (OpenAPI, natural language, or structured Markdown).
2. Describe your requirements.
3. Invoke the ASDD agent pipeline.
4. Receive compilable, reviewed, Screenplay-pattern test code.

The agents cover analysis, design, code generation, and quality review — each with clear responsibilities and validated outputs.

---

## Template Overview

This template provides:

| Layer | Contents |
|---|---|
| **Automation Framework** | Gradle project, Serenity BDD 5.x, Screenplay Pattern, Cucumber BDD |
| **API Testing** | PetStore CRUD example with Tasks, Questions, Models, Step Definitions |
| **E2E Testing** | Login journey example with Page Objects, Tasks, Questions |
| **Agent Layer** | 9 agents each with precise responsibilities (`.github/agents/`) |
| **Skills Layer** | 11 reusable skills invoked by agents (`.github/skills/`) |
| **Instructions** | API, E2E, and shared coding conventions (`.github/instructions/`) |
| **Specs & Requirements** | Example specs and requirement templates (`.github/specs/`, `.github/requirements/`) |
| **Code Templates** | Java `.template` files for each artifact type (`templates/`) |
| **Documentation** | Full architecture, workflow, and configuration docs (`docs/`) |
| **CI Pipeline** | GitHub Actions workflow for automated builds and tests (`.github/workflows/`) |

---

## Quick Start

### Prerequisites

- Java 17+
- Chrome (for E2E tests)

### Clone and Build

```bash
git clone <repo-url>
cd asdd-screenplay-template
./gradlew compileTestJava
```

### Run API Tests

```bash
./gradlew test -Dcucumber.filter.tags="@api"
```

### Run E2E Tests

```bash
./gradlew test -Dcucumber.filter.tags="@e2e"
```

### Run All Tests and View Report

```bash
./gradlew test
open build/site/serenity/index.html
```

---

## Project Structure

```
.
├── build.gradle                          # Gradle build — Java 17, Serenity 5.x, Cucumber 7.x
├── settings.gradle
├── gradlew / gradlew.bat
│
├── .github/                              # GitHub + ASDD governance
│   ├── agents/                           # AI agent definitions
│   │   ├── orchestrator-agent.md
│   │   ├── spec-analyst-agent.md
│   │   ├── test-designer-agent.md
│   │   ├── screenplay-architect-agent.md
│   │   ├── task-generator-agent.md
│   │   ├── question-generator-agent.md
│   │   ├── model-generator-agent.md
│   │   ├── step-definition-agent.md
│   │   └── review-agent.md
│   ├── instructions/                     # Coding instructions for agents
│   │   ├── api-automation-instructions.md
│   │   ├── e2e-automation-instructions.md
│   │   └── shared-conventions.md
│   ├── requirements/                     # Requirement capture templates
│   │   ├── api-requirements-template.md
│   │   └── e2e-requirements-template.md
│   ├── skills/                           # Reusable agent skills
│   │   ├── spec-analyzer.md
│   │   ├── gherkin-generator.md
│   │   ├── screenplay-task-generator.md
│   │   ├── screenplay-question-generator.md
│   │   ├── model-generator.md
│   │   ├── step-definition-generator.md
│   │   ├── test-data-generator.md
│   │   ├── coverage-analyzer.md
│   │   ├── naming-convention-checker.md
│   │   ├── contract-consistency-checker.md
│   │   └── negative-case-designer.md
│   ├── specs/                            # Example specifications
│   │   ├── api/petstore-api-spec.md
│   │   └── e2e/sample-webapp-spec.md
│   └── workflows/
│       └── ci.yml                        # GitHub Actions CI pipeline
│
├── docs/                                 # Full documentation
│   ├── architecture.md
│   ├── workflow.md
│   ├── orchestration.md
│   ├── agent-responsibilities.md
│   ├── skill-catalog.md
│   ├── inputs-outputs.md
│   └── configuration-and-execution.md
│
├── templates/                            # Java code templates with placeholders
│   ├── api/  Task, Question, Model, StepDefinition
│   └── e2e/  Task, Question, Page, StepDefinition
│
└── src/test/
    ├── java/com/asdd/template/
    │   ├── core/                         # Shared / framework utilities
    │   │   ├── actors/ActorFactory.java  # Centralised actor creation
    │   │   ├── hooks/BaseHooks.java      # Global lifecycle hooks
    │   │   └── util/Constants.java       # Shared constants
    │   ├── api/                          # API test module
    │   │   ├── hooks/ApiHooks.java
    │   │   ├── model/Pet.java
    │   │   ├── questions/ ResponseStatusCode, ResponseBody
    │   │   ├── runners/ApiTestRunner.java
    │   │   ├── stepdefinitions/PetStoreStepDefinitions.java
    │   │   ├── tasks/ CreatePet, GetPet, UpdatePet, DeletePet, ConfigureApi
    │   │   └── util/ApiConstants.java
    │   └── e2e/                          # E2E test module
    │       ├── hooks/E2eHooks.java
    │       ├── model/UserCredentials.java
    │       ├── pages/ LoginPage, DashboardPage
    │       ├── questions/ CurrentUrl, PageTitle
    │       ├── runners/E2eTestRunner.java
    │       ├── stepdefinitions/LoginStepDefinitions.java
    │       ├── tasks/ NavigateTo, Login, Logout
    │       └── util/E2eConstants.java
    └── resources/
        ├── features/
        │   ├── api/petstore.feature
        │   └── e2e/login.feature
        ├── serenity.conf
        └── logback-test.xml
```

---

## Using This Template for API Tests

### 1. Add Your Spec

Place your API specification in `.github/specs/api/my-api-spec.md`. Use the [PetStore example](.github/specs/api/petstore-api-spec.md) as a reference.

### 2. Fill in Requirements

Copy `.github/requirements/api-requirements-template.md`, fill in endpoints, acceptance criteria, and test data needs.

### 3. Run the ASDD Pipeline (or generate manually)

**With agents**: Invoke the Orchestrator Agent with your spec and requirements. It will produce Tasks, Questions, Models, Feature Files, and Step Definitions.

**Manually**: Use the instructions in `.github/instructions/api-automation-instructions.md` and the templates in `templates/api/` to create your files.

### 4. Add Generated Files

Place files in the correct packages under `src/test/java/com/asdd/template/api/`.

### 5. Run

```bash
./gradlew test -Dcucumber.filter.tags="@api"
```

---

## Using This Template for E2E Tests

### 1. Add Your Spec

Place your E2E specification in `.github/specs/e2e/my-app-spec.md`.

### 2. Fill in Requirements

Copy `.github/requirements/e2e-requirements-template.md`, fill in user journeys, page inventory, and UI elements.

### 3. Generate or Write Code

Follow `.github/instructions/e2e-automation-instructions.md`. Page objects go in `e2e/pages/`, tasks in `e2e/tasks/`.

### 4. Configure the Browser

Edit `src/test/resources/serenity.conf`:
```hocon
serenity {
  webdriver {
    driver = chrome   # or firefox, edge
  }
}
```

### 5. Run

```bash
./gradlew test -Dcucumber.filter.tags="@e2e"
```

---

## ASDD Workflow

```
Spec/Requirements
      │
      ▼
Orchestrator Agent
      │
      ├──► Spec Analyst Agent    → Entity Map, Scenario Candidates
      │
      ├──► Test Designer Agent   → Feature Files (.feature)
      │
      ├──► Screenplay Architect  → Implementation Blueprint
      │         │
      │         ├──► Task Generator      → Task.java
      │         ├──► Question Generator  → Question.java
      │         ├──► Model Generator     → Model.java
      │         └──► Step Def Generator  → StepDefinitions.java
      │
      └──► Review Agent          → Review Report (PASS/FAIL/WARN)
```

See [docs/workflow.md](docs/workflow.md) for the complete step-by-step workflow.

---

## Agent Overview

| Agent | Responsibility |
|---|---|
| **Orchestrator** | Coordinates the pipeline, manages feedback loops |
| **Spec Analyst** | Parses specs into structured entity maps and scenario candidates |
| **Test Designer** | Converts candidates into Gherkin feature files |
| **Screenplay Architect** | Plans the implementation structure (tasks, questions, models) |
| **Task Generator** | Generates Screenplay `Task` Java classes |
| **Question Generator** | Generates Screenplay `Question` Java classes |
| **Model Generator** | Generates POJO data model classes |
| **Step Definition** | Generates thin Cucumber glue code |
| **Review** | Validates naming, coverage, compliance, and consistency |

See [docs/agent-responsibilities.md](docs/agent-responsibilities.md) for full details.

---

## Skill Catalog Overview

| Skill | Purpose |
|---|---|
| Spec Analyzer | Parse and normalize specifications |
| Gherkin Generator | Produce BDD feature files |
| Screenplay Task Generator | Generate Task classes |
| Screenplay Question Generator | Generate Question classes |
| Model Generator | Generate POJO classes |
| Step Definition Generator | Generate Cucumber glue |
| Test Data Generator | Generate Examples tables and fixtures |
| Coverage Analyzer | Map requirements to scenarios |
| Naming Convention Checker | Validate class/method naming |
| Contract Consistency Checker | Detect spec drift |
| Negative Case Designer | Derive error/boundary test cases |

See [docs/skill-catalog.md](docs/skill-catalog.md) for the full catalog.

---

## Configuration

### Environment URLs

Override URLs via system properties:

```bash
./gradlew test -Dapi.base.url=https://my-api.example.com
./gradlew test -De2e.base.url=https://my-app.example.com
```

### Tag Filtering

```bash
./gradlew test -Dcucumber.filter.tags="@api and @smoke"
./gradlew test -Dcucumber.filter.tags="@e2e and not @wip"
```

### Headless Chrome (CI)

```bash
./gradlew test -Dwebdriver.chrome.arguments="--headless,--no-sandbox,--disable-dev-shm-usage"
```

---

## Execution Commands

| Command | What it Does |
|---|---|
| `./gradlew compileTestJava` | Compile all test sources |
| `./gradlew test` | Run all tests + generate Serenity report |
| `./gradlew test -Dcucumber.filter.tags="@api"` | API tests only |
| `./gradlew test -Dcucumber.filter.tags="@e2e"` | E2E tests only |
| `./gradlew test -Dcucumber.filter.tags="@smoke"` | Smoke suite only |
| `./gradlew aggregate` | Re-generate Serenity report from existing results |
| `./gradlew clean test` | Clean build then full test run |

---

## How to Extend

### New API Endpoint
1. Add the endpoint to `.github/specs/api/your-spec.md`.
2. Add requirements to `.github/requirements/api-requirements-template.md`.
3. Run the ASDD pipeline → generates new Task + Scenario + Step Definition.
4. Or manually: add a Task class in `api/tasks/`, a scenario in the `.feature` file, and wire it in `api/stepdefinitions/`.

### New Page / User Journey
1. Add the page to `.github/specs/e2e/your-spec.md`.
2. Run the ASDD pipeline → generates Page Object + Task + Scenario + Step Definition.
3. Or manually: add a `*Page.java` in `e2e/pages/`, a Task in `e2e/tasks/`, and wire in step defs.

### New Domain (beyond API and E2E)
1. Create package `com.asdd.template.{domain}/`.
2. Create constants, hooks, runner, tasks, questions, step defs.
3. Add feature files in `src/test/resources/features/{domain}/`.
4. Update runner `@SelectClasspathResource` and `@ConfigurationParameter(GLUE_PROPERTY_NAME)`.

---

## Documentation

| Doc | Description |
|---|---|
| [docs/architecture.md](docs/architecture.md) | Full architecture with component diagram and tech stack |
| [docs/workflow.md](docs/workflow.md) | End-to-end generation workflow |
| [docs/orchestration.md](docs/orchestration.md) | Agent coordination and feedback loops |
| [docs/agent-responsibilities.md](docs/agent-responsibilities.md) | Per-agent responsibilities |
| [docs/skill-catalog.md](docs/skill-catalog.md) | All skills with inputs/outputs |
| [docs/inputs-outputs.md](docs/inputs-outputs.md) | Pipeline stage contracts |
| [docs/configuration-and-execution.md](docs/configuration-and-execution.md) | Config, run commands, CI setup |
| [.github/instructions/api-automation-instructions.md](.github/instructions/api-automation-instructions.md) | API coding guide for agents |
| [.github/instructions/e2e-automation-instructions.md](.github/instructions/e2e-automation-instructions.md) | E2E coding guide for agents |
| [.github/instructions/shared-conventions.md](.github/instructions/shared-conventions.md) | Naming and code style conventions |

---

## License

MIT License — see `LICENSE` for details.
