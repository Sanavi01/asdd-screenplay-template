# E2E Requirements Template

Use this template to capture E2E test requirements before running the ASDD pipeline.

---

## Meta

| Field | Value |
|---|---|
| **Application Name** | _(e.g., The Internet Demo App)_ |
| **Base URL** | _(e.g., https://the-internet.herokuapp.com)_ |
| **Spec Location** | _(e.g., specs/e2e/sample-webapp-spec.md)_ |
| **Browser** | _(e.g., Chrome)_ |
| **Author** | _(your name)_ |
| **Date** | _(YYYY-MM-DD)_ |

---

## User Roles

| Role | Description | Test Credentials |
|---|---|---|
| Standard User | Registered user with standard access | username: tomsmith, password: SuperSecretPassword! |
| Admin User | User with elevated privileges | _(if applicable)_ |
| Guest | Unauthenticated visitor | _(no credentials)_ |

---

## Feature Areas

### Authentication

| ID | Description | Priority | User Role |
|---|---|---|---|
| REQ-E2E-001 | Successful login with valid credentials | High | Standard User |
| REQ-E2E-002 | Login fails with invalid password | High | Standard User |
| REQ-E2E-003 | Login fails with invalid username | High | Standard User |
| REQ-E2E-004 | Successful logout from secure area | High | Standard User |
| REQ-E2E-005 | Login page displays correct elements (form, title) | Medium | Guest |

### Navigation

| ID | Description | Priority |
|---|---|---|
| REQ-E2E-010 | After login, user is redirected to secure area | High |
| REQ-E2E-011 | Secure area displays correct welcome message | Medium |
| REQ-E2E-012 | After logout, user is returned to login page | High |

### Error Handling

| ID | Description | Priority |
|---|---|---|
| REQ-E2E-020 | Invalid login shows error flash message | High |
| REQ-E2E-021 | Error message disappears after dismissal | Low |

---

## Page Inventory

| Page | URL Path | Key Elements |
|---|---|---|
| Login Page | `/login` | Username field (`#username`), Password field (`#password`), Submit button |
| Secure Area | `/secure` | Flash message (`#flash`), Logout button |

---

## UI Element Reference

| Page | Element | Locator | Type |
|---|---|---|---|
| Login | Username field | `#username` | Input |
| Login | Password field | `#password` | Input |
| Login | Login button | `button[type='submit']` | Button |
| Secure Area | Flash message | `#flash` | Div |
| Secure Area | Logout button | `a.button[href='/logout']` | Link |

---

## Test Data Requirements

| Scenario | Username | Password | Expected Result |
|---|---|---|---|
| Valid login | tomsmith | SuperSecretPassword! | Redirect to /secure |
| Invalid password | tomsmith | wrongpassword | Error flash message |
| Invalid username | invalid | wrongpassword | Error flash message |

---

## Browser Requirements

| Browser | Version | Headless Supported |
|---|---|---|
| Chrome | Latest stable | Yes (add `--headless`) |
| Firefox | Latest stable | Yes (add `-headless`) |
| Edge | Latest stable | Yes |

---

## Out of Scope

- Mobile/responsive layout testing
- Performance / load testing
- Cross-browser matrix (test in Chrome only by default)

---

## Notes

_(Any additional context, known issues, or special instructions for the agents)_
