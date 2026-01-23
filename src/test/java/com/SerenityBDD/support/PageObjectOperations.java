package com.SerenityBDD.support;

import net.serenitybdd.core.pages.PageObject;

import java.lang.reflect.Field;

/**
 * Helper class for Serenity Page Objects operations.
 */
public class PageObjectOperations extends PageObject {

    private final String BASE_PATH = "com.SerenityBDD.pages.";

    /**
     * Converts a field name to a Page Object element name.
     *
     * @param fieldName The field name to be converted.
     * @return String The converted Page Object element name.
     */
    public String poeName(String fieldName) {
        // Replaces special characters with underscores and applies naming conventions.
        // Removes leading or trailing double underscores and converts to uppercase.
        // Handles special case for the '#' character.
        // Example: "Field Name With Spaces" -> "FIELD_NAME_WITH_SPACES"
        // Example: "Field#Name" -> "FIELD_NUMBERNAME"
        // Example: "Field   " -> "FIELD"
        fieldName = fieldName
                .replace('?', '_')
                .replace('/', '_')
                .replace('+', '_')
                .replace('>', '_')
                .replace('.', '_')
                .replace('<', '_')
                .replace("&", "AND")
                .replace('-', '_')
                .replace('(', '_')
                .replace(") ", "_")
                .replace(')', '_')
                .replace("#", "NUMBER")
                .replace(' ', '_')
                .replace("__", "") // Remove any leading or trailing double underscores
                .toUpperCase();
        if (fieldName.endsWith("_")) {
            fieldName = fieldName.substring(0, fieldName.length() - 1);
        }
        return fieldName;
    }

