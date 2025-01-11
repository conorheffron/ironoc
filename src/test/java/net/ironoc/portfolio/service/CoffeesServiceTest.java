package net.ironoc.portfolio.service;

import net.ironoc.portfolio.dto.CoffeeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CoffeesServiceTest {

    private CoffeesService coffeesService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @BeforeEach
    public void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        coffeesService = new CoffeesService(restTemplateBuilder);
    }

    @Test
    public void test_getCoffeeDetails_success() {
        // given
        CoffeeDto brew1 = CoffeeDto.builder()
                .title("Espresso")
                .ingredients(Arrays.asList("Water", "Coffee beans"))
                .image("https://example.com/espresso.jpg")
                .build();
        CoffeeDto brew2 = CoffeeDto.builder()
                .title("Cappuccino")
                .ingredients(Arrays.asList("Espresso", "Steamed milk", "Foam milk", "Dark Choc Shavings"))
                .image("https://example.com/cappuccino.jpg")
                .build();
        CoffeeDto[] mockResponse = {brew1, brew2};

        when(restTemplate.getForEntity("https://api.sampleapis.com/coffee/hot", CoffeeDto[].class))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        when(restTemplate.getForEntity("https://api.sampleapis.com/coffee/iced", CoffeeDto[].class))
                .thenReturn(new ResponseEntity<>(new CoffeeDto[]{}, HttpStatus.OK));

        // when
        List<CoffeeDto> coffeeDtoList = coffeesService.getCoffeeDetails();

        // then
        verify(restTemplate, times(2)).getForEntity(anyString(), any(Class.class));

        assertEquals(2, coffeeDtoList.size());
        assertEquals("Espresso", coffeeDtoList.get(0).getTitle());
        assertEquals("Cappuccino", coffeeDtoList.get(1).getTitle());
    }
}
