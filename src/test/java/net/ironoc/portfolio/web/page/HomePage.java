package net.ironoc.portfolio.web.page;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class HomePage {

    protected WebDriver driver;

    @FindBy(linkText = "Charity Options")
    private WebElement charityOptions;

    @FindBy(linkText = "Donate")
    private WebElement donate;

    @FindBy(linkText = "iRonoc")
    private WebElement iRonoc;

    @FindBy(linkText = "About")
    private WebElement about;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public DonatePage goToDonate() {
        charityOptions.click();
        donate.click();
        return PageFactory.initElements(driver, DonatePage.class);
    }

    public AboutPage goToAbout() {
        iRonoc.click();
        about.click();
        return PageFactory.initElements(driver, AboutPage.class);
    }
}
