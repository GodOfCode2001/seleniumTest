package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
/**
 * Radio Button & Checkbox Demo Page Object Class
 */
public class FormPage extends BasePage {
    // Radio button locators
    private By radioOption1Locator = By.id("vfb-7-1");
    private By radioOption2Locator = By.id("vfb-7-2");
    private By radioOption3Locator = By.id("vfb-7-3");
    
    // Checkbox locators
    private By checkbox1Locator = By.id("vfb-6-0");
    private By checkbox2Locator = By.id("vfb-6-1");
    private By checkbox3Locator = By.id("vfb-6-2");
    
    private By navbarLocator = By.cssSelector(".navbar-nav");
    private By dropdownToggleLocator = By.cssSelector(".dropdown-toggle");
    private By dropdownMenuLocator = By.cssSelector(".dropdown-menu");
    private By dropdownItemLocator = By.cssSelector(".dropdown-menu li a");
    
    public FormPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Open form page
     */
    public FormPage openPage() {
        driver.get("https://demo.guru99.com/test/radio.html");
        System.out.println("Opened radio button and checkbox test page");
        return this;
    }
    
    /**
     * Select radio button
     * @param option Option number (1, 2, or 3)
     */
    public FormPage selectRadioButton(int option) {
        By locator;
        switch (option) {
            case 1:
                locator = radioOption1Locator;
                break;
            case 2:
                locator = radioOption2Locator;
                break;
            case 3:
                locator = radioOption3Locator;
                break;
            default:
                throw new IllegalArgumentException("Invalid option number: " + option);
        }
        
        try {
            WebElement radioButton = waitForElementClickable(locator);
            if (!radioButton.isSelected()) {
                radioButton.click();
            }
            System.out.println("Selected option " + option);
        } catch (Exception e) {
            System.out.println("Failed to select radio button: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Check or uncheck checkbox
     * @param checkboxNumber Checkbox number (1, 2, or 3)
     * @param check Whether to check
     */
    public FormPage toggleCheckbox(int checkboxNumber, boolean check) {
        By locator;
        switch (checkboxNumber) {
            case 1:
                locator = checkbox1Locator;
                break;
            case 2:
                locator = checkbox2Locator;
                break;
            case 3:
                locator = checkbox3Locator;
                break;
            default:
                throw new IllegalArgumentException("Invalid checkbox number: " + checkboxNumber);
        }
        
        try {
            WebElement checkbox = waitForElementClickable(locator);
            // Only click if current state is different from target state
            if ((check && !checkbox.isSelected()) || (!check && checkbox.isSelected())) {
                checkbox.click();
            }
            System.out.println((check ? "Checked" : "Unchecked") + " checkbox " + checkboxNumber);
        } catch (Exception e) {
            System.out.println("Failed to operate checkbox: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Check if radio button is selected
     * @param option Option number (1, 2, or 3)
     */
    public boolean isRadioButtonSelected(int option) {
        By locator;
        switch (option) {
            case 1:
                locator = radioOption1Locator;
                break;
            case 2:
                locator = radioOption2Locator;
                break;
            case 3:
                locator = radioOption3Locator;
                break;
            default:
                throw new IllegalArgumentException("Invalid option number: " + option);
        }
        
        try {
            return driver.findElement(locator).isSelected();
        } catch (NoSuchElementException e) {
            System.out.println("Radio button not found: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if checkbox is selected
     * @param checkboxNumber Checkbox number (1, 2, or 3)
     */
    public boolean isCheckboxSelected(int checkboxNumber) {
        By locator;
        switch (checkboxNumber) {
            case 1:
                locator = checkbox1Locator;
                break;
            case 2:
                locator = checkbox2Locator;
                break;
            case 3:
                locator = checkbox3Locator;
                break;
            default:
                throw new IllegalArgumentException("Invalid checkbox number: " + checkboxNumber);
        }
        
        try {
            return driver.findElement(locator).isSelected();
        } catch (NoSuchElementException e) {
            System.out.println("Checkbox not found: " + e.getMessage());
            return false;
        }
    }

    /**
     * 点击导航栏中的下拉菜单
     * @param menuName 菜单名称
     */
    public void clickDropdown(String menuName) {
        try {
            // 等待导航栏加载
            waitForElementVisible(navbarLocator);
            
            // 查找并点击下拉菜单
            WebElement dropdown = driver.findElement(
                By.xpath("//a[contains(text(),'" + menuName + "') and contains(@class,'dropdown-toggle')]")
            );
            jsClick(By.xpath("//a[contains(text(),'" + menuName + "')]"));
            System.out.println("Clicked dropdown menu: " + menuName);
        } catch (Exception e) {
            System.err.println("Failed to click dropdown menu: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 检查下拉菜单是否展开
     * @param menuName 菜单名称
     * @return 是否展开
     */
    public boolean isDropdownExpanded(String menuName) {
        try {
            WebElement dropdownParent = driver.findElement(
                By.xpath("//a[contains(text(),'" + menuName + "')]/parent::li")
            );
            boolean isExpanded = dropdownParent.getAttribute("class").contains("open");
            System.out.println("Dropdown menu " + menuName + " expanded: " + isExpanded);
            return isExpanded;
        } catch (Exception e) {
            System.err.println("Failed to check dropdown state: " + e.getMessage());
            return false;
        }
    }

    /**
     * 选择下拉菜单选项
     * @param optionText 选项文本
     */
    public void selectDropdownOption(String optionText) {
        try {
            WebElement option = waitForElementClickable(
                By.xpath("//ul[contains(@class,'dropdown-menu')]//a[contains(text(),'" + optionText + "')]")
            );
            jsClick(By.xpath("//ul[contains(@class,'dropdown-menu')]//a[contains(text(),'" + optionText + "')]"));
            System.out.println("Selected dropdown option: " + optionText);
        } catch (Exception e) {
            System.err.println("Failed to select dropdown option: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 获取当前页面标题
     * @return 页面标题
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}