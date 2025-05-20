package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.io.File;

/**
 * File Upload Page Object Class
 */
public class FileUploadPage extends BasePage {
    // Element locators
    private By fileInputLocator = By.id("uploadfile_0");
    private By termsCheckboxLocator = By.id("terms");
    private By submitButtonLocator = By.id("submitbutton");
    private By resultMessageLocator = By.id("res");
    
    public FileUploadPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Open file upload page
     */
    public FileUploadPage openPage() {
        driver.get("https://demo.guru99.com/test/upload/");
        System.out.println("Opened file upload test page");
        return this;
    }
    
    /**
     * Select file to upload
     * @param filePath Complete path to the file
     */
    public FileUploadPage selectFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Warning: File does not exist - " + filePath);
            }
            
            // Fix: Use waitForElementVisible method
            WebElement fileInput = waitForElementVisible(fileInputLocator);
            fileInput.sendKeys(file.getAbsolutePath());
            System.out.println("Selected file: " + file.getName());
        } catch (Exception e) {
            System.out.println("Failed to select file: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Check the accept terms checkbox
     */
    public FileUploadPage acceptTerms() {
        try {
            WebElement checkbox = waitForElementClickable(termsCheckboxLocator);
            if (!checkbox.isSelected()) {
                checkbox.click();
                System.out.println("Checked accept terms checkbox");
            }
        } catch (Exception e) {
            System.out.println("Failed to check terms checkbox: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Click submit file button
     */
    public FileUploadPage clickSubmit() {
        try {
            WebElement submitButton = waitForElementClickable(submitButtonLocator);
            submitButton.click();
            System.out.println("Clicked submit button");
            
            // Wait for upload to complete, result message to appear
            waitForElementVisible(resultMessageLocator);
        } catch (Exception e) {
            System.out.println("Failed to click submit button or wait for result: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Check if upload was successful
     * @return Whether upload was successful
     */
    
    public boolean isUploadSuccessful() {
        try {
            WebElement resultMessage = waitForElementVisible(resultMessageLocator);
            boolean isDisplayed = resultMessage.isDisplayed();
            String message = resultMessage.getText();
            System.out.println("Upload result message: " + message);
            // The website always shows success message, so we just check if the message is displayed
            return isDisplayed && message.length() > 0;
        } catch (Exception e) {
            System.out.println("Failed to check upload result: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get upload result message text
     * @return Result message text
     */
    public String getResultMessage() {
        try {
            WebElement resultMessage = waitForElementVisible(resultMessageLocator);
            return resultMessage.getText();
        } catch (Exception e) {
            System.out.println("Failed to get result message: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Complete file upload workflow
     * @param filePath File path
     */
    public FileUploadPage uploadFile(String filePath) {
        this.selectFile(filePath)
            .acceptTerms()
            .clickSubmit();
        return this;
    }
}