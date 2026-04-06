# Skill: Naming Convention Checker

## Purpose
Validate that all generated Java class names, method names, field names, and package names conform to the conventions defined in `instructions/shared-conventions.md`.

## Inputs
- Generated Java source files
- `instructions/shared-conventions.md`

## Outputs
- List of violations with file name, line hint, expected vs. actual, and severity
- Summary: pass count, fail count, warn count

## Rules Checked

### Class Names
- Must be `PascalCase`
- Task classes: must follow `VerbNoun` pattern (e.g., `CreatePet`, `GetPet`, not `PetCreator`)
- Question classes: must be noun phrases (e.g., `ResponseStatusCode`, not `GetStatusCode`)
- Step definition classes: must end in `StepDefinitions`
- Runner classes: must end in `TestRunner`
- Page classes: must end in `Page`
- Model classes: must be `PascalCase` nouns matching entity names
- Hook classes: must end in `Hooks`
- Constants classes: must end in `Constants`

### Method Names
- Must be `camelCase`
- Static factories in Tasks/Questions: descriptive verb phrases (e.g., `withId`, `ofTheLastResponse`)
- Step definition methods: must match the Gherkin step pattern (camelCase conversion)
- `performAs` in Tasks: must be named exactly `performAs`
- `answeredBy` in Questions: must be named exactly `answeredBy`

### Package Names
- Must be all lowercase with dots: `com.asdd.template.{domain}.{layer}`
- Valid domains: `api`, `e2e`, `core`
- Valid layers: `tasks`, `questions`, `model`, `stepdefinitions`, `runners`, `hooks`, `pages`, `util`

### Field Names
- Must be `camelCase`
- Constants in constants classes: must be `UPPER_SNAKE_CASE`

## Example Output
```
[FAIL] CreatePet.java: class has no static factory method — add `public static CreatePet with(Pet pet)`
[WARN] petStoreStepDefinitions.java: class name should be PascalCase — rename to PetStoreStepDefinitions
[PASS] All Question class names are noun phrases
```

## Constraints
- Must not auto-fix violations — report only.
- Severity: FAIL for blocking violations; WARN for style issues.