    /**
     * Converts a page name to a valid Page Object class name.
     *
     * @param pageName The page name to be converted.
     * @return String The converted Page Object class name.
     */
    public String pageObjectName(String pageName) {
        // Removes spaces, dashes, parentheses, and converts '&' to "And".
        // Example: "My Page Name" -> "MyPageName"
        // Example: "Special-Page" -> "SpecialPage"
        // Example: "Page (Special)" -> "PageSpecial"
        return pageName
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "")
                .replace("/", "")
                .replace("&", "And");
    }

    /**
     * Retrieves the Class object for a specified Page Object.
     *
     * @param pageObjectName The name of the Page Object.
     * @return Class<?> The Class object representing the Page Object.
     * @throws RuntimeException If the Page Object class is not found or accessible.
     */
    public Class<?> getPageClass(String pageObjectName) {
        try {
            return Class.forName(BASE_PATH + pageObjectName(pageObjectName));
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new RuntimeException("Unable to find " + pageObjectName(pageObjectName)
                    + " class under package: " + BASE_PATH + "Perhaps class not created or inaccessible.");
        }
    }

    /**
     * Retrieves the Class object for a specified Page Object within a workflow package.
     *
     * @param pageObjectName The name of the Page Object.
     * @param workflow       The workflow package name.
     * @return Class<?> The Class object representing the Page Object within the workflow package.
     */
    public Class<?> getPageClass(String pageObjectName, String workflow) {
        return getPageClass(pageObjectName(workflow).toLowerCase() + "."
                + pageObjectName(pageObjectName));
    }

    /**
     * Retrieves the Field object for a specified Page Object element.
     *
     * @param poeName             The name of the Page Object element.
     * @param pageObjectClassName The Page Object class name.
     * @param workflow            The workflow package name.
     * @return Field The Field object representing the Page Object element.
     * @throws RuntimeException If the element field is not found or accessible.
     */
    public Field poeFieldClass(String poeName, String pageObjectClassName, String workflow) {
        Class<?> pageClass = getPageClass(pageObjectClassName, workflow);
        poeName = poeName(poeName);
        try {
            return pageClass.getField(poeName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + poeName + " field in the class: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase()
                    + "." + pageObjectName(pageObjectClassName) + ". Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + poeName + " field in the class: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() +
                    "." + pageObjectName(pageObjectClassName) + ". Perhaps element access is not allowed.");
        }
    }

    /**
     * Retrieves the Field object for a specified Page Object element.
     *
     * @param poeName             The name of the Page Object element.
     * @param pageObjectClassName The Page Object class name.
     * @return Field The Field object representing the Page Object element.
     * @throws RuntimeException If the element field is not found or accessible.
     */
    public Field poeFieldClass(String poeName, String pageObjectClassName) {
        Class<?> pageClass = getPageClass(pageObjectClassName);
        poeName = poeName(poeName);
        try {
            return pageClass.getField(poeName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + poeName + " field in the class: " +
                    BASE_PATH + pageObjectName(pageObjectClassName)
                    + ". Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + poeName + " field in the class: "
                    + BASE_PATH + pageObjectName(pageObjectClassName)
                    + ". Perhaps element access is not allowed.");
        }
    }

    /**
     * Retrieves the Class object for a specified Page Object section.
     *
     * @param section  The section name.
     * @param workflow The workflow package name.
     * @return Class<?> The Class object representing the Page Object section.
     * @throws RuntimeException If the section class is not found or accessible.
     */
    public Class<?> getSectionClass(String section, String workflow) {
        try {
            return Class.forName(BASE_PATH + pageObjectName(workflow)
                    .toLowerCase() + ".section." + pageObjectName(section));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find " + pageObjectName(section) + " class under package: " +
                    BASE_PATH + pageObjectName(workflow).toLowerCase() + ".section. Perhaps " +
                    "class not created or inaccessible.");
        }
    }

    /**
     * Retrieves the Field object for a specified Page Object section element.
     *
     * @param sectionName            The name of the Page Object section element.
     * @param sectionObjectClassName The Page Object section class name.
     * @param workflow               The workflow package name.
     * @return Field The Field object representing the Page Object section element.
     * @throws RuntimeException If the section element field is not found or accessible.
     */
    public Field poeSectionClass(String sectionName, String sectionObjectClassName, String workflow) {
        Class<?> sectionClass = getSectionClass(sectionObjectClassName, workflow);
        try {
            return sectionClass.getDeclaredField(sectionName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + sectionName + " field in the class: " + BASE_PATH
                    + pageObjectName(workflow).toLowerCase() + ".section." + sectionObjectClassName
                    + ". Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + sectionName + " field in the class: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() + ".section." +
                    sectionObjectClassName + ". Perhaps element access is not allowed.");
        }
    }

    /**
     * Retrieves the Class object for a specified Page Object grid.
     *
     * @param grid     The grid name.
     * @param workflow The workflow package name.
     * @return Class<?> The Class object representing the Page Object grid.
     * @throws RuntimeException If the grid class is not found or accessible.
     */
    public Class<?> getGridClass(String grid, String workflow) {
        try {
            return Class.forName(BASE_PATH + pageObjectName(workflow).toLowerCase() + ".grids." +
                    pageObjectName(grid));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find " + pageObjectName(grid) + " class under package: " + BASE_PATH
                    + pageObjectName(workflow).toLowerCase() + ".grids. Perhaps class not created or inaccessible.");
        }
    }

    /**
     * Retrieves the Field object for a specified Page Object grid element.
     *
     * @param gridField The name of the Page Object grid element.
     * @param gridName  The Page Object grid class name.
     * @param workflow  The workflow package name.
     * @return Field The Field object representing the Page Object grid element.
     * @throws RuntimeException If the grid element field is not found or accessible.
     */
    public Field poeGridClass(String gridField, String gridName, String workflow) {
        Class<?> gridClass = getGridClass(gridName, workflow);
        try {
            return gridClass.getDeclaredField(gridField);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + gridField + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() +
                    ".grids." + gridName + ". Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + gridName + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() +
                    ".grids." + gridName + ". Perhaps element access is not allowed.");
        }
    }

    /**
     * Retrieves the Class object for a specified Page Object dialog.
     *
     * @param dialog   The dialog name.
     * @param workflow The workflow package name.
     * @return Class<?> The Class object representing the Page Object dialog.
     * @throws RuntimeException If the dialog class is not found or accessible.
     */
    public Class<?> getDialogClass(String dialog, String workflow) {
        try {
            return Class.forName(BASE_PATH + pageObjectName(workflow).toLowerCase() + ".dialogs." +
                    pageObjectName(dialog));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find " + pageObjectName(dialog) + " class under package: " + BASE_PATH
                    + pageObjectName(workflow).toLowerCase() + ".dialogs. Perhaps class not created or inaccessible.");
        }
    }

    /**
     * Retrieves the Field object for a specified Page Object dialog element.
     *
     * @param dialogField The name of the Page Object dialog element.
     * @param dialogName  The Page Object dialog class name.
     * @param workflow    The workflow package name.
     * @return Field The Field object representing the Page Object dialog element.
     * @throws RuntimeException If the dialog element field is not found or accessible.
     */
    public Field poeDialogClass(String dialogField, String dialogName, String workflow) {
        Class<?> dialogClass = getDialogClass(dialogName, workflow);
        try {
            return dialogClass.getDeclaredField(dialogField);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + dialogField + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() +
                    ".dialogs." + dialogName + ". Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + dialogName + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() +
                    ".grids." + dialogName + ". Perhaps element access is not allowed.");
        }
    }

    /**
     * Retrieves the Class object for a specified Page Object tab.
     *
     * @param tab      The tab name.
     * @param page     The page name.
     * @param workflow The workflow package name.
     * @return Class<?> The Class object representing the Page Object tab.
     * @throws RuntimeException If the tab class is not found or accessible.
     */
    public Class<?> getTabClass(String tab, String page, String workflow) {
        try {
            return Class.forName(BASE_PATH + pageObjectName(workflow).toLowerCase() + ".tabs." +
                    pageObjectName(page).toLowerCase() + "." + pageObjectName(tab));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find " + pageObjectName(tab) + " class under package: " + BASE_PATH
                    + pageObjectName(workflow).toLowerCase() + ".tabs." +
                    pageObjectName(page).toLowerCase() + ". Perhaps class not created or inaccessible.");
        }
    }

    /**
     * Retrieves the Field object for a specified Page Object tab element.
     *
     * @param tabField The name of the Page Object tab element.
     * @param tabName  The Page Object tab class name.
     * @param pageName The Page Object page name.
     * @param workflow The workflow package name.
     * @return Field The Field object representing the Page Object tab element.
     * @throws RuntimeException If the tab element field is not found or accessible.
     */
    public Field poeTabClass(String tabField, String tabName, String pageName, String workflow) {
        Class<?> dialogClass = getTabClass(tabName, pageName, workflow);
        try {
            return dialogClass.getDeclaredField(tabField);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + tabField + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() + ".tabs." +
                    pageObjectName(pageName).toLowerCase() + ".Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + tabField + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() + ".tabs." +
                    pageObjectName(pageName).toLowerCase() + ". Perhaps element access is not allowed.");
        }
    }
}
