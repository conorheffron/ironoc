package net.ironoc.portfolio.controller;

import net.ironoc.portfolio.dto.CoffeeDto;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    private CoffeesService coffeesService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void test_getCoffeeDetails_success() throws Exception {
        // given
        CoffeeDto mockCoffeeDto1 = CoffeeDto.builder()
                .title("Espresso")
                .ingredients(Arrays.asList("Water", "Coffee beans"))
                .image("https://example.com/espresso.jpg")
                .build();
        CoffeeDto mockCoffeeDto2 = CoffeeDto.builder()
                .title("Cappuccino")
                .ingredients(Arrays.asList("Espresso", "Steamed milk", "Foam milk"))
                .image("https://example.com/cappuccino.jpg")
                .build();

        when(coffeesService.getCoffeeDetails()).thenReturn(List.of(mockCoffeeDto1, mockCoffeeDto2));

        // when, then
        mockMvc.perform(get("/coffees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'title':'Espresso','ingredients':['Water','Coffee beans']," +
                        "'image':'https://example.com/espresso.jpg'},{'title':'Cappuccino'," +
                        "'ingredients':['Espresso','Steamed milk','Foam milk']," +
                        "'image':'https://example.com/cappuccino.jpg'}]"));

        // then
        verify(coffeesService).getCoffeeDetails();
    }
}
