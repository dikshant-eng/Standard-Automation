package com.SerenityBDD.steps;

import com.SerenityBDD.execute.TestDataManager;
import com.SerenityBDD.state.VerifyStateOf;
import com.SerenityBDD.support.PageObjectOperations;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import java.lang.reflect.Field;

public class PageStateVerificationStepDefinitions {
    @Steps
    VerifyStateOf verifyStateOf;
    @Steps
    PageObjectOperations pageObjectOperations;

    /**
     * Step definition for verifying the visibility of a page within a specific workflow.
     *
     * @param page     The name of the page.
     * @throws IllegalAccessException If there's an issue accessing page-related fields.
     *
     * Example usage:
     * <pre>{@code
     * Then I should be on the "Home" page
     * }</pre>
     * <pre>{@code
     * Then I should be on the "Home" page under the "Main" workflow
     * }</pre>
     */
    @Then("I should be on the {string} page")
    public void iShouldBeOnThePage(String page) throws IllegalAccessException {
        Class<?> pageClass = pageObjectOperations.getPageClass(page);
        Field field = pageObjectOperations.poeFieldClass("PAGE_LOADED", page);
        verifyStateOf.theVisibilityOf(field, pageClass);
        TestDataManager.setSessionAndTestData("Current Page", pageObjectOperations.pageObjectName(page));
    }

    /**
     * Step definition for verifying the visibility of a page within a specific workflow.
     *
     * @param page     The name of the page.
     * @param workflow The name of the workflow.
     * @throws IllegalAccessException If there's an issue accessing page-related fields.
     *
     * Example usage:
     * <pre>{@code
     * Then I should be on the "Home" page of the "Main" workflow
     * }</pre>
     * <pre>{@code
     * Then I should be on the "Home" page under the "Main" workflow
     * }</pre>
     */
    @Then("I should be on the {string} page of/under the {string} workflow")
    public void iShouldBeOnThePageUnderTheWorkflow(String page, String workflow) throws IllegalAccessException {
        Class<?> pageClass = pageObjectOperations.getPageClass(page, workflow);
        Field field = pageObjectOperations.poeFieldClass("PAGE_LOADED", page, workflow);
        verifyStateOf.theVisibilityOf(field, pageClass);
        TestDataManager.setSessionAndTestData("Current Page", pageObjectOperations.pageObjectName(workflow)
                .toLowerCase() + "." + pageObjectOperations.pageObjectName(page));
    }

    @When("I focus on the {string} page of/under the {string} workflow")
    public void iFocusOnThePageUnderTheWorkflow(String page, String workflow) throws IllegalAccessException {
        iShouldBeOnThePageUnderTheWorkflow(page, workflow);
    }

    /**
     * Step definition for focusing on a section within a specific workflow.
     *
     * @param section  The name of the section.
     * @param workflow The name of the workflow.
     * @throws IllegalAccessException If there's an issue accessing section-related fields.
     *
     * Example usage:
     * <pre>{@code
     * When I focus on the "Header" section of the "Main" workflow
     * }</pre>
     */
    @When("I focus on the {string} section under the {string} workflow")
    public void iFocusOnSectionUnderTheWorkflow(String section, String workflow) throws IllegalAccessException {
        Class<?> sectionClass = pageObjectOperations.getSectionClass(section, workflow);
        Field sectionLoaded = pageObjectOperations.poeSectionClass("SECTION_LOADED", section, workflow);
        verifyStateOf.theVisibilityOf(sectionLoaded, sectionClass);
        TestDataManager.setSessionAndTestData("Current Page", pageObjectOperations.pageObjectName(workflow)
                .toLowerCase() + ".section." + pageObjectOperations.pageObjectName(section));
    }

    /**
     * Step definition for focusing on a grid within a specific workflow.
     *
     * @param grid     The name of the grid.
     * @param workflow The name of the workflow.
     * @throws IllegalAccessException If there's an issue accessing grid-related fields.
     *
     * Example usage:
     * <pre>{@code
     * When I focus on the "Data Grid" grid under the "Main" workflow
     * }</pre>
     */
    @When("I focus on the {string} grid under the {string} workflow")
    public void iFocusOnTheGridUnderTheWorkflow(String grid, String workflow) throws IllegalAccessException {
        Class<?> gridClass = pageObjectOperations.getGridClass(grid, workflow);
        Field field = pageObjectOperations.poeGridClass("GRID_LOADED", grid, workflow);
        verifyStateOf.theVisibilityOf(field, gridClass);
        TestDataManager.setSessionAndTestData("Current Page", pageObjectOperations.pageObjectName(workflow)
                .toLowerCase() + ".grids." + pageObjectOperations.pageObjectName(grid));
    }

    /**
     * Step definition for verifying the presence of a dialog within a specific workflow.
     *
     * @param dialog   The name of the dialog.
     * @param workflow The name of the workflow.
     * @throws IllegalAccessException If there's an issue accessing dialog-related fields.
     * Example usage:
     * <pre>{@code
     * Then I should be on the "Confirmation Dialog" dialog of the "Main" workflow
     * }</pre>
     */
    @Then("I should be on the {string} dialog under the {string} workflow")
    public void iShouldBeOnTheDialogUnderTheWorkflow(String dialog, String workflow) throws IllegalAccessException {
        Class<?> dialogClass = pageObjectOperations.getDialogClass(dialog, workflow);
        Field field = pageObjectOperations.poeDialogClass("DIALOG_LOADED", dialog, workflow);
        verifyStateOf.theVisibilityOf(field, dialogClass);
        TestDataManager.setSessionAndTestData("Current Page", pageObjectOperations.pageObjectName(workflow)
                .toLowerCase() + ".dialogs." + pageObjectOperations.pageObjectName(dialog));
    }

    /**
     * Step definition for verifying the visibility of a tab within a specific workflow.
     *
     * @param tab      The name of the tab.
     * @param page     The name of the page.
     * @param workflow The name of the workflow.
     * @throws IllegalAccessException If there's an issue accessing tab-related fields.
     *
     * Example usage:
     * <pre>{@code
     * Then I should be on the "Overview" tab of the "Dashboard" page of the "Main" workflow
     * }</pre>
     */
    @Then("I should be on the {string} tab of the {string} page under the {string} workflow")
    public void iShouldBeOnTheTabOfThePageUnderTheWorkflow(String tab, String page, String workflow) throws IllegalAccessException {
        Class<?> tabClass = pageObjectOperations.getTabClass(tab, page, workflow);
        Field field = pageObjectOperations.poeTabClass("TAB_LOADED", tab, page, workflow);
        verifyStateOf.theVisibilityOf(field, tabClass);
        TestDataManager.setSessionAndTestData("Current Page", pageObjectOperations.pageObjectName(workflow)
                .toLowerCase() + ".tabs." + pageObjectOperations.pageObjectName(page).toLowerCase() + "." +
                pageObjectOperations.pageObjectName(tab));
    }
}