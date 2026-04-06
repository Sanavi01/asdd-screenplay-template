package com.asdd.template.api.util;

/**
 * Constants for API test configuration.
 */
public final class ApiConstants {

    public static final String BASE_URL = System.getProperty("api.base.url", "https://petstore.swagger.io/v2");
    public static final String PET_ENDPOINT = "/pet";
    public static final String PET_BY_ID_ENDPOINT = "/pet/{id}";

    private ApiConstants() {}
}
