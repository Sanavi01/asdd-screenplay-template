# Skill: Gherkin Generator

## Purpose
Convert a list of scenario candidates and structured spec data into syntactically correct, BDD-compliant Gherkin feature files.

## Inputs
- Scenario candidate list (from Spec Analyzer)
- Entity map
- Acceptance criteria
- Tag configuration

## Outputs
- One or more `.feature` files with valid Gherkin syntax
- Coverage matrix mapping each criterion to a scenario

## Algorithm
1. Group candidates by feature area.
2. Identify shared preconditions → extract to `Background`.
3. Detect repeated patterns → convert to `Scenario Outline` + `Examples`.
4. Write `Given/When/Then` for each candidate.
5. Assign tags per the naming conventions.
6. Validate syntax with a Gherkin parser.

## Example Usage

**Input**: Candidate "Create a new pet with name and status"

**Output**:
```gherkin
Scenario: Create a new pet
  When I create a pet with name "Buddy" and status "available"
  Then the response status code should be 200
```

## Gherkin Best Practices Applied
- Use `{string}` placeholders for parameterised values.
- Avoid assertions in `Given` steps.
- Use `And` only to extend the same actor's action — not to switch subjects.
- Keep feature descriptions in business language.

## Constraints
- Must produce syntactically valid Gherkin (parseable by Cucumber).
- Step text must not contain implementation details (class names, URLs, XPaths).
- Each scenario must have at least one `Then` step.
