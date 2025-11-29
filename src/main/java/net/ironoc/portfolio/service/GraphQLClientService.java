package net.ironoc.portfolio.service;

import module java.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.logger.AbstractLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GraphQLClientService extends AbstractLogger implements GraphQLClient {

    private final ObjectMapper objectMapper;

    private final ResourceLoader resourceLoader;

    private final RestTemplate restTemplate;

    private final PropertyConfigI propertyConfig;

    @Autowired
    public GraphQLClientService(RestTemplateBuilder restTemplateBuilder,
                                ObjectMapper objectMapper,
                                ResourceLoader resourceLoader,
                                PropertyConfigI propertyConfig) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.propertyConfig = propertyConfig;
    }

    @Override
    public Map<String, Object> fetchCoffeeDetails() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String query = this.loadQuery();
        if (!StringUtils.isBlank(query)) {
            String requestPayload = "{ \"query\": \"" + query.replace("\"", "\\\"")
                    .replace("\n", " ") + "\" }";
            HttpEntity<String> request = new HttpEntity<>(requestPayload, headers);
            ResponseEntity<String> response = restTemplate.exchange(propertyConfig.getBrewGraphEndpoint(),
                    HttpMethod.POST, request, String.class);
            return objectMapper.readValue(response.getBody(), Map.class);
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public List<Map<String, Object>> getAllIcedCoffees(Map<String, Object> response) {
        Map<String, Object> data = getDataFromResponse(response);
        if (data != null) {
            return (List<Map<String, Object>>) data.get("allIceds");
        }
        warn("No response data found during getAllIcedCoffees for response, response={}", response);
        return null;
    }

    @Override
    public List<Map<String, Object>> getAllHotCoffees(Map<String, Object> response) {
        Map<String, Object> data = getDataFromResponse(response);
        if (data != null) {
            return (List<Map<String, Object>>) data.get("allHots");
        }
        warn("No response data found during getAllHotCoffees for response, response={}", response);
        return null;
    }

    String loadQuery() {
        Resource resource = resourceLoader.getResource("classpath:graphql" + File.separator + "coffeesQuery.graphqls");
        try {
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (IOException e) {
            error("Unexpected exception occurred loading GraphQL query, msg={}", e.getMessage());
        }
        return null;
    }

    private static Map<String, Object> getDataFromResponse(Map<String, Object> response) {
        return (Map<String, Object>) response.get("data");
    }
}