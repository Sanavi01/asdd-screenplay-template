# Skill: Screenplay Task Generator

## Purpose
Generate compilable Java Screenplay `Task` classes from a task manifest, applying correct Serenity BDD REST or WebDriver interactions.

## Inputs
- Task manifest (task name, parameters, HTTP method/path or UI interaction)
- Model classes (for type references)
- Constants classes (for endpoint/URL values)
- Java template: `templates/api/Task.java.template` or `templates/e2e/Task.java.template`

## Outputs
- Compilable `*Task.java` files placed in `com.asdd.template.{domain}.tasks`

## Generation Steps
1. Select the correct template based on domain (api/e2e).
2. Substitute `{{TASK_NAME}}`, `{{PACKAGE}}`, `{{INTERACTION}}` placeholders.
3. Add imports for the Serenity interaction class used.
4. Add imports for model and constants classes referenced.
5. Write the `performAs` method delegating to `actor.attemptsTo(...)`.
6. Validate that no assertions appear in the generated code.

## REST Interaction Mapping
| HTTP Method | Serenity Class |
|---|---|
| GET | `Get.resource(path)` |
| POST | `Post.to(path)` |
| PUT | `Put.to(path)` |
| DELETE | `Delete.from(path)` |
| PATCH | `Patch.to(path)` |

## E2E Interaction Mapping
| Action | Serenity Class |
|---|---|
| Navigate | `Open.url(url)` |
| Click | `Click.on(target)` |
| Type | `Enter.theValue(v).into(target)` |
| Select | `SelectFromOptions.byVisibleText(v).from(target)` |

## Constraints
- No assertions in `performAs`.
- No direct RestAssured or WebDriver calls.
- All parameters must be `final` fields.
