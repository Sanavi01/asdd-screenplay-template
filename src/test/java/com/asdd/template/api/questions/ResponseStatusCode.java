package com.asdd.template.api.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Question that returns the HTTP status code from the last REST response.
 */
public class ResponseStatusCode implements Question<Integer> {

    public static ResponseStatusCode ofTheLastResponse() {
        return new ResponseStatusCode();
    }

    @Override
    public Integer answeredBy(Actor actor) {
        return SerenityRest.lastResponse().statusCode();
    }
}
