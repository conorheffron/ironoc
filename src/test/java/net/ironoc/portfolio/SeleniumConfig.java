package net.ironoc.portfolio;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SeleniumConfig {

    @Bean
    public WebDriver webDriver() {
        // Use Chrome Pop-Up to run locally
//        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
//        WebDriver webDriver = new ChromeDriver();
//        return webDriver;

        // Run selenium web driver as background process with headless chrome
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless=new"); // Use new headless mode
//        options.addArguments("--disable-gpu"); // Recommended for headless execution
//        options.addArguments("--window-size=1920,1080"); // Set specific window size
//        WebDriver webDriver = new ChromeDriver(options);
//        return webDriver;

        // Run selenium web driver as background process with headless FireFox
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless"); // Enable headless mode
        options.addArguments("--disable-gpu"); // Recommended for headless execution
        options.addArguments("--window-size=1920,1080"); // Set specific window size
        WebDriver webDriver = new FirefoxDriver(options);
        return webDriver;
    }
}
