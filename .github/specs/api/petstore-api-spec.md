# PetStore API Specification

**Source**: [Swagger PetStore v2](https://petstore.swagger.io/)  
**Base URL**: `https://petstore.swagger.io/v2`  
**Format**: OpenAPI 2.0 (Swagger)

---

## Overview

The PetStore API provides endpoints for managing pets, orders, and users in a sample pet store application. This spec focuses on the **Pet** resource for demonstration purposes.

---

## Authentication

The API supports two authentication mechanisms:

| Type | Description |
|---|---|
| `api_key` | API key via header `api_key` |
| `petstore_auth` | OAuth 2.0 implicit flow |

For testing, authentication is optional for most read operations.

---

## Entities

### Pet

```json
{
  "id": 0,
  "category": {
    "id": 0,
    "name": "string"
  },
  "name": "doggie",
  "photoUrls": ["string"],
  "tags": [
    { "id": 0, "name": "string" }
  ],
  "status": "available"
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `id` | `Long` | No | Auto-generated pet ID |
| `category` | Object | No | Pet category |
| `name` | `String` | **Yes** | Pet name |
| `photoUrls` | `List<String>` | **Yes** | List of photo URLs |
| `tags` | `List<Tag>` | No | List of tags |
| `status` | `String` | No | Pet status: `available`, `pending`, `sold` |

---

## Endpoints

### POST /pet

**Summary**: Add a new pet to the store.

**Request Body**: `Pet` (JSON)

**Responses**:
| Code | Description |
|---|---|
| 200 | Successful operation — returns the created Pet |
| 405 | Invalid input |

---

### GET /pet/{petId}

**Summary**: Find pet by ID.

**Path Parameters**:
| Name | Type | Required |
|---|---|---|
| `petId` | `Long` | Yes |

**Responses**:
| Code | Description |
|---|---|
| 200 | Successful operation — returns Pet |
| 400 | Invalid ID supplied |
| 404 | Pet not found |

---

### PUT /pet

**Summary**: Update an existing pet.

**Request Body**: `Pet` (JSON) — must include `id` field.

**Responses**:
| Code | Description |
|---|---|
| 200 | Successful operation |
| 400 | Invalid ID supplied |
| 404 | Pet not found |
| 405 | Validation exception |

---

### DELETE /pet/{petId}

**Summary**: Deletes a pet.

**Path Parameters**:
| Name | Type | Required |
|---|---|---|
| `petId` | `Long` | Yes |

**Responses**:
| Code | Description |
|---|---|
| 200 | Successful operation |
| 400 | Invalid pet value |

---

### GET /pet/findByStatus

**Summary**: Finds pets by status.

**Query Parameters**:
| Name | Type | Required | Allowed Values |
|---|---|---|---|
| `status` | `String` | Yes | `available`, `pending`, `sold` |

**Responses**:
| Code | Description |
|---|---|
| 200 | Successful operation — returns `List<Pet>` |
| 400 | Invalid status value |

---

## Test Scenarios (Candidates)

From the Spec Analyst's analysis, the following scenarios are recommended:

### Happy Path
1. Create a new pet with name and status → 200
2. Retrieve the created pet by ID → 200, returns correct name
3. Update the pet's status → 200
4. Delete the pet by ID → 200
5. Find pets by status "available" → 200, non-empty list

### Negative Cases
1. Create pet with missing name → 405
2. Get pet with non-existent ID (e.g., 0) → 404
3. Find pets with invalid status → 400

### Data-Driven
1. Create pets with each status value: `available`, `pending`, `sold`

---

## Notes

- The PetStore API is a public demo API. IDs are shared across all users — tests should use unique names.
- Responses may be slow or occasionally unreliable — add retry logic for CI environments.
- The `id` field returned in the POST response should be captured for subsequent GET/PUT/DELETE calls.
