# Orchestration

How the ASDD Orchestrator coordinates agents to produce test automation artifacts.

---

## Orchestrator Role

The Orchestrator Agent is the sole entry point for the ASDD pipeline. It:
1. Receives the operator's request (spec + requirements + mode).
2. Validates inputs.
3. Creates an execution plan.
4. Invokes agents in order.
5. Collects outputs and passes them between agents.
6. Manages feedback loops when the Review Agent finds issues.
7. Produces the final delivery package.

The Orchestrator never generates code directly. It delegates all generation to specialist agents.

---

## Execution Modes

| Mode | Pipeline Invoked |
|---|---|
| `api` | Spec Analyst → Test Designer → Architect → API generators → Review |
| `e2e` | Spec Analyst → Test Designer → Architect → E2E generators → Review |
| `full` | Both pipelines in parallel, shared Review pass at the end |

---

## Agent Invocation Order

### API Pipeline

```
1. Spec Analyst Agent
   Input:  spec file, requirements file
   Output: entity-map.json, scenario-candidates.json

2. Test Designer Agent
   Input:  entity-map.json, scenario-candidates.json
   Output: features/api/*.feature

3. Screenplay Architect Agent
   Input:  entity-map.json, *.feature
   Output: task-manifest.json, question-manifest.json, model-manifest.json

4. [Parallel]
   4a. Task Generator Agent
       Input:  task-manifest.json, model classes
       Output: api/tasks/*.java

   4b. Question Generator Agent
       Input:  question-manifest.json
       Output: api/questions/*.java

   4c. Model Generator Agent
       Input:  model-manifest.json, entity-map.json
       Output: api/model/*.java

   4d. Step Definition Agent
       Input:  *.feature, task-manifest.json, question-manifest.json
       Output: api/stepdefinitions/*.java

5. Review Agent
   Input:  all generated *.java, *.feature, requirements
   Output: review-report.md, issue-list.json
```

### E2E Pipeline (additional step)

Between Architect and generators, for E2E:
- Architect also produces `page-manifest.json` with `Target` definitions.
- E2E generators additionally produce `e2e/pages/*.java`.

---

## Feedback Loop

When the Review Agent produces FAIL-level issues:

```
Review Agent output → issue-list.json
        │
Orchestrator reads issue-list.json
        │
For each FAIL issue:
    - Identify which agent produced the faulty artifact
    - Re-invoke that agent with the issue as additional context
    - Cap at max.review.cycles (default: 3)
        │
        ▼
Re-run Review Agent on updated artifacts
        │
        ▼
If still FAIL after max cycles → deliver with issues flagged
If all PASS → deliver clean
```

---

## State Handoff Between Agents

The Orchestrator maintains a **pipeline context** object that accumulates outputs:

```json
{
  "entityMap": { ... },
  "scenarioCandidates": [ ... ],
  "featureFiles": ["features/api/petstore.feature"],
  "taskManifest": { ... },
  "questionManifest": { ... },
  "modelManifest": { ... },
  "generatedFiles": [
    "api/tasks/CreatePet.java",
    "api/questions/ResponseStatusCode.java"
  ],
  "reviewReport": {
    "issues": [],
    "overallResult": "PASS"
  }
}
```

Each agent reads only the portions of context it needs and appends its output.

---

## Coherence Validation

Before invoking the Step Definition Agent, the Orchestrator performs a coherence check:

1. Every Gherkin step in the feature files must map to exactly one task or question in the manifests.
2. Every task in the manifest must be referenced by at least one step.
3. Every question in the manifest must be referenced by at least one `Then` step.

If coherence fails, the Orchestrator re-invokes the Screenplay Architect to resolve gaps.

---

## Configuration

| Property | Default | Description |
|---|---|---|
| `pipeline.mode` | `full` | Which pipeline(s) to run |
| `review.enabled` | `true` | Whether to run the Review Agent |
| `max.review.cycles` | `3` | Maximum feedback loop iterations |
| `parallel.generation` | `true` | Run generator agents in parallel |
| `coherence.check` | `true` | Validate step-task mapping before generation |

---

## Error Handling

| Error | Orchestrator Response |
|---|---|
| Spec file not found | Abort with descriptive error |
| Spec is empty or unparseable | Flag as ambiguity, request clarification |
| Generator agent fails | Log failure, attempt once more, then abort |
| Review agent fails | Log failure, deliver without review, flag as unreviewed |
| Max review cycles reached | Deliver with open issues documented |
