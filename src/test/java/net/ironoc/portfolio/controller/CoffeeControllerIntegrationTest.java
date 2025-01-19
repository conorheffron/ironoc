package net.ironoc.portfolio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.ironoc.portfolio.domain.CoffeeDomain;
import net.ironoc.portfolio.service.GraphQLClientService;
import net.ironoc.portfolio.service.CoffeesCache;
import net.ironoc.portfolio.service.CoffeesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
public class CoffeeControllerIntegrationTest extends ControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @InjectMocks
    private CoffeeController coffeeController;

    @MockitoBean
    private CoffeesService coffeesServiceMock;

    @MockitoBean
    private CoffeesCache coffeesCacheMock;

    @MockitoBean
    private GraphQLClientService graphQLClientServiceMock;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void test_getCoffeeDetails_success() throws Exception {
        // given
        CoffeeDomain mockCoffeeDomain1 = CoffeeDomain.builder()
                .title("Espresso")
                .ingredients(Arrays.asList("Water", "Coffee beans"))
                .image("https://example.com/espresso.jpg")
                .build();
        CoffeeDomain mockCoffeeDomain2 = CoffeeDomain.builder()
                .title("Cappuccino")
                .ingredients(Arrays.asList("Espresso", "Steamed milk", "Foam milk"))
                .image("https://example.com/cappuccino.jpg")
                .build();

        when(coffeesServiceMock.getCoffeeDetails()).thenReturn(List.of(mockCoffeeDomain1, mockCoffeeDomain2));

        // when, then
        mockMvc.perform(get("/coffees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'title':'Espresso','ingredients':['Water','Coffee beans']," +
                        "'image':'https://example.com/espresso.jpg'},{'title':'Cappuccino'," +
                        "'ingredients':['Espresso','Steamed milk','Foam milk']," +
                        "'image':'https://example.com/cappuccino.jpg'}]"));

        // then when(coffeesCacheMock.get()).thenReturn(List.of(mockResponse));
        verify(coffeesServiceMock).getCoffeeDetails();
    }

    @Test
    public void test_getCoffeeDetails_cacheResult_success() throws Exception {
        // given
        CoffeeDomain mockCoffeeDomain1 = CoffeeDomain.builder()
                .title("Cafecito")
                .ingredients(Arrays.asList("Frotted Milk", "Cuban Coffee beans"))
                .image("https://ironoc.net/cubano.jpg")
                .build();
        CoffeeDomain mockCoffeeDomain2 = CoffeeDomain.builder()
                .title("Yerba Mate")
                .ingredients(Arrays.asList("Uruguayan Leaves", "Argentinean Gourd & Bombilla"))
                .image("https://ironoc.net/yerba.jpg")
                .build();

        when(coffeesCacheMock.get()).thenReturn(List.of(mockCoffeeDomain1, mockCoffeeDomain2));

        // when, then
        mockMvc.perform(get("/coffees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'title':'Cafecito','ingredients':['Frotted Milk'," +
                        "'Cuban Coffee beans']," +
                        "'image':'https://ironoc.net/cubano.jpg'},{'title':'Yerba Mate'," +
                        "'ingredients':['Uruguayan Leaves','Argentinean Gourd & Bombilla']," +
                        "'image':'https://ironoc.net/yerba.jpg'}]"));

        // then
        verify(coffeesCacheMock).get();
        verify(coffeesServiceMock, never()).getCoffeeDetails();
    }

    @Test
    public void testGetCoffeeDetailsGraphQl_CachedResults() throws Exception {
        // Given
        List<CoffeeDomain> cachedResults = getSampleCoffeeDomainList();
        when(coffeesCacheMock.get()).thenReturn(cachedResults);

        // When
        // When & Then
        mockMvc.perform(get("/coffees-graph-ql")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'title':'Iced Coffee','ingredients':['Coffee'," +
                        "'Ice']," +
                        "'image':'image_url_1'},{'title':'Latte'," +
                        "'ingredients':['Espresso', 'Milk']," +
                        "'image':'image_url_2'}]"));

        // Then
        verify(coffeesCacheMock, times(1)).get();
        verify(graphQLClientServiceMock, never()).fetchCoffeeDetails();
    }

    @Test
    public void testGetCoffeeDetailsGraphQl_NoCachedResults() throws Exception {
        // Given
        when(coffeesCacheMock.get()).thenReturn(null);
        Map<String, Object> response = getSampleResponse();
        when(graphQLClientServiceMock.fetchCoffeeDetails()).thenReturn(response);
        when(graphQLClientServiceMock.getAllHotCoffees(response)).thenReturn((List<Map<String, Object>>) response.get("allHots"));
        when(graphQLClientServiceMock.getAllIcedCoffees(response)).thenReturn((List<Map<String, Object>>) response.get("allIceds"));

        // When & Then
        mockMvc.perform(get("/coffees-graph-ql")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'title':'Black Coffee','ingredients':['Coffee']," +
                        "'image':'image_url_1'},{'title':'Latte'," +
                        "'ingredients':['Espresso', 'Milk']," +
                        "'image':'image_url_2'}]"));

        verify(graphQLClientServiceMock, times(1)).fetchCoffeeDetails();
        verify(graphQLClientServiceMock, times(1)).getAllHotCoffees(response);
        verify(graphQLClientServiceMock, times(1)).getAllIcedCoffees(response);
        verify(coffeesCacheMock, times(1)).put(anyList());
    }

    @Test
    public void testGetCoffeeDetailsGraphQl_JsonProcessingException() throws Exception {
        // Given
        when(coffeesCacheMock.get()).thenReturn(null);
        when(graphQLClientServiceMock.fetchCoffeeDetails()).thenThrow(JsonProcessingException.class);

        // When & Then
        mockMvc.perform(get("/coffees-graph-ql")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(graphQLClientServiceMock, times(1)).fetchCoffeeDetails();
        verify(coffeesCacheMock).get();
    }

    private List<CoffeeDomain> getSampleCoffeeDomainList() {
        List<CoffeeDomain> coffeeDomains = new ArrayList<>();
        coffeeDomains.add(CoffeeDomain.builder()
                .id(1)
                .ingredients(List.of("Coffee", "Ice"))
                .image("image_url_1")
                .title("Iced Coffee").build());
        coffeeDomains.add(CoffeeDomain.builder()
                .id(2)
                .ingredients(List.of("Espresso", "Milk"))
                .image("image_url_2")
                .title("Latte").build());
        return coffeeDomains;
    }

    private Map<String, Object> getSampleResponse() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> allHots = new ArrayList<>();
        List<Map<String, Object>> allIceds = new ArrayList<>();

        Map<String, Object> hotCoffee = new HashMap<>();
        hotCoffee.put("id", "1");
        hotCoffee.put("ingredients", List.of("Coffee"));
        hotCoffee.put("image", "image_url_1");
        hotCoffee.put("title", "Black Coffee");
        allHots.add(hotCoffee);

        Map<String, Object> icedCoffee = new HashMap<>();
        icedCoffee.put("id", "2");
        icedCoffee.put("ingredients", List.of("Espresso", "Milk"));
        icedCoffee.put("image", "image_url_2");
        icedCoffee.put("title", "Latte");
        allIceds.add(icedCoffee);

        Map<String, Object> icedCoffee2 = new HashMap<>();
        icedCoffee2.put("id", "3");
        icedCoffee2.put("ingredients", "invalid_array");
        icedCoffee2.put("image", "image_url_3");
        icedCoffee2.put("title", "Ice Black");
        allIceds.add(icedCoffee2);

        response.put("allHots", allHots);
        response.put("allIceds", allIceds);
        return response;
    }
}
