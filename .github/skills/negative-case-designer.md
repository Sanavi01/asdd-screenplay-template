# Skill: Negative Case Designer

## Purpose
Identify and generate negative test scenarios — those that verify system behavior under invalid, missing, or malformed inputs, and under error conditions. Ensures the test suite is not limited to happy-path coverage.

## Inputs
- Entity map (required fields, enums, types)
- Positive scenario list (to derive negatives from)
- API spec (response codes for error conditions)
- Domain context

## Outputs
- List of negative scenario candidates (name + description)
- Suggested Gherkin steps for each negative case
- Suggested Examples table rows for negative data sets

## Negative Case Categories

| Category | Description | Example |
|---|---|---|
| Missing required field | Omit a field marked as required | Create pet with no name |
| Invalid enum value | Use a value not in the allowed set | Create pet with status "deleted" |
| Wrong data type | Send integer where string expected | Pet name = 12345 |
| Boundary value — minimum | Value at the lower boundary | Page size = 0 |
| Boundary value — maximum | Value at the upper boundary | Name = 256 characters |
| Unauthorized | Call endpoint without credentials | GET /pet without API key |
| Not found | Reference a resource that does not exist | GET /pet/999999999 |
| Conflict | Create a resource that already exists | POST /user with duplicate email |
| Method not allowed | Wrong HTTP method on endpoint | DELETE /pet (without ID) |
| Malformed body | Invalid JSON in request body | `{name: Buddy}` (missing quotes) |

## Derivation Algorithm
1. For each positive scenario, identify its preconditions and inputs.
2. For each required field → generate a "missing field" negative.
3. For each enum field → generate an "invalid enum" negative.
4. For each GET/DELETE by ID → generate a "not found" negative.
5. For each secured endpoint → generate an "unauthorized" negative.

## Example Output

**Input**: "Create a new pet with name and status"

**Derived Negatives**:
```gherkin
Scenario: Create pet with missing name fails
  When I create a pet with no name and status "available"
  Then the response status code should be 400

Scenario: Create pet with invalid status fails
  When I create a pet with name "Buddy" and status "deleted"
  Then the response status code should be 400
```

## Constraints
- Negative scenarios must be realistic — based on the spec's error response definitions.
- Must not generate redundant negatives (e.g., two identical "missing field" scenarios for the same field).
