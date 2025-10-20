package net.ironoc.portfolio.graph;

import module java.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.kickstart.tools.GraphQLQueryResolver;
import net.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class BrewsResolver extends AbstractLogger implements GraphQLQueryResolver {

    protected static final String BREWS_JSON_FILE = "json/brews.json";

    public List<Map<String, Object>> getBrews() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load the JSON file from resources
            return objectMapper.readValue(
                    new ClassPathResource(BREWS_JSON_FILE).getInputStream(),
                    new TypeReference<>() {});
        } catch (IOException e) {
            error("Failed to load Brews JSON", e);
        }
        return Collections.emptyList();
    }
}
