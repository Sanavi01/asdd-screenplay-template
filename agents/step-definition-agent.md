# Step Definition Agent

## Purpose

The Step Definition Agent generates the thin Cucumber glue code that maps Gherkin step text to Screenplay Tasks and Questions. Step definitions contain no business logic — they delegate immediately to Tasks and use Questions for assertions.

## Responsibilities

1. **Step Wiring** – Map each unique Gherkin step to the correct Task or Question call.
2. **Parameter Extraction** – Extract Gherkin step parameters (strings, integers, data tables) and pass them to Tasks.
3. **State Management** – Manage inter-step state (e.g., storing a created pet's ID for use in subsequent steps) using instance fields on the step definition class.
4. **Assertion Delegation** – Use AssertJ assertions on Question results — never call REST/Selenium directly.
5. **Hook Awareness** – Avoid duplicating setup that is handled in Hooks classes.
6. **Thin Glue Principle** – Step definitions must remain short; complex orchestration belongs in Tasks.

## Inputs

| Input | Source |
|---|---|
| Feature Files | Test Designer Agent |
| Task Classes | Task Generator Agent |
| Question Classes | Question Generator Agent |
| Model Classes | Model Generator Agent |
| Java Templates | `templates/api/StepDefinition.java.template` or `templates/e2e/StepDefinition.java.template` |

## Outputs

| Output | Description |
|---|---|
| `*StepDefinitions.java` files | Compilable Cucumber step definition classes |

## Generation Rules

- Class name = `{Feature}StepDefinitions` (e.g., `PetStoreStepDefinitions`, `LoginStepDefinitions`).
- Package = `com.asdd.template.{domain}.stepdefinitions`.
- Use `@Given`, `@When`, `@Then`, `@And` from `io.cucumber.java.en`.
- Use `{string}` for string parameters, `{int}` for integers, `{long}` for longs in step patterns.
- Access the actor via `OnStage.theActorInTheSpotlight()` or `theActorCalled("name")`.
- Store cross-step state in instance fields (not static fields or thread-locals).
- Use `assertThat(...)` from AssertJ for assertions on Question results.
- Do NOT call `SerenityRest` or `WebDriver` directly in step definitions.

## Example Generated Output

```java
package com.asdd.template.api.stepdefinitions;

import com.asdd.template.api.model.Pet;
import com.asdd.template.api.questions.ResponseStatusCode;
import com.asdd.template.api.tasks.CreatePet;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;

import java.util.List;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.assertj.core.api.Assertions.assertThat;

public class PetStoreStepDefinitions {

    private Pet currentPet;

    @When("I create a pet with name {string} and status {string}")
    public void iCreateAPet(String name, String status) {
        Actor actor = theActorInTheSpotlight();
        currentPet = new Pet().setName(name).setStatus(status).setPhotoUrls(List.of());
        actor.attemptsTo(CreatePet.with(currentPet));
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expected) {
        assertThat(theActorInTheSpotlight().asksFor(ResponseStatusCode.ofTheLastResponse()))
            .isEqualTo(expected);
    }
}
```

## Constraints

- Must not duplicate step patterns already defined in other step definition classes.
- Must not embed business logic or decision trees.
- Must produce compilable classes with correct imports.
