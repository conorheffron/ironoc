package net.ironoc.portfolio.graph;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.kickstart.tools.GraphQLQueryResolver;
import jakarta.annotation.PostConstruct;
import net.ironoc.portfolio.dto.Donate;
import net.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DonateItemsResolver extends AbstractLogger implements GraphQLQueryResolver {

    protected static final String DONATE_ITEMS_JSON_FILE = "json/donate-items.json";

    // In-memory list to store donate items as POJOs
    protected final List<Donate> donateItems = new ArrayList<>();

    @PostConstruct
    public void loadDonateItems() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map<String, Object>> loadedList = objectMapper.readValue(
                    new ClassPathResource(DONATE_ITEMS_JSON_FILE).getInputStream(),
                    new TypeReference<>() {});
            donateItems.clear();
            for (Map<String, Object> map : loadedList) {
                Donate donate = objectMapper.convertValue(map, Donate.class);
                donateItems.add(donate);
            }
        } catch (IOException e) {
            error("Failed to load Donate items JSON", e);
        }
    }

    /**
     * Returns a list of donate items from memory as Map for compatibility.
     */
    public List<Map<String, Object>> getDonateItems() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> asMaps = new ArrayList<>();
        for (Donate donate : donateItems) {
            asMaps.add(objectMapper.convertValue(donate, new TypeReference<>() {}));
        }
        return asMaps;
    }

    /**
     * Add a new charity option (donate item) to the in-memory list, if all fields are valid.
     * @param donate the Donate item to add
     */
    public void addDonateItem(Donate donate) {
        if (isValidDonate(donate)) {
            donateItems.add(donate);
            info("Added new DonateItem to memory: {}", donate);
        } else {
            info("Attempted to add invalid DonateItem: {}", donate);
        }
    }

    /**
     * Only allow add to memory for valid alpha numeric strings, emails, years and hyperlinks.
     */
    private boolean isValidDonate(Donate donate) {
        return isAlphanumericOrSpace(donate.getAlt())
                && isAlphanumericOrSpace(donate.getName())
                && isValidHyperlink(donate.getLink())
                && isValidHyperlink(donate.getDonate())
                && isAlphanumericOrSpace(donate.getImg())
                && isAlphanumericOrSpace(donate.getOverview())
                && isValidYear(donate.getFounded())
                && isValidPhoneOrEmail(donate.getPhone());
    }

    // Accept only alphanumeric and space (and basic punctuation for overview)
    private boolean isAlphanumericOrSpace(String value) {
        return value != null && value.matches("^[\\p{L}0-9 .,'’\\-()\\[\\]/&;:!%€$@#\\?\\r\\n]*$");
    }

    // Accept HTTP/HTTPS links
    private boolean isValidHyperlink(String value) {
        return value != null && value.matches("^(https?://)[\\w\\-._~:/?@!$&'()*+,;=%\\[\\]]+$");
    }

    // Accept year between 1000 and 2100
    private boolean isValidYear(Integer year) {
        return year != null && year >= 1000 && year <= 2100;
    }

    // Accept emails, phone numbers, or empty
    private boolean isValidPhoneOrEmail(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        String phonePattern = "^[+\\d()\\- .,/\\[\\]\\r\\n]{7,}$";
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return value.matches(phonePattern) || value.matches(emailPattern) || isAlphanumericOrSpace(value);
    }
}
