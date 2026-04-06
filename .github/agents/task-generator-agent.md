# Task Generator Agent

## Purpose

The Task Generator Agent creates Screenplay `Task` Java classes based on the Task Manifest produced by the Screenplay Architect Agent. Each generated task encapsulates a single interaction with the system under test.

## Responsibilities

1. **Class Generation** – Produce compilable Java `Task` implementations for each entry in the Task Manifest.
2. **Static Factory Methods** – Include at least one descriptive static factory method per class.
3. **REST Action Mapping** – Map HTTP operations to the correct Serenity Screenplay REST interactions (`Get`, `Post`, `Put`, `Delete`, `Patch`).
4. **Parameter Handling** – Accept task parameters as constructor arguments; store as `final` fields.
5. **Instrumentation** – Apply `@Step` annotations where they add diagnostic value in Serenity reports.
6. **Delegation** – Tasks call `actor.attemptsTo(...)` with Serenity interaction objects only — no assertions.

## Inputs

| Input | Source |
|---|---|
| Task Manifest | Screenplay Architect Agent |
| Model Classes | Model Generator Agent |
| Constants | `ApiConstants.java` / `E2eConstants.java` |
| Java Templates | `templates/api/Task.java.template` or `templates/e2e/Task.java.template` |

## Outputs

| Output | Description |
|---|---|
| `*Task*.java` files | Compilable Task class files placed in the correct package |

## REST Action Mapping Reference

| HTTP Method | Serenity Interaction | Import |
|---|---|---|
| GET | `Get.resource(path)` | `net.serenitybdd.screenplay.rest.interactions.Get` |
| POST | `Post.to(path)` | `net.serenitybdd.screenplay.rest.interactions.Post` |
| PUT | `Put.to(path)` | `net.serenitybdd.screenplay.rest.interactions.Put` |
| DELETE | `Delete.from(path)` | `net.serenitybdd.screenplay.rest.interactions.Delete` |
| PATCH | `Patch.to(path)` | `net.serenitybdd.screenplay.rest.interactions.Patch` |

## Generation Rules

- Class name = `PascalCase` verb + noun (e.g., `CreatePet`, `GetPet`, `UpdatePetStatus`).
- Package = `com.asdd.template.{domain}.tasks`.
- All imports must be fully qualified and resolvable.
- `performAs(T actor)` must call `actor.attemptsTo(...)` — never call REST clients directly.
- Path parameters go via `.with(request -> request.pathParam("key", value))`.
- Request bodies go via `.with(spec -> spec.body(obj).contentType(ContentType.JSON))`.
- Use `List.of(...)` for immutable collections where applicable.

## Example Generated Output

```java
package com.asdd.template.api.tasks;

import com.asdd.template.api.model.Pet;
import com.asdd.template.api.util.ApiConstants;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

public class CreatePet implements Task {

    private final Pet pet;

    public CreatePet(Pet pet) { this.pet = pet; }

    public static CreatePet with(Pet pet) { return new CreatePet(pet); }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Post.to(ApiConstants.PET_ENDPOINT)
                .with(spec -> spec.body(pet).contentType(ContentType.JSON))
        );
    }
}
```

## Constraints

- Must not include assertions inside tasks.
- Must not read from `SerenityRest.lastResponse()` inside a task (that belongs in Questions).
- Must produce a compilable class with correct imports.
