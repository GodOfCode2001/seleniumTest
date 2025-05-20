package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Drag and Drop Page Object
 */
public class DragAndDropPage extends BasePage {
    private String pageUrl = "https://demo.guru99.com/test/drag_drop.html";
    
    // Source element locators
    private By bankButtonLocator = By.xpath("//li[@id='credit2']/a");
    private By salesButtonLocator = By.xpath("//li[@id='credit1']/a");
    private By amount5000Locator = By.xpath("//li[@id='fourth']/a");
    
    // Target element locators
    private By bankAccountDropLocator = By.xpath("//ol[@id='bank']");
    private By salesAccountDropLocator = By.xpath("//ol[@id='loan']");
    private By amountDebitDropLocator = By.xpath("//ol[@id='amt7']");
    private By amountCreditDropLocator = By.xpath("//ol[@id='amt8']");
    
    // Verification elements
    private By perfectButtonLocator = By.xpath("//div[@id='equal']/a");
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public DragAndDropPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Open drag and drop demo page
     * @return Current page object instance, supports method chaining
     */
    public DragAndDropPage openPage() {
        driver.get(pageUrl);
        System.out.println("Opened drag and drop demo page: " + pageUrl);
        return this;
    }
    
    /**
     * Perform drag and drop operation
     * @param source Source element locator
     * @param target Target element locator
     * @return Current page object instance, supports method chaining
     */
    public void dragAndDrop(By source, By target) {
        try {
            WebElement sourceElement = waitForElementVisible(source);
            WebElement targetElement = waitForElementVisible(target);
            
            Actions actions = new Actions(driver);
            actions.dragAndDrop(sourceElement, targetElement).perform();
            
            System.out.println("Performed drag and drop operation: from " + source + " to " + target);
        } catch (Exception e) {
            System.err.println("Drag and drop operation failed: " + e.getMessage());
            
            // Try using JavaScript method to perform drag and drop
            try {
                WebElement sourceElement = driver.findElement(source);
                WebElement targetElement = driver.findElement(target);
                
                String js = "function createEvent(typeOfEvent) {\n" +
                        "    var event = document.createEvent(\"CustomEvent\");\n" +
                        "    event.initCustomEvent(typeOfEvent, true, true, null);\n" +
                        "    event.dataTransfer = {\n" +
                        "        data: {},\n" +
                        "        setData: function(key, value) {\n" +
                        "            this.data[key] = value;\n" +
                        "        },\n" +
                        "        getData: function(key) {\n" +
                        "            return this.data[key];\n" +
                        "        }\n" +
                        "    };\n" +
                        "    return event;\n" +
                        "}\n" +
                        "\n" +
                        "function dispatchEvent(element, event, transferData) {\n" +
                        "    if (transferData !== undefined) {\n" +
                        "        event.dataTransfer = transferData;\n" +
                        "    }\n" +
                        "    if (element.dispatchEvent) {\n" +
                        "        element.dispatchEvent(event);\n" +
                        "    } else if (element.fireEvent) {\n" +
                        "        element.fireEvent(\"on\" + event.type, event);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "function simulateHTML5DragAndDrop(element, target) {\n" +
                        "    var dragStartEvent = createEvent('dragstart');\n" +
                        "    dispatchEvent(element, dragStartEvent);\n" +
                        "    var dropEvent = createEvent('drop');\n" +
                        "    dispatchEvent(target, dropEvent, dragStartEvent.dataTransfer);\n" +
                        "    var dragEndEvent = createEvent('dragend');\n" +
                        "    dispatchEvent(element, dragEndEvent, dropEvent.dataTransfer);\n" +
                        "}\n" +
                        "\n" +
                        "var source = arguments[0];\n" +
                        "var target = arguments[1];\n" +
                        "simulateHTML5DragAndDrop(source, target);";
                
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js, sourceElement, targetElement);
                System.out.println("Performed drag and drop operation using JavaScript");
            } catch (Exception ex) {
                System.err.println("JavaScript drag and drop also failed: " + ex.getMessage());
            }
        }
    }
    
    /**
     * Complete all drag and drop operations
     */
    public void completeAllDragAndDrop() {
        // Drag Bank to account column
        dragAndDrop(bankButtonLocator, bankAccountDropLocator);
        
        // Drag 5000 to debit amount column
        dragAndDrop(amount5000Locator, amountDebitDropLocator);
        
        // Drag Sales to credit account column
        dragAndDrop(salesButtonLocator, salesAccountDropLocator);
        
        // Drag 5000 to credit amount column
        dragAndDrop(amount5000Locator, amountCreditDropLocator);
    }
    
    /**
     * Check if "Perfect!" button is displayed
     * @return Whether the completion button is displayed
     */
    public boolean isPerfectButtonDisplayed() {
        try {
            return driver.findElement(perfectButtonLocator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}