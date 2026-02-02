package com.SerenityBDD.steps;

import com.SerenityBDD.execute.Perform;
import com.SerenityBDD.support.DataObjectOperations;
import com.SerenityBDD.support.PageObjectOperations;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FieldInteractionsStepDefinitions {
    @Steps
    Perform perform;

    @Steps
    PageObjectOperations pageObjectOperations;

    @Steps
    DataObjectOperations dataObjectOperations;

    @When("I set the {string} text field/area as {string}")
    public void iSetTextFieldAs(String fieldName, String value) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field textElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        perform.settingFieldValue(textElement, pageClass, value);
    }

    @When("I clear and set the {string} text field/area as {string}")
    public void iClearAndSetTextFieldAs(String fieldName, String value) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field textElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        perform.clearingFieldValue(textElement, pageClass);
        perform.settingFieldValue(textElement, pageClass, value);
    }

    @When("I set the {string} autofill field as {string}")
    public void iSetAutoFillFieldAs(String fieldName, String value) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field textElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        perform.settingAutofillValue(textElement, pageClass, value);
    }

    @When("I set the {string} date field as {string}")
    public void iSetDateFieldAs(String fieldName, String value) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field textElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        perform.settingDateFieldValue(textElement, pageClass, value);
    }

    @When("I click the {string} button/link/icon/radio/field/tab/option")
    public void iClickTheElement(String clickableElementName) {
        String poe = pageObjectOperations.poeName(clickableElementName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field clickableElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        perform.clickOn(clickableElement, pageClass);

        // ⏱️ Wait 5 seconds on the same page
        try {
            Thread.sleep(7000);
        } catch (InterruptedException ignored) {}
    }

    @When("I set the {string} dropdown as {string}")
    public void iSetDropdownAs(String fieldName, String value) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field dropdownElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        value = value.contains("$") ? dataObjectOperations.transformDataValue(value) : value;
        perform.settingDropdownValue(dropdownElement, pageClass, value);
    }

    @When("I set the {string} checkbox as {string}")
    public void iSetCheckboxAs(String fieldName, String value) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field checkboxElement = pageObjectOperations.poeFieldClass(poe, currentPage);
        perform.settingCheckboxAs(checkboxElement, pageClass, value);
    }

    @When("I set these fields with following values:")
    public void iSetTheseFieldsWithFollowingValues(List<Map<String, String>> dataTable) {
        if (!dataTable.get(0).keySet().containsAll(Arrays.asList("field", "fieldType", "value")))
            throw new RuntimeException("The data table with this step is incorrect. Please make sure that the table" +
                    " the right headers: [field, fieldType, value]");
        if (dataTable.get(0).keySet().size() != 3)
            throw new RuntimeException("The data table with this step is incorrect. Please make sure that the table" +
                    " the right headers: [field, fieldType, value]");
        for (Map<String, String> tableRow : dataTable) {
            String valueToFill = "";
            if (tableRow.get("value").contains("`$`")) {
                valueToFill = tableRow.get("value");
            } else {
                valueToFill = tableRow.get("value").contains("$") ? dataObjectOperations
                        .transformDataValue(tableRow.get("value")) : tableRow.get("value");
            }
            switch (tableRow.get("fieldType").toLowerCase()) {
                case "text field", "textarea" -> iSetTextFieldAs(tableRow.get("field"), valueToFill);
                case "autofill field" -> iSetAutoFillFieldAs(tableRow.get("field"), valueToFill);
                case "date field" -> iSetDateFieldAs(tableRow.get("field"), valueToFill);
                case "dropdown" -> iSetDropdownAs(tableRow.get("field"), valueToFill);
                case "checkbox" -> iSetCheckboxAs(tableRow.get("field"), valueToFill);
                default -> throw new IllegalArgumentException("Unknown field type: " + tableRow.get("fieldType"));
            }
        }
    }

    @When("I send the {string} key from the {string} field")
    public void iSendKeyFromTheField(String key, String fieldName) {
        String poe = pageObjectOperations.poeName(fieldName);
        String currentPage = Serenity.sessionVariableCalled("Current Page");
        Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
        Field field = pageObjectOperations.poeFieldClass(poe, currentPage);
        perform.sendingKey(field, pageClass, key);
    }

    @When("I select {string} option from the Search field in the header")
    public void iSelectOptionFromTheSearchFieldInTheHeader(String pageName) {

        // 1️⃣ Open global search
        WebElement searchButton = perform.getWebElement(
                By.id("desktop-navbar-modal-search")
        );
        perform.clickOn(searchButton);

        // 2️⃣ Type search keyword
        WebElement searchInput = perform.getWebElement(By.id("navbar-search"));
        searchInput.clear();
        searchInput.sendKeys(pageName);

        String searchKey = pageName.toLowerCase().trim();

        // 3️⃣ Retry loop to handle Awesomplete DOM refresh
        for (int attempt = 0; attempt < 10; attempt++) {
            try {
                List<WebElement> options = perform.getDriver()
                        .findElements(By.cssSelector(".awesomplete ul[role='listbox'] li a span"));

                WebElement bestMatch = null;

                for (WebElement option : options) {

                    String optionText = option.getText()
                            .replace("\n", " ")
                            .toLowerCase()
                            .trim();

                    // 1️⃣ Exact match (highest priority)
                    if (optionText.equals(searchKey)) {
                        searchInput.sendKeys(Keys.ENTER);
                        return;
                    }

                    // 2️⃣ Starts-with match
                    if (bestMatch == null && optionText.startsWith(searchKey)) {
                        bestMatch = option;
                    }

                    // 3️⃣ Contains match (fallback)
                    if (bestMatch == null && optionText.contains(searchKey)) {
                        bestMatch = option;
                    }
                }

                // Use best available match
                if (bestMatch != null) {
                    searchInput.sendKeys(Keys.ENTER);
                    return;
                }

            } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
                // DOM refreshed, retry
            }

            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }

        throw new RuntimeException(
                "No search result matched the keyword: " + pageName
        );
    }

        @When("I generate the selectors for the page")
        public void iGenerateTheSelectorsForThePage () {
            String currentPage = Serenity.sessionVariableCalled("Current Page");
            Class<?> pageClass = pageObjectOperations.getPageClass(currentPage);
            String loadedElement = "";
            if (currentPage.contains("dialogs")) {
                loadedElement = "DIALOG_LOADED";
            } else if (currentPage.contains("section")) {
            loadedElement = "SECTION_LOADED";
        } else if (currentPage.contains("grid")) {
            loadedElement = "GRID_LOADED";
        } else {
            loadedElement = "PAGE_LOADED";
        }
        Field pageLoaded = pageObjectOperations.poeFieldClass(loadedElement, currentPage);
        WebElement pageLoadedElement = perform.getWebElement(pageLoaded, pageClass);
        List<WebElement> inputs = pageLoadedElement.findElements(By.tagName("input"));
        List<WebElement> selects = pageLoadedElement.findElements(By.tagName("select"));
        List<WebElement> textAreas = pageLoadedElement.findElements(By.tagName("textarea"));
        perform.generatingSelectors(inputs);
        perform.generatingSelectors(selects);
        perform.generatingSelectors(textAreas);
        perform.presentPendingSelectorsPrompt();
    }
}
