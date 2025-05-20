package pages;

import org.openqa.selenium.*;

/**
 * Login Success Page Object
 */
public class SuccessPage extends BasePage {
    // Locators
    private By successMessageLocator = By.tagName("h3");
    
    public SuccessPage(WebDriver driver) {
        super(driver);
        waitForElementVisible(successMessageLocator);
    }
    
    /**
     * Get success message
     */
    public String getSuccessMessage() {
        return getElementText(successMessageLocator);
    }
    
    /**
     * Verify URL contains success identifier
     */
    public boolean isOnSuccessPage() {
        return driver.getCurrentUrl().contains("success.html");
    }
}