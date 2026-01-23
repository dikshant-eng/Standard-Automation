package com.SerenityBDD.steps;

import com.SerenityBDD.execute.Perform;
import com.SerenityBDD.support.DataObjectOperations;
import com.SerenityBDD.support.PageObjectOperations;
import io.cucumber.java.en.Then;
import net.thucydides.core.annotations.Steps;
import net.serenitybdd.core.Serenity;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class FieldStateVerificationStepDefinitions {
    @Steps
    PageObjectOperations pageObjectOperations;

    @Steps
    Perform perform;

    @Steps
    DataObjectOperations dataObjectOperations;

    @Then("I should see the {string} field/button/link/label is {string}")
    public void iShouldSeeTheFieldIs(String fieldName, String fieldState) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field field = pageObjectOperations.poeFieldClass(poe, currentPage);
        Assert.assertTrue(fieldName + " is not " + fieldState, perform.gettingFieldState(field, pageClass)
                .get(fieldState.toLowerCase()));
    }

    @Then("I should see the following fields/buttons/links/labels/tabs are {string}:")
    public void iShouldSeeTheFollowingFieldsAre(String fieldState, List<Map<String, String>> dataTable) {
        if (!dataTable.getFirst().containsKey("field"))
            throw new RuntimeException("The data table with this step is incorrect. Please make sure that the table" +
                    " the right headers: [field]");
        if (dataTable.getFirst().keySet().size() != 1)
            throw new RuntimeException("The data table with this step is incorrect. Please make sure that the table" +
                    " the right headers: [field]");
        for (Map<String, String> data : dataTable) {
            iShouldSeeTheFieldIs(data.get("field"), fieldState);
        }
    }

    @Then("I should see the count of {string} button(s)/label(s)/section(s) as {string}")
    public void iShouldSeeTheCountOfFieldAs(String fieldName, String expectedCount) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field field = pageObjectOperations.poeFieldClass(poe, currentPage);
        expectedCount = expectedCount.contains("$") ? dataObjectOperations.transformDataValue(expectedCount) : expectedCount;
        Assert.assertEquals(Integer.parseInt(expectedCount), perform.getWebElements(field, pageClass).size());
    }
}
