# E2E Automation Instructions

These instructions guide agents in generating E2E (End-to-End) UI test automation using Java, Serenity BDD, Screenplay Pattern, WebDriver, and Cucumber.

---

## 1. Reading the E2E Spec

When given a user journey or UI specification:

1. **Identify the application base URL** — store in `E2eConstants.BASE_URL` as a system property override.
2. **List all user journeys** — actor, precondition, actions, expected outcomes.
3. **Identify UI elements** — for each page, list buttons, fields, links, and messages.
4. **Note navigation flows** — which page leads to which.
5. **Identify test data** — credentials, form values, expected display text.

---

## 2. What to Generate

For each E2E specification, generate:

| Artifact | Location | One Per |
|---|---|---|
| Feature file | `src/test/resources/features/e2e/` | User journey / feature area |
| Task class | `src/test/java/.../e2e/tasks/` | User action |
| Question class | `src/test/java/.../e2e/questions/` | Observable state |
| Page object | `src/test/java/.../e2e/pages/` | Page or major section |
| Model class | `src/test/java/.../e2e/model/` | Data structure (e.g., credentials) |
| Step definitions | `src/test/java/.../e2e/stepdefinitions/` | Feature file |
| Constants | `src/test/java/.../e2e/util/E2eConstants.java` | Once (add new constants) |

---

## 3. Naming Rules

| Class Type | Pattern | Example |
|---|---|---|
| Task | `VerbNoun` | `NavigateTo`, `Login`, `Logout`, `FillForm`, `ClickButton` |
| Question | Noun phrase | `CurrentUrl`, `PageTitle`, `FlashMessage`, `ErrorText` |
| Page Object | `{PageName}Page` | `LoginPage`, `DashboardPage`, `CheckoutPage` |
| Model | Data name | `UserCredentials`, `FormData` |
| Step Definitions | `{Feature}StepDefinitions` | `LoginStepDefinitions` |
| Constants | `{Domain}Constants` | `E2eConstants` |

---

## 4. Page Objects

Page objects hold only `Target` definitions — no logic:

```java
public class LoginPage {
    public static final Target USERNAME_FIELD =
        Target.the("username field").locatedBy("#username");

    public static final Target PASSWORD_FIELD =
        Target.the("password field").locatedBy("#password");

    public static final Target LOGIN_BUTTON =
        Target.the("login button").locatedBy("button[type='submit']");
}
```

**Locator priority** (most to least preferred):
1. `id` — `#elementId`
2. `data-testid` attribute — `[data-testid='value']`
3. ARIA label — `[aria-label='value']`
4. CSS class (stable, semantic) — `.button-primary`
5. XPath — use only as last resort

---

## 5. Screenplay Patterns for E2E

### Navigation Task

```java
public class NavigateTo implements Task {
    private final String url;

    public NavigateTo(String url) { this.url = url; }

    public static NavigateTo thePageAt(String url) { return new NavigateTo(url); }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.url(url));
    }
}
```

### Interaction Task

```java
public class Login implements Task {
    private final String username;
    private final String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Login withCredentials(String username, String password) {
        return new Login(username, password);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Enter.theValue(username).into(LoginPage.USERNAME_FIELD),
            Enter.theValue(password).into(LoginPage.PASSWORD_FIELD),
            Click.on(LoginPage.LOGIN_BUTTON)
        );
    }
}
```

### Question Pattern (E2E)

```java
public class CurrentUrl implements Question<String> {

    public static CurrentUrl ofTheBrowser() { return new CurrentUrl(); }

    @Override
    public String answeredBy(Actor actor) {
        return BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
    }
}
```

### Element Text Question (using Serenity built-in)

```java
// In step definition
String text = actor.asksFor(Text.of(DashboardPage.FLASH_MESSAGE));
```

---

## 6. Actor and Ability Setup (E2eHooks)

```java
@Managed
WebDriver driver;

@Before("@e2e")
public void setUp() {
    setTheStage(new OnlineCast());
    Actor user = theActorCalled("User");
    user.can(BrowseTheWeb.with(driver));
}

@After("@e2e")
public void tearDown() {
    drawTheCurtainOn();
}
```

---

## 7. WebDriver Interaction Reference

```java
// Navigate to URL
Open.url("https://example.com/login")

// Type into a field
Enter.theValue("text").into(LoginPage.USERNAME_FIELD)

// Click a target
Click.on(LoginPage.LOGIN_BUTTON)

// Select dropdown option
SelectFromOptions.byVisibleText("Option").from(DropdownPage.DROPDOWN)

// Clear and type
Clear.field(LoginPage.USERNAME_FIELD)
Enter.theValue("new text").into(LoginPage.USERNAME_FIELD)

// Scroll to element
Scroll.to(DashboardPage.FOOTER)

// Wait for element
WaitUntil.the(LoginPage.LOGIN_BUTTON, isVisible())
```

---

## 8. Assertions (E2E)

```java
// Assert URL
assertThat(actor.asksFor(CurrentUrl.ofTheBrowser())).contains("/secure");

// Assert page title
assertThat(actor.asksFor(PageTitle.ofTheBrowser())).isEqualTo("Welcome");

// Assert element text
assertThat(actor.asksFor(Text.of(DashboardPage.FLASH_MESSAGE))).contains("You logged in");

// Assert element visibility (Serenity Ensure)
actor.attemptsTo(
    Ensure.that(DashboardPage.FLASH_MESSAGE).isDisplayed()
);
```

---

## 9. WebDriver Configuration

Configure WebDriver in `serenity.conf`:

```hocon
serenity {
  webdriver {
    driver = chrome
    auto.manage.downloads = true
  }
}
```

For CI headless execution, add to `build.gradle`:
```groovy
test {
    systemProperty 'webdriver.chrome.arguments', '--headless,--no-sandbox,--disable-dev-shm-usage'
}
```

---

## 10. Waits and Synchronisation

- Prefer Serenity's built-in smart waits (configured via `serenity.conf`).
- Do NOT use `Thread.sleep()`.
- Use `WaitUntil` for explicit conditions:

```java
actor.attemptsTo(
    WaitUntil.the(DashboardPage.FLASH_MESSAGE, isVisible()).forNoMoreThan(10).seconds()
);
```
