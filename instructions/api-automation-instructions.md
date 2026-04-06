# API Automation Instructions

These instructions guide agents in generating API test automation using Java, Serenity BDD, Screenplay Pattern, and Cucumber.

---

## 1. Reading the Spec

When given an API specification:

1. **Identify the base URL** — store in `ApiConstants.BASE_URL` as a system property override.
2. **List all endpoints** — method, path, path parameters, query parameters, request body, response codes.
3. **Extract entity schemas** — create a POJO for every request/response body that has more than 2 fields.
4. **Note authentication** — if the API requires authentication, add it as an ability or request filter.
5. **Identify status codes** — note which codes mean success (2xx) and which mean client/server errors.

---

## 2. What to Generate

For each API specification, generate:

| Artifact | Location | One Per |
|---|---|---|
| Feature file | `src/test/resources/features/api/` | Functional area |
| Task class | `src/test/java/.../api/tasks/` | HTTP operation |
| Question class | `src/test/java/.../api/questions/` | Observable value |
| Model POJO | `src/test/java/.../api/model/` | Request/response entity |
| Step definitions | `src/test/java/.../api/stepdefinitions/` | Feature file |
| Constants | `src/test/java/.../api/util/ApiConstants.java` | Once (add new constants) |

---

## 3. Naming Rules

| Class Type | Pattern | Example |
|---|---|---|
| Task | `VerbNoun` | `CreatePet`, `GetPet`, `UpdatePetStatus`, `DeletePet` |
| Question | Noun phrase | `ResponseStatusCode`, `PetName`, `ResponseBody` |
| Model | Entity name | `Pet`, `Order`, `ApiError` |
| Step Definitions | `{Feature}StepDefinitions` | `PetStoreStepDefinitions` |
| Constants | `{Domain}Constants` | `ApiConstants` |

---

## 4. Screenplay Patterns for REST

### Task Pattern (REST)

```java
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

### Question Pattern (REST)

```java
public class ResponseStatusCode implements Question<Integer> {

    public static ResponseStatusCode ofTheLastResponse() { return new ResponseStatusCode(); }

    @Override
    public Integer answeredBy(Actor actor) {
        return SerenityRest.lastResponse().statusCode();
    }
}
```

### Step Definition Pattern

```java
@When("I create a pet with name {string} and status {string}")
public void iCreateAPet(String name, String status) {
    Actor actor = theActorInTheSpotlight();
    Pet pet = new Pet().setName(name).setStatus(status).setPhotoUrls(List.of());
    actor.attemptsTo(CreatePet.with(pet));
}

@Then("the response status code should be {int}")
public void theResponseStatusCodeShouldBe(int expected) {
    assertThat(theActorInTheSpotlight().asksFor(ResponseStatusCode.ofTheLastResponse()))
        .isEqualTo(expected);
}
```

---

## 5. REST Interaction Reference

```java
// GET with path param
Get.resource("/pet/{id}").with(req -> req.pathParam("id", petId))

// POST with body
Post.to("/pet").with(spec -> spec.body(pet).contentType(ContentType.JSON))

// PUT with body
Put.to("/pet").with(spec -> spec.body(pet).contentType(ContentType.JSON))

// DELETE with path param
Delete.from("/pet/{id}").with(req -> req.pathParam("id", petId))

// GET with query param
Get.resource("/pet/findByStatus").with(req -> req.queryParam("status", "available"))
```

---

## 6. Actor and Ability Setup (ApiHooks)

```java
@Before("@api")
public void setUp() {
    setTheStage(new OnlineCast());
    Actor actor = theActorCalled("API User");
    actor.can(CallAnApi.at(ApiConstants.BASE_URL));
}
```

---

## 7. Cross-Step State

When a scenario needs to use the ID of a created resource in subsequent steps:

```java
// In step definition class — instance field, NOT static
private Pet currentPet;

@When("I create a pet ...")
public void iCreateAPet(...) {
    actor.attemptsTo(CreatePet.with(pet));
    // Extract ID from response and store
    Long id = SerenityRest.lastResponse().jsonPath().getLong("id");
    currentPet.setId(id);
}

@When("I retrieve the pet by id")
public void iRetrieveThePetById() {
    actor.attemptsTo(GetPet.withId(currentPet.getId()));
}
```

---

## 8. Content Types

- Always set `contentType(ContentType.JSON)` on POST/PUT requests.
- Always set `accept(ContentType.JSON)` on GET requests where JSON response is expected (optional but recommended).

---

## 9. Assertions

Use AssertJ in step definitions only:

```java
assertThat(actor.asksFor(ResponseStatusCode.ofTheLastResponse())).isEqualTo(200);
assertThat(actor.asksFor(ResponseBody.ofTheLastResponse())).contains("Buddy");
```

---

## 10. Authentication Patterns

For Bearer token authentication:
```java
Get.resource(ApiConstants.PET_ENDPOINT)
   .with(req -> req.header("Authorization", "Bearer " + token))
```

For API key:
```java
Get.resource(ApiConstants.PET_ENDPOINT)
   .with(req -> req.queryParam("api_key", ApiConstants.API_KEY))
```
