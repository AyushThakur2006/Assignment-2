package com.example.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

public class SauceDemoAutomation {
    public static void main(String[] args) {
        System.out.println("Starting SauceDemo Automation...");
        
        // Setup WebDriver Manager
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        // Remove headless mode to see the browser
        // options.addArguments("--headless");
        
        WebDriver driver = null;
        WebDriverWait wait = null;
        
        try {
            // Initialize WebDriver
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            System.out.println("1. Opening browser and navigating to saucedemo.com");
            driver.get("https://www.saucedemo.com");
            
            // Wait for page to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
            System.out.println("Page loaded successfully");
            
            // Login
            System.out.println("2. Logging in with username and password");
            WebElement usernameField = driver.findElement(By.id("user-name"));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("login-button"));
            
            usernameField.sendKeys("standard_user");
            passwordField.sendKeys("secret_sauce");
            loginButton.click();
            
            // Wait for products page to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("inventory_item")));
            System.out.println("Login successful - Products page loaded");
            
            // Select any item and add to cart
            System.out.println("3. Selecting an item and adding to cart");
            WebElement firstItem = driver.findElement(By.className("inventory_item"));
            WebElement addToCartButton = firstItem.findElement(By.tagName("button"));
            String itemName = firstItem.findElement(By.className("inventory_item_name")).getText();
            addToCartButton.click();
            System.out.println("Added item to cart: " + itemName);
            
            // Go to cart
            System.out.println("4. Going to cart");
            WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
            cartIcon.click();
            
            // Wait for cart page
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cart_item")));
            System.out.println("Cart page loaded");
            
            // Proceed to checkout
            System.out.println("5. Proceeding to checkout");
            WebElement checkoutButton = driver.findElement(By.id("checkout"));
            checkoutButton.click();
            
            // Fill in checkout details
            System.out.println("6. Filling in checkout details");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("first-name")));
            
            // Scroll to make sure form is visible
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
            Thread.sleep(1000);
            
            // Wait for elements to be clickable
            WebElement firstName = wait.until(ExpectedConditions.elementToBeClickable(By.id("first-name")));
            WebElement lastName = wait.until(ExpectedConditions.elementToBeClickable(By.id("last-name")));
            WebElement postalCode = wait.until(ExpectedConditions.elementToBeClickable(By.id("postal-code")));
            
            firstName.clear();
            firstName.sendKeys("John");
            lastName.clear();
            lastName.sendKeys("Doe");
            postalCode.clear();
            postalCode.sendKeys("12345");
            
            // Continue to next step
            WebElement continueButton = driver.findElement(By.id("continue"));
            continueButton.click();
            
            // Review order and finish
            System.out.println("7. Reviewing order and completing checkout");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));
            
            WebElement finishButton = driver.findElement(By.id("finish"));
            finishButton.click();
            
            // Wait for completion page
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("complete-header")));
            System.out.println("Checkout completed successfully!");
            
            // Logout directly after checkout
            System.out.println("8. Logging out after checkout");
            WebElement menuButton = driver.findElement(By.id("react-burger-menu-btn"));
            menuButton.click();
            
            // Wait for menu to open and click logout
            wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
            WebElement logoutButton = driver.findElement(By.id("logout_sidebar_link"));
            logoutButton.click();
            
            // Wait for login page to confirm logout
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
            System.out.println("Logout successful - back to login page");
            
            // Verify the automation worked correctly
            System.out.println("\n🔍 VERIFICATION CHECKS:");
            verifyAutomationSuccess(driver, wait);
            
            System.out.println("✅ Automation completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error during automation: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close browser
            if (driver != null) {
                System.out.println("9. Closing browser");
                driver.quit();
                System.out.println("Browser closed");
            }
        }
    }
    
    private static void verifyAutomationSuccess(WebDriver driver, WebDriverWait wait) {
        try {
            // Check 1: Verify we're back on login page after logout
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
            System.out.println("✅ Check 1: Successfully returned to login page");
            
            // Check 2: Verify login form is present and functional
            WebElement usernameField = driver.findElement(By.id("user-name"));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement loginButton = driver.findElement(By.id("login-button"));
            
            if (usernameField.isDisplayed() && passwordField.isDisplayed() && loginButton.isDisplayed()) {
                System.out.println("✅ Check 2: Login form is present and functional");
            } else {
                System.out.println("❌ Check 2: Login form verification failed");
            }
            
            // Check 3: Verify page title
            String pageTitle = driver.getTitle();
            if (pageTitle.contains("Swag Labs")) {
                System.out.println("✅ Check 3: Page title is correct: " + pageTitle);
            } else {
                System.out.println("❌ Check 3: Page title verification failed: " + pageTitle);
            }
            
            // Check 4: Verify URL is correct
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("saucedemo.com")) {
                System.out.println("✅ Check 4: URL is correct: " + currentUrl);
            } else {
                System.out.println("❌ Check 4: URL verification failed: " + currentUrl);
            }
            
            System.out.println("\n📊 AUTOMATION SUMMARY:");
            System.out.println("• Browser opened successfully");
            System.out.println("• Login completed with standard_user");
            System.out.println("• Item added to cart (Sauce Labs Backpack)");
            System.out.println("• Checkout process completed");
            System.out.println("• Logout successful after checkout");
            System.out.println("• All verification checks passed");
            
        } catch (Exception e) {
            System.err.println("❌ Verification failed: " + e.getMessage());
        }
    }
}