package com.asdd.template.e2e.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.core.webdriver.SerenityWebdriverManager;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.thucydides.core.annotations.Managed;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.actors.OnStage.setTheStage;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.drawTheCurtainOn;

/**
 * Cucumber hooks for E2E test setup and teardown.
 */
public class E2eHooks {

    private static final Logger log = LoggerFactory.getLogger(E2eHooks.class);

    @Managed
    WebDriver driver;

    @Before("@e2e")
    public void setUpE2eStage() {
        log.info("Setting up E2E stage with WebDriver");
        setTheStage(new OnlineCast());
        Actor user = theActorCalled("User");
        user.can(BrowseTheWeb.with(driver));
    }

    @After("@e2e")
    public void tearDownE2eStage() {
        log.info("Tearing down E2E stage");
        drawTheCurtainOn();
    }
}
