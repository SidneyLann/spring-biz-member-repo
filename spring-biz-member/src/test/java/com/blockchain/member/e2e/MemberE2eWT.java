package com.blockchain.member.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

// JUnit 5 Test Class
public class MemberE2eWT {

    // Declare WebDriver instance
    WebDriver driver;

    // This method runs once before all tests
    @BeforeAll
    public static void setupClass() {
        // WebDriverManager automatically downloads and sets up the correct ChromeDriver
        WebDriverManager.chromedriver().setup();
    }

    // This method runs before each test
    @BeforeEach
    public void setupTest() {
        // Initialize the ChromeDriver instance
        driver = new ChromeDriver();
        
        // Maximize the browser window
        driver.manage().window().maximize();
        
        // Set an implicit wait (a default wait for elements to be found)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // This method runs after each test
    @AfterEach
    public void teardown() {
        // Check if the driver instance is not null before quitting
        if (driver != null) {
            // Close the browser and terminate the WebDriver session
            driver.quit();
        }
    }

    // This is our test case
    @Test
    public void testGoogleSearch() {
        // 1. Navigate to Google
        driver.get("https://www.google.com");

        // 2. Find the search box element by its HTML name attribute
        WebElement searchBox = driver.findElement(By.name("q"));

        // 3. Type "Selenium Java" into the search box and press ENTER
        searchBox.sendKeys("Selenium Java" + Keys.ENTER);

        // 4. Wait for the search results page to load.
        // We wait for the title to contain our search term.
        // This is an "Explicit Wait" - more precise than implicit wait.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains("Selenium Java"));

        // 5. Assert that the final page title contains the search term
        String pageTitle = driver.getTitle();
        assertTrue(pageTitle.contains("Selenium Java"), 
                   "Page title does not contain search term. Title was: " + pageTitle);

        // Alternatively, assert that the first result link contains "selenium.dev"
        WebElement firstResult = driver.findElement(By.cssSelector("div#search a"));
        String firstResultLink = firstResult.getAttribute("href");
        assertTrue(firstResultLink.contains("selenium.dev"), 
                   "First result link does not point to selenium.dev. Link was: " + firstResultLink);
    }
}