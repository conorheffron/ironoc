package net.ironoc.portfolio;

import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

        // Run selenium web driver as background process for CI only
        ChromeDriverManager.getInstance().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        return new ChromeDriver(chromeOptions);
    }
}
