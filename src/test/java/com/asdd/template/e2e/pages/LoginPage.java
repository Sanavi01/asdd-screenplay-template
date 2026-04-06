package com.asdd.template.e2e.pages;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Page Object for the Login page UI elements.
 */
public class LoginPage {

    public static final Target USERNAME_FIELD =
        Target.the("username field").locatedBy("#username");

    public static final Target PASSWORD_FIELD =
        Target.the("password field").locatedBy("#password");

    public static final Target LOGIN_BUTTON =
        Target.the("login button").locatedBy("button[type='submit']");
}
