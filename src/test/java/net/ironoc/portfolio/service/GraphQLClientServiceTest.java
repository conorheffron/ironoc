package net.ironoc.portfolio.service;

import module java.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.exception.IronocJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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

    private GraphQLClientService graphQLClientService;

    @BeforeEach
    public void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        graphQLClientService = new GraphQLClientService(
                restTemplateBuilder,
                objectMapper,
                resourceLoader,
                propertyConfig
        );
    }

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
    public void test_getAllIcedCoffees_null_data() {
        // given
        Map<String, Object> response = new HashMap<>();

        // when
        List<Map<String, Object>> result = graphQLClientService.getAllIcedCoffees(response);

        // then
        assertThat(result, is(nullValue()));
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

    @Test
    public void test_getAllHotCoffees_null_data() {
        // given
        Map<String, Object> response = new HashMap<>();

        // when
        List<Map<String, Object>> result = graphQLClientService.getAllHotCoffees(response);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void test_fetchCoffeeDetails_success() throws Exception {
        // given
        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        URI uri = Paths.get("src/test/resources/graphql/query.graphql").toUri();
        when(resource.getURI()).thenReturn(uri);

        when(propertyConfig.getBrewGraphEndpoint()).thenReturn("http://localhost/graphql");

        ResponseEntity<String> responseEntity = ResponseEntity.ok("{\"data\": {}}");
        when(restTemplate.exchange(
                eq("http://localhost/graphql"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        Map<String, Object> expectedMap = Map.of("data", Map.of());
        when(objectMapper.readValue("{\"data\": {}}", Map.class)).thenReturn(expectedMap);

        // when
        Map<String, Object> result = graphQLClientService.fetchCoffeeDetails();

        // then
        assertThat(result, is(expectedMap));
    }

    @Test
    public void test_fetchCoffeeDetails_blank_query() throws Exception {
        // given
        GraphQLClientService spyService = spy(graphQLClientService);
        doReturn("").when(spyService).loadQuery();

        // when
        Map<String, Object> result = spyService.fetchCoffeeDetails();

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_fetchCoffeeDetails_parsing_error() throws Exception {
        // given
        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        URI uri = Paths.get("src/test/resources/graphql/query.graphql").toUri();
        when(resource.getURI()).thenReturn(uri);

        when(propertyConfig.getBrewGraphEndpoint()).thenReturn("http://localhost/graphql");

        ResponseEntity<String> responseEntity = ResponseEntity.ok("{\"invalid\": json}");
        when(restTemplate.exchange(
                eq("http://localhost/graphql"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        when(objectMapper.readValue("{\"invalid\": json}", Map.class)).thenThrow(new JsonProcessingException("bad json") {});

        // when / then
        assertThrows(IronocJsonException.class, () -> {
            graphQLClientService.fetchCoffeeDetails();
        });
    }

    @Test
    public void test_loadQuery_error() throws Exception {
        // given
        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        // throwing IOException when getURI is called
        when(resource.getURI()).thenThrow(new IOException("Disk read failure"));

        // when / then
        assertThrows(IronocJsonException.class, () -> {
            graphQLClientService.loadQuery();
        });
    }
}
