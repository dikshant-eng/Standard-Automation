package com.SerenityBDD.support;

import com.SerenityBDD.execute.Perform;
import net.thucydides.core.annotations.Steps;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class GridObjectOperations extends PageObjectOperations {

    public final LoggingEventBuilder LOGGER_INFO = LoggerFactory.getLogger(GridObjectOperations.class).atInfo();

    @Steps
    Perform perform;

    @Steps
    PageObjectOperations pageObjectOperations;

    /**
     * Returns all table headers, including empty headings, for a given table element.
     *
     * @param table The locator for the table element.
     * @return List of table headers as strings.
     */
    public List<String> getAllTableHeaders(By table) {
        WebElement tableElement = perform.getWebElement(table);
        return getAllTableHeaders(tableElement);
    }

    /**
     * Returns all table headers, including empty headings, for a given table element.
     *
     * @param tableElement The table WebElement.
     * @return List of table headers as strings.
     */
    public List<String> getAllTableHeaders(WebElement tableElement) {
        return tableElement.findElements(By.tagName("th")).stream().map(WebElement::getText)
                .toList().stream().map(String::trim).toList();
    }

    /**
     * Returns the visible table headers for a given table locator.
     *
     * @param table The locator for the table element.
     * @return List of visible table headers as strings.
     */
    public List<String> getTableHeaders(By table) {
        return getAllTableHeaders(table).stream().filter(r -> !r.equals("")).toList();
    }

    /**
     * Returns the visible table headers for a given table WebElement.
     *
     * @param tableElement The table WebElement.
     * @return List of visible table headers as strings.
     */
    public List<String> getTableHeaders(WebElement tableElement) {
        return getAllTableHeaders(tableElement).stream().filter(r -> !r.equals("")).toList();
    }

    /**
     * Get the total number of rows in a table's body for a given table locator.
     *
     * @param table The locator for the table element.
     * @return The number of rows in the table.
     */
    public int getRowCount(By table) {
        WebElement tableElement = perform.getWebElement(table);
        return tableElement.findElements(By.cssSelector("tbody tr")).size();
    }

    /**
     * Get data of the rows as a List present on the UI under a column in the table.
     *
     * @param columns List of Columns to fetch the data
     * @param table {@link By} locator of the table
     * @return HashMap<String, ArrayList<String>>
     */
    public HashMap<String, ArrayList<String>> getRowsDataForColumns(List<String> columns, By table) {
        HashMap<String, ArrayList<String>> rowsForColumnsMap = new HashMap<>();
        List<String> tableHeaders = getAllTableHeaders(table);
        WebElement tableElement = perform.getWebElement(table);
        for (String column : columns) {
            int colIndex = tableHeaders.indexOf(column);
            List<WebElement> rowElements = tableElement.findElements(By.cssSelector("tbody tr"));
            rowsForColumnsMap.put(column, new ArrayList<>(){{
                for (WebElement rowElement : rowElements) {
                    add(rowElement.findElement(By.cssSelector("td:nth-child(" + (colIndex + 1) + ")")).getText());
                }
            }});
        }
        return rowsForColumnsMap;
    }

    /**
     * Get values for a specified row and column as a Map for a given table locator.
     *
     * @param tableRow The Map containing row and column data.
     * @param table The locator for the table element.
     * @return Map where keys are column names, and values are the corresponding row values.
     */
    public Map<String, String> getValueForRowColumn(Map<String, String> tableRow, By table) {
        Map<String, String> results = new HashMap<>();
        List<String> columns = new ArrayList<>(tableRow.keySet());
        if (columns.indexOf("row") != 0) {
            int tempIndex = columns.indexOf("row");
            columns.set(tempIndex, columns.get(0));
            columns.set(0, "row");
        }
        results.put("row", tableRow.get("row"));
        for (int i = 1; i < columns.size(); i++) {
            int rowNumber = Integer.parseInt(tableRow.get("row")) - 1;
            LOGGER_INFO.log("Getting Data for: " + columns.get(i));
            String tdSelector = "td:nth-child(" + (getAllTableHeaders(table).indexOf(columns.get(i)) + 1) + ")";
            String result = perform.getWebElement(table).findElements(By.cssSelector("tbody tr")).get(rowNumber)
                    .findElement(By.cssSelector(tdSelector)).getText();
            if (Objects.equals(result, "")) {
                result = null;
            }
            results.put(columns.get(i), result);
        }
        return results;
    }

    /**
     * Get the locator for a focused row in a grid for a specified row number and grid class name.
     *
     * @param rowNumber     The row number to focus on.
     * @param gridClassName The class name of the grid.
     * @return HashMap containing the selector style and locator.
     */
    public HashMap<String, String> getRowFocusedLocator(int rowNumber, String gridClassName) {
        Field elementLoaded = pageObjectOperations.poeFieldClass("GRID_LOADED", gridClassName);
        Class<?> gridClass = pageObjectOperations.getPageClass(gridClassName);
        By field = perform.fieldToInteract(elementLoaded, gridClass);
        WebElement table = perform.getWebElement(elementLoaded, gridClass);
        if (field.toString().toLowerCase().contains("xpath")) {
            return constructFieldSelector("table", field, "/tbody/tr[" + rowNumber + "]");
        } else {
            return constructFieldSelector("table", field, ">tbody tr:nth-child(" + rowNumber + ")");
        }
    }

    /**
     * Get the WebElement for a focused row in a grid for a specified row number and grid page.
     *
     * @param rowNumber The row number to focus on.
     * @param gridPage  The grid page name.
     * @return WebElement representing the focused row.
     */
    public WebElement getRowFocusedWebElement(int rowNumber, String gridPage) {
        HashMap<String, String> rowElementMap = getRowFocusedLocator(rowNumber, gridPage);
        String selectorStyle = rowElementMap.keySet().stream().toList().get(0);
        String locator = rowElementMap.values().stream().toList().get(0);
        return perform.getWebElement(selectorStyle, locator);
    }

    /**
     * Construct a field selector for a given main tag, field, and additional selector.
     *
     * @param mainTag            The main HTML tag for the field.
     * @param field              The field locator.
     * @param additionalSelector Additional selector (if any).
     * @return HashMap containing the selector style and locator.
     */
    public HashMap<String, String> constructFieldSelector(String mainTag, By field, String... additionalSelector) {
        String[] splitString = field.toString().split(":");
        HashMap<String, String> selector = new HashMap<>();
        String constructedSelector = switch(splitString[0]) {
            case "By.id" -> "[id*=\"";
            case "By.name" -> "[name*=\"";
            case "By.className" -> "[class*=\"";
            case "By.cssSelector", "By.xpath" -> splitString[1];
            default -> throw new IllegalArgumentException("Invalid Argument");
        };
        if (splitString[0].equals("By.xpath") || splitString[0].equals("By.cssSelector")) {
            selector.put(splitString[0], splitString[1] + additionalSelector[0]);
        } else {
            selector.put("By.cssSelector", mainTag + constructedSelector + splitString[1].trim() + "\"]"
                    + additionalSelector[0]);
        }
        return selector;
    }
}
