# Workflow

End-to-end workflow for generating test automation using the ASDD pipeline.

---

## Overview

The ASDD workflow transforms a specification into a runnable, reviewed test suite in a series of deterministic steps. The workflow supports both API and E2E automation, and can run both pipelines in parallel when both spec types are provided.

---

## Step-by-Step Workflow

### Step 0: Prerequisites

Before starting the pipeline:

1. Fill in `requirements/api-requirements-template.md` and/or `requirements/e2e-requirements-template.md`.
2. Place your spec in `specs/api/` or `specs/e2e/`.
3. Confirm your Gradle build is working: `./gradlew compileTestJava`.

---

### Step 1: Invoke the Orchestrator

Provide the Orchestrator Agent with:
- The path to your requirements file.
- The path to your spec file.
- The pipeline mode: `api`, `e2e`, or `full`.

**Decision point**: Does the spec contain sufficient information?
- Yes → proceed to Step 2.
- No → Orchestrator requests clarification or applies documented defaults.

---

### Step 2: Spec Analysis

The **Spec Analyst Agent** reads the specification and produces:
- A structured JSON entity map.
- A list of endpoint/action candidates.
- A list of scenario candidates.
- An ambiguity report (if any).

**Decision point**: Are there ambiguities that block generation?
- Yes → surface to operator; apply safe defaults and continue.
- No → proceed to Step 3.

---

### Step 3: Test Design

The **Test Designer Agent** converts scenario candidates into Gherkin feature files:
- Groups scenarios by feature.
- Extracts `Background` preconditions.
- Generates `Scenario Outline` for data-driven cases.
- Assigns tags.

**Output**: `.feature` files in `src/test/resources/features/{api|e2e}/`.

---

### Step 4: Architecture Planning

The **Screenplay Architect Agent** analyses the feature files and entity map to produce:
- Task Manifest (which Tasks to create).
- Question Manifest (which Questions to create).
- Model Manifest (which POJOs to create).
- Page Object Manifest (E2E only).

---

### Step 5: Code Generation (Parallel)

The following generator agents run in parallel:

| Agent | Generates | Location |
|---|---|---|
| Task Generator | `*Task.java` files | `src/test/java/.../tasks/` |
| Question Generator | `*Question.java` files | `src/test/java/.../questions/` |
| Model Generator | `*Model.java` files | `src/test/java/.../model/` |
| Step Def Generator | `*StepDefinitions.java` files | `src/test/java/.../stepdefinitions/` |

---

### Step 6: Review

The **Review Agent** validates all generated artifacts:
- Naming conventions check.
- Screenplay compliance check.
- Coverage completeness.
- Duplicate detection.
- Import validation.

**Decision point**: Review result?
- All PASS → proceed to Step 7.
- Any FAIL → route affected artifacts back to appropriate generator (max 3 cycles).
- Only WARN → proceed to Step 7 with warnings recorded.

---

### Step 7: Delivery

The Orchestrator assembles the final deliverable:
- All `.feature` files.
- All `.java` source files.
- Review report.
- Coverage matrix.

The developer then:
1. Places files in the project according to the package structure.
2. Runs `./gradlew test` to verify.
3. Reviews the Serenity HTML report in `build/site/serenity/`.

---

## Workflow Diagram

```
[Spec/Requirements]
        │
        ▼
[Orchestrator: validate input, plan execution]
        │
        ▼
[Spec Analyst: parse, extract entities and scenario candidates]
        │
        ▼
[Test Designer: generate Gherkin feature files]
        │
        ▼
[Screenplay Architect: plan implementation structure]
        │
        ├──────────────────────────────────────┐
        ▼          ▼          ▼                ▼
[Task Gen]  [Question Gen]  [Model Gen]  [StepDef Gen]
        │          │          │                │
        └──────────┴──────────┴────────────────┘
                              │
                              ▼
                    [Review Agent: validate]
                              │
                    ┌─────────┴─────────┐
                    ▼                   ▼
               [FAIL → re-gen]    [PASS → deliver]
```

---

## Example: Adding a New API Endpoint

Given a new `PATCH /pet/{id}/status` endpoint:

1. Add the endpoint to `specs/api/petstore-api-spec.md`.
2. Add the requirement to `requirements/api-requirements-template.md`.
3. Run the ASDD pipeline (or invoke agents manually).
4. New artifacts generated:
   - `Scenario: Update pet status via PATCH` in `petstore.feature`.
   - `PatchPetStatus.java` Task.
   - New step in `PetStoreStepDefinitions.java`.
5. Review Agent validates.
6. Run `./gradlew test -Dcucumber.filter.tags="@api"`.

---

## Iteration Cycle

```
Requirement change → Update spec → Re-run pipeline → Review → Commit
```

The pipeline is designed to be run iteratively. Each run produces a delta of new/updated artifacts.
