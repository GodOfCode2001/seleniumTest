package pages;

import org.openqa.selenium.*;

/**
 * Browser history test page object
 */
public class HistoryTestPage extends BasePage {
    private String firstPageUrl = "https://demo.guru99.com/test/";
    private String secondPageUrl = "https://demo.guru99.com/test/drag_drop.html";
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public HistoryTestPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Visit the first page
     * @return Current object instance, supports chained calls
     */
    public HistoryTestPage visitFirstPage() {
        driver.get(firstPageUrl);
        System.out.println("Visited first page: " + firstPageUrl);
        return this;
    }
    
    /**
     * Visit the second page
     * @return Current object instance, supports chained calls
     */
    public HistoryTestPage visitSecondPage() {
        driver.get(secondPageUrl);
        System.out.println("Visited second page: " + secondPageUrl);
        return this;
    }
    
    /**
     * Click browser's back button
     * @return Current object instance, supports chained calls
     */
    public HistoryTestPage goBack() {
        driver.navigate().back();
        System.out.println("Clicked browser's back button");
        
        // Wait for page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return this;
    }
    
    /**
     * Click browser's forward button
     * @return Current object instance, supports chained calls
     */
    public HistoryTestPage goForward() {
        driver.navigate().forward();
        System.out.println("Clicked browser's forward button");
        
        // Wait for page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return this;
    }
    
    /**
     * Refresh current page
     * @return Current object instance, supports chained calls
     */
    public HistoryTestPage refresh() {
        driver.navigate().refresh();
        System.out.println("Refreshed current page");
        
        // Wait for page to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return this;
    }
    
    /**
     * Get current page URL
     * @return Current page URL
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        System.out.println("Current page URL: " + url);
        return url;
    }
    
    /**
     * Check if current page is the first page
     * @return Whether it is the first page
     */
    public boolean isOnFirstPage() {
        return driver.getCurrentUrl().startsWith(firstPageUrl);
    }
    
    /**
     * Check if current page is the second page
     * @return Whether it is the second page
     */
    public boolean isOnSecondPage() {
        return driver.getCurrentUrl().startsWith(secondPageUrl);
    }
}