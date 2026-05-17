package net.ironoc.portfolio.web.page;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class PortfolioPage {
    protected WebDriver driver;

    @FindBy(linkText = "iRonoc")
    private WebElement iRonoc;

    @FindBy(linkText = "Brews")
    private WebElement brews;

    @FindBy(css = ".content-inner .navbar-toggler-icon")
    private WebElement hamburgerIcon;

    public PortfolioPage(WebDriver driver) {
        this.driver = driver;
    }

    public BrewsPage goToBrews() {
        hamburgerIcon.click();
        iRonoc.click();
        brews.click();
        return PageFactory.initElements(driver, BrewsPage.class);
    }
}
