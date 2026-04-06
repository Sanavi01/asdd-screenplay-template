package com.asdd.template.api.tasks;

import com.asdd.template.api.util.ApiConstants;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

/**
 * Task that configures the API ability for an actor.
 */
public class ConfigureApi implements Task {

    public static Performable forActor() {
        return new ConfigureApi();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.can(CallAnApi.at(ApiConstants.BASE_URL));
    }
}
