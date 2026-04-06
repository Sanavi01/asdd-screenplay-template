package com.asdd.template.e2e.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

/**
 * Question that returns the current URL of the browser.
 */
public class CurrentUrl implements Question<String> {

    public static CurrentUrl ofTheBrowser() {
        return new CurrentUrl();
    }

    @Override
    public String answeredBy(Actor actor) {
        return BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
    }
}
