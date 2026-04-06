# Skill: Coverage Analyzer

## Purpose
Analyze the relationship between requirements/acceptance criteria and generated Gherkin scenarios to identify gaps, redundancies, and coverage percentages.

## Inputs
- Requirements document (`requirements/api-requirements-template.md` or e2e equivalent)
- Generated feature files

## Outputs
- Coverage matrix (requirement ID ↔ scenario name)
- List of uncovered requirements
- List of orphan scenarios (scenarios with no matching requirement)
- Coverage percentage per feature area

## Coverage Matrix Format

| Requirement ID | Description | Covered By Scenario | Status |
|---|---|---|---|
| REQ-API-001 | Create pet returns 200 | `Create a new pet` | ✅ Covered |
| REQ-API-002 | Create pet with invalid data returns 400 | — | ❌ Missing |
| REQ-API-003 | Retrieve pet by ID returns correct pet | `Get an existing pet` | ✅ Covered |

## Coverage Rules
- Every requirement marked as `Priority: High` must have at least one scenario.
- Every requirement marked as `Priority: Medium` should have at least one scenario.
- Low-priority requirements should have coverage where feasible.
- Negative/error cases count as coverage for the corresponding negative requirement.

## Algorithms Applied
1. Parse requirement IDs and descriptions.
2. Perform semantic matching between requirement descriptions and scenario names/steps.
3. Flag requirements with zero matching scenarios as `❌ Missing`.
4. Flag scenarios with no matching requirement as `⚠️ Orphan`.

## Constraints
- Must not modify requirements or feature files — read-only analysis.
- Coverage percentage = (covered requirements / total requirements) × 100.
- Output must be deterministic.
