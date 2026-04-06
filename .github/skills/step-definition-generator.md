# Skill: Step Definition Generator

## Purpose
Generate thin Cucumber step definition classes that wire Gherkin steps to Screenplay Tasks and Questions without containing business logic.

## Inputs
- Feature files (Gherkin)
- Task classes (for delegation)
- Question classes (for assertions)
- Model classes
- Java template: `templates/api/StepDefinition.java.template` or `templates/e2e/StepDefinition.java.template`

## Outputs
- Compilable `*StepDefinitions.java` files in `com.asdd.template.{domain}.stepdefinitions`

## Generation Steps
1. Parse each unique Gherkin step pattern from the feature files.
2. Determine if the step triggers a Task (`When`, `Given` actions) or a Question assertion (`Then`).
3. Map step parameters (`{string}`, `{int}`) to method parameters.
4. Generate the method with the appropriate Cucumber annotation.
5. Delegate to the correct Task or Question class.
6. Identify cross-step state requirements and add instance fields.

## Step Pattern to Annotation Mapping
| Keyword | Annotation |
|---|---|
| Given | `@Given` |
| When | `@When` |
| Then | `@Then` |
| And / But | `@And` or reuse the primary keyword's annotation |

## Cross-Step State Management
- Store the result of creation tasks (e.g., created pet ID) in instance fields.
- Never use static fields for state — this breaks parallel execution.
- Reset state in `@After` hooks if needed.

## Example Output Fragment
```java
@When("I create a pet with name {string} and status {string}")
public void iCreateAPet(String name, String status) {
    Actor actor = theActorInTheSpotlight();
    currentPet = new Pet().setName(name).setStatus(status).setPhotoUrls(List.of());
    actor.attemptsTo(CreatePet.with(currentPet));
}
```

## Constraints
- No direct `SerenityRest` or `WebDriver` calls.
- No business logic or branching — only delegation.
- No duplicate step patterns across step definition classes in the same glue package.
