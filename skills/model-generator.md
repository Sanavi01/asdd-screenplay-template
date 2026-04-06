# Skill: Model Generator

## Purpose
Generate plain Java POJO classes representing request/response models from entity maps extracted by the Spec Analyzer.

## Inputs
- Entity map (field names, types, constraints)
- Model manifest from Screenplay Architect
- Java template: `templates/api/Model.java.template`

## Outputs
- Compilable POJO `.java` files in `com.asdd.template.{domain}.model`

## Generation Steps
1. For each entity in the entity map, create a POJO class.
2. Add a no-args constructor for JSON deserialization.
3. Add getter for each field (standard `getXxx()` naming).
4. Add builder-style setter for each field (returns `this`).
5. Override `toString()` to include all fields.
6. If a field has an enum constraint, reference or generate an inner enum.
7. Handle `List<T>` fields with proper generics.

## Field Type Mapping

| Spec Type | Java Type |
|---|---|
| string | `String` |
| integer (32-bit) | `Integer` |
| integer (64-bit) / long | `Long` |
| number / float | `Double` |
| boolean | `Boolean` |
| array of string | `List<String>` |
| array of object | `List<ModelType>` |
| object | Nested POJO class |

## Example Output Fragment
```java
public class Pet {
    private Long id;
    private String name;

    public Pet() {}
    public Long getId() { return id; }
    public Pet setId(Long id) { this.id = id; return this; }
    public String getName() { return name; }
    public Pet setName(String name) { this.name = name; return this; }
}
```

## Constraints
- No Lombok annotations (not configured in this template).
- No Screenplay imports.
- Field names must match JSON keys in the API contract.
- `toString()` must include all fields for traceability in reports.
