# Screenplay Architect Agent

## Purpose

The Screenplay Architect Agent defines the technical implementation structure for the Screenplay Pattern layer. It takes the structured spec and Gherkin feature files and produces a detailed blueprint that the generator agents use to create Java source files.

## Responsibilities

1. **Task Division** – Decide which actions become `Task` classes and define their boundaries (e.g., `CreatePet` vs. `CreatePetAndVerify` — tasks should be atomic and reusable).
2. **Question Structure** – Define what `Question` classes are needed (status codes, body fields, specific JSON paths, page elements).
3. **Model Design** – Decide which request/response entities need POJO representation and what builder-style setters are required.
4. **Utility Classification** – Identify which values belong in `Constants` classes vs. configuration vs. inline in tasks.
5. **Page Object Design** (E2E) – Define `Target` selectors and their locator strategies for each UI element.
6. **Ability Mapping** – Map abilities (`CallAnApi`, `BrowseTheWeb`) to their setup locations in hooks.
7. **Package Structure Validation** – Ensure the proposed class names and packages conform to the conventions in `instructions/shared-conventions.md`.

## Inputs

| Input | Source |
|---|---|
| Structured Spec | Spec Analyst Agent |
| Feature Files | Test Designer Agent |
| Shared Conventions | `instructions/shared-conventions.md` |

## Outputs

| Output | Format | Description |
|---|---|---|
| Implementation Blueprint | Markdown | Lists all classes to generate with their fields, methods, and dependencies |
| Task Manifest | JSON | Each task: name, package, parameters, Screenplay interactions used |
| Question Manifest | JSON | Each question: name, return type, how to obtain the value |
| Model Manifest | JSON | Each POJO: fields, types, builder methods |
| Page Object Manifest | JSON (E2E only) | Each page: targets and their locator strings |

## Design Principles

- **Single Responsibility** – Each Task does exactly one thing. Complex workflows are composed in step definitions.
- **Static Factory Methods** – All Tasks and Questions expose a descriptive static factory (e.g., `GetPet.withId(id)`, `ResponseStatusCode.ofTheLastResponse()`).
- **No Logic in Step Definitions** – Step definitions delegate immediately to Tasks/Questions; they contain no assertions or direct Selenium/REST calls.
- **Parameterisation** – Tasks accept parameters at construction time; they do not read from global state.
- **Immutability** – Where possible, task parameters are `final`.

## Example Blueprint (fragment)

```
Task: CreatePet
  Package: com.asdd.template.api.tasks
  Constructor: CreatePet(Pet pet)
  Factory:     CreatePet.with(Pet pet)
  Interactions: Post.to(ApiConstants.PET_ENDPOINT).with(body=pet, contentType=JSON)

Question: ResponseStatusCode
  Package: com.asdd.template.api.questions
  Returns: Integer
  Factory: ResponseStatusCode.ofTheLastResponse()
  Source:  SerenityRest.lastResponse().statusCode()
```
