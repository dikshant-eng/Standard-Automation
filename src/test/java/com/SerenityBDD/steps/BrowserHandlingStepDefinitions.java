package com.SerenityBDD.steps;

import com.SerenityBDD.execute.Perform;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

public class BrowserHandlingStepDefinitions {
    @Steps
    Perform perform;

    @When("I switch to the( new) window with url containing {string}")
    public void iSwitchToTheNewWindowWithUrlContaining(String partOfUrl) {
        perform.switchToWindowHandle(Perform.WindowHandleClassificationType.URL_SUBSTRING, partOfUrl);
    }

    @When("I switch to the( new) window titled as {string}")
    public void iSwitchToTheNewWindowTitledAs(String pageTitle) {
        perform.switchToWindowHandle(Perform.WindowHandleClassificationType.TITLE_MATCH, pageTitle);
    }

    @Then("I should see the number of open window handles as {int}")
    public void iShouldSeeNumberOfOpenWindowHandles(int windowsCount) {
        Assert.assertEquals("A new window is not either opened or closed!",
                windowsCount, perform.gettingWindowHandles().size());
    }

    @When("I refresh the page")
    public void iRefreshThePage() {
        perform.pageRefresh();
    }

    @When("I navigate to the previous page")
    public void iNavigateToThePreviousPage() { perform.headingToPreviousPage(); }

    @When("I close the window with url containing {string}")
    public void iCloseTheWindowWithUrlContaining(String partOfUrl) {
        perform.closingWindow(Perform.WindowHandleClassificationType.URL_SUBSTRING, partOfUrl);
    }

    @When("I close the window titled as {string}")
    public void iCloseTheWindowTitledAs(String pageTitle) {
        perform.closingWindow(Perform.WindowHandleClassificationType.TITLE_MATCH, pageTitle);
    }
}
