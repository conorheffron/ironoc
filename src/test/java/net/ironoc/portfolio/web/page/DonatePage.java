package net.ironoc.portfolio.web.page;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class DonatePage {

    protected WebDriver driver;

    @FindBy(css = ".App .navbar-toggler-icon")
    private WebElement hamburgerIcon;

    @FindBy(xpath = "//div[2]/div/div/nav/div/div/div/a/img")
    private WebElement home;

    public DonatePage(WebDriver driver) {
        this.driver = driver;
    }

    public HomePage goToHome() {
        hamburgerIcon.click();
        home.click();
        return PageFactory.initElements(driver, HomePage.class);
    }
}
