package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Insurance Broker System Homepage Object (post-login)
 */
public class HomePage extends BasePage {
    // Page element locators
    private By loggedInEmailLocator = By.cssSelector("div.content h4");
    private By logoutButtonLocator = By.xpath("//input[@value='Log out']");
    private By homeTabLocator = By.id("ui-id-1");
    private By requestQuotationTabLocator = By.id("ui-id-2");
    private By retrieveQuotationTabLocator = By.id("ui-id-3");
    private By profileTabLocator = By.id("ui-id-4");
    private By editProfileTabLocator = By.id("ui-id-5");
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Get logged in user email
     * @return User email
     */
    public String getLoggedInEmail() {
        try {
            String text = waitForElementVisible(loggedInEmailLocator).getText();
            System.out.println("Retrieved logged in user email: " + text);
            if (text.startsWith("Welcome")) {
                return text.replace("Welcome ", "").trim();
            }
            return text;
        } catch (Exception e) {
            System.err.println("Failed to get logged in user email: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Perform logout operation
     */
    public void logout() {
        try {
            // Wait for logout button to appear and click it
            wait.until(ExpectedConditions.elementToBeClickable(logoutButtonLocator));
            WebElement logoutButton = driver.findElement(logoutButtonLocator);
            logoutButton.click();
            System.out.println("Clicked logout button");
            
            // Modification: Wait for any page change, not requiring specific URL
            try {
                Thread.sleep(2000); // Give some time for page transition
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            System.err.println("Logout failed, trying to click using JavaScript: " + e.getMessage());
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                WebElement logoutButton = driver.findElement(logoutButtonLocator);
                js.executeScript("arguments[0].click();", logoutButton);
                
                // Wait for page change
                Thread.sleep(2000);
            } catch (Exception ex) {
                System.err.println("JavaScript click also failed: " + ex.getMessage());
            }
        }
    }

    /**
     * Verify if successfully logged in
     * @return Whether logged in
     */
    public boolean isLoggedIn() {
        try {
            // Increase wait time to ensure page is fully loaded
            wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButtonLocator));
            boolean result = driver.findElement(logoutButtonLocator).isDisplayed();
            System.out.println("Checking if logged in: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("Logout button not found, determined not logged in: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click Home tab
     */
    public void clickHomeTab() {
        clickElement(homeTabLocator);
        System.out.println("Clicked Home tab");
    }
    
    /**
     * Click Request Quotation tab
     */
    public void clickRequestQuotationTab() {
        clickElement(requestQuotationTabLocator);
        System.out.println("Clicked Request Quotation tab");
    }
    
    /**
     * Click Retrieve Quotation tab
     */
    public void clickRetrieveQuotationTab() {
        clickElement(retrieveQuotationTabLocator);
        System.out.println("Clicked Retrieve Quotation tab");
    }
    
    /**
     * Click Profile tab
     */
    public void clickProfileTab() {
        clickElement(profileTabLocator);
        System.out.println("Clicked Profile tab");
    }
    
    /**
     * Click Edit Profile tab
     */
    public void clickEditProfileTab() {
        clickElement(editProfileTabLocator);
        System.out.println("Clicked Edit Profile tab");
    }
}