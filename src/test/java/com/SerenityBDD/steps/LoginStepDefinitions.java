package com.SerenityBDD.steps;

import com.SerenityBDD.navigation.NavigateTo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.lang.reflect.InvocationTargetException;

public class LoginStepDefinitions {
    public final LoggingEventBuilder LOGGER_INFO = LoggerFactory.getLogger(LoginStepDefinitions.class).atInfo();

    /**
     * Step definition for launching the browser and opening a login page.
     *
     * @throws NoSuchMethodException    If a matching method is not found.
     * @throws InvocationTargetException If the invoked method throws an exception.
     * @throws IllegalAccessException    If there's an issue accessing the method.
     * <p>
     * Example usage:
     * <pre>{@code
     * Given I launch the browser and open the "Gmail" login page
     * }</pre>
     * </p>
     */
    @Given("I launch the browser and open the login page")
    public void launchBrowserAndOpenTheLoginPage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        NavigateTo.class.getMethod("theLoginPage").invoke(new NavigateTo());
    }

    /**
     * Step definition for stopping the debugger.
     * For halting the flow of feature run put up this statement ion the feature steps and put the IDE debugger here.
     * This debugging methodology will only work with IDEs like Intellij IDEA or Eclipse.
     *<p>
     * Example usage:
     * <pre>{@code
     * When I stop the debugger here
     * }</pre>
     * </p>
     */
    @When("I stop the debugger here")
    public void iStopTheDebuggerHere() {
        System.out.println("Pause Run");
    }

    @When("I wait for {int} seconds for delayed page load")
    public void iWaitForSecondsForDelayedPageLoad(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignore) {}
    }
}
