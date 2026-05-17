package net.ironoc.portfolio.web.page;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class HomePage {

    protected WebDriver driver;

    @FindBy(css = ".App .dropdown:nth-child(5) > .dropdown-toggle")
    private WebElement charityOptions;

    @FindBy(linkText = "Donate")
    private WebElement donate;

    @FindBy(linkText = "iRonoc")
    private WebElement iRonoc;

    @FindBy(linkText = "About")
    private WebElement about;

    @FindBy(css = ".App .navbar-toggler-icon")
    private WebElement hamburgerIcon;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public DonatePage goToDonate() {
        hamburgerIcon.click();
        charityOptions.click();
        donate.click();
        return PageFactory.initElements(driver, DonatePage.class);
    }

    public AboutPage goToAbout() {
        hamburgerIcon.click();
        iRonoc.click();
        about.click();
        return PageFactory.initElements(driver, AboutPage.class);
    }
}
