package net.ironoc.portfolio.graph;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.kickstart.tools.GraphQLQueryResolver;
import net.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class DonateItemsResolver extends AbstractLogger implements GraphQLQueryResolver {

    protected static final String DONATE_ITEMS_JSON_FILE = "json/donate-items.json";

    public List<Map<String, Object>> getDonateItems() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load the JSON file from resources
            return objectMapper.readValue(
                    new ClassPathResource(DONATE_ITEMS_JSON_FILE).getInputStream(),
                    new TypeReference<>() {});
        } catch (IOException e) {
            error("Failed to load donate items JSON", e);
        }
        return Collections.emptyList();
    }
}
