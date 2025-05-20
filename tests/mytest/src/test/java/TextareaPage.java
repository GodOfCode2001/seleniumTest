package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Textarea test page object
 */
public class TextareaPage extends BasePage {
    private String pageUrl = "https://demo.guru99.com/test/autoit.html";
    
    // Updated to correct element locator
    private By textareaLocator = By.id("input_6"); // Modified to the actual ID on the webpage
    private By submitButtonLocator = By.id("input_2"); // Submit button ID
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public TextareaPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Open Textarea test page
     * @return Current page instance, supports chained calls
     */
    public TextareaPage openPage() {
        driver.get(pageUrl);
        System.out.println("Opened Textarea test page: " + pageUrl);
        return this;
    }
    
    /**
     * Enter text in textarea
     * @param text Text to input
     * @return Current page instance, supports chained calls
     */
    public TextareaPage enterText(String text) {
        try {
            // Click "Create A Course" button to display form containing textarea
            WebElement createCourseBtn = driver.findElement(By.id("getjob"));
            createCourseBtn.click();
            System.out.println("Clicked 'Create A Course' button");
            
            // Wait for textarea to display
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(textareaLocator));
            
            textarea.clear();
            textarea.sendKeys(text);
            System.out.println("Entered text in textarea: " + (text.length() > 30 ? text.substring(0, 27) + "..." : text));
            return this;
        } catch (Exception e) {
            System.err.println("Failed to enter text in textarea: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get textarea content
     * @return Text in textarea
     */
    public String getTextContent() {
        try {
            WebElement textarea = driver.findElement(textareaLocator);
            return textarea.getAttribute("value");
        } catch (Exception e) {
            System.err.println("Failed to get textarea content: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Submit form
     * @return Current page instance, supports chained calls
     */
    public TextareaPage submitForm() {
        try {
            WebElement submitButton = driver.findElement(submitButtonLocator);
            submitButton.click();
            System.out.println("Clicked submit button");
            return this;
        } catch (Exception e) {
            System.err.println("Failed to submit form: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Test textarea validation function
     * @return Whether validation message is displayed
     */
    public boolean isValidationDisplayed() {
        try {
            // Clear textarea
            WebElement textarea = driver.findElement(textareaLocator);
            textarea.clear();
            
            // Click elsewhere to trigger validation
            driver.findElement(By.tagName("body")).click();
            
            // Check if validation style is applied
            return textarea.getAttribute("class").contains("validate-error");
        } catch (Exception e) {
            System.err.println("Failed to test validation function: " + e.getMessage());
            return false;
        }
    }
}