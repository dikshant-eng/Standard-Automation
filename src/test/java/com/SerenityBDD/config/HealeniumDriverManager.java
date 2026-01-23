package com.SerenityBDD.config;

import com.epam.healenium.SelfHealingDriver;
import net.thucydides.core.webdriver.DriverSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Custom Driver Manager that integrates Healenium's SelfHealingDriver with Serenity BDD
 * This class wraps the standard WebDriver with Healenium's self-healing capabilities
 */
public class HealeniumDriverManager implements DriverSource {
    
    private static final String BROWSER_TYPE = System.getProperty("environment", "chrome");
    
    @Override
    public WebDriver newDriver() {
        WebDriver baseDriver;
        
        switch (BROWSER_TYPE.toLowerCase()) {
            case "firefox":
                baseDriver = createFirefoxDriver();
                break;
            case "edge":
                baseDriver = createEdgeDriver();
                break;
            case "chrome":
            default:
                baseDriver = createChromeDriver();
                break;
        }
        
        // Wrap the base driver with Healenium's SelfHealingDriver
        WebDriver healeniumDriver = SelfHealingDriver.create(baseDriver);
        
        return healeniumDriver;
    }
    
    @Override
    public boolean takesScreenshots() {
        return true;
    }
    
    /**
     * Creates a Chrome driver with standard configurations
     */
    private WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("test-type");
        options.addArguments("no-sandbox");
        options.addArguments("ignore-certificate-errors");
        options.addArguments("disable-popup-blocking");
        options.addArguments("disable-default-apps");
        options.addArguments("disable-extensions-file-access-check");
        options.addArguments("incognito");
        options.addArguments("disable-infobars");
        options.addArguments("disable-gpu");
        options.addArguments("disable-dev-shm-usage");
        options.addArguments("window-size=1920,1080");
        options.addArguments("enable-features=NetworkService");
        options.setAcceptInsecureCerts(true);
        
        ChromeDriver driver = new ChromeDriver(options);
        configureTimeouts(driver);
        return driver;
    }
    
    /**
     * Creates a Firefox driver with standard configurations
     */
    private WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.setAcceptInsecureCerts(true);
        
        FirefoxDriver driver = new FirefoxDriver(options);
        configureTimeouts(driver);
        return driver;
    }
    
    /**
     * Creates an Edge driver with standard configurations
     */
    private WebDriver createEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("start-maximized");
        options.addArguments("test-type");
        options.addArguments("no-sandbox");
        options.addArguments("ignore-certificate-errors");
        options.addArguments("disable-popup-blocking");
        options.addArguments("disable-default-apps");
        options.addArguments("disable-extensions-file-access-check");
        options.addArguments("inprivate");
        options.addArguments("disable-infobars");
        options.addArguments("disable-gpu");
        options.addArguments("disable-dev-shm-usage");
        options.addArguments("window-size=1920,1080");
        options.setAcceptInsecureCerts(true);
        
        EdgeDriver driver = new EdgeDriver(options);
        configureTimeouts(driver);
        return driver;
    }
    
    /**
     * Configures standard timeouts for the driver
     */
    private void configureTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(300));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
    }
}

