package com.SerenityBDD.steps;

import com.SerenityBDD.execute.Perform;
import com.SerenityBDD.support.DataObjectOperations;
import com.SerenityBDD.support.PageObjectOperations;
import io.cucumber.java.en.Then;
import net.thucydides.core.annotations.Steps;
import net.serenitybdd.core.Serenity;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FieldValueVerificationStepDefinitions {
    @Steps
    PageObjectOperations pageObjectOperations;

    @Steps
    Perform perform;

    @Steps
    DataObjectOperations dataObjectOperations;

    @Then("I should see the {string} {string} value as {string}")
    public void iShouldSeeTheFieldValueAs(String fieldName, String fieldType, String value) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field valueElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        Perform.FieldType fieldTypeEnum = Perform.FieldType.resoluteFieldType(fieldType);
        String fieldValue = perform.gettingFieldValue(valueElement, pageClass, fieldTypeEnum).strip();
        value = value.contains("$") ? dataObjectOperations.transformDataValue(value) : value;
        value = fieldType.contains("date") ? dataObjectOperations.transformDateValue(value,
                DataObjectOperations.DateTimeFormatters.getDtf("ui_dtf")) : value;
        String msg = "Incorrect value obtained for field: " + fieldName + ". Expected: " + value + ", Got: " +
                fieldValue;
        Assert.assertEquals(msg, value, fieldValue);
    }

    @Then("I should see the {string} {string} value contains {string}")
    public void iShouldSeeTheFieldValuesContains(String fieldName, String fieldType, String value) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field valueElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        Perform.FieldType fieldTypeEnum = Perform.FieldType.resoluteFieldType(fieldType);
        String fieldValue = perform.gettingFieldValue(valueElement, pageClass, fieldTypeEnum).strip();
        value = value.contains("$") ? dataObjectOperations.transformDataValue(value) : value;
        value = fieldType.contains("date") ? dataObjectOperations.transformDateValue(value,
                DataObjectOperations.DateTimeFormatters.getDtf("ui_dtf")) : value;
        String msg = "Incorrect value obtained for field: " + fieldName + ". Expected: " + value + ", Got: " +
                fieldValue;
        Assert.assertTrue(msg, fieldValue.contains(value));
    }

    @Then("I should see the following field values as:")
    public void iShouldSeeTheFollowingFieldValuesAs(List<Map<String, String>> dataTable) {
        if (!dataTable.get(0).keySet().containsAll(Arrays.asList("field", "fieldType", "value")))
            throw new RuntimeException("The data table with this step is incorrect. Please make sure that the table" +
                    " the right headers: [field, fieldType, value]");
        if (dataTable.get(0).keySet().size() != 3)
            throw new RuntimeException("The data table with this step is incorrect. Please make sure that the table" +
                    " the right headers: [field, fieldType, value]");
        for (Map<String, String> data : dataTable) {
            iShouldSeeTheFieldValueAs(data.get("field"), data.get("fieldType"), data.get("value"));
        }
    }

    @Then("I should see the following field values contains:")
    public void iShouldSeeTheFollowingFieldValuesContains(List<Map<String, String>> dataTable) {
        if (!dataTable.get(0).keySet().containsAll(Arrays.asList("field", "fieldType", "value")))
            throw new RuntimeException("The data table with this step is incorrect. Please make sure that the table" +
                    " the right headers: [field, fieldType, value]");
        if (dataTable.get(0).keySet().size() != 3)
            throw new RuntimeException("The data table with this step is incorrect. Please make sure that the table" +
                    " the right headers: [field, fieldType, value]");
        for (Map<String, String> data : dataTable) {
            iShouldSeeTheFieldValuesContains(data.get("field"), data.get("fieldType"), data.get("value"));
        }
    }

    @Then("I should see the {string} {string} value contains the following:")
    public void iShouldSeeTheValueContainsTheFollowing(String fieldName, String fieldType, List<Map<String, String>> dataTable) {
        if (!dataTable.get(0).containsKey("values"))
            throw new RuntimeException("The data table with this step is incorrect. Please make sure that the table" +
                    " the right headers: [values]");
        if (dataTable.get(0).keySet().size() != 1)
            throw new RuntimeException("The data table with this step is incorrect. Please make sure that the table" +
                    " the right headers: [values]");
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field valueElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        Perform.FieldType fieldTypeEnum = Perform.FieldType.resoluteFieldType(fieldType);
        String fieldValue = perform.gettingFieldValue(valueElement, pageClass, fieldTypeEnum);
        for (Map<String, String> data : dataTable) {
            String valueToVerify = data.get("values");
            valueToVerify = valueToVerify.contains("$") ? dataObjectOperations
                    .transformDataValue(valueToVerify) : valueToVerify;
            valueToVerify = fieldType.contains("date") ? (valueToVerify.contains("&dtf=") ? dataObjectOperations
                        .extractDtfAndTransformDateValue(valueToVerify) : dataObjectOperations
                        .transformDateValue(valueToVerify, DataObjectOperations.DateTimeFormatters.UI_DTF.getDtf())) :
                    valueToVerify;
            Assert.assertTrue(valueToVerify + " not included in the " + fieldValue, fieldValue
                    .contains(valueToVerify));
        }
    }
}
