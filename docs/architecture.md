# Architecture

## Overview

ASDD (Agentic Spec-Driven Development) is an AI-assisted framework that generates production-ready test automation code from specifications. The architecture consists of four layers: **Specification**, **Orchestration**, **Implementation**, and **Quality**.

---

## Architectural Layers

```
┌─────────────────────────────────────────────────────────┐
│                   SPECIFICATION LAYER                    │
│  specs/        requirements/       OpenAPI/Gherkin/text  │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                  ORCHESTRATION LAYER                     │
│  agents/           skills/          instructions/        │
│                                                          │
│  Orchestrator ──► Spec Analyst ──► Test Designer         │
│       │                                ▼                 │
│       │              Screenplay Architect                 │
│       │         ┌─────────┼──────────┐                   │
│       │     Task Gen  Question Gen  Model Gen  StepDef   │
│       │                                                   │
│       └──────────────────► Review Agent                   │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                 IMPLEMENTATION LAYER                     │
│  src/test/java/com/asdd/template/                        │
│                                                          │
│  ├── core/          (hooks, constants)                   │
│  ├── api/           (tasks, questions, model, steps)     │
│  └── e2e/           (tasks, questions, pages, steps)     │
│                                                          │
│  src/test/resources/                                     │
│  ├── features/api/  (.feature files)                     │
│  ├── features/e2e/  (.feature files)                     │
│  └── serenity.conf                                       │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                    QUALITY LAYER                         │
│  Review Agent  ──  Naming Check  ──  Coverage Analysis   │
│  Contract Check  ──  Screenplay Compliance Check         │
└─────────────────────────────────────────────────────────┘
```

---

## Technology Stack

| Component | Technology | Version |
|---|---|---|
| Language | Java | 17 |
| Build Tool | Gradle | 8.5 |
| Test Framework | Serenity BDD | 4.1.5 |
| BDD Layer | Cucumber | 7.15.0 |
| Pattern | Screenplay Pattern | — |
| API Testing | Serenity Screenplay REST | 4.1.5 |
| E2E Testing | Serenity Screenplay WebDriver | 4.1.5 |
| Assertions | AssertJ | 3.25.1 |
| Test Runner | JUnit Platform | 1.10.1 |
| Logging | Logback | 1.4.14 |
| Reporting | Serenity Single-Page HTML | — |

---

## Screenplay Pattern Architecture

The Screenplay Pattern organises test code into three categories of objects:

```
Actor
  │
  ├── Abilities (what the actor CAN do)
  │     ├── CallAnApi (REST)
  │     └── BrowseTheWeb (WebDriver)
  │
  ├── Tasks (what the actor DOES — actions that change state)
  │     ├── CreatePet / GetPet / UpdatePet / DeletePet  (API)
  │     └── NavigateTo / Login / Logout                 (E2E)
  │
  └── Questions (what the actor OBSERVES — read-only)
        ├── ResponseStatusCode / ResponseBody           (API)
        └── CurrentUrl / PageTitle                      (E2E)
```

Step definitions wire Gherkin to Tasks and Questions. Page objects hold locators.

---

## Data Flow

```
Spec/Requirements
     │
     ▼
Spec Analyst → Entity Map + Scenario Candidates
     │
     ▼
Test Designer → Feature Files (.feature)
     │
     ▼
Screenplay Architect → Implementation Blueprint
     │
     ├──► Task Generator → *Task.java
     ├──► Question Generator → *Question.java
     ├──► Model Generator → *Model.java
     └──► Step Def Generator → *StepDefinitions.java
     │
     ▼
Review Agent → Review Report (PASS/FAIL/WARN)
     │
     ▼ (if FAIL)
Back to appropriate generator
     │
     ▼ (if all PASS)
Final deliverable: compilable, runnable test suite
```

---

## Package Architecture

```
com.asdd.template
├── core
│   ├── hooks.BaseHooks        — abstract lifecycle methods
│   └── util.Constants         — shared constants
├── api
│   ├── hooks.ApiHooks         — OnlineCast + CallAnApi setup
│   ├── model.Pet              — request/response POJO
│   ├── questions.*            — ResponseStatusCode, ResponseBody
│   ├── runners.ApiTestRunner  — JUnit Suite runner
│   ├── stepdefinitions.*      — Cucumber glue
│   ├── tasks.*                — CreatePet, GetPet, UpdatePet, DeletePet
│   └── util.ApiConstants      — base URL, endpoint paths
└── e2e
    ├── hooks.E2eHooks         — BrowseTheWeb setup
    ├── model.UserCredentials  — test data model
    ├── pages.*                — LoginPage, DashboardPage
    ├── questions.*            — CurrentUrl, PageTitle
    ├── runners.E2eTestRunner  — JUnit Suite runner
    ├── stepdefinitions.*      — Cucumber glue
    ├── tasks.*                — NavigateTo, Login, Logout
    └── util.E2eConstants      — base URL, page paths
```

---

## Configuration Architecture

```
serenity.conf
    environments
        default
            webdriver.base.url  →  E2eConstants.BASE_URL (overridden by -De2e.base.url)
            api.base.url        →  ApiConstants.BASE_URL  (overridden by -Dapi.base.url)
```

Runners use JUnit Platform Suite, selecting features by classpath resource path.  
Tags filter which scenarios execute via `-Dcucumber.filter.tags`.
