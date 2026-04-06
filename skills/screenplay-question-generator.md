# Skill: Screenplay Question Generator

## Purpose
Generate compilable Java Screenplay `Question` classes that retrieve observable state from the system under test (HTTP responses or browser state).

## Inputs
- Question manifest (question name, return type, source)
- Model classes (for typed deserialization)
- Java template: `templates/api/Question.java.template` or `templates/e2e/Question.java.template`

## Outputs
- Compilable `*Question.java` files in `com.asdd.template.{domain}.questions`

## Generation Steps
1. Select correct template based on domain.
2. Substitute `{{QUESTION_NAME}}`, `{{RETURN_TYPE}}`, `{{VALUE_EXPRESSION}}` placeholders.
3. Add correct imports (`SerenityRest` for API; `BrowseTheWeb` for E2E).
4. Implement `answeredBy(Actor actor)` returning the specified expression.
5. Validate that no `attemptsTo` or `actor.can` calls appear.

## Question Type Reference

| Type | Return | Expression |
|---|---|---|
| HTTP Status Code | `Integer` | `SerenityRest.lastResponse().statusCode()` |
| Response Body String | `String` | `SerenityRest.lastResponse().body().asString()` |
| JSON Path Value | `String` | `SerenityRest.lastResponse().jsonPath().getString("field")` |
| Typed Response Body | `T` | `SerenityRest.lastResponse().as(T.class)` |
| Response Header | `String` | `SerenityRest.lastResponse().header("Header-Name")` |
| Current URL | `String` | `BrowseTheWeb.as(actor).getDriver().getCurrentUrl()` |
| Page Title | `String` | `BrowseTheWeb.as(actor).getDriver().getTitle()` |
| Element Text | `String` | `Text.of(target).answeredBy(actor)` |

## Constraints
- No assertions in `answeredBy`.
- No calls to `actor.attemptsTo(...)`.
- Questions must be stateless and idempotent.
