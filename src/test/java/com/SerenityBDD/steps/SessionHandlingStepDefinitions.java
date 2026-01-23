package com.SerenityBDD.steps;

import com.SerenityBDD.execute.Perform;
import com.SerenityBDD.execute.TestDataManager;
import com.SerenityBDD.support.DataObjectOperations;
import com.SerenityBDD.support.PageObjectOperations;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import net.serenitybdd.core.Serenity;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.lang.reflect.Field;

public class SessionHandlingStepDefinitions {
    public final LoggingEventBuilder LOGGER_INFO = LoggerFactory.getLogger(SessionHandlingStepDefinitions.class).atInfo();

    @Steps
    DataObjectOperations dataObjectOperations;

    @Steps
    PageObjectOperations pageObjectOperations;

    @Steps
    Perform perform;

    @When("I set the session variable {string} as {string}")
    public void iSetTheSessionVariableAs(String sessionVariable, String value) {
        value = value.contains("$") ? dataObjectOperations.transformDataValue(value) : value;
        TestDataManager.setSessionAndTestData(sessionVariable, value);
        LOGGER_INFO.log("Set Session Variable: " + sessionVariable + " to value: " + value);
    }

    @When("I add the session variable values {string} and {string} saved in {string}")
    public void iAddTheSessionVariableValuesAndSavedIn(String sessionVariable1, String sessionVariable2, String result) {
        int value = Integer.parseInt(Serenity.sessionVariableCalled(sessionVariable1)) + Integer.parseInt(Serenity.sessionVariableCalled(sessionVariable2));
        TestDataManager.setSessionAndTestData(result, value);
        LOGGER_INFO.log("Set Session Variable: " + result + " to value: " + value);

    }

    @When("I set the global test data {string} as {string}")
    public void iSetTheGlobalTestDataAs(String key, String value) {
        value = value.contains("$") ? dataObjectOperations.transformDataValue(value) : value;
        TestDataManager.setTestData(key, value);
        LOGGER_INFO.log("Set Global Test Data: " + key + " to value: " + value);
    }

    @When("I get the global test data {string} and save to session variable {string}")
    public void iGetTheGlobalTestDataAndSaveToSessionVariable(String globalKey, String sessionKey) {
        LOGGER_INFO.log("Attempting to retrieve global test data for key: " + globalKey);
        LOGGER_INFO.log("All available global test data keys: " + TestDataManager.getAllKeys());
        
        Object value = TestDataManager.getTestData(globalKey);
        if (value != null) {
            Serenity.setSessionVariable(sessionKey).to(value);
            LOGGER_INFO.log("Successfully retrieved Global Test Data: " + globalKey + " with value: " + value + " and saved to session: " + sessionKey);
        } else {
            LOGGER_INFO.log("ERROR: Global Test Data not found for key: " + globalKey);
            LOGGER_INFO.log("Available keys are: " + TestDataManager.getAllKeys());
            throw new RuntimeException("Global Test Data not found for key: " + globalKey + ". Available keys: " + TestDataManager.getAllKeys());
        }
    }

    @When("I copy session variable {string} to global test data {string}")
    public void iCopySessionVariableToGlobalTestData(String sessionKey, String globalKey) {
        String value = Serenity.sessionVariableCalled(sessionKey);
        TestDataManager.setTestData(globalKey, value);
        LOGGER_INFO.log("Copied Session Variable: " + sessionKey + " to Global Test Data: " + globalKey + " with value: " + value);
    }

    @When("I clear all global test data")
    public void iClearAllGlobalTestData() {
        TestDataManager.clearTestData();
        LOGGER_INFO.log("Cleared all Global Test Data");
    }

    @When("I clear global test data {string}")
    public void iClearGlobalTestData(String key) {
        TestDataManager.clearTestData(key);
        LOGGER_INFO.log("Cleared Global Test Data for key: " + key);
    }

    @Then("I get the {string} {string} value to be saved in global test data {string}")
    public void iGetTheValueToBeSavedInGlobalTestData(String fieldName, String fieldType, String globalKey) {
        LOGGER_INFO.log("Attempting to get field value: " + fieldName + " of type: " + fieldType + " to save in global test data: " + globalKey);
        
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        LOGGER_INFO.log("Current Page: " + currentPage + ", POE Name: " + poe);
        
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field valueElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        Perform.FieldType fieldTypeEnum = Perform.FieldType.resoluteFieldType(fieldType);
        
        String fieldValue = perform.gettingFieldValue(valueElement, pageClass, fieldTypeEnum);
        if (fieldValue != null) {
            fieldValue = fieldValue.strip();
            TestDataManager.setTestData(globalKey, fieldValue);
            LOGGER_INFO.log("Successfully set Global Test Data: " + globalKey + " to value: " + fieldValue);
            LOGGER_INFO.log("All global test data keys after save: " + TestDataManager.getAllKeys());
        } else {
            LOGGER_INFO.log("ERROR: Field value is null for field: " + fieldName);
            throw new RuntimeException("Field value is null for field: " + fieldName + " of type: " + fieldType);
        }
    }
}
