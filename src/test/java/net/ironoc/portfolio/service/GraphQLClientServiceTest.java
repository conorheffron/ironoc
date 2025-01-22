package net.ironoc.portfolio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ironoc.portfolio.config.PropertyConfigI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(MockitoExtension.class)
public class GraphQLClientServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private PropertyConfigI propertyConfig;

    @InjectMocks
    private GraphQLClientService graphQLClientService;

    @Test
    public void test_getAllIcedCoffees_success() {
        // given
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("allIceds", List.of(Map.of("name", "Iced Coffee")));
        response.put("data", data);

        // when
        List<Map<String, Object>> result = graphQLClientService.getAllIcedCoffees(response);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(1));
        assertThat(result.getFirst().get("name"), is("Iced Coffee"));
    }

    @Test
    public void test_getAllHotCoffees_success() {
        // given
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("allHots", List.of(Map.of("name", "Hot Coffee")));
        response.put("data", data);

        // when
        List<Map<String, Object>> result = graphQLClientService.getAllHotCoffees(response);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(1));
        assertThat(result.getFirst().get("name"), is("Hot Coffee"));
    }
}
