package net.ironoc.portfolio.graph;

import net.ironoc.portfolio.service.PortfolioItemsResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class PortfolioItemsResolverTest {

    private PortfolioItemsResolver portfolioItemsResolver;

    public static List<Map<String, String>> portfolioItems = List.of(
            Map.of(
                    "link", "https://github.com/conorheffron/ironoc-db",
                    "img", "navy",
                    "alt", "navy1",
                    "title", "ironoc-db",
                    "description", "Sample Data Manager Service with UI",
                    "techStack", "Java & Spring Boot, Thymeleaf Templating Engine, & MySQL."
            ),
            Map.of(
                    "link", "https://github.com/conorheffron/booking-sys",
                    "img", "teal",
                    "alt", "teal2",
                    "title", "booking-sys",
                    "description", "Sample Reservations & Viewer System",
                    "techStack", "Python & Django Web App, JavaScript, SQLite3 or MySQL database."
            ),
            Map.of(
                    "link", "https://github.com/conorheffron/nba-stats",
                    "img", "navy",
                    "alt", "navy3",
                    "title", "nba-stats",
                    "description", "NBA Analytics (Seasons 2015 - 2024): Player Statistics",
                    "techStack", "Jupyter Notebooks, Python, Pandas, PandasAI, OpenAI GPT-3.5 Turbo, LLM, & Requests / JSON API."
            ),
            Map.of(
                    "link", "https://github.com/conorheffron/cbio-skin-canc",
                    "img", "red",
                    "alt", "red4",
                    "title", "cbio-skin-canc",
                    "description", "Skin Cancer Dataset Analysis",
                    "techStack", "R, dplyr, plotly, knitr, testthat, covr, GIT."
            ),
            Map.of(
                    "link", "https://github.com/conorheffron/gene-expr",
                    "img", "navy",
                    "alt", "navy5",
                    "title", "gene-expr",
                    "description", "Breast Cancer Dataset Analysis",
                    "techStack", "R, ggplot2, dplyr, deseq2-analysis, & R markdown."
            ),
            Map.of(
                    "link", "https://github.com/conorheffron/bio-cell-red-edge",
                    "img", "red",
                    "alt", "red6",
                    "title", "bio-cell-red-edge",
                    "description", "Edge Detection of Biological Cell (Image Processing Script)",
                    "techStack", "Python, sci-kit-image, matplotlib.pyplot, & scipy.ndimage."
            ),
            Map.of(
                    "link", "https://github.com/conorheffron/global-max-sim-matrix",
                    "img", "teal",
                    "alt", "teal7",
                    "title", "global-max-sim-matrix",
                    "description", "Compute Global Maximum Similarity Matrix",
                    "techStack", "R Package, testthat, stringr, & devtools."
            )
    );

    @BeforeEach
    void setUp() {
        portfolioItemsResolver = new PortfolioItemsResolver();
    }

    @Test
    void testGetPortfolioItems_ValidJson() throws IOException {
        // Execute the method
        List<Map<String, Object>> actualItems = portfolioItemsResolver.getPortfolioItems();

        // Assert the results
        assertThat(portfolioItems, is(actualItems));
    }
}
