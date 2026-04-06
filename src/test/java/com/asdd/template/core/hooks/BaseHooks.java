package com.asdd.template.core.hooks;

import io.cucumber.java.BeforeAll;
import net.serenitybdd.core.Serenity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base hooks that provide shared setup/teardown logic.
 * Extended by ApiHooks and E2eHooks.
 */
public abstract class BaseHooks {

    protected static final Logger log = LoggerFactory.getLogger(BaseHooks.class);

    @BeforeAll
    public static void globalSetup() {
        log.info("Global test suite setup initiated");
        Serenity.initializeTestSession();
    }
}
