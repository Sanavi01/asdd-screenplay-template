# Skill: Spec Analyzer

## Purpose
Parse and normalize a raw specification document into a structured representation consumable by downstream agents.

## Inputs
- Raw specification (OpenAPI YAML/JSON, natural language Markdown, or Gherkin)
- Optional: existing entity map for incremental updates

## Outputs
- Structured spec JSON
- Entity map (field names, types, constraints)
- List of scenario candidates
- List of ambiguities requiring clarification

## Algorithm
1. Detect input format (OpenAPI, Markdown, Gherkin, plain text).
2. Extract all nouns → candidate entities.
3. Extract all verbs + nouns → candidate operations.
4. For OpenAPI: parse `paths`, `components/schemas`, and `responses`.
5. For natural language: use pattern matching for "should", "must", "when", "given".
6. Output a normalized JSON structure.

## Example Usage

**Input**: OpenAPI spec with `/pet` POST endpoint and `Pet` schema.

**Output**:
```json
{
  "entities": { "Pet": { "id": "Long", "name": "String", "status": "String" } },
  "endpoints": [{ "method": "POST", "path": "/pet", "requestBody": "Pet" }],
  "scenarioCandidates": ["Create a new pet successfully", "Create pet with missing required field"]
}
```

## Constraints
- Must handle malformed specs gracefully — flag errors, do not crash.
- Must preserve all enum values verbatim.
- Output must be valid JSON.
