# Sample Web Application E2E Specification

**Application**: The Internet (Herokuapp Demo)  
**Base URL**: `https://the-internet.herokuapp.com`  
**Purpose**: Demonstrates common UI test patterns — used as a reference implementation for this template.

---

## Overview

"The Internet" is a publicly available test application by Dave Haeffner that provides a variety of UI patterns commonly encountered in web automation. This spec focuses on the **Login** feature.

---

## Feature: User Authentication

### User Journey

```
User visits /login
    → Enters username and password
    → Clicks the Login button
    → If credentials valid: redirected to /secure with success flash
    → If credentials invalid: stays on /login with error flash
```

---

## Pages

### Login Page (`/login`)

| Element | Locator | Type | Purpose |
|---|---|---|---|
| Page title | `h2` | Text | Should read "Login Page" |
| Username field | `#username` | Input[text] | Username entry |
| Password field | `#password` | Input[password] | Password entry |
| Login button | `button[type='submit']` | Button | Submits the form |

### Secure Area (`/secure`)

| Element | Locator | Type | Purpose |
|---|---|---|---|
| Flash message | `#flash` | Div | Success or error notification |
| Page heading | `h2` | Text | Should read "Secure Area" |
| Logout button | `a.button[href='/logout']` | Link | Logs the user out |

---

## Test Credentials

| Type | Username | Password | Expected |
|---|---|---|---|
| Valid | `tomsmith` | `SuperSecretPassword!` | Login success |
| Invalid password | `tomsmith` | `wrongpassword` | Error flash |
| Invalid username | `invalid` | `wrongpassword` | Error flash |

---

## Acceptance Criteria

### AC-001: Successful Login
- **Given** I am on the login page
- **When** I enter valid credentials and click login
- **Then** I am redirected to `/secure`
- **And** a success flash message containing "You logged into a secure area!" is shown

### AC-002: Failed Login — Invalid Password
- **Given** I am on the login page
- **When** I enter username "tomsmith" and an incorrect password
- **Then** I remain on the login page
- **And** an error flash message is displayed

### AC-003: Failed Login — Invalid Username
- **Given** I am on the login page
- **When** I enter an unrecognised username and any password
- **Then** an error flash message is displayed

### AC-004: Logout
- **Given** I am logged into the secure area
- **When** I click the Logout button
- **Then** I am redirected back to the login page
- **And** a logout success flash message is shown

---

## UI Patterns Demonstrated

| Pattern | Description |
|---|---|
| Form submission | Standard username + password + submit form |
| Flash messages | Success/error notification bar (`#flash`) |
| Redirect after action | Post-login redirect to protected resource |
| Authenticated state | Different page content based on login state |

---

## Notes

- The application is a publicly available demo — credentials are widely known and intentional.
- The flash message auto-dismisses after a few seconds — tests should assert on it immediately.
- No API backend is exposed — all interactions are through the browser UI.
- The application may be slow on first load — use sensible timeouts (10–15 seconds).
