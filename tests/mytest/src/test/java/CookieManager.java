package utils;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

/**
 * Cookie Management Utility Class
 */
public class CookieManager {
    private WebDriver driver;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public CookieManager(WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Add Cookie
     * @param name Cookie name
     * @param value Cookie value
     */
    public void addCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        driver.manage().addCookie(cookie);
        System.out.println("Added Cookie: " + name + "=" + value);
    }
    
    /**
     * Get Cookie value
     * @param name Cookie name
     * @return Cookie value, returns null if it doesn't exist
     */
    public String getCookieValue(String name) {
        Cookie cookie = driver.manage().getCookieNamed(name);
        return cookie != null ? cookie.getValue() : null;
    }
    
    /**
     * Delete specific Cookie
     * @param name Cookie name
     */
    public void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
        System.out.println("Deleted Cookie: " + name);
    }
    
    /**
     * Delete all Cookies
     */
    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
        System.out.println("Deleted All Cookies");
    }
    
    /**
     * Print all Cookies
     */
    public void printAllCookies() {
        Set<Cookie> cookies = driver.manage().getCookies();
        System.out.println("Currently have " + cookies.size() + " Cookies:");
        for (Cookie cookie : cookies) {
            System.out.println("  " + cookie.getName() + ": " + cookie.getValue());
        }
    }
    
    /**
     * Add Cookie to disable consent popup
     */
    public void addConsentCookie() {
        // Adjust Cookie name and value according to actual website
        addCookie("cookieconsent_status", "dismiss");
        addCookie("consent", "accepted");
        System.out.println("Added Cookie to disable consent popup");
    }
}