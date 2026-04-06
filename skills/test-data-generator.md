# Skill: Test Data Generator

## Purpose
Generate representative test data sets for use in Scenario Outlines, step definitions, and data-driven scenarios, covering positive, negative, and boundary cases.

## Inputs
- Entity map (field constraints, enums, required/optional flags)
- Scenario candidates
- Domain context (e.g., "Pet" with status enum `[available, pending, sold]`)

## Outputs
- Examples tables for Scenario Outlines
- JSON fixtures for API request bodies
- Data class suggestions for complex test data

## Data Categories Generated

| Category | Description | Example |
|---|---|---|
| Valid positive | Standard happy-path values | `name="Buddy"`, `status="available"` |
| Valid boundary | Edge-case valid values | Maximum string length, zero values |
| Invalid type | Wrong data type | Integer where string expected |
| Invalid enum | Value not in allowed set | `status="unknown"` |
| Missing required | Omit a required field | No `name` field in request |
| Null values | Explicit null/empty | `name=null`, `name=""` |
| Special characters | Values with escaping needs | `name="O'Brien"`, `name="<script>"` |

## Example Output (Examples table)

```gherkin
Examples:
  | name    | status    | expectedStatus |
  | Fido    | available | 200            |
  | Shadow  | pending   | 200            |
  | Goldie  | sold      | 200            |
  |         | available | 400            |
  | Max     | invalid   | 400            |
```

## Constraints
- Test data must be deterministic and reproducible.
- Sensitive data (real credentials, PII) must use fictional/anonymized values.
- Data must cover all enum values for enum-typed fields.
