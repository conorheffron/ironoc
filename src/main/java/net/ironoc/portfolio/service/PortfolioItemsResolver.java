package net.ironoc.portfolio.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class PortfolioItemsResolver extends AbstractLogger {

    protected static final String PORTFOLIO_ITEMS_JSON_FILE = "json/portfolio-items.json";

    public List<Map<String, Object>> getPortfolioItems() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load the JSON file from resources
            return objectMapper.readValue(
                    new ClassPathResource(PORTFOLIO_ITEMS_JSON_FILE).getInputStream(),
                    new TypeReference<>() {});
        } catch (IOException e) {
            error("Failed to load Portfolio items JSON", e);
        }
        return Collections.emptyList();
    }
}
