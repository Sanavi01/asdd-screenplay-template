# Skill: Contract Consistency Checker

## Purpose
Validate that the Java model classes, step definitions, and Gherkin scenarios are consistent with the API contract (OpenAPI spec or equivalent). Detects drift between the spec and the generated automation code.

## Inputs
- API specification (OpenAPI or structured spec)
- Generated model classes (`*.java` in `model` packages)
- Generated feature files

## Outputs
- List of inconsistencies with severity and remediation hint
- Summary: consistent items count, inconsistent items count

## Checks Performed

### Model vs. Spec
- Every required field in the spec must have a corresponding Java field.
- Java field types must match the spec's JSON types (using the type mapping table).
- Enum constraints on spec fields must be reflected in the model or documented.
- Field names must match JSON keys (accounting for `@JsonProperty` overrides).

### Feature vs. Spec
- Every API endpoint described in the spec must have at least one scenario that exercises it.
- HTTP methods used in scenarios (implied by step text) must match spec methods.
- Response status codes asserted in scenarios must be listed in the spec's response definitions.

### Step Definitions vs. Model
- Model class fields referenced in step definitions must exist on the model.
- Parameters passed to Tasks must match the model's setter parameter types.

## Type Consistency Matrix

| OpenAPI Type | Java Type | Valid |
|---|---|---|
| string | String | ✅ |
| integer (int32) | Integer | ✅ |
| integer (int64) | Long | ✅ |
| number | Double | ✅ |
| boolean | Boolean | ✅ |
| array | List<T> | ✅ |
| string | Long | ❌ Type mismatch |

## Example Output
```
[FAIL] Pet.java: field `photoUrls` is required in OpenAPI spec but missing required validation
[WARN] petstore.feature: endpoint DELETE /pet/{id} has no scenario
[PASS] All model field names match OpenAPI property names
```

## Constraints
- Read-only analysis — does not modify files.
- Must handle missing spec gracefully (warn, do not crash).
