package net.ironoc.portfolio;

import lombok.extern.slf4j.Slf4j;
import net.ironoc.portfolio.web.page.HomePage;
import net.ironoc.portfolio.web.page.AboutPage;
import net.ironoc.portfolio.web.page.PortfolioPage;
import net.ironoc.portfolio.web.page.DonatePage;
import net.ironoc.portfolio.web.page.BrewsPage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@SpringBootTest
@ActiveProfiles("selenium")
@Disabled("Disabled until Ghost Driver Setup for CI")
public class RemoteBrowserBasedIntTest {

    @Autowired
    private WebDriver webDriver;

    @Test
    @Disabled("Disabled until Ghost Driver Setup for CI")
    public void test_quick_tour() {
        try {
            // Navigate to iRonoc
//            driver.get("http://localhost:8080/");
            webDriver.get("https://ironoc.net/");

            HomePage homePage = PageFactory.initElements(webDriver, HomePage.class);
            getPageDetails(webDriver, 1000);
            assertThat(homePage, is(notNullValue()));

            DonatePage donatePage = homePage.goToDonate();
            getPageDetails(donatePage.getDriver(), 1000);
            assertThat(donatePage, is(notNullValue()));

            HomePage homePage1 = donatePage.goToHome();
            getPageDetails(homePage1.getDriver(), 1000);
            assertThat(homePage1, is(notNullValue()));

            AboutPage aboutPage = homePage.goToAbout();
            getPageDetails(aboutPage.getDriver(), 1000);
            assertThat(aboutPage, is(notNullValue()));

            PortfolioPage portfolioPage = aboutPage.goToPortfolio();
            getPageDetails(portfolioPage.getDriver(), 1000);
            assertThat(portfolioPage, is(notNullValue()));

            BrewsPage brewsPage = portfolioPage.goToBrews();
            assertThat(brewsPage, is(notNullValue()));
            getPageDetails(brewsPage.getDriver(), 7000);
        } catch (Exception e) {
            log.error("Unexpected error occurred.", e);
            fail("Failed to navigate quick tour of iRonoc");
        } finally {
            // Close the browser
            webDriver.quit();
        }
    }

    private void getPageDetails(WebDriver driver, int millis) throws InterruptedException {
        String title = driver.getTitle();
        assertThat(title, containsString("iRonoc React App"));
        assertThat(title, containsString("| Conor Heffron"));
        log.info("Page Title: {}", title);
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl, not(blankOrNullString()));
        log.info("Current URL: {}", currentUrl);
        Thread.sleep(millis);
    }
}
