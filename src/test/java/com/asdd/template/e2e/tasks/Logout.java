package com.asdd.template.e2e.tasks;

import com.asdd.template.e2e.pages.DashboardPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;

/**
 * Task to perform a logout action.
 */
public class Logout implements Task {

    public static Logout fromTheApplication() {
        return new Logout();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(DashboardPage.LOGOUT_BUTTON)
        );
    }
}
