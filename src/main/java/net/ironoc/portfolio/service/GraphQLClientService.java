package net.ironoc.portfolio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ironoc.portfolio.logger.AbstractLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphQLClientService extends AbstractLogger {

    private final ObjectMapper objectMapper;

    private final ResourceLoader resourceLoader;

    private final RestTemplate restTemplate;

    private static final String GRAPHQL_URL = "https://api.sampleapis.com/coffee/graphql";// TODO move to yml

    @Autowired
    public GraphQLClientService(RestTemplateBuilder restTemplateBuilder,
                          ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    public Map<String, Object> fetchCoffeeDetails() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String query = this.loadQuery("query.graphql");
        if (!StringUtils.isBlank(query)) {
            String requestPayload = "{ \"query\": \"" + query.replace("\"", "\\\"").replace("\n", " ") + "\" }";
            HttpEntity<String> request = new HttpEntity<>(requestPayload, headers);
            ResponseEntity<String> response = restTemplate.exchange(GRAPHQL_URL, HttpMethod.POST, request, String.class);
            return objectMapper.readValue(response.getBody(), Map.class);
        } else {
            return new HashMap<>();
        }
    }

    private String loadQuery(String fileName) {
        Resource resource = resourceLoader.getResource("classpath:graphql" + File.separator + fileName);
        try {
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (IOException e) {
            error("Unexpected exception occurred loading GraphQL query, msg={}", e.getMessage());
        }
        return null;
    }

    public List<Map<String, Object>> getAllIcedCoffees(Map<String, Object> response) {
        return (List<Map<String, Object>>) ((Map<String, Object>) response.get("data")).get("allIceds");
    }

    public List<Map<String, Object>> getAllHotCoffees(Map<String, Object> response) {
        return (List<Map<String, Object>>) ((Map<String, Object>) response.get("data")).get("allHots");
    }
}