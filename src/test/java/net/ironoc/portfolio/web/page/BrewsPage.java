package net.ironoc.portfolio.web.page;

import lombok.Getter;
import org.openqa.selenium.WebDriver;

@Getter
public class BrewsPage {
    protected WebDriver driver;

    public BrewsPage(WebDriver driver) {
        this.driver = driver;
    }
}
