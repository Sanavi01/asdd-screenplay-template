package com.asdd.template.core.actors;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnlineCast;

import static net.serenitybdd.screenplay.actors.OnStage.setTheStage;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

/**
 * Factory for creating and managing Screenplay actors.
 * Provides centralised actor initialisation for both API and E2E tests.
 *
 * <p>Default actor names are defined in {@link com.asdd.template.core.util.Constants}.
 */
public final class ActorFactory {

    private ActorFactory() {}

    /**
     * Initialises the OnStage with a fresh OnlineCast and returns the named actor.
     *
     * @param actorName the display name of the actor
     * @return a configured Actor instance
     */
    public static Actor createActor(String actorName) {
        setTheStage(new OnlineCast());
        return theActorCalled(actorName);
    }
}
