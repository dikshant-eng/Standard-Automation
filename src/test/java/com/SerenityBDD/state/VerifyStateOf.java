package com.SerenityBDD.state;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.lang.reflect.Field;

public class VerifyStateOf extends PageObject {

    /**
     * Wait for the visibility of a web element identified by a field and page class.
     *
     * @param field     The field representing the element.
     * @param pageClass The class of the page where the element is located.
     * @throws IllegalAccessException If there is an issue accessing the field.
     */
    public void theVisibilityOf(Field field, Class<?> pageClass) throws IllegalAccessException {
        By elementForVisibility = (By) field.get(pageClass);
        theVisibilityOf(elementForVisibility);
    }

    /**
     * Wait for the visibility of a web element identified by a locator.
     *
     * @param byField The locator of the element.
     */
    public void theVisibilityOf(By byField) {
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(byField));
    }

    /**
     * Wait for the visibility of a web element.
     *
     * @param element The web element to wait for.
     */
    public void theVisibilityOf(WebElement element) {
        waitForCondition().until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Check if a web element identified by a locator is visible on the page.
     *
     * @param byField The locator of the element.
     * @return True if the element is visible, false otherwise.
     */
    public boolean elementIsVisible(By byField) {
        try {
            return getDriver().findElement(byField).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Wait for the presence of a web element identified by a field and page class.
     *
     * @param field     The field representing the element.
     * @param pageClass The class of the page where the element is located.
     * @throws IllegalAccessException If there is an issue accessing the field.
     */
    public void thePresenceOf(Field field, Class<?> pageClass) throws IllegalAccessException {
        By elementForAvailability = (By) field.get(pageClass);
        thePresenceOf(elementForAvailability);
    }

    /**
     * Wait for the presence of a web element identified by a locator.
     *
     * @param byField The locator of the element.
     */
    public void thePresenceOf(By byField) {
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(byField));
    }

    /**
     * Check if a web element identified by a locator is present on the page.
     *
     * @param byField The locator of the element.
     * @return True if the element is present, false otherwise.
     */
    public boolean elementIsPresent(By byField) {
        try {
            return getDriver().findElements(byField).size() > 0;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check if a web element is visible within the viewport.
     *
     * @param element The web element to check.
     * @return True if the element is visible in the viewport, false otherwise.
     */
    public boolean visibilityOfElementInViewPort(WebElement element) {
        WebDriver driver = getDriver();

        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var elem = arguments[0],                 " +
                        "  box = elem.getBoundingClientRect(),    " +
                        "  cx = box.left + box.width / 2,         " +
                        "  cy = box.top + box.height / 2,         " +
                        "  e = document.elementFromPoint(cx, cy); " +
                        "for (; e; e = e.parentElement) {         " +
                        "  if (e === elem)                        " +
                        "    return true;                         " +
                        "}                                        " +
                        "return false;                            "
                , element);
    }

    /**
     * Wait for the invisibility of a web element identified by a field and page class.
     *
     * @param field     The field representing the element.
     * @param pageClass The class of the page where the element is located.
     */
    public void theInvisibilityOf(Field field, Class<?> pageClass) {
        By elementForInvisibility;
        try {
            elementForInvisibility = (By) field.get(pageClass);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        theInvisibilityOf(elementForInvisibility);
    }

    /**
     * Wait for the invisibility of a web element identified by a locator.
     *
     * @param byField The locator of the element.
     */
    public void theInvisibilityOf(By byField) {
        waitForCondition().until(ExpectedConditions.invisibilityOfElementLocated(byField));
    }

    /**
     * Check if a checkbox WebElement is selected.
     *
     * @param checkbox The checkbox WebElement to check.
     * @return True if the checkbox is selected, false otherwise.
     */
    public boolean checkboxIsSelected(WebElement checkbox) {
        if (checkbox.getTagName().contains("input")) {
            return checkbox.isSelected();
        }
        return checkbox.findElement(By.className("mat-checkbox-input"))
                .getAttribute("aria-checked").equals("true");
    }
}
