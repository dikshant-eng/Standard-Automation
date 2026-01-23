package com.SerenityBDD.testsuites;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin={"pretty"},
        features="src/test/resources/features/ConfigurationMaster/CategoryMaster.feature",
        glue="com.SerenityBDD",
        tags = "not @ignore"
)
public class SanitySuiteTest { }
