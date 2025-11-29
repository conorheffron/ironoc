package net.ironoc.portfolio.service;

import module java.base;

import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.domain.CoffeeDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CoffeesServiceTest {

    private CoffeesService coffeesService;

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private RestTemplateBuilder restTemplateBuilderMock;

    @Mock
    private PropertyConfigI propertyConfigMock;

    @Mock
    private CoffeesCache coffeesCacheMock;

    private static final String TEST_ICE_BREW_ENDPOINT = "www.brewskis.test/iced";
    private static final String TEST_HOT_BREW_ENDPOINT = "www.brewskis.test/hot";

    @BeforeEach
    public void setUp() {
        when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);

        coffeesService = new CoffeesService(restTemplateBuilderMock, propertyConfigMock, coffeesCacheMock);
    }

    @Test
    void testPopulateBrewsCache_enabled() {
        when(propertyConfigMock.isBrewsCacheJobEnabled()).thenReturn(true);
        CoffeesService spyService = spy(coffeesService);

        doReturn(Collections.emptyList()).when(spyService).getCoffeeDetails();

        spyService.populateBrewsCache();

        verify(spyService, times(1)).getCoffeeDetails();
    }

    @Test
    void testPopulateBrewsCache_disabled() {
        when(propertyConfigMock.isBrewsCacheJobEnabled()).thenReturn(false);
        CoffeesService spyService = spy(coffeesService);

        spyService.populateBrewsCache();

        verify(spyService, never()).getCoffeeDetails();
        // no exception means we pass (warn logging tested via logs)
    }

    @Test
    void testTriggerBrewsCacheJob_enabled() {
        when(propertyConfigMock.isBrewsCacheJobEnabled()).thenReturn(true);
        CoffeesService spyService = spy(coffeesService);

        doReturn(Collections.emptyList()).when(spyService).getCoffeeDetails();

        spyService.triggerBrewsCacheJob();

        verify(spyService, times(1)).getCoffeeDetails();
    }

    @Test
    void testTriggerBrewsCacheJob_disabled() {
        when(propertyConfigMock.isBrewsCacheJobEnabled()).thenReturn(false);
        CoffeesService spyService = spy(coffeesService);

        spyService.triggerBrewsCacheJob();

        verify(spyService, never()).getCoffeeDetails();
        // no exception means we pass (warn logging tested via logs)
    }

    @Test
    public void test_getCoffeeDetails_success() {
        // given
        CoffeeDomain brew1 = CoffeeDomain.builder()
                .title("Espresso")
                .ingredients(Arrays.asList("Water", "Coffee beans"))
                .image("https://example.com/espresso.jpg")
                .build();
        CoffeeDomain brew2 = CoffeeDomain.builder()
                .title("Cappuccino")
                .ingredients(Arrays.asList("Espresso", "Steamed milk", "Foam milk", "Dark Choc Shavings"))
                .image("https://example.com/cappuccino.jpg")
                .build();
        List<CoffeeDomain> mockResponse = List.of(brew1, brew2);

        when(propertyConfigMock.getBrewApiEndpointIce()).thenReturn(TEST_ICE_BREW_ENDPOINT);
        when(propertyConfigMock.getBrewApiEndpointHot()).thenReturn(TEST_HOT_BREW_ENDPOINT);
        when(restTemplateMock.getForEntity(TEST_HOT_BREW_ENDPOINT, Object.class))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        when(restTemplateMock.getForEntity(TEST_ICE_BREW_ENDPOINT, Object.class))
                .thenReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK));

        // when
        List<CoffeeDomain> coffeeDomainList = coffeesService.getCoffeeDetails();

        // then
        verify(coffeesCacheMock).put(mockResponse);
        verify(propertyConfigMock).getBrewApiEndpointIce();
        verify(propertyConfigMock).getBrewApiEndpointHot();
        verify(restTemplateMock, times(2)).getForEntity(anyString(), any(Class.class));

        assertEquals(2, coffeeDomainList.size());
        assertEquals("Espresso", coffeeDomainList.get(0).getTitle());
        assertEquals("Cappuccino", coffeeDomainList.get(1).getTitle());
    }

    @Test
    void testGetBody_successWithList() {
        String endpoint = "coffee-endpoint";
        List<CoffeeDomain> body = Arrays.asList(new CoffeeDomain(), new CoffeeDomain());
        ResponseEntity<Object> response = new ResponseEntity<>(body, HttpStatus.OK);

        when(restTemplateMock.getForEntity(endpoint, Object.class)).thenReturn(response);

        // Use reflection to call private method
        List<CoffeeDomain> result = invokeGetBody(coffeesService, endpoint);

        assertEquals(body, result);
    }

    @Test
    void testGetBody_successWithNonListBody() {
        String endpoint = "coffee-endpoint";
        Object nonListBody = new Object();
        ResponseEntity<Object> response = new ResponseEntity<>(nonListBody, HttpStatus.OK);

        when(restTemplateMock.getForEntity(endpoint, Object.class)).thenReturn(response);

        List<CoffeeDomain> result = invokeGetBody(coffeesService, endpoint);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetBody_non2xxStatus() {
        String endpoint = "coffee-endpoint";
        List<CoffeeDomain> body = List.of(new CoffeeDomain());
        ResponseEntity<Object> response = new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplateMock.getForEntity(endpoint, Object.class)).thenReturn(response);

        List<CoffeeDomain> result = invokeGetBody(coffeesService, endpoint);

        assertTrue(result.isEmpty());
    }

    // Helper to invoke private method getBody via reflection
    @SuppressWarnings("unchecked")
    private List<CoffeeDomain> invokeGetBody(CoffeesService service, String endpoint) {
        try {
            java.lang.reflect.Method m = CoffeesService.class.getDeclaredMethod("getBody", String.class);
            m.setAccessible(true);
            return (List<CoffeeDomain>) m.invoke(service, endpoint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
