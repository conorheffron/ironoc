package net.ironoc.portfolio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.ironoc.portfolio.domain.CoffeeDomain;
import net.ironoc.portfolio.graph.BrewsResolver;
import net.ironoc.portfolio.service.Coffees;
import net.ironoc.portfolio.service.CoffeesCache;
import net.ironoc.portfolio.service.GraphQLClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static net.ironoc.portfolio.utils.TestRequestResponseUtils.getSampleResponse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public class CoffeeControllerTest {

    @Mock
    private Coffees coffeesService;

    @Mock
    private CoffeesCache coffeesCache;

    @Mock
    private GraphQLClient graphQLClient;

    @Mock
    private BrewsResolver brewsResolver;

    @InjectMocks
    private CoffeeController coffeeController;

    @Test
    public void test_getCoffeeDetails_withCache() {
        // given
        CoffeeDomain mockCoffeeDomain1 = CoffeeDomain.builder()
                .title("Latte")
                .ingredients(Arrays.asList("Espresso", "Steamed milk"))
                .image("https://example.com/latte.jpg")
                .build();
        CoffeeDomain mockCoffeeDomain2 = CoffeeDomain.builder()
                .title("Mocha")
                .ingredients(Arrays.asList("Espresso", "Steamed milk", "Chocolate"))
                .image("https://example.com/mocha.jpg")
                .build();
        List<CoffeeDomain> cachedCoffeeDomains = List.of(mockCoffeeDomain1, mockCoffeeDomain2);

        when(coffeesCache.get()).thenReturn(cachedCoffeeDomains);

        // when
        ResponseEntity<List<CoffeeDomain>> response = coffeeController.getCoffeeDetails();

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(cachedCoffeeDomains));
    }

    @Test
    public void test_getCoffeeDetails_withoutCache() {
        // given
        CoffeeDomain mockCoffeeDomain1 = CoffeeDomain.builder()
                .title("Americano")
                .ingredients(Arrays.asList("Espresso", "Water"))
                .image("https://example.com/americano.jpg")
                .build();
        CoffeeDomain mockCoffeeDomain2 = CoffeeDomain.builder()
                .title("Macchiato")
                .ingredients(Arrays.asList("Espresso", "Foamed milk"))
                .image("https://example.com/macchiato.jpg")
                .build();
        List<CoffeeDomain> coffeeDomains = List.of(mockCoffeeDomain1, mockCoffeeDomain2);

        when(coffeesCache.get()).thenReturn(Collections.emptyList());
        when(coffeesService.getCoffeeDetails()).thenReturn(coffeeDomains);

        // when
        ResponseEntity<List<CoffeeDomain>> response = coffeeController.getCoffeeDetails();

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(coffeeDomains));
    }

    @Test
    public void test_getCoffeeDetailsGraphQl_withCache() {
        // given
        CoffeeDomain mockCoffeeDomain1 = CoffeeDomain.builder()
                .title("Latte")
                .ingredients(Arrays.asList("Espresso", "Steamed milk"))
                .image("https://example.com/latte.jpg")
                .build();
        CoffeeDomain mockCoffeeDomain2 = CoffeeDomain.builder()
                .title("Mocha")
                .ingredients(Arrays.asList("Espresso", "Steamed milk", "Chocolate"))
                .image("https://example.com/mocha.jpg")
                .build();
        List<CoffeeDomain> cachedCoffeeDomains = List.of(mockCoffeeDomain1, mockCoffeeDomain2);

        when(coffeesCache.get()).thenReturn(cachedCoffeeDomains);

        // when
        ResponseEntity<List<CoffeeDomain>> response = coffeeController.getCoffeeDetailsGraphQl();

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(cachedCoffeeDomains));
    }

    @Test
    public void test_getCoffeeDetailsGraphQl_withoutCache() throws JsonProcessingException {
        // given
        when(coffeesCache.get()).thenReturn(Collections.emptyList());
        when(graphQLClient.fetchCoffeeDetails()).thenReturn(getSampleResponse(true));
        when(graphQLClient.getAllHotCoffees(any())).thenReturn(
                (List<Map<String, Object>>) getSampleResponse(false).get("allHots"));
        when(graphQLClient.getAllIcedCoffees(any())).thenReturn(
                (List<Map<String, Object>>) getSampleResponse(false).get("allIceds"));

        // when
        ResponseEntity<List<CoffeeDomain>> response = coffeeController.getCoffeeDetailsGraphQl();

        // then
        verify(coffeesCache).get();
        verify(coffeesService, never()).getCoffeeDetails();
        verify(graphQLClient).fetchCoffeeDetails();
        verify(graphQLClient).getAllIcedCoffees(anyMap());
        verify(graphQLClient).getAllHotCoffees(anyMap());
        verify(coffeesCache).put(anyList());

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), iterableWithSize(2));
    }

    @Test
    void putCoffeesIntoMemoryStorage_returnsCachedResults_whenCacheIsPresent() {
        List<CoffeeDomain> cachedList = new ArrayList<>();
        cachedList.add(new CoffeeDomain());
        when(coffeesCache.get()).thenReturn(cachedList);

        ResponseEntity<List<CoffeeDomain>> response = coffeeController.putCoffeesIntoMemoryStorage();

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(cachedList));
        verify(coffeesCache, never()).tearDown();
        verify(brewsResolver, never()).getBrews();
        verify(coffeesCache, never()).put(anyList());
    }

    @Test
    void putCoffeesIntoMemoryStorage_populatesCache_whenCacheIsEmpty() {
        when(coffeesCache.get()).thenReturn(null);
        List<Map<String, Object>> brewMaps = new ArrayList<>();
        Map<String, Object> brew = new HashMap<>();
        brew.put("name", "Test Brew");
        brewMaps.add(brew);
        when(brewsResolver.getBrews()).thenReturn(brewMaps);

        // Simulate mapping logic (assume mapBrewsToCoffeesList just wraps maps into CoffeeDomain)
        CoffeeDomain mappedCoffee = new CoffeeDomain();
        List<CoffeeDomain> mappedList = Collections.singletonList(mappedCoffee);

        // Stub the mapping if it's a private method, otherwise you may need to refactor for testability
        CoffeeController controllerSpy = spy(coffeeController);
        doReturn(mappedList).when(controllerSpy).mapBrewsToCoffeesList(brewMaps);

        ResponseEntity<List<CoffeeDomain>> response = controllerSpy.putCoffeesIntoMemoryStorage();

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(mappedList));
        verify(coffeesCache).tearDown();
        verify(brewsResolver).getBrews();
        verify(coffeesCache).put(mappedList);
    }

    @Test
    void putCoffeesIntoMemoryStorage_populatesCache_whenCacheIsEmptyList() {
        when(coffeesCache.get()).thenReturn(Collections.emptyList());
        List<Map<String, Object>> brewMaps = new ArrayList<>();
        when(brewsResolver.getBrews()).thenReturn(brewMaps);

        CoffeeDomain mappedCoffee = new CoffeeDomain();
        List<CoffeeDomain> mappedList = Collections.singletonList(mappedCoffee);

        CoffeeController controllerSpy = spy(coffeeController);
        doReturn(mappedList).when(controllerSpy).mapBrewsToCoffeesList(brewMaps);

        ResponseEntity<List<CoffeeDomain>> response = controllerSpy.putCoffeesIntoMemoryStorage();

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(mappedList));
        verify(coffeesCache).tearDown();
        verify(brewsResolver).getBrews();
        verify(coffeesCache).put(mappedList);
    }
}