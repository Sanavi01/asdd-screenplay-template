# API Requirements Template

Use this template to capture API test requirements before running the ASDD pipeline.

---

## Meta

| Field | Value |
|---|---|
| **API Name** | _(e.g., Pet Store API)_ |
| **Version** | _(e.g., v2)_ |
| **Base URL** | _(e.g., https://petstore.swagger.io/v2)_ |
| **Spec Location** | _(e.g., specs/api/petstore-api-spec.md or URL)_ |
| **Author** | _(your name)_ |
| **Date** | _(YYYY-MM-DD)_ |

---

## Authentication

| Field | Value |
|---|---|
| **Auth Type** | _(None / API Key / Bearer Token / OAuth2 / Basic)_ |
| **Auth Header/Param** | _(e.g., Authorization: Bearer {token})_ |
| **How to Obtain** | _(e.g., POST /auth/login with credentials)_ |

---

## Functional Requirements

List each requirement with a unique ID. The Coverage Analyzer uses these IDs.

### CRUD Operations

| ID | Description | Priority | Endpoint | Method |
|---|---|---|---|---|
| REQ-API-001 | Create a new resource successfully | High | /pet | POST |
| REQ-API-002 | Create resource with missing required field returns 400 | High | /pet | POST |
| REQ-API-003 | Retrieve resource by valid ID returns 200 | High | /pet/{id} | GET |
| REQ-API-004 | Retrieve resource by non-existent ID returns 404 | Medium | /pet/{id} | GET |
| REQ-API-005 | Update existing resource successfully returns 200 | High | /pet | PUT |
| REQ-API-006 | Delete existing resource successfully returns 200 | High | /pet/{id} | DELETE |
| REQ-API-007 | Delete non-existent resource returns 404 | Medium | /pet/{id} | DELETE |

### Validation Requirements

| ID | Description | Priority | Field | Rule |
|---|---|---|---|---|
| REQ-API-010 | Status field must be one of allowed enum values | High | status | enum: available, pending, sold |
| REQ-API-011 | Name field is required | High | name | required, non-empty |
| REQ-API-012 | ID is auto-generated on creation | Medium | id | read-only |

### Search / Filter Requirements

| ID | Description | Priority | Endpoint | Parameters |
|---|---|---|---|---|
| REQ-API-020 | Find pets by status returns filtered results | Medium | /pet/findByStatus | status (query param) |
| REQ-API-021 | Invalid status parameter returns 400 | Medium | /pet/findByStatus | status = "invalid" |

---

## Non-Functional Requirements

| ID | Description | Priority |
|---|---|---|
| REQ-NFR-001 | All API responses must be returned within 2 seconds | Medium |
| REQ-NFR-002 | API must accept and return application/json | High |

---

## Test Data Requirements

| Entity | Field | Valid Values | Invalid Values | Required |
|---|---|---|---|---|
| Pet | name | Any non-empty string | null, empty | Yes |
| Pet | status | available, pending, sold | deleted, unknown | Yes |
| Pet | photoUrls | List of URL strings | — | Yes |
| Pet | id | Auto-generated Long | — | No (generated) |

---

## Out of Scope

List anything explicitly NOT to be tested:
- _(e.g., Performance testing)_
- _(e.g., Security penetration testing)_

---

## Notes

_(Any additional context, known issues, or special instructions for the agents)_
