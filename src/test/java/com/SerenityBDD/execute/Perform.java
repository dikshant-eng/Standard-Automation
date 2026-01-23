package com.SerenityBDD.execute;

import com.SerenityBDD.state.VerifyStateOf;
import com.SerenityBDD.support.DataObjectOperations;
import com.SerenityBDD.support.PageObjectOperations;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.lang.reflect.Field;
import java.util.*;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Perform extends PageObjectOperations {
    public final LoggingEventBuilder LOGGER_INFO = LoggerFactory.getLogger(Perform.class).atInfo();
    public final LoggingEventBuilder LOGGER_WARN = LoggerFactory.getLogger(Perform.class).atWarn();
    final VerifyStateOf verifyStateOf = new VerifyStateOf();
    final DataObjectOperations dataObjectOperations = new DataObjectOperations();

    /**
     * Retrieves a By locator object associated with a given field within a specified page class.
     *
     * @param field     The field for which to retrieve the By locator.
     * @param pageClass The page class containing the field.
     * @return By       The By locator for the specified field.
     * @throws RuntimeException If there is an IllegalAccessException when accessing the field.
     */
    public By fieldToInteract(Field field, Class<?> pageClass) {
        try {
            return (By) field.get(pageClass);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a specified value to an HTML element identified by a By locator.
     *
     * @param field     The field associated with the element to interact with.
     * @param pageClass The page class containing the field.
     * @param value     The value to be set in the HTML element.
     */
    public void settingFieldValue(Field field, Class<?> pageClass, String value) {
        WebElement fieldToFill = getFinalWebElement(field, pageClass);
        settingFieldValue(fieldToFill, value);
    }

    /**
     * Sets a specified value to an HTML element identified by a By locator.
     *
     * @param element The By locator for the HTML element.
     * @param value   The value to be set in the HTML element.
     */
    public void settingFieldValue(WebElement element, String value) {
        element.clear();
        element.sendKeys(value);
    }

    public void clearingFieldValue(Field field, Class<?> pageClass) {
        WebElement fieldToFill = getFinalWebElement(field, pageClass);
        clearingFieldValue(fieldToFill);
    }

    public void clearingFieldValue(WebElement field) {
        try {
            field.clear();
        } catch (InvalidElementStateException e) {
            field.click();
            field.getText();
            field.clear();
        }
    }

    /**
     * Sets a specified value to an HTML element identified by a By locator.
     *
     * @param field     The field associated with the element to interact with.
     * @param pageClass The page class containing the field.
     * @param value     The value to be set in the HTML element.
     */
    public void settingAutofillValue(Field field, Class<?> pageClass, String value) {
        WebElement fieldToFill = getFinalWebElement(field, pageClass);
        settingAutofillValue(fieldToFill, value);
    }

    /**
     * Sets a specified value to an HTML element identified by a By locator.
     *
     * @param element The WebElement locator for the HTML element.
     * @param value   The value to be set in the HTML element.
     */
    public void settingAutofillValue(WebElement element, String value) {
        element.clear();
        try { Thread.sleep(1500); } catch (InterruptedException ignore) {}
        char[] valueArray = value.toCharArray();
        for(char character : valueArray) {
            element.sendKeys(Character.toString(character));
            try { Thread.sleep(100); } catch (InterruptedException ignore) {}
        }
        try { Thread.sleep(1500); } catch (InterruptedException ignore) {}
        clickOn(getWebElement(By.cssSelector("p[title='" + value + "']")));
    }

    public void sendingKey(Field field, Class<?> pageClass, String key) {
        WebElement fieldForKeyInteraction = getFinalWebElement(field, pageClass);
        sendingKey(fieldForKeyInteraction, key);
    }

    public void sendingKey(WebElement webElement, String key) {
        webElement.sendKeys(Keys.valueOf(key));
    }

    /**
     * Clicks on an HTML element identified by a By locator.
     *
     * @param field     The field associated with the element to click.
     * @param pageClass The page class containing the field.
     */
    public void clickOn(Field field, Class<?> pageClass) {
        WebElement fieldToClick = getFinalWebElement(field, pageClass);
        clickOn(fieldToClick);
    }

    /**
     * Clicks on an HTML element identified by a By locator.
     *
     * @param byField The By locator for the HTML element.
     */
    public void clickOn(By byField) {
        WebElement elementToClick = getDriver().findElement(byField);
        clickOn(elementToClick);
    }

    /**
     * Clicks on a provided WebElement.
     *
     * @param elementToClick The WebElement to click on.
     */
    public void clickOn(WebElement elementToClick) {
        try {
            elementToClick.click();
        } catch (TimeoutException | ElementClickInterceptedException e) {
            LOGGER_INFO.log("Failed to click due to {" + e + "}. Attempting JS Click");
            jsClickOn(elementToClick);
        }
    }

    public void jsClickOn(WebElement elementToClick) {
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        jse.executeScript("arguments[0].click();", elementToClick);
    }

    /**
     * Retrieves a WebElement associated with a field within a specified page class.
     *
     * @param field     The field for which to retrieve the WebElement.
     * @param pageClass The page class containing the field.
     * @return WebElement The WebElement associated with the specified field.
     */
    public WebElement getWebElement(Field field, Class<?> pageClass) {
        By byElement = fieldToInteract(field, pageClass);
        return getWebElement(byElement);
    }

    /**
     * Retrieves a WebElement based on a provided By locator.
     *
     * @param byElement The By locator to locate the WebElement.
     * @return WebElement The WebElement identified by the provided By locator.
     */
    public WebElement getWebElement(By byElement) {
        return getDriver().findElement(byElement);
    }

    /**
     * Retrieves a WebElement based on the specified selector style and locator.
     * The supported selector styles are "By.cssSelector" and "By.xpath".
     *
     * @param selectorStyle The selector style to use for locating the WebElement.
     * @param locator       The locator string used to identify the WebElement.
     * @return WebElement  The WebElement identified by the provided selector style and locator.
     * @throws IllegalArgumentException If an invalid selector type is provided in the arguments.
     */
    public WebElement getWebElement(String selectorStyle, String locator) {
        switch (selectorStyle) {
            case "By.cssSelector" -> {
                return getWebElement(By.cssSelector(locator));
            }
            case "By.xpath" -> {
                return getWebElement(By.xpath(locator));
            }
            default -> throw new IllegalArgumentException("Invalid selector type in the arguments");
        }
    }

    /**
     * Retrieves a list of WebElements associated with a field within a specified page class.
     *
     * @param field     The field for which to retrieve the list of WebElements.
     * @param pageClass The page class containing the field.
     * @return List<WebElement> A list of WebElements associated with the specified field.
     */
    public List<WebElement> getWebElements(Field field, Class<?> pageClass) {
        By byElement = fieldToInteract(field, pageClass);
        return getWebElements(byElement);
    }

    /**
     * Retrieves a list of WebElements based on a provided By locator.
     *
     * @param element The By locator to locate the list of WebElements.
     * @return List<WebElement> A list of WebElements identified by the provided By locator.
     */
    public List<WebElement> getWebElements(By element) {
        return getDriver().findElements(element);
    }

    /**
     * Retrieves the value of a text field identified by a By locator.
     *
     * @param byField The By locator for the text field.
     * @return String The value of the text field identified by the provided By locator.
     */
    public String gettingTextFieldValue(By byField) {
        WebElement fieldValueToGet = getDriver().findElement(byField);
        return fieldValueToGet.getAttribute("value");
    }

    /**
     * Retrieves the selected option's text from a dropdown element identified by a By locator.
     *
     * @param byField The By locator for the dropdown element.
     * @return String The text of the selected option in the dropdown identified by the provided By locator.
     */
    public String gettingDropdownValue(By byField) {
        WebElement dropdownElement = getDriver().findElement(byField);
        return new Select(dropdownElement).getFirstSelectedOption().getText();
    }

    /**
     * Retrieves the status (Selected or Unselected) of a radio button element identified by a By locator.
     *
     * @param byField The By locator for the radio button element.
     * @return String The status ("Selected" or "Unselected") of the radio button identified by the provided By locator.
     */
    public String gettingRadioStatus(By byField) {
        if (getDriver().findElement(byField).isSelected()) {
            return "Selected";
        } else {
            return "Unselected";
        }
    }

    /**
     * Retrieves the status (Selected or Unselected) of a radio button element.
     *
     * @param element The WebElement representing the radio button element.
     * @return String The status ("Selected" or "Unselected") of the radio button represented by the provided WebElement.
     */
    public String gettingRadioStatus(WebElement element) {
        if (element.isSelected()) {
            return "Selected";
        } else {
            return "Unselected";
        }
    }

    /**
     * Retrieves the status (Checked or Unchecked) of a checkbox element identified by a By locator.
     *
     * @param byField The By locator for the checkbox element.
     * @return String The status ("Checked" or "Unchecked") of the checkbox identified by the provided By locator.
     */
    public String gettingCheckboxStatus(By byField) {
        if (gettingRadioStatus(byField).equalsIgnoreCase("selected")) return "Checked";
        else return "Unchecked";
    }

    /**
     * Retrieves the status (Checked or Unchecked) of a checkbox element.
     *
     * @param element The WebElement representing the checkbox element.
     * @return String The status ("Checked" or "Unchecked") of the checkbox represented by the provided WebElement.
     */
    public String gettingCheckboxStatus(WebElement element) {
        if (gettingRadioStatus(element).equalsIgnoreCase("selected")) return "Checked";
        else return "Unchecked";
    }

    /**
     * Retrieves the state of a field (visibility, read-only, selected) identified by a By locator.
     *
     * @param field     The field for which to retrieve the state.
     * @param pageClass The page class containing the field.
     * @return HashMap<String, Boolean> A HashMap containing field states, including "not visible," "visible," "readonly," and "selected."
     */
    public HashMap<String, Boolean> gettingFieldState(Field field, Class<?> pageClass) {
        By fieldForState = fieldToInteract(field, pageClass);
        return gettingFieldState(fieldForState);
    }

    /**
     * Retrieves the state of a field (visibility, read-only, selected) identified by a By locator.
     *
     * @param byField The By locator for the field.
     * @return HashMap<String, Boolean> A HashMap containing field states, including "not visible," "visible," "readonly," and "selected."
     */
    public HashMap<String, Boolean> gettingFieldState(By byField) {
        HashMap<String, Boolean> fieldState = new HashMap<>();
        WebElement element = null;
        try {
            element = getDriver().findElement(byField);
            fieldState.put("not visible", !element.isDisplayed());
            fieldState.put("visible", element.isDisplayed());
        } catch (NoSuchElementException e) {
            fieldState.put("not visible", true);
        }
        if (element != null) {
            fieldState.put("visible", element.isDisplayed());
            try {
                fieldState.put("readonly", !element.isEnabled());
                fieldState.put("selected", element.isSelected());
            } catch (Exception e) {
                LOGGER_INFO.log("Attempt to perform an unsupported operation for the field.");
            }
        }
        return fieldState;
    }

    /**
     * Retrieves the state of an alert element, including text, background color, and style.
     *
     * @return HashMap<String, String> A HashMap containing alert element states, including "text," "background-color," and "style."
     */
    public HashMap<String, String> gettingAlertState() {
        HashMap<String, String> alertState = new HashMap<>();
        WebElement alertElement = getDriver().findElement(By.cssSelector("div[role='alert'] div[class*='jq-toast']"));
        alertState.put("text", alertElement.getText());
        alertState.put("background-color", alertElement.getCssValue("background-color"));
        alertState.put("style", alertElement.getAttribute("style"));
        return alertState;
    }

    /**
     * Retrieves the text content of a browser alert.
     *
     * @return String The text content of the browser alert.
     */
    public String gettingBrowserAlertText() {
        String alertText = null;
        try {
            alertText = getDriver().switchTo().alert().getText();
        } catch (UnhandledAlertException ignored) {
        }
        return alertText;
    }

    /**
     * Accepts the currently displayed browser alert.
     */
    public void acceptingBrowserAlert() {
        getDriver().switchTo().alert().accept();
    }

    /**
     * Dismisses the currently displayed browser alert.
     */
    public void dismissingBrowserAlert() {
        try {
            getDriver().switchTo().alert().dismiss();
        } catch (NoAlertPresentException ignored) {
        }
    }

    /**
     * Retrieves the value of a field based on its type and the associated By locator.
     *
     * @param field     The field for which to retrieve the value.
     * @param pageClass The page class containing the field.
     * @param fieldType The type of the field, such as TEXT_FIELD, TEXTAREA, DATE_FIELD, LABEL, DATE_LABEL, DROPDOWN, RADIO, or CHECKBOX.
     * @return String   The value of the field as a String.
     * @throws RuntimeException If an unsupported field type is provided.
     */
    public String gettingFieldValue(Field field, Class<?> pageClass, FieldType fieldType) {
        By fieldValueToGet = fieldToInteract(field, pageClass);
        switch (fieldType) {
            case TEXT_FIELD, TEXTAREA, DATE_FIELD -> {
                return gettingTextFieldValue(fieldValueToGet);
            }
            case LABEL, DATE_LABEL -> {
                return gettingFieldValue(fieldValueToGet);
            }
            case DROPDOWN -> {
                return gettingDropdownValue(fieldValueToGet);
            }
            case RADIO -> {
                return gettingRadioStatus(fieldValueToGet);
            }
            case CHECKBOX -> {
                return gettingCheckboxStatus(fieldValueToGet);
            }
        }
        throw new RuntimeException("Unsupported field type: " + fieldType);
    }

    /**
     * Retrieves the value of an HTML element identified by a By locator.
     *
     * @param byField The By locator for the HTML element.
     * @return String The value of the HTML element identified by the provided By locator.
     */
    public String gettingFieldValue(By byField) {
        WebElement fieldValueToGet = getDriver().findElement(byField);
        String value = fieldValueToGet.getAttribute("value");
        value = value == null ? fieldValueToGet.getText() : value;
        return value;
    }

    /**
     * Sets the value of a date field identified by a Field and Class in the page object.
     *
     * @param field     The field representing the date input.
     * @param pageClass The page class containing the date field.
     * @param value     The date value to set in the date field.
     */
    public void settingDateFieldValue(Field field, Class<?> pageClass, String value) {
        By fieldToFill = fieldToInteract(field, pageClass);
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(fieldToFill));
        settingDateFieldValue(fieldToFill, value);
    }

    /**
     * Sets the value of a date field identified by a By locator.
     *
     * @param byField The By locator for the date field.
     * @param value   The date value to set in the date field.
     */
    public void settingDateFieldValue(By byField, String value) {
        // Check if the date input from feature is not in traditional format
        if (value.equals("t") || value.contains("t-") || value.contains("t+") || value.contains("t&dtf")) {
            // Check if the date requires special DateTimeFormatter
            if (value.contains("&dtf=")) value = dataObjectOperations.extractDtfAndTransformDateValue(value);
            else value = dataObjectOperations.transformUIDateValue(value);
        }
        getWebElement(byField).sendKeys(value);
    }

    /**
     * Sets the selected value of a dropdown element identified by a Field and Class in the page object.
     *
     * @param field     The field representing the dropdown input.
     * @param pageClass The page class containing the dropdown field.
     * @param value     The value to select from the dropdown.
     */
    public void settingDropdownValue(Field field, Class<?> pageClass, String value) {
        settingDropdownValue(getFinalWebElement(field, pageClass), value);
    }

    /**
     * Sets the selected value of a dropdown element identified by a By locator.
     *
     * @param element The WebElement locator for the dropdown element.
     * @param value   The value to select from the dropdown.
     */
    public void settingDropdownValue(WebElement element, String value) {
        Select field = new Select(element);
        try {
            field.selectByVisibleText(value);
        } catch (Exception e) {
            LOGGER_WARN.log(
                    "Unable to find value: {} in dropdown By: {}. Available Options:", value, element.toString());
            LOGGER_INFO.log(field.getOptions().stream().map(WebElement::getText).toList().toString());
            throw new RuntimeException(e);
        }

    }

    /**
     * Sets the state of a checkbox element identified by a Field and Class in the page object.
     *
     * @param field     The field representing the checkbox input.
     * @param pageClass The page class containing the checkbox field.
     * @param value     The desired state for the checkbox ("checked" or "unchecked").
     */
    public void settingCheckboxAs(Field field, Class<?> pageClass, String value) {
        By checkboxField = fieldToInteract(field, pageClass);
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(checkboxField));
        settingCheckboxAs(checkboxField, value);
    }

    /**
     * Sets the state of a checkbox element identified by a By locator.
     *
     * @param byField The By locator for the checkbox element.
     * @param value   The desired state for the checkbox ("checked" or "unchecked").
     */
    public void settingCheckboxAs(By byField, String value) {
        WebElement checkbox = getDriver().findElement(byField);
        settingCheckboxAs(checkbox, value);
    }

    /**
     * Sets the state of a checkbox element represented by a WebElement.
     *
     * @param checkbox The WebElement representing the checkbox.
     * @param value    The desired state for the checkbox ("checked" or "unchecked").
     */
    public void settingCheckboxAs(WebElement checkbox, String value) {
        boolean checkboxState = verifyStateOf.checkboxIsSelected(checkbox);
        boolean expectedState = value.equalsIgnoreCase("checked");
        if ((!checkboxState && expectedState) || (checkboxState && !expectedState)) {
            checkbox.click();
        } else if (checkboxState == expectedState) {
            LOGGER_INFO.log("Checkbox state is already as expected i.e.: " + value);
        } else if (!value.equalsIgnoreCase("checked") && !value.equalsIgnoreCase("unchecked")) {
            throw new IllegalArgumentException("Incorrect value provided for setting the checkbox. It should " +
                    "either be 'Checked' or 'Unchecked' (Case-Insensitive)");
        } else {
            throw new RuntimeException("Unable to " + value.replace("ed", "") + " checkbox");
        }
    }

    /**
     * Refreshes the current page in the web browser.
     */
    public void pageRefresh() {
        getDriver().navigate().refresh();
    }

    /**
     * Navigates to the previous page in the web browser's history.
     */
    public void headingToPreviousPage() {
        getDriver().navigate().back();
    }

    /**
     * Retrieves a set of window handles currently open in the web browser.
     *
     * @return Set<String> A set of window handles as strings.
     */
    public Set<String> gettingWindowHandles() {
        try {
            // Wait for new window to open if expected
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return getDriver().getWindowHandles();
    }

    /**
     * Switches to a window handle based on the specified classification type and matching parameter.
     *
     * @param type              The classification type for identifying the window handle (TITLE_MATCH or URL_SUBSTRING).
     * @param matchingParameter The parameter to match for identifying the window handle.
     */
    public void switchToWindowHandle(WindowHandleClassificationType type, String matchingParameter) {
        Set<String> windowHandles = gettingWindowHandles();
        if (windowHandles.size() == 1) {
            getDriver().switchTo().window((String) windowHandles.toArray()[0]);
        }
        boolean windowFound = false;
        for (int attempt = 1; attempt <= 20; attempt++) { // Attempt 20 times to switch and find the matching pattern
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (type.equals(WindowHandleClassificationType.TITLE_MATCH)) {
                for (String handle : windowHandles) {
                    getDriver().switchTo().window(handle);
                    if (getDriver().getTitle().contains(matchingParameter)) {
                        windowFound = true;
                        break;
                    }
                }
            } else if (type.equals(WindowHandleClassificationType.URL_SUBSTRING)) {
                for (String handle : windowHandles) {
                    getDriver().switchTo().window(handle);
                    if (getDriver().getCurrentUrl().contains(matchingParameter)) {
                        windowFound = true;
                        break;
                    }
                }
            }
            if (windowFound) {
                LOGGER_INFO.log("Window found: TRUE. Switched to window with title: "
                        + getDriver().getTitle());
                break;
            }
            LOGGER_INFO.log("Attempt No." + attempt + " to locate window with handle of type: " + type);
        }
        if (!windowFound) {
            throw new RuntimeException("Unable to find window with type: " + type +
                    " and value: " + matchingParameter);
        }
    }

    /**
     * Closes the current window based on the specified classification type and matching parameter.
     *
     * @param windowHandleClassificationType The classification type for identifying the window handle (TITLE_MATCH or URL_SUBSTRING).
     * @param matchingParameter              The parameter used to match the window handle.
     */
    public void closingWindow(WindowHandleClassificationType windowHandleClassificationType, String matchingParameter) {
        boolean rightWindowFocused = false;
        if (windowHandleClassificationType.equals(WindowHandleClassificationType.TITLE_MATCH)) {
            rightWindowFocused = getDriver().getTitle().equals(matchingParameter);
        } else if (windowHandleClassificationType.equals(WindowHandleClassificationType.URL_SUBSTRING)) {
            rightWindowFocused = getDriver().getCurrentUrl().contains(matchingParameter);
        }
        if (!rightWindowFocused) {
            switchToWindowHandle(windowHandleClassificationType, matchingParameter);
        }
        getDriver().close();
    }

    public void generatingSelectors(List<WebElement> elements) {
        List<WebElement> elementsCopy = new ArrayList<>() {{ addAll(elements); }};

        for (WebElement element : elements) {
            try {
                String fieldName = element.getAttribute("data-fieldname");
                if (fieldName.equals("null") && !element.isDisplayed()) {
                    continue;
                }
                highlightElement(element, "red");
                elementsCopy.remove(element);
                System.out.printf(
                        "public static final By %s = By.cssSelector(\"%s[data-fieldname='%s']\");%n",
                        poeName(storeValueWithAwt(elementsCopy)),
                        element.getTagName(),
                        fieldName
                );
                highlightElement(element, "green");
            } catch (StaleElementReferenceException | NullPointerException ignore) {}
        }
    }

    public void presentPendingSelectorsPrompt() {
        final CountDownLatch latch = new CountDownLatch(1); // Used to wait for input completion

        Frame frame = new Frame("All possible elements have been Extracted. Please create the locators for " +
                "elements without green highlight to complete page object class.");
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        // Create components
        Button okButton = new Button("I'm now done...");

        frame.add(okButton);

        // Action listener for the button
        okButton.addActionListener(e -> {
            latch.countDown(); // Unblock the waiting thread
        });

        // Close the frame properly
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                latch.countDown(); // Ensure to unblock in case of closing the window
                frame.dispose();
            }
        });

        // Display the frame
        frame.setVisible(true);

        // Wait for the input to be completed
        try {
            latch.await(); // This will block until latch.countDown() is called
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Dispose the frame after getting the value
        frame.dispose();
    }

    private String storeValueWithAwt(List<WebElement> elementList) {
        final String[] storedValue = {""};
        final CountDownLatch latch = new CountDownLatch(1); // Used to wait for input completion

        Frame frame = new Frame("Enter the selector name");
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        // Create components
        Label label = new Label("Enter value:");
        TextField textField = new TextField(20);
        Button storeButton = new Button("Store Value");
        Label outputLabel = new Label("Stored Value: None");
        Label pendingSelectors = new Label("Pending Selectors: " + elementList.size());

        // Add components to the frame
        frame.add(label);
        frame.add(textField);
        frame.add(storeButton);
        frame.add(outputLabel);
        frame.add(pendingSelectors);

        // Action listener for the button
        storeButton.addActionListener(e -> {
            // Store the value from the text field
            storedValue[0] = textField.getText();
            // Update the output label to show the stored value
            outputLabel.setText("Stored Value: " + storedValue[0]);
            latch.countDown(); // Unblock the waiting thread
        });

        // Close the frame properly
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                latch.countDown(); // Ensure to unblock in case of closing the window
                frame.dispose();
            }
        });

        // Display the frame
        frame.setVisible(true);

        // Wait for the input to be completed
        try {
            latch.await(); // This will block until latch.countDown() is called
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Dispose the frame after getting the value
        frame.dispose();

        // Return the stored value
        return storedValue[0];
    }

    public void highlightElement(WebElement element, String color) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].style.border='3px solid " + color.toLowerCase() + "'", element);
    }

    private By getLoadingElement(Class<?> pageClass) {
        String pageType = "PAGE";
        if (pageClass.toString().contains("dialog"))
            pageType = "DIALOG";
        else if (pageClass.toString().contains("grid"))
            pageType = "GRID";
        else if (pageClass.toString().contains("section"))
            pageType = "SECTION";
        else if (pageClass.toString().contains("tab"))
            pageType = "TAB";

        try {
            return fieldToInteract(pageClass.getDeclaredField(pageType + "_LOADED"), pageClass);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private WebElement getFinalWebElement(Field field, Class<?> pageClass) {
        By interactingField = fieldToInteract(field, pageClass);
        try {
            waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(interactingField));
            return getWebElement(interactingField);
        } catch (TimeoutException e) {
            try {
                By loadingElement = getLoadingElement(pageClass);
                waitForCondition().until(ExpectedConditions.visibilityOf(getWebElement(loadingElement)
                        .findElement(interactingField)));
                return getWebElement(loadingElement).findElement(interactingField);
            } catch (Exception ex) {
                List<WebElement> possibleFields = getWebElements(interactingField);
                if (possibleFields.isEmpty())
                    throw new RuntimeException("No Element found with Locator: " + interactingField);
                for (WebElement possibleField : possibleFields) {
                    try {
                        waitForCondition().until(ExpectedConditions.visibilityOf(possibleField));
                        return possibleField;
                    } catch (TimeoutException ignore) {
                    }
                }
                throw new RuntimeException("Unable to find any Element with Locator: " + interactingField);
            }
        }
    }

    /**
     * An enumeration representing different window handle classification types.
     */
    public enum WindowHandleClassificationType {
        TITLE_MATCH,
        URL_SUBSTRING
    }

    public enum KeyType {
        TAB,
        ENTER,
        ESC;

        /**
         * Resolves a FieldType based on a given field type string.
         *
         * @param keyType The field type string.
         * @return FieldType The resolved FieldType.
         * @throws RuntimeException If an unsupported field type is provided.
         */
        public static KeyType resoluteKeyType(String keyType) {
            return switch (keyType.toLowerCase()) {
                case "esc" -> ESC;
                case "enter" -> ENTER;
                case "tab" -> TAB;
                default -> throw new RuntimeException("Unsupported key type: " + keyType);
            };
        }

        /**
         * Resolves the field name associated with a KeyType.
         *
         * @param keyType The FieldType.
         * @return String The resolved field name.
         */
        public static String resoluteKeyName(KeyType keyType) {
            return switch (keyType) {
                case ESC -> "esc";
                case TAB -> "tab";
                case ENTER -> "enter";
            };
        }
    }

    /**
     * An enumeration representing different field types.
     */
    public enum FieldType {
        TEXT_FIELD,
        DATE_FIELD,
        DATE_LABEL,
        DROPDOWN,
        CHECKBOX,
        RADIO,
        LABEL,
        TEXTAREA;

        /**
         * Resolves a FieldType based on a given field type string.
         *
         * @param fieldType The field type string.
         * @return FieldType The resolved FieldType.
         * @throws RuntimeException If an unsupported field type is provided.
         */
        public static FieldType resoluteFieldType(String fieldType) {
            return switch (fieldType.toLowerCase()) {
                case "text field" -> TEXT_FIELD;
                case "date field" -> DATE_FIELD;
                case "date label" -> DATE_LABEL;
                case "radio" -> RADIO;
                case "checkbox" -> CHECKBOX;
                case "dropdown" -> DROPDOWN;
                case "label" -> LABEL;
                case "textarea" -> TEXTAREA;
                default -> throw new RuntimeException("Unsupported field type: " + fieldType);
            };
        }

        /**
         * Resolves the field name associated with a FieldType.
         *
         * @param fieldType The FieldType.
         * @return String The resolved field name.
         */
        public static String resoluteFieldName(FieldType fieldType) {
            return switch (fieldType) {
                case TEXT_FIELD -> "text field";
                case DATE_FIELD -> "date field";
                case DATE_LABEL -> "date label";
                case RADIO -> "radio";
                case CHECKBOX -> "checkbox";
                case DROPDOWN -> "dropdown";
                case LABEL -> "label";
                case TEXTAREA -> "textarea";
            };
        }
    }

}
