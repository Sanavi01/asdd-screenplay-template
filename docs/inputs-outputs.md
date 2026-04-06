# Inputs and Outputs

Contract documentation for what enters and exits the ASDD system at each stage of the pipeline.

---

## Pipeline Entry Point

### What the Operator Provides

| Input | Location | Format | Required |
|---|---|---|---|
| API Specification | `.github/specs/api/*.md` or URL | OpenAPI/Markdown | Conditional (API mode) |
| E2E Specification | `.github/specs/e2e/*.md` | Markdown | Conditional (E2E mode) |
| API Requirements | `.github/requirements/api-requirements-template.md` | Markdown | Recommended |
| E2E Requirements | `.github/requirements/e2e-requirements-template.md` | Markdown | Recommended |
| Pipeline Mode | CLI or config | `api`/`e2e`/`full` | No (default: `full`) |
| Base URL overrides | System properties | `-Dapi.base.url=...` | No |

---

## Stage 1: Spec Analysis

### Inputs
- Spec file (OpenAPI/Markdown/natural language)
- Requirements file

### Outputs

#### `entity-map.json` (internal)
```json
{
  "entities": {
    "Pet": {
      "fields": {
        "id": { "type": "Long", "required": false },
        "name": { "type": "String", "required": true },
        "status": { "type": "String", "enum": ["available","pending","sold"] },
        "photoUrls": { "type": "List<String>", "required": true }
      }
    }
  }
}
```

#### `scenario-candidates.json` (internal)
```json
[
  { "id": "SC-001", "name": "Create a new pet", "type": "positive", "endpoint": "POST /pet" },
  { "id": "SC-002", "name": "Create pet with missing name", "type": "negative", "endpoint": "POST /pet" }
]
```

#### `ambiguity-report.md` (surfaced to operator if blocking)
- Lists fields or behaviors that are unclear in the spec.

---

## Stage 2: Test Design

### Inputs
- `entity-map.json`
- `scenario-candidates.json`
- Requirements file (for acceptance criteria)

### Outputs

#### `.feature` files
```
src/test/resources/features/api/petstore.feature
src/test/resources/features/e2e/login.feature
```
Valid Gherkin with tags, Background, Scenarios, Scenario Outlines.

#### `coverage-matrix-initial.md` (internal)
Maps requirement IDs to scenario names before review.

---

## Stage 3: Architecture Planning

### Inputs
- `entity-map.json`
- `.feature` files

### Outputs

#### `task-manifest.json` (internal)
```json
[
  {
    "name": "CreatePet",
    "package": "com.asdd.template.api.tasks",
    "httpMethod": "POST",
    "endpoint": "ApiConstants.PET_ENDPOINT",
    "parameters": [{ "name": "pet", "type": "Pet" }],
    "interaction": "Post.to(...).with(body=pet, contentType=JSON)"
  }
]
```

#### `question-manifest.json` (internal)
```json
[
  {
    "name": "ResponseStatusCode",
    "package": "com.asdd.template.api.questions",
    "returnType": "Integer",
    "source": "SerenityRest.lastResponse().statusCode()"
  }
]
```

#### `model-manifest.json` (internal)
```json
[
  {
    "name": "Pet",
    "package": "com.asdd.template.api.model",
    "fields": [
      { "name": "id", "type": "Long" },
      { "name": "name", "type": "String" },
      { "name": "status", "type": "String" },
      { "name": "photoUrls", "type": "List<String>" }
    ]
  }
]
```

#### `page-manifest.json` (E2E only, internal)
```json
[
  {
    "name": "LoginPage",
    "package": "com.asdd.template.e2e.pages",
    "targets": [
      { "constant": "USERNAME_FIELD", "description": "username field", "locator": "#username" },
      { "constant": "PASSWORD_FIELD", "description": "password field", "locator": "#password" }
    ]
  }
]
```

---

## Stage 4: Code Generation

### Inputs
- Task / Question / Model / Page manifests
- `entity-map.json`
- Java templates from `templates/`

### Outputs (delivered to operator)

| File Type | Location | Description |
|---|---|---|
| Task classes | `src/test/java/.../tasks/` | Screenplay Task implementations |
| Question classes | `src/test/java/.../questions/` | Screenplay Question implementations |
| Model POJOs | `src/test/java/.../model/` | Request/response data classes |
| Page objects | `src/test/java/.../pages/` | UI element Target definitions (E2E) |
| Step definitions | `src/test/java/.../stepdefinitions/` | Cucumber glue code |

---

## Stage 5: Review

### Inputs
- All generated `.java` files
- All `.feature` files
- Requirements documents
- `.github/instructions/shared-conventions.md`

### Outputs

#### `review-report.md` (delivered to operator)
Structured PASS/FAIL/WARN findings for all checks.

#### `issue-list.json` (internal — used for feedback loop)
```json
[
  {
    "severity": "FAIL",
    "file": "api/tasks/createPet.java",
    "issue": "Class name is not PascalCase",
    "remediation": "Rename to CreatePet"
  }
]
```

---

## Final Deliverable

The operator receives:
1. All `.feature` files (Gherkin)
2. All `.java` source files (Tasks, Questions, Models, Pages, Step Defs)
3. `review-report.md` with overall result
4. `coverage-matrix-final.md` confirming requirement coverage
5. Instructions to place files and run `./gradlew test`

---

## What the Pipeline Does NOT Produce

- `build.gradle` or `settings.gradle` — already in the template, not regenerated.
- `serenity.conf` — configuration is operator-managed.
- Hook classes — already in the template; extend if new domains are added.
- Runner classes — already in the template; extend for new domains.
- `Constants` files — add new constants manually following the convention.
