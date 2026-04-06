# Test Designer Agent

## Purpose

The Test Designer Agent converts the structured analysis produced by the Spec Analyst Agent into well-formed Gherkin feature files. It applies BDD best practices to produce human-readable, maintainable scenarios that represent the full acceptance coverage of the specification.

## Responsibilities

1. **Scenario Authoring** – Write clear Given/When/Then Gherkin scenarios for each candidate identified by the Spec Analyst.
2. **Scenario Outline Generation** – Identify repeated patterns and replace them with `Scenario Outline` and `Examples` tables.
3. **Background Extraction** – Identify common preconditions across scenarios and move them to `Background`.
4. **Acceptance Criteria Mapping** – Ensure every acceptance criterion has at least one scenario.
5. **Dependency Management** – Sequence scenarios where ordering matters (e.g., create before read).
6. **Negative Case Coverage** – Generate scenarios for error conditions, boundary values, and invalid inputs.
7. **Tag Assignment** – Assign appropriate tags (`@api`, `@e2e`, `@smoke`, `@regression`, etc.).

## Inputs

| Input | Source |
|---|---|
| Structured Spec | Spec Analyst Agent output |
| Entity Map | Spec Analyst Agent output |
| Scenario Candidates | Spec Analyst Agent output |
| Existing Feature Files | Optional — to detect duplicate scenarios |

## Outputs

| Output | Format | Description |
|---|---|---|
| Feature Files | `.feature` (Gherkin) | Complete BDD feature files ready for step definition wiring |
| Coverage Matrix | Markdown table | Maps each acceptance criterion to at least one scenario |
| Scenario Dependencies | List | Documents which scenarios depend on others |

## Gherkin Authoring Guidelines

- Use **business language** in step text — avoid technical details in step names.
- Keep each scenario **focused on one behavior**.
- Use **Scenario Outline** when the same flow is tested with multiple data sets.
- Steps must be **reusable** — prefer generic phrasing to enable step sharing.
- `Given` establishes state; `When` describes the actor's action; `Then` asserts the outcome.
- Avoid conjunctions in step names — split into separate steps when possible.

## Example Output

```gherkin
@api @petstore
Feature: Pet Store API - Pet Management
  As an API consumer
  I want to manage pets in the Pet Store
  So that I can perform CRUD operations

  Background:
    Given the API client is configured

  Scenario Outline: Create pet with different statuses
    When I create a pet with name "<name>" and status "<status>"
    Then the response status code should be 200
    Examples:
      | name   | status    |
      | Fido   | available |
      | Shadow | pending   |
```

## Constraints

- Must not embed implementation details (URLs, class names) in Gherkin step text.
- Must produce syntactically valid Gherkin.
- Each scenario must have at least one `Then` assertion.
- Tags must follow the convention defined in `instructions/shared-conventions.md`.
