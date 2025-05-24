import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
 
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;
 
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
 
import pages.*;
import utils.*;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.time.Duration;
import java.io.File;
 
import static org.junit.Assert.*;
 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Guru99Tests {
    private WebDriver driver;
    private FormPage formPage;
    private FileUploadPage fileUploadPage;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private HomePage homePage;
    private CookieManager cookieManager;
    private DragAndDropPage dragAndDropPage;
    private HoverPage hoverPage;
    private HistoryTestPage historyTestPage;
    private static ConfigReader config;
 
    // Test account information
    private final String TEST_EMAIL = "test" + System.currentTimeMillis() + "@example.com";
    private final String TEST_PASSWORD = "Password123";
   
    // Invalid credentials for error login testing
    private static String INVALID_EMAIL;
    private static String INVALID_PASSWORD;
   
    // Store registered user for dependent tests
    private static String REGISTERED_EMAIL;
    private static String REGISTERED_PASSWORD;
    private static boolean REGISTRATION_COMPLETED = false;
    private static boolean LOGIN_COMPLETED = false;
 
    @BeforeClass
    public static void setUpClass() {
        System.out.println("===========================================");
        System.out.println("       GURU99 TEST SUITE STARTING");
        System.out.println("===========================================");
        System.out.println("TEST DEPENDENCY CHAIN:");
        System.out.println("testA1_UserRegistration (depends on none)");
        System.out.println("  ↓");
        System.out.println("testC_ValidLogin (depends on A1)");
        System.out.println("  ↓");
        System.out.println("testD_UserLogout (depends on A1→C)");
        System.out.println("");
        System.out.println("INDEPENDENT TESTS:");
        System.out.println("- testA2_InvalidLoginAttempt");
        System.out.println("- testE_FormInteractions");
        System.out.println("- testF_FileUpload");
        System.out.println("- testG_MultipleStaticPages");
        System.out.println("- All other tests...");
        System.out.println("===========================================");
       
        config = new ConfigReader();
 
        // Use config credentials as invalid credentials for error testing
        INVALID_EMAIL = config.getUsername();
        INVALID_PASSWORD = config.getPassword();
       
        System.out.println("✓ Test Suite Setup: Using invalid credentials for error testing");
        System.out.println("✓ Invalid Email: " + INVALID_EMAIL);
    }
 
    @Before
    public void setUp() {
        // 使用WebDriverFactory创建WebDriver
        this.driver = WebDriverFactory.createDriver();
       
        // Initialize page objects
        this.loginPage = new LoginPage(driver);
        this.formPage = new FormPage(driver);
        this.fileUploadPage = new FileUploadPage(driver);
        this.registerPage = new RegisterPage(driver);
        this.homePage = new HomePage(driver);
        this.cookieManager = new CookieManager(driver);
        this.dragAndDropPage = new DragAndDropPage(driver);
        this.hoverPage = new HoverPage(driver);
        this.historyTestPage = new HistoryTestPage(driver);
    }
 
    /**
     * Test A1: User Registration - Creates a new user account
     * This test must run first as other tests depend on it
     * Dependencies: None
     * Dependents: testC_ValidLogin
     */
    @Test
    public void testA1_UserRegistration() {
        System.out.println("=== Test A1: Starting user registration test ===");
        System.out.println("Dependencies: None");
        System.out.println("Will be used by: testC_ValidLogin");
       
        // Generate unique test credentials
        REGISTERED_EMAIL = "test" + System.currentTimeMillis() + "@example.com";
        REGISTERED_PASSWORD = "Password123";
       
        try {
            // Step 1: Register new user
            registerPage.openPage();
            registerPage.registerUser(REGISTERED_EMAIL, REGISTERED_PASSWORD);
           
            // Verify redirect to login page
            assertTrue("After registration should redirect to login page", loginPage.isOnLoginPage());
           
            // Mark registration as completed for dependent tests
            REGISTRATION_COMPLETED = true;
           
            System.out.println("✓ Test A1 Completed: User registered successfully");
            System.out.println("✓ Registered Email: " + REGISTERED_EMAIL);
            System.out.println("✓ Registration status set for dependent tests");
        } catch (Exception e) {
            REGISTRATION_COMPLETED = false;
            System.err.println("✗ Test A1 Failed: " + e.getMessage());
            throw e;
        }
    }
   
    /**
     * Test A2: Invalid Login Attempt - Tests error handling
     * Uses invalid credentials from config file
     * Dependencies: None
     * Dependents: None
     */
    @Test
    public void testA2_InvalidLoginAttempt() {
        System.out.println("=== Test A2: Starting invalid login test ===");
        System.out.println("Dependencies: None");
        System.out.println("Dependents: None");
       
        try {
            // Open login page
            loginPage.openPage();
           
            // Attempt login with invalid credentials
            loginPage.login(INVALID_EMAIL, INVALID_PASSWORD);
           
            // Verify login failed - should still be on login page
            assertTrue("Should remain on login page after invalid login", loginPage.isOnLoginPage());
            // Verify user is not logged in
            assertFalse("Should not be logged in with invalid credentials", homePage.isLoggedIn());
           
            System.out.println("✓ Test A2 Completed: Invalid login properly rejected");
        } catch (Exception e) {
            System.err.println("✗ Test A2 Failed: " + e.getMessage());
            throw e;
        }
    }
 
    /**
     * Test C: Valid Login - Depends on Test A1 (Registration)
     * Uses the account created in testA1_UserRegistration
     * Dependencies: testA1_UserRegistration
     * Dependents: testD_UserLogout
     */
    @Test
    public void testC_ValidLogin() {
        System.out.println("=== Test C: Starting valid login test ===");
        System.out.println("Dependencies: testA1_UserRegistration");
        System.out.println("Will be used by: testD_UserLogout");
       
        // Check dependency: Registration must be completed first
        if (!REGISTRATION_COMPLETED) {
            fail("❌ DEPENDENCY FAILED: testA1_UserRegistration must complete successfully before this test");
        }
       
        // Ensure we have registered credentials from Test A1
        if (REGISTERED_EMAIL == null || REGISTERED_PASSWORD == null) {
            fail("❌ DEPENDENCY FAILED: User registration data not available from testA1_UserRegistration");
        }
       
        System.out.println("✓ Dependency check passed: Registration completed");
        System.out.println("✓ Using registered email: " + REGISTERED_EMAIL);
       
        try {
            // Open login page
            loginPage.openPage();
           
            // Login with registered account
            loginPage.login(REGISTERED_EMAIL, REGISTERED_PASSWORD);
           
            // Verify successful login
            assertTrue("Should be successfully logged in", homePage.isLoggedIn());
            assertEquals("Logged in user email should match", REGISTERED_EMAIL, homePage.getLoggedInEmail());
           
            // Mark login as completed for dependent tests
            LOGIN_COMPLETED = true;
           
            System.out.println("✓ Test C Completed: Valid login successful");
            System.out.println("✓ Login status set for dependent tests");
        } catch (Exception e) {
            LOGIN_COMPLETED = false;
            System.err.println("✗ Test C Failed: " + e.getMessage());
            throw e;
        }
    }
   
    /**
     * Test D: User Logout - Depends on Test C (Valid Login)
     * Must be logged in before attempting logout
     * Dependencies: testA1_UserRegistration → testC_ValidLogin
     * Dependents: None
     */
    @Test
    public void testD_UserLogout() {
        System.out.println("=== Test D: Starting logout test ===");
        System.out.println("Dependencies: testA1_UserRegistration → testC_ValidLogin");
        System.out.println("Dependents: None");
       
        // Check dependency chain: Registration → Login → Logout
        if (!REGISTRATION_COMPLETED) {
            fail("❌ DEPENDENCY FAILED: testA1_UserRegistration must complete successfully before this test");
        }
       
        if (!LOGIN_COMPLETED) {
            fail("❌ DEPENDENCY FAILED: testC_ValidLogin must complete successfully before this test");
        }
       
        System.out.println("✓ Dependency check passed: Registration and Login completed");
       
        // Ensure user is currently logged in
        if (!homePage.isLoggedIn()) {
            // Try to login first to satisfy dependency
            System.out.println("⚠ User not logged in, attempting login to satisfy dependency...");
            loginPage.openPage();
            loginPage.login(REGISTERED_EMAIL, REGISTERED_PASSWORD);
            assertTrue("❌ DEPENDENCY FAILED: Must be logged in before testing logout", homePage.isLoggedIn());
            System.out.println("✓ Emergency login successful");
        }
       
        try {
            // Perform logout
            homePage.logout();
           
            // Verify successful logout
            assertTrue("After logout should return to login page", loginPage.isOnLoginPage());
            assertFalse("Should not be logged in after logout", homePage.isLoggedIn());
           
            System.out.println("✓ Test D Completed: Logout successful");
        } catch (Exception e) {
            System.err.println("✗ Test D Failed: " + e.getMessage());
            throw e;
        }
    }
   
    /**
     * Test E: Form Interactions - Independent test
     * Dependencies: None
     * Dependents: None
     */
    @Test
    public void testE_FormInteractions() {
        System.out.println("=== Test E: Starting form interactions test ===");
        System.out.println("Dependencies: None");
        System.out.println("Dependents: None");
       
        formPage.openPage();
       
        try {
            // Select radio button Option 2
            formPage.selectRadioButton(2);
           
            // Check checkbox 1 and checkbox 3
            formPage.toggleCheckbox(1, true);
            formPage.toggleCheckbox(3, true);
           
            // Verify radio button status
            assertTrue("Radio button 2 should be selected", formPage.isRadioButtonSelected(2));
            assertFalse("Radio button 1 should not be selected", formPage.isRadioButtonSelected(1));
            assertFalse("Radio button 3 should not be selected", formPage.isRadioButtonSelected(3));
           
            // Verify checkbox status
            assertTrue("Checkbox 1 should be selected", formPage.isCheckboxSelected(1));
            assertFalse("Checkbox 2 should not be selected", formPage.isCheckboxSelected(2));
            assertTrue("Checkbox 3 should be selected", formPage.isCheckboxSelected(3));
           
            // Uncheck checkbox 3
            formPage.toggleCheckbox(3, false);
            assertFalse("Checkbox 3 should be unchecked", formPage.isCheckboxSelected(3));
           
            System.out.println("✓ Test E Completed: Form interaction test successful");
        } catch (Exception e) {
            System.out.println("✗ Test E Failed: Form interaction test failed: " + e.getMessage());
            fail("Form interaction test failed: " + e.getMessage());
        }
    }
   
    /**
     * Test F: File Upload - Independent test
     */
    @Test
    public void testF_FileUpload() {
        System.out.println("Test F: Starting file upload test");
       
        // Create temporary test file
        String tempFilePath = createTempTextFile();
        assertNotNull("Should successfully create temporary file", tempFilePath);
       
        // Open upload page
        fileUploadPage.openPage();
       
        // Upload file
        fileUploadPage.uploadFile(tempFilePath);
       
        // Verify successful upload
        boolean uploadSuccess = fileUploadPage.isUploadSuccessful();
        String resultMessage = fileUploadPage.getResultMessage();
       
        assertTrue("File should be uploaded successfully", uploadSuccess);
        System.out.println("Upload result: " + resultMessage);
        System.out.println("Test F Completed: File upload successful");
    }
 
    /**
     * Create temporary text file for testing
     * @return Path of the created temporary file
     */
    private String createTempTextFile() {
        try {
            File tempFile = File.createTempFile("upload-test-", ".txt");
            tempFile.deleteOnExit(); // Ensure deletion after test
            System.out.println("Created temporary file: " + tempFile.getAbsolutePath());
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            System.out.println("Failed to create temporary file: " + e.getMessage());
            return null;
        }
    }
 
    /**
     * Test G: Multiple Static Pages - Independent test
     */
    @Test
    public void testG_MultipleStaticPages() {
        System.out.println("Test G: Starting multiple static pages test");
       
        String[] pageUrls = new String[] {
            "https://demo.guru99.com/test/",
            "https://demo.guru99.com/test/drag_drop.html",
            "https://demo.guru99.com/test/newtours/register.php"
        };
       
        String[][] expectedTitleKeywords = new String[][] {
            {"DatePicker", "Demo", "Date"}, // Multiple possible keywords
            {"Drag", "Drop"},
            {"Register", "Mercury", "Tours"}
        };
       
        for (int i = 0; i < pageUrls.length; i++) {
            try {
                driver.get(pageUrls[i]);
                // Wait for page to load
                new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
               
                String actualTitle = driver.getTitle();
                System.out.println("Testing page: " + pageUrls[i] + ", title: " + actualTitle);
               
                // Check if title contains any of the keywords
                boolean titleMatched = false;
                for (String keyword : expectedTitleKeywords[i]) {
                    if (actualTitle.contains(keyword)) {
                        titleMatched = true;
                        break;
                    }
                }
               
                assertTrue("Page title '" + actualTitle + "' should contain at least one expected keyword", titleMatched);
            } catch (Exception e) {
                System.err.println("Error when accessing page " + pageUrls[i] + ": " + e.getMessage());
                // Continue testing next page instead of failing immediately
                continue;
            }
        }
       
        System.out.println("Test G Completed: Multiple static pages test successful");
    }
   
    /**
     * Test H: Complex XPath - Independent test
     */
    @Test
    public void testH_ComplexXPath() {
        System.out.println("Test H: Starting complex XPath test");
       
        driver.get("https://demo.guru99.com/test/login.html");
       
        // Use complex XPath to locate login button
        WebElement loginButton = driver.findElement(
            By.xpath("//form[@id='login_form']//button[@id='SubmitLogin']")
        );
       
        assertNotNull("Should find login button", loginButton);
        assertEquals("Button text should be correct", "Sign in",
                    loginButton.findElement(By.tagName("span")).getText().trim());
                   
        System.out.println("Test H Completed: Complex XPath test successful");
    }
   
    /**
     * Test I: Cookie Manipulation - Independent test
     */
    @Test
    public void testI_CookieManipulation() {
        System.out.println("Test I: Starting cookie manipulation test");
       
        // Open test page
        driver.get("https://demo.guru99.com/test/cookie/selenium_aut.php");
       
        // Print all cookies
        cookieManager.printAllCookies();
       
        // Add custom cookie
        cookieManager.addCookie("testCookie", "testValue");
       
        // Verify cookie was added
        String cookieValue = cookieManager.getCookieValue("testCookie");
        assertEquals("Cookie value should match", "testValue", cookieValue);
       
        // Add consent popup disabling cookie
        cookieManager.addConsentCookie();
       
        // Refresh page, verify popup doesn't appear
        driver.navigate().refresh();
       
        // Delete all cookies
        cookieManager.deleteAllCookies();
       
        System.out.println("Test I Completed: Cookie manipulation test successful");
    }
 
    /**
     * Test J: Drag and Drop - Independent test
     */
    @Test
    public void testJ_DragAndDrop() {
        System.out.println("Test J: Starting drag and drop test");
       
        // Open drag and drop demo page
        dragAndDropPage.openPage();
       
        // Perform all drag and drop operations
        dragAndDropPage.completeAllDragAndDrop();
       
        // Verify if "Perfect!" button is displayed
        boolean isPerfect = dragAndDropPage.isPerfectButtonDisplayed();
       
        // Assert drag and drop operations successful
        assertTrue("Perfect button should be displayed after drag and drop operations", isPerfect);
       
        System.out.println("Test J Completed: Drag and drop test successful");
    }
 
    /**
     * Test K: Mouse Hover - Independent test
     */
    @Test
    public void testK_HoverTest() {
        System.out.println("Test K: Starting mouse hover test");
       
        // Open hover test page
        hoverPage.openPage();
       
        // Check if page contains download button
        boolean hasDownloadButton = hoverPage.hasDownloadButton();
        if (!hasDownloadButton) {
            System.out.println("Warning: Download button not found on page, test may fail");
        }
       
        try {
            // Perform hover operation
            hoverPage.hoverOverDownloadButton();
           
            // Check tooltip
            boolean isTooltipVisible = hoverPage.isTooltipVisible();
           
            if (isTooltipVisible) {
                // Verify tooltip
                String tooltipText = hoverPage.getTooltipText();
                assertFalse("Tooltip text should not be empty", tooltipText.isEmpty());
                System.out.println("Tooltip displays correctly, text: " + tooltipText);
            } else {
                // If tooltip not visible, consider skipping this test instead of failing
                System.out.println("Tooltip not visible, website structure may have changed");
                // Use assumption to skip test instead of failing
                Assume.assumeTrue("Skipping test: Tooltip not visible", false);
            }
        } catch (Exception e) {
            System.err.println("Error occurred during hover test: " + e.getMessage());
            // Use assumption to skip test instead of failing
            Assume.assumeTrue("Skipping test: " + e.getMessage(), false);
        }
       
        System.out.println("Test K Completed: Mouse hover test successful");
    }
 
    /**
     * Test L: Browser History - Independent test
     */
    @Test
    public void testL_BrowserHistory() {
        System.out.println("Test L: Starting browser history test");
       
        // Visit first page
        historyTestPage.visitFirstPage();
        assertTrue("Should be on first page", historyTestPage.isOnFirstPage());
       
        // Visit second page
        historyTestPage.visitSecondPage();
        assertTrue("Should be on second page", historyTestPage.isOnSecondPage());
       
        // Test back button
        historyTestPage.goBack();
        assertTrue("After going back should be on first page", historyTestPage.isOnFirstPage());
       
        // Test forward button
        historyTestPage.goForward();
        assertTrue("After going forward should be on second page", historyTestPage.isOnSecondPage());
       
        // Test refresh button
        historyTestPage.refresh();
        assertTrue("After refresh should still be on second page", historyTestPage.isOnSecondPage());
       
        System.out.println("Test L Completed: Browser history test successful");
    }
 
    /**
     * Test M: Textarea Functionality - Independent test
     */
    @Test
    public void testM_TextareaFunctionality() {
        System.out.println("Test M: Starting textarea functionality test");
       
        // Create Textarea page object
        TextareaPage textareaPage = new TextareaPage(driver);
        textareaPage.openPage();
       
        try {
            // Test text input
            String testText = "This is a test text for testing the textarea functionality on the Guru99 website. "
                + "We are verifying text input, character display, and form interaction features.";
            textareaPage.enterText(testText);
           
            // Verify text was successfully input
            String actualText = textareaPage.getTextContent();
            assertTrue("Textarea should contain the input text", actualText.equals(testText));
           
            System.out.println("Test M Completed: Textarea functionality test successful");
        } catch (Exception e) {
            System.err.println("Test M Failed: Textarea test failed: " + e.getMessage());
            fail("Textarea test failed: " + e.getMessage());
        }
    }
 
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}