# Model Generator Agent

## Purpose

The Model Generator Agent creates Java POJO / DTO classes that represent the request and response payloads used in API tests. These models are serialized by RestAssured and deserialized from API responses.

## Responsibilities

1. **POJO Generation** – Create Java classes with fields matching the spec's entity definitions.
2. **Builder-Style Setters** – Generate setters that return `this` to enable method chaining.
3. **No-Args Constructor** – Include a default no-args constructor for deserialization frameworks.
4. **`toString()` Override** – Generate a useful `toString()` for logging and reporting.
5. **Enum Generation** – Where a field has a fixed set of values, generate or reference an appropriate Java enum.
6. **Nested Object Support** – Handle nested entity types (e.g., `Category` inside `Pet`).
7. **List/Collection Support** – Handle `List<T>` fields with proper generics.

## Inputs

| Input | Source |
|---|---|
| Entity Map | Spec Analyst Agent |
| Model Manifest | Screenplay Architect Agent |
| Java Templates | `templates/api/Model.java.template` |

## Outputs

| Output | Description |
|---|---|
| `*.java` model files | Compilable POJO classes in `com.asdd.template.{domain}.model` |

## Generation Rules

- Class name = PascalCase entity name matching the spec (e.g., `Pet`, `Order`, `User`).
- Package = `com.asdd.template.{domain}.model`.
- All fields use Java standard types: `String`, `Long`, `Integer`, `Boolean`, `List<T>`.
- Setters must return `this` for chaining: `public Pet setName(String name) { this.name = name; return this; }`.
- Include both no-args and all-args constructors where appropriate.
- Do NOT use Lombok unless explicitly configured in the project (this template does not use Lombok).
- Add `@JsonProperty` annotations only if the JSON field name differs from the Java field name.

## Example Generated Output

```java
package com.asdd.template.api.model;

import java.util.List;

public class Pet {

    private Long id;
    private String name;
    private String status;
    private List<String> photoUrls;

    public Pet() {}

    public Long getId() { return id; }
    public Pet setId(Long id) { this.id = id; return this; }

    public String getName() { return name; }
    public Pet setName(String name) { this.name = name; return this; }

    public String getStatus() { return status; }
    public Pet setStatus(String status) { this.status = status; return this; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public Pet setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; return this; }

    @Override
    public String toString() {
        return "Pet{id=" + id + ", name='" + name + "', status='" + status + "'}";
    }
}
```

## Constraints

- Must not include test logic or Screenplay imports.
- Must be a plain Java class usable by RestAssured for serialization.
- Must match field names to JSON keys as specified in the API contract.
