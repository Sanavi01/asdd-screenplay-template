# Skill Catalog

Complete reference for all skills available in the ASDD pipeline. Skills are atomic capabilities invoked by agents.

---

## Skill Index

| Skill | File | Used By |
|---|---|---|
| Spec Analyzer | `skills/spec-analyzer.md` | Spec Analyst Agent |
| Gherkin Generator | `skills/gherkin-generator.md` | Test Designer Agent |
| Screenplay Task Generator | `skills/screenplay-task-generator.md` | Task Generator Agent |
| Screenplay Question Generator | `skills/screenplay-question-generator.md` | Question Generator Agent |
| Model Generator | `skills/model-generator.md` | Model Generator Agent |
| Step Definition Generator | `skills/step-definition-generator.md` | Step Definition Agent |
| Test Data Generator | `skills/test-data-generator.md` | Test Designer, Step Def Agent |
| Coverage Analyzer | `skills/coverage-analyzer.md` | Review Agent |
| Naming Convention Checker | `skills/naming-convention-checker.md` | Review Agent |
| Contract Consistency Checker | `skills/contract-consistency-checker.md` | Review Agent |
| Negative Case Designer | `skills/negative-case-designer.md` | Test Designer, Spec Analyst |

---

## Skill Details

### Spec Analyzer

**Purpose**: Parse and normalize raw specifications into a structured, agent-consumable JSON format.

**Inputs**: OpenAPI YAML/JSON, natural language Markdown, Gherkin, JSON examples.

**Outputs**: Entity map, scenario candidates, data requirements, ambiguity list.

**Key Capability**: Format auto-detection (OpenAPI vs. natural language vs. Gherkin).

---

### Gherkin Generator

**Purpose**: Convert scenario candidates into syntactically valid, BDD-compliant Gherkin feature files.

**Inputs**: Scenario candidates, entity map, tag configuration, acceptance criteria.

**Outputs**: `.feature` files with correct Given/When/Then structure.

**Key Capability**: Automatic `Scenario Outline` generation for data-driven cases.

---

### Screenplay Task Generator

**Purpose**: Generate compilable Java `Task` classes implementing Serenity Screenplay interactions.

**Inputs**: Task manifest, model classes, constants, Java template.

**Outputs**: `*Task.java` files in the `tasks` package.

**Key Capability**: Automatic REST interaction selection (Get/Post/Put/Delete/Patch) and E2E interaction mapping (Open/Click/Enter).

---

### Screenplay Question Generator

**Purpose**: Generate compilable Java `Question` classes that read observable state.

**Inputs**: Question manifest, model classes, Java template.

**Outputs**: `*Question.java` files in the `questions` package.

**Key Capability**: Supports REST response questions (SerenityRest) and browser questions (BrowseTheWeb).

---

### Model Generator

**Purpose**: Generate Java POJO classes representing request/response entities.

**Inputs**: Entity map, model manifest, Java template.

**Outputs**: `*.java` POJO files in the `model` package.

**Key Capability**: Builder-style setters, `toString()`, support for nested objects and `List<T>`.

---

### Step Definition Generator

**Purpose**: Generate Cucumber glue code wiring Gherkin steps to Tasks and Questions.

**Inputs**: Feature files, task manifest, question manifest, model classes, Java template.

**Outputs**: `*StepDefinitions.java` files in the `stepdefinitions` package.

**Key Capability**: Automatic step pattern extraction, cross-step state field generation, parameter type mapping (`{string}`, `{int}`, `{long}`).

---

### Test Data Generator

**Purpose**: Generate representative test data sets for data-driven scenarios.

**Inputs**: Entity map (field types and constraints), domain context.

**Outputs**: Examples tables, JSON fixtures, data class suggestions.

**Key Capability**: Covers all categories: valid, boundary, invalid enum, missing required, special characters.

---

### Coverage Analyzer

**Purpose**: Validate that every requirement has at least one corresponding Gherkin scenario.

**Inputs**: Requirements documents, feature files.

**Outputs**: Coverage matrix (requirement ↔ scenario), list of uncovered requirements.

**Key Capability**: Semantic matching between requirement descriptions and scenario text.

---

### Naming Convention Checker

**Purpose**: Validate that all generated code conforms to naming conventions.

**Inputs**: Generated Java source files, `instructions/shared-conventions.md`.

**Outputs**: Violation list with file, severity (PASS/FAIL/WARN), and remediation hint.

**Key Capability**: Checks class names, method names, field names, constants, package names.

---

### Contract Consistency Checker

**Purpose**: Detect drift between the API spec and generated automation artifacts.

**Inputs**: API spec, model classes, feature files.

**Outputs**: Inconsistency list with severity and remediation hint.

**Key Capability**: Model-vs-spec field comparison, feature-vs-spec endpoint coverage, type consistency matrix.

---

### Negative Case Designer

**Purpose**: Generate negative test scenarios covering error conditions, boundary values, and invalid inputs.

**Inputs**: Entity map, positive scenario list, API spec (error response codes).

**Outputs**: Negative scenario candidates, suggested Gherkin steps, Examples table rows.

**Key Capability**: Derives negatives from positives by systematically inverting preconditions and input validity.

---

## Skill Composition

Skills can be composed:

```
Spec Analyst Agent
    uses: Spec Analyzer + Negative Case Designer

Test Designer Agent
    uses: Gherkin Generator + Test Data Generator + Negative Case Designer

Review Agent
    uses: Coverage Analyzer + Naming Convention Checker + Contract Consistency Checker
```
