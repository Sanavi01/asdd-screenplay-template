package com.asdd.template.e2e.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

/**
 * Question that returns the current page title.
 */
public class PageTitle implements Question<String> {

    public static PageTitle ofTheBrowser() {
        return new PageTitle();
    }

    @Override
    public String answeredBy(Actor actor) {
        return BrowseTheWeb.as(actor).getDriver().getTitle();
    }
}
