package net.ironoc.portfolio;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import net.ironoc.portfolio.controller.BaseControllerIntegrationTest;
import net.ironoc.portfolio.controller.VersionController;
import net.ironoc.portfolio.web.page.HomePage;
import net.ironoc.portfolio.web.page.AboutPage;
import net.ironoc.portfolio.web.page.PortfolioPage;
import net.ironoc.portfolio.web.page.DonatePage;
import net.ironoc.portfolio.web.page.BrewsPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
public class RemoteBrowserBasedIntTest extends BaseControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @MockitoBean
    private VersionController versionControllerMock;

    @MockitoBean
    private OpenAPI ironocOpenAPI;

    @Autowired
    private WebDriver webDriver;

    @Test
    public void test_quick_tour() {
        try {
            Dimension dimension = new Dimension(878, 963);// mimic small device size
            webDriver.manage().window().setSize(dimension);
            // Navigate to iRonoc
//            webDriver.get("http://localhost:8080/");
            webDriver.get("https://ironoc.net/");

            HomePage homePage = PageFactory.initElements(webDriver, HomePage.class);
            getPageDetails(webDriver, 2);
            assertThat(homePage, is(notNullValue()));

            DonatePage donatePage = homePage.goToDonate();
            getPageDetails(donatePage.getDriver(), 1);
            assertThat(donatePage, is(notNullValue()));

            HomePage homePage1 = donatePage.goToHome();
            getPageDetails(homePage1.getDriver(), 1);
            assertThat(homePage1, is(notNullValue()));

            AboutPage aboutPage = homePage.goToAbout();
            getPageDetails(aboutPage.getDriver(), 1);
            assertThat(aboutPage, is(notNullValue()));

            PortfolioPage portfolioPage = aboutPage.goToPortfolio();
            getPageDetails(portfolioPage.getDriver(), 1);
            assertThat(portfolioPage, is(notNullValue()));

            BrewsPage brewsPage = portfolioPage.goToBrews();
            assertThat(brewsPage, is(notNullValue()));
            getPageDetails(brewsPage.getDriver(), 2);
        } catch (Exception e) {
            log.error("Unexpected exception occurred during test quick_tour", e);
            fail("Failed to navigate quick tour of iRonoc via page object calls.");
        } finally {
            // Close the browser
            webDriver.quit();
        }
    }

    private void getPageDetails(WebDriver driver, int waitInSeconds) {
        String title = driver.getTitle();
        assertThat(title, containsString("iRonoc React App"));
        assertThat(title, containsString("| Conor Heffron"));
        log.info("Page Title: {}", title);
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl, not(blankOrNullString()));
        log.info("Current URL: {}", currentUrl);

        synchronized (driver) {
            try {
                driver.wait(Duration.ofSeconds(waitInSeconds).toMillis());
            } catch (InterruptedException e) {
                log.error("Unexpected exception occurred during test quick_tour", e);
                fail("Failed to navigate quick tour of iRonoc verifying page details.");
            }
        }
    }
}
