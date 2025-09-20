package com.blockchain.member.e2e;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MemberE2eWT {

	// Declare WebDriver instance
	WebDriver driver;

	// This method runs once before all tests
	@BeforeAll
	public static void setupClass() {
		System.setProperty("webdriver.chrome.driver", "D:\\DEV\\chromedriver\\chromedriver.exe");
		
		// WebDriverManager automatically downloads and sets up the correct ChromeDriver
		// WebDriverManager.chromedriver().setup();
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

	@Test
	public void testLogin() {
		// Navigate to website
		driver.get("http://ec2-13-229-223-170.ap-southeast-1.compute.amazonaws.com:8443");

		// Find the login element by its HTML name attribute
		WebElement loginButton = driver.findElement(By.xpath("//button[text()='Please Login']"));
		loginButton.click();

		// Wait for the Login page to load.
		// We wait for the Login button.
		// This is an "Explicit Wait" - more precise than implicit wait.
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Login']")));

		// Find the element by its ID
		WebElement loginNameField = driver.findElement(By.id("loginName"));
		// Clear any existing text (optional)
		loginNameField.clear();
		// Input text into the field
		loginNameField.sendKeys("mem123");

		// Find the element by its ID
		WebElement passwordField = driver.findElement(By.id("password"));
		// Clear any existing text (optional)
		passwordField.clear();
		// Input text into the field
		passwordField.sendKeys("P@ssw0rd");

		loginButton.click();

		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement	exitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Exit']")));
		
		// Assert that the exitButton is not null and is displayed
		assertNotNull(exitButton, "Exit button should be found");
		assertTrue(exitButton.isDisplayed(), "Exit button should be displayed");
	}
}