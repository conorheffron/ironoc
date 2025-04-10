package net.ironoc.portfolio.controller;

import net.ironoc.portfolio.graph.DonateItemsResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class DonateRestControllerTest {

    @Mock
    private DonateItemsResolver donateItemsResolver;

    @InjectMocks
    private DonateRestController donateRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDonateItems() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("name", "Charity A");
        item.put("founded", 2000);
        mockDonateItems.add(item);
        when(donateItemsResolver.getDonateItems()).thenReturn(mockDonateItems);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = donateRestController.getDonateItems();

        // Assert
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), hasSize(1));
        assertThat(response.getBody().get(0), hasEntry("name", "Charity A"));
        assertThat(response.getBody().get(0), hasEntry("founded", 2000));
        verify(donateItemsResolver, times(1)).getDonateItems();
    }
}
