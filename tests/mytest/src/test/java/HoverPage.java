package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List; // Add this line to import List class
import utils.*;

/**
 * Mouse hover test page object
 */
public class HoverPage extends BasePage {
    private String pageUrl = "https://demo.guru99.com/test/tooltip.html";
    
    // Using more locator methods to increase stability
    private By downloadButtonLocator = By.xpath("//a[@id='download']"); // Use XPath instead of By.id
    private By tooltipTextLocator = By.xpath("//div[@class='tooltip']/span"); // Adjust tooltip locator
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public HoverPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Open tooltip test page
     */
    public HoverPage openPage() {
        driver.get(pageUrl);
        System.out.println("Opened tooltip test page");
        
        // Increase page load wait time
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return this;
    }
    
    /**
     * Check if page contains download button
     */
    public boolean hasDownloadButton() {
        try {
            // Ensure page is fully loaded
            new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Print page source for debugging
            System.out.println("Page source contains 'download' string: " + 
                               driver.getPageSource().contains("download"));
            
            // Try multiple locator methods
            try {
                WebElement button = driver.findElement(By.id("download"));
                System.out.println("Found download button by ID");
                return true;
            } catch (NoSuchElementException e1) {
                try {
                    WebElement button = driver.findElement(By.linkText("Download now"));
                    System.out.println("Found download button by link text");
                    return true;
                } catch (NoSuchElementException e2) {
                    try {
                        WebElement button = driver.findElement(By.className("download"));
                        System.out.println("Found download button by class name");
                        return true;
                    } catch (NoSuchElementException e3) {
                        // Finally try XPath
                        try {
                            WebElement button = driver.findElement(By.xpath("//a[contains(@href,'download')]"));
                            System.out.println("Found download button by href containing 'download'");
                            return true;
                        } catch (NoSuchElementException e4) {
                            System.out.println("Could not find download button");
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking download button: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Perform hover operation over download button
     */
    public HoverPage hoverOverDownloadButton() {
        try {
            // Check if there are iframes
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            if (!iframes.isEmpty()) {
                System.out.println("Page contains " + iframes.size() + " iframes, attempting to switch");
                for (int i = 0; i < iframes.size(); i++) {
                    try {
                        driver.switchTo().frame(i);
                        if (driver.findElements(downloadButtonLocator).size() > 0) {
                            System.out.println("Found download button in iframe " + i);
                            break;
                        }
                        driver.switchTo().defaultContent();
                    } catch (Exception e) {
                        driver.switchTo().defaultContent();
                    }
                }
            }
            
            // Try different location strategies
            WebElement downloadButton = null;
            try {
                downloadButton = driver.findElement(By.id("download"));
            } catch (NoSuchElementException e1) {
                try {
                    downloadButton = driver.findElement(By.xpath("//a[contains(@href,'download')]"));
                } catch (NoSuchElementException e2) {
                    try {
                        downloadButton = driver.findElement(By.linkText("Download now"));
                    } catch (NoSuchElementException e3) {
                        throw new NoSuchElementException("Could not find download button, tried multiple locator methods");
                    }
                }
            }
            
            if (downloadButton != null) {
                Actions actions = new Actions(driver);
                actions.moveToElement(downloadButton).perform();
                System.out.println("Performed hover over download button");
                
                // Wait for tooltip to display
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Hover operation failed: " + e.getMessage());
            // Print page source for debugging
            System.out.println("Page source: " + driver.getPageSource());
        }
        return this;
    }
    
    /**
     * Get tooltip text
     */
    public String getTooltipText() {
        try {
            // Try multiple ways to find tooltip
            WebElement tooltipText = null;
            try {
                tooltipText = driver.findElement(tooltipTextLocator);
            } catch (NoSuchElementException e1) {
                try {
                    tooltipText = driver.findElement(By.cssSelector(".tooltip"));
                } catch (NoSuchElementException e2) {
                    try {
                        tooltipText = driver.findElement(By.xpath("//*[contains(@class,'tooltip')]"));
                    } catch (NoSuchElementException e3) {
                        System.err.println("Cannot find tooltip element");
                        return "";
                    }
                }
            }
            
            if (tooltipText != null) {
                String text = tooltipText.getText();
                System.out.println("Tooltip text: " + text);
                return text;
            }
            return "";
        } catch (Exception e) {
            System.err.println("Failed to get tooltip text: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Check if tooltip is visible
     */
    public boolean isTooltipVisible() {
        try {
            // Try multiple ways to find tooltip
            List<WebElement> tooltips = driver.findElements(By.xpath("//*[contains(@class,'tooltip')]"));
            if (!tooltips.isEmpty()) {
                for (WebElement tooltip : tooltips) {
                    if (tooltip.isDisplayed()) {
                        System.out.println("Found visible tooltip");
                        return true;
                    }
                }
            }
            
            // If not found, try other locator methods
            tooltips = driver.findElements(By.cssSelector(".tooltip"));
            if (!tooltips.isEmpty()) {
                for (WebElement tooltip : tooltips) {
                    if (tooltip.isDisplayed()) {
                        return true;
                    }
                }
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Failed to check tooltip visibility: " + e.getMessage());
            return false;
        }
    }
}