package com.asdd.template.e2e.pages;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Page Object for the Dashboard/Secure Area page UI elements.
 */
public class DashboardPage {

    public static final Target FLASH_MESSAGE =
        Target.the("flash message").locatedBy("#flash");

    public static final Target LOGOUT_BUTTON =
        Target.the("logout button").locatedBy("a.button[href='/logout']");
}
