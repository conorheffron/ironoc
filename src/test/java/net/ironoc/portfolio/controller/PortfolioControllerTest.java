package net.ironoc.portfolio.controller;

import module java.base;

import net.ironoc.portfolio.service.PortfolioItemsResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class PortfolioControllerTest {

    @Mock
    private PortfolioItemsResolver portfolioItemsResolver;

    @InjectMocks
    private PortfolioController portfolioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPortfolioItems() {
        // Arrange
        List<Map<String, Object>> mockPortfolioItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("link", "https://github.com/conorheffron/ironoc-db");
        item.put("description", "Sample Data Manager Service with UI");
        mockPortfolioItems.add(item);
        when(portfolioItemsResolver.getPortfolioItems()).thenReturn(mockPortfolioItems);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = portfolioController.getPortfolioItems();

        // Assert
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), hasSize(1));
        assertThat(response.getBody().getFirst(), hasEntry("link", "https://github.com/conorheffron/ironoc-db"));
        assertThat(response.getBody().getFirst(), hasEntry("description", "Sample Data Manager Service with UI"));
        verify(portfolioItemsResolver, times(1)).getPortfolioItems();
    }
}
