# Agent Responsibilities

Detailed description of every agent in the ASDD pipeline, including their role, responsibilities, inputs, outputs, and interaction with other agents.

---

## Agent Overview Table

| Agent | Primary Role | Invoked By | Invokes |
|---|---|---|---|
| Orchestrator | Pipeline coordination | Operator | All agents |
| Spec Analyst | Spec parsing and structuring | Orchestrator | — |
| Test Designer | Gherkin authoring | Orchestrator | — |
| Screenplay Architect | Implementation planning | Orchestrator | — |
| Task Generator | Task class creation | Orchestrator | — |
| Question Generator | Question class creation | Orchestrator | — |
| Model Generator | POJO creation | Orchestrator | — |
| Step Definition | Glue code creation | Orchestrator | — |
| Review | Quality validation | Orchestrator | — |

---

## Orchestrator Agent

**File**: `.github/agents/orchestrator-agent.md`

**Role**: Master coordinator. Does not generate code; only plans, delegates, and integrates.

**Responsibilities**:
- Accept operator input (spec + requirements + mode).
- Validate that required inputs exist and are parseable.
- Build and maintain the pipeline context (shared state).
- Invoke agents in correct order with correct context.
- Manage feedback loops for failed reviews.
- Produce the final deliverable package.

**Key Decisions**:
- Which pipeline mode to use (api/e2e/full).
- When to re-invoke a generator due to review failures.
- When to abort vs. deliver with warnings.

---

## Spec Analyst Agent

**File**: `.github/agents/spec-analyst-agent.md`

**Role**: First-pass spec interpreter. Transforms raw input into structured data.

**Responsibilities**:
- Parse OpenAPI, natural language, or Gherkin specs.
- Extract entities, fields, types, and constraints.
- Enumerate endpoint operations (for API) or user actions (for E2E).
- Produce scenario candidates and data requirements.
- Flag ambiguities without blocking the pipeline.

**Critical Output**: `entity-map.json` — used by every downstream agent.

---

## Test Designer Agent

**File**: `.github/agents/test-designer-agent.md`

**Role**: BDD scenario author. Produces human-readable, business-language Gherkin.

**Responsibilities**:
- Create feature files from scenario candidates.
- Apply BDD best practices (Given/When/Then, Background, Scenario Outline).
- Assign tags per the convention in `.github/instructions/shared-conventions.md`.
- Ensure every acceptance criterion maps to at least one scenario.
- Include negative cases identified by the Spec Analyst.

**Quality Bar**: Scenarios must be readable by a non-technical stakeholder.

---

## Screenplay Architect Agent

**File**: `.github/agents/screenplay-architect-agent.md`

**Role**: Technical design authority. Decides the shape of the implementation before any code is generated.

**Responsibilities**:
- Map Gherkin steps to Tasks and Questions.
- Decide granularity: what is a Task vs. what is composed in a step definition.
- Design Page Object `Target` definitions for E2E.
- Define model structure based on entity map.
- Produce manifests (task, question, model, page) consumed by generators.

**Critical Decision**: Single Responsibility — each task must do exactly one thing.

---

## Task Generator Agent

**File**: `.github/agents/task-generator-agent.md`

**Role**: Generates Screenplay Task Java classes.

**Responsibilities**:
- Create one Java class per entry in the Task Manifest.
- Use the correct Serenity REST or WebDriver interaction.
- Include static factory methods.
- Ensure all imports are correct and the class compiles.
- Must not add assertions to generated tasks.

---

## Question Generator Agent

**File**: `.github/agents/question-generator-agent.md`

**Role**: Generates Screenplay Question Java classes.

**Responsibilities**:
- Create one Java class per entry in the Question Manifest.
- Return the correct value type from `answeredBy`.
- Use `SerenityRest` for API questions; `BrowseTheWeb` for E2E.
- Must not call `attemptsTo` inside a question.

---

## Model Generator Agent

**File**: `.github/agents/model-generator-agent.md`

**Role**: Generates Java POJOs representing API request/response entities.

**Responsibilities**:
- Create plain Java classes with fields, getters, builder-style setters, and `toString()`.
- No Lombok, no Screenplay imports.
- Field names must match JSON keys from the spec.
- Support `List<T>` generics for array fields.

---

## Step Definition Agent

**File**: `.github/agents/step-definition-agent.md`

**Role**: Generates Cucumber glue code connecting Gherkin to Screenplay.

**Responsibilities**:
- Map each unique Gherkin step pattern to a method.
- Delegate to Tasks for actions; use Questions for assertions.
- Manage cross-step state with instance fields (not static).
- Never call REST or WebDriver APIs directly.
- Produce compilable code with no duplicate step patterns.

---

## Review Agent

**File**: `.github/agents/review-agent.md`

**Role**: Quality gate. Validates all artifacts before delivery.

**Responsibilities**:
- Run naming convention checks.
- Validate Screenplay pattern compliance.
- Check coverage completeness against requirements.
- Detect duplicate step patterns.
- Validate imports.
- Produce a structured review report with PASS/FAIL/WARN.
- Trigger re-generation for FAIL-level issues via the Orchestrator.

**Does NOT**: Modify any file. Read-only analysis only.
