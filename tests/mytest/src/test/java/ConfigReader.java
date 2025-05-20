package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration reader for test properties
 */
public class ConfigReader {
    private Properties properties;
    private static final String CONFIG_FILE = "src/test/resources/config.properties";
    
    /**
     * Constructor - loads properties from configuration file
     */
    public ConfigReader() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            properties.load(fis);
            fis.close();
            System.out.println("Loaded configuration from: " + CONFIG_FILE);
        } catch (IOException e) {
            System.err.println("Failed to load config file: " + e.getMessage());
            System.err.println("Using default configuration values");
        }
    }
    
    /**
     * Get browser name from configuration
     * @return Browser name (chrome, firefox, headless)
     */
    public String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }
    
    /**
     * Get base URL for the application
     * @return Base URL
     */
    public String getBaseUrl() {
        return properties.getProperty("baseUrl", "https://demo.guru99.com");
    }
    
    /**
     * Get implicit wait timeout in seconds
     * @return Implicit wait timeout
     */
    public int getImplicitWait() {
        try {
            return Integer.parseInt(properties.getProperty("implicitWait", "10"));
        } catch (NumberFormatException e) {
            System.err.println("Invalid implicitWait value in config, using default: 10");
            return 10;
        }
    }
    
    /**
     * Get explicit wait timeout in seconds
     * @return Explicit wait timeout
     */
    public int getExplicitWait() {
        try {
            return Integer.parseInt(properties.getProperty("explicitWait", "15"));
        } catch (NumberFormatException e) {
            System.err.println("Invalid explicitWait value in config, using default: 15");
            return 15;
        }
    }
    
    /**
     * Get test username
     * @return Test username
     */
    public String getUsername() {
        return properties.getProperty("username", "testuser@example.com");
    }
    
    /**
     * Get test password
     * @return Test password
     */
    public String getPassword() {
        return properties.getProperty("password", "password");
    }
    
    /**
     * Check if headless mode is enabled
     * @return True if headless mode is enabled
     */
    public boolean isHeadless() {
        return "true".equalsIgnoreCase(properties.getProperty("headless", "false"));
    }
}