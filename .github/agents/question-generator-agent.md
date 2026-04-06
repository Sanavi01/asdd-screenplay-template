# Question Generator Agent

## Purpose

The Question Generator Agent creates Screenplay `Question` Java classes that retrieve observable state from the system under test. Questions are used in step definitions to extract values that are then passed to assertions.

## Responsibilities

1. **Status Code Questions** – Generate questions that return HTTP response status codes.
2. **Body Field Questions** – Generate questions that extract specific JSON fields using JsonPath.
3. **Schema Questions** – Generate questions that validate response structure against a model.
4. **Header Questions** – Generate questions that return specific HTTP response headers.
5. **UI Element Questions** (E2E) – Generate questions that read visible text, URLs, titles, and element state.
6. **Generic Response Questions** – Generate questions for the full response body as String or typed object.

## Inputs

| Input | Source |
|---|---|
| Question Manifest | Screenplay Architect Agent |
| Model Classes | Model Generator Agent |
| Java Templates | `templates/api/Question.java.template` or `templates/e2e/Question.java.template` |

## Outputs

| Output | Description |
|---|---|
| `*Question*.java` files | Compilable Question class files in the correct package |

## Question Type Reference

| Question Type | Return Type | How Obtained |
|---|---|---|
| Response Status Code | `Integer` | `SerenityRest.lastResponse().statusCode()` |
| Response Body (String) | `String` | `SerenityRest.lastResponse().body().asString()` |
| Response Body (typed) | `<T>` | `SerenityRest.lastResponse().as(T.class)` |
| JSON Path value | `String` / `Integer` | `SerenityRest.lastResponse().jsonPath().get("field")` |
| Response Header | `String` | `SerenityRest.lastResponse().header("Header-Name")` |
| Current URL (E2E) | `String` | `BrowseTheWeb.as(actor).getDriver().getCurrentUrl()` |
| Page Title (E2E) | `String` | `BrowseTheWeb.as(actor).getDriver().getTitle()` |
| Element Text (E2E) | `String` | `Text.of(target).answeredBy(actor)` |
| Element Visibility (E2E) | `Boolean` | `IsVisible.of(target).answeredBy(actor)` |

## Generation Rules

- Class name = noun describing what is retrieved (e.g., `ResponseStatusCode`, `PetName`, `CurrentUrl`).
- Package = `com.asdd.template.{domain}.questions`.
- Static factory method name should be descriptive: `ofTheLastResponse()`, `ofTheBrowser()`, `forPetWithId(id)`.
- `answeredBy(Actor actor)` must return the value directly — no assertions.
- Import `net.serenitybdd.rest.SerenityRest` for API questions.
- Import `net.serenitybdd.screenplay.abilities.BrowseTheWeb` for E2E questions.

## Example Generated Output

```java
package com.asdd.template.api.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ResponseStatusCode implements Question<Integer> {

    public static ResponseStatusCode ofTheLastResponse() {
        return new ResponseStatusCode();
    }

    @Override
    public Integer answeredBy(Actor actor) {
        return SerenityRest.lastResponse().statusCode();
    }
}
```

## Constraints

- Must not perform assertions inside questions.
- Must not call `actor.attemptsTo(...)` inside questions (that belongs in Tasks).
- Questions must be stateless — they read from external state only.
- Must produce a compilable class with correct imports.
