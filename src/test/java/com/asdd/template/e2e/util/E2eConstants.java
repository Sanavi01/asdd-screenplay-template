package com.asdd.template.e2e.util;

/**
 * Constants for E2E test configuration.
 */
public final class E2eConstants {

    public static final String BASE_URL = System.getProperty("e2e.base.url", "https://the-internet.herokuapp.com");
    public static final String LOGIN_PATH = "/login";

    private E2eConstants() {}
}
