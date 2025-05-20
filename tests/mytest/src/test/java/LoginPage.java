package pages;

import org.openqa.selenium.*;

/**
 * Insurance Broker System Login Page Object
 */
public class LoginPage extends BasePage {
    // Page URL
    private static final String PAGE_URL = "https://demo.guru99.com/insurance/v1/index.php";
    
    // Login form elements
    private By emailInputLocator = By.id("email");
    private By passwordInputLocator = By.id("password");
    private By loginButtonLocator = By.name("submit");
    private By registerLinkLocator = By.linkText("Register");
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Open login page
     */
    public LoginPage openPage() {
        driver.get(PAGE_URL);
        System.out.println("Opened login page: " + PAGE_URL);
        return this;
    }
    
    /**
     * Enter email
     */
    public LoginPage enterEmail(String email) {
        enterText(emailInputLocator, email);
        System.out.println("Entered email: " + email);
        return this;
    }
    
    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        enterText(passwordInputLocator, password);
        System.out.println("Entered password");
        return this;
    }
    
    /**
     * Click login button
     */
    public void clickLogin() {
        try {
            clickElement(loginButtonLocator);
            System.out.println("Clicked login button");
        } catch (Exception e) {
            System.err.println("Failed to click login button, trying JavaScript click: " + e.getMessage());
            jsClick(loginButtonLocator);
        }
    }
    
    /**
     * Perform login
     */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
        
        // Wait for login to complete, page should change
        try {
            Thread.sleep(2000); // Brief wait to ensure page loads
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Click register link
     */
    public void clickRegister() {
        clickElement(registerLinkLocator);
        System.out.println("Clicked register link");
    }
    
    /**
     * Check if on login page
     */
    public boolean isOnLoginPage() {
        try {
            return driver.findElement(loginButtonLocator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}