package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.interactions.Actions;
import java.util.List;
import org.openqa.selenium.Cookie;
import utils.*;

/**
 * The base class for all page objects
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    /**
     * Constructor
     * @param driver WebDriver
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    /**
     * Waiting for an element to be visible
     * @param locator Element locator
     * @return The found WebElement
     */
    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable
     * @param locator Element locator
     * @return The found WebElement
     */
    protected WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Clear and enter text
     * @param locator Element locator
     * @param text Text to input
     */
    protected void enterText(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Click element
     * @param locator Element locator
     */
    protected void clickElement(By locator) {
        waitForElementClickable(locator).click();
    }
    
    /**
     * Get element text
     * @param locator Element locator
     * @return Element text
     */
    protected String getElementText(By locator) {
        return waitForElementVisible(locator).getText();
    }
    
    /**
     * Get page title
     * @return Page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current URL
     * @return Current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Wait for URL to contain specified text
     * @param urlFragment URL fragment
     */
    protected void waitForUrlContains(String urlFragment) {
        wait.until(ExpectedConditions.urlContains(urlFragment));
    }
    
    /**
     * Click element using JavaScript
     * @param locator Element locator
     */
    protected void jsClick(By locator) {
        WebElement element = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
    /**
     * Perform drag and drop operation
     * @param sourceLocator Source element locator
     * @param targetLocator Target element locator
     */
    protected void dragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = waitForElementVisible(sourceLocator);
        WebElement target = waitForElementVisible(targetLocator);
        
        Actions actions = new Actions(driver);
        actions.dragAndDrop(source, target).perform();
    }

    /**
     * Perform mouse hover operation on element
     * @param locator Element locator to hover over
     */
    protected void hoverOverElement(By locator) {
        WebElement element = waitForElementVisible(locator);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    /**
     * Browser back
     */
    protected void browserBack() {
        driver.navigate().back();
    }

    /**
     * Browser forward
     */
    protected void browserForward() {
        driver.navigate().forward();
    }

    /**
     * Refresh page
     */
    protected void refreshPage() {
        driver.navigate().refresh();
    }

    /**
     * Add Cookie
     * @param name Cookie name
     * @param value Cookie value
     */
    protected void addCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        driver.manage().addCookie(cookie);
    }

    /**
     * Get Cookie value
     * @param name Cookie name
     * @return Cookie value, returns null if it doesn't exist
     */
    protected String getCookieValue(String name) {
        Cookie cookie = driver.manage().getCookieNamed(name);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * Delete specified Cookie
     * @param name Name of the Cookie to delete
     */
    protected void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
    }
}