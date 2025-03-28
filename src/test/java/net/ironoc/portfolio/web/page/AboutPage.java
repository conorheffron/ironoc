package net.ironoc.portfolio.web.page;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class AboutPage {

    protected WebDriver driver;

    @FindBy(linkText = "iRonoc")
    private WebElement iRonoc;

    @FindBy(linkText = "Portfolio")
    private WebElement portfolio;

    public AboutPage(WebDriver driver) {
        this.driver = driver;
    }

    public PortfolioPage goToPortfolio() {
        iRonoc.click();
        portfolio.click();
        return PageFactory.initElements(driver, PortfolioPage.class);
    }
}
