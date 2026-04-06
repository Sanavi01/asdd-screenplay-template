package com.asdd.template.api.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Question that returns the body of the last REST response as a String.
 */
public class ResponseBody implements Question<String> {

    public static ResponseBody ofTheLastResponse() {
        return new ResponseBody();
    }

    @Override
    public String answeredBy(Actor actor) {
        return SerenityRest.lastResponse().body().asString();
    }
}
