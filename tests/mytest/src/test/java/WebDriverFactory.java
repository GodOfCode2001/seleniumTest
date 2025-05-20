package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.SessionNotCreatedException;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * WebDriver factory for creating and configuring browser instances
 */
public class WebDriverFactory {
    
    /**
     * Create WebDriver with fallback to Firefox if Chrome fails
     * @return Configured WebDriver instance
     */
    public static WebDriver createDriver() {
        WebDriver driver;
        
        // Setup Chrome WebDriver
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");  // Disable browser notifications
        options.addArguments("--start-maximized");        // Start with maximized window
        options.addArguments("--disable-popup-blocking"); // Disable popup blocking
        options.addArguments("--disable-infobars");       // Disable info bars
        
        // Set download preferences
        String downloadPath = System.getProperty("user.dir") + File.separator + "downloads";
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadPath);
        prefs.put("download.prompt_for_download", false);
        prefs.put("profile.default_content_settings.popups", 0);
        options.setExperimentalOption("prefs", prefs);
        
        try {
            System.out.println("Initializing Chrome WebDriver with custom configurations");
            driver = new ChromeDriver(options);
        } catch (SessionNotCreatedException e) {
            // If Chrome fails, try using Firefox
            System.out.println("Chrome initialization failed. Falling back to Firefox: " + e.getMessage());
            WebDriverManager.firefoxdriver().setup();
            
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            FirefoxProfile profile = new FirefoxProfile();
            
            // Configure Firefox profile
            profile.setPreference("browser.download.folderList", 2);
            profile.setPreference("browser.download.dir", downloadPath);
            profile.setPreference("browser.download.manager.showWhenStarting", false);
            profile.setPreference("browser.helperApps.neverAsk.saveToDisk", 
                                 "application/pdf,application/x-pdf,application/octet-stream,text/csv");
            
            firefoxOptions.setProfile(profile);
            
            driver = new FirefoxDriver(firefoxOptions);
            driver.manage().window().maximize();
        }
        
        System.out.println("WebDriver initialized successfully");
        return driver;
    }
    
    /**
     * Create a specified browser WebDriver with custom configurations
     * @param browserName Browser name ("chrome" or "firefox")
     * @return Configured WebDriver instance
     */
    public static WebDriver createDriver(String browserName) {
        if (browserName.equalsIgnoreCase("firefox")) {
            return createFirefoxDriver();
        } else {
            return createChromeDriver();
        }
    }
    
    /**
     * Create a configured Chrome WebDriver
     * @return Configured Chrome WebDriver
     */
    public static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        
        // Set download preferences
        String downloadPath = System.getProperty("user.dir") + File.separator + "downloads";
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadPath);
        prefs.put("download.prompt_for_download", false);
        prefs.put("profile.default_content_settings.popups", 0);
        options.setExperimentalOption("prefs", prefs);
        
        System.out.println("Creating Chrome WebDriver with custom configurations");
        System.out.println("Downloads will be saved to: " + downloadPath);
        return new ChromeDriver(options);
    }
    
    /**
     * Create a configured Firefox WebDriver
     * @return Configured Firefox WebDriver
     */
    public static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        FirefoxProfile profile = new FirefoxProfile();
        
        // Configure Firefox profile
        String downloadPath = System.getProperty("user.dir") + File.separator + "downloads";
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", downloadPath);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", 
                             "application/pdf,application/x-pdf,application/octet-stream,text/csv");
        
        options.setProfile(profile);
        
        System.out.println("Creating Firefox WebDriver with custom configurations");
        System.out.println("Downloads will be saved to: " + downloadPath);
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        return driver;
    }
    
    /**
     * Create a headless Chrome WebDriver (for CI/CD environments)
     * @return Headless Chrome WebDriver
     */
    public static WebDriver createHeadlessChromeDriver() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        
        System.out.println("Creating headless Chrome WebDriver");
        return new ChromeDriver(options);
    }
    
    /**
     * Create WebDriver from configuration
     * @param config Configuration reader instance
     * @return Configured WebDriver based on configuration
     */
    public static WebDriver createDriverFromConfig(ConfigReader config) {
        String browser = config.getBrowser();
        System.out.println("Creating WebDriver for browser: " + browser);
        
        if (browser.equalsIgnoreCase("firefox")) {
            return createFirefoxDriver();
        } else if (browser.equalsIgnoreCase("headless")) {
            return createHeadlessChromeDriver();
        } else {
            return createChromeDriver();
        }
    }
}