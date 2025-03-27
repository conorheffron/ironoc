package net.ironoc.portfolio;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class RemoteBrowserBasedIntTest {

    public static void main(String[] args) {
        // Set the system property for ChromeDriver (path to chromedriver executable)
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        // Create an instance of ChromeDriver (launch the Chrome browser)
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to iRonoc
            driver.get("https://ironoc.net/");

            // Get and print the page title
            getPageDetails(driver, 1000);

            By charityOptions = By.ByLinkText.linkText("Charity Options");
            driver.findElement(charityOptions).click();

            By donate = By.ByLinkText.linkText("Donate");
            driver.findElement(donate).click();

            getPageDetails(driver, 1000);

            By home = By.ByLinkText.linkText("Home");
            driver.findElement(home).click();

            getPageDetails(driver, 1000);

            By iRonoc = By.ByLinkText.linkText("iRonoc");
            driver.findElement(iRonoc).click();

            By about = By.ByLinkText.linkText("About");
            driver.findElement(about).click();

            getPageDetails(driver, 1000);

            driver.findElement(iRonoc).click();
            By portfolio = By.ByLinkText.linkText("Portfolio");
            driver.findElement(portfolio).click();

            getPageDetails(driver, 1000);

            driver.findElement(iRonoc).click();
            By brews = By.ByLinkText.linkText("Brews");
            driver.findElement(brews).click();

            getPageDetails(driver, 7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private static void getPageDetails(WebDriver driver, int millis) throws InterruptedException {
        System.out.println("Page Title: " + driver.getTitle());
        System.out.println("Page Title: " + driver.getCurrentUrl());
        Thread.sleep(millis);
    }
}
