package com.asdd.template.api.hooks;

import com.asdd.template.api.util.ApiConstants;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.actors.OnStage.setTheStage;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

/**
 * Cucumber hooks for API test setup and teardown.
 */
public class ApiHooks {

    private static final Logger log = LoggerFactory.getLogger(ApiHooks.class);

    @Before("@api")
    public void setUpApiStage() {
        log.info("Setting up API stage with base URL: {}", ApiConstants.BASE_URL);
        setTheStage(new OnlineCast());
        Actor apiUser = theActorCalled("API User");
        apiUser.can(CallAnApi.at(ApiConstants.BASE_URL));
    }
}
