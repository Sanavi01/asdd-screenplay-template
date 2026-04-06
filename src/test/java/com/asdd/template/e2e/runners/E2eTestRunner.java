package com.asdd.template.e2e.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * JUnit Platform Suite runner for E2E tests.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/e2e")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "io.cucumber.serenity.SerenityReporterParallel")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.asdd.template.e2e")
public class E2eTestRunner {}
