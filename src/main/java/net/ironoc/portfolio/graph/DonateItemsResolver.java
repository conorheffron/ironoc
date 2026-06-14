package net.ironoc.portfolio.graph;

import module java.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.kickstart.tools.GraphQLQueryResolver;
import jakarta.annotation.PostConstruct;
import net.ironoc.portfolio.domain.CharityOption;
import net.ironoc.portfolio.dto.Donate;
import net.ironoc.portfolio.dto.DonateItemOrder;
import net.ironoc.portfolio.exception.IronocJsonException;
import net.ironoc.portfolio.logger.AbstractLogger;
import net.ironoc.portfolio.repository.CharityOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DonateItemsResolver extends AbstractLogger implements GraphQLQueryResolver {

    private static final String DONATE_ITEMS_JSON_FILE = "json/donate-items.json";
    private static final String CHARITIES_LIST_FILE = "graphql/charities.txt";

    private final CharityOptionRepository charityOptionRepository;

    // In-memory set to store allowed charity names
    protected final Set<String> allowedCharityNames = new HashSet<>();

    @Autowired
    public DonateItemsResolver(CharityOptionRepository charityOptionRepository) {
        this.charityOptionRepository = charityOptionRepository;
    }


    @PostConstruct
    public void loadDonateItems() {
        ObjectMapper objectMapper = new ObjectMapper();
        loadAllowedCharityNames();
        try {
            List<Map<String, Object>> loadedList = objectMapper.readValue(
                    new ClassPathResource(DONATE_ITEMS_JSON_FILE).getInputStream(),
                    new TypeReference<>() {});
            for (Map<String, Object> map : loadedList) {
                Donate donate = objectMapper.convertValue(map, Donate.class);
                // Only insert if not already present (idempotent on restart)
                if (!charityOptionRepository.existsByName(donate.getName())) {
                    charityOptionRepository.save(toEntity(donate));
                }
            }
        } catch (IOException e) {
            error("Failed to load Donate items JSON", e);
            throw new IronocJsonException("Failed to load donate items JSON", e);
        }
    }

    /**
     * Loads the allowed charity names from the text file into memory.
     */
    private void loadAllowedCharityNames() {
        allowedCharityNames.clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource(CHARITIES_LIST_FILE).getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    allowedCharityNames.add(trimmed);
                }
            }
            info("Loaded allowed charity names: {}", allowedCharityNames);
        } catch (IOException e) {
            error("Failed to load charities.txt", e);
        }
    }

    /**
     * Returns a list of donate items from the database as Map for compatibility.
     */
    public List<Map<String, Object>> getDonateItems() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> asMaps = new ArrayList<>();
        for (CharityOption entity : charityOptionRepository.findAll()) {
            asMaps.add(objectMapper.convertValue(toDto(entity), new TypeReference<>() {}));
        }
        return asMaps;
    }

    public List<Map<String, Object>> getDonateItemsByOrder(DonateItemOrder donateItemOrder) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CharityOption> allItems = charityOptionRepository.findAll();

        List<CharityOption> sortedItems = allItems;
        // sort by year founded
        if (donateItemOrder.getFounded() != null) {
            sortedItems = switch (donateItemOrder.getFounded()) {
                case ASC -> allItems.stream()
                        .sorted(Comparator.comparingInt(CharityOption::getFounded))
                        .toList();
                case DESC -> allItems.stream()
                        .sorted(Comparator.comparingInt(CharityOption::getFounded))
                        .toList().reversed();
            };
        }

        // sort by charity name (alphabetical order)
        if (donateItemOrder.getName() != null) {
            sortedItems = switch (donateItemOrder.getName()) {
                case ASC -> allItems.stream()
                        .sorted(Comparator.comparing(CharityOption::getName))
                        .toList();
                case DESC -> allItems.stream()
                        .sorted(Comparator.comparing(CharityOption::getName))
                        .toList().reversed();
            };
        }

        List<Map<String, Object>> asMaps = new ArrayList<>();
        for (CharityOption entity : sortedItems) {
            asMaps.add(objectMapper.convertValue(toDto(entity), new TypeReference<>() {}));
        }
        return asMaps;
    }

    /**
     * Add a new charity option (donate item) to the database, if all fields are valid.
     * Only allows items whose name matches an entry in charities.txt,
     * and only if that name is not already present in the database.
     * @param donate the Donate item to add
     */
    public void addDonateItem(Donate donate) {
        String charityName = donate.getName() != null ? donate.getName().trim() : null;
        if (!isInAllowedCharities(charityName)) {
            info("Attempted to add DonateItem with name not in allowed charities list: {}", donate);
            return;
        }
        if (charityOptionRepository.existsByName(charityName)) {
            info("Attempted to add DonateItem with name already present in database: {}", donate);
            return;
        }
        donate.setName(charityName);
        if (isValidDonate(donate)) {
            charityOptionRepository.save(toEntity(donate));
            info("Added new DonateItem to database: {}", donate);
        } else {
            info("Attempted to add invalid DonateItem: {}", donate);
        }
    }

    /**
     * Converts a Donate DTO to a CharityOption JPA entity.
     */
    private CharityOption toEntity(Donate donate) {
        return CharityOption.builder()
                .name(donate.getName())
                .donate(donate.getDonate())
                .link(donate.getLink())
                .img(donate.getImg())
                .alt(donate.getAlt())
                .overview(donate.getOverview())
                .founded(donate.getFounded())
                .phone(donate.getPhone())
                .build();
    }

    /**
     * Converts a CharityOption JPA entity to a Donate DTO.
     */
    private Donate toDto(CharityOption entity) {
        return Donate.builder()
                .name(entity.getName())
                .donate(entity.getDonate())
                .link(entity.getLink())
                .img(entity.getImg())
                .alt(entity.getAlt())
                .overview(entity.getOverview())
                .founded(entity.getFounded())
                .phone(entity.getPhone())
                .build();
    }

    /**
     * Checks if the given name exists in the allowed charities list.
     */
    private boolean isInAllowedCharities(String name) {
        return name != null && allowedCharityNames.contains(name.trim());
    }

    /**
     * Only allow add to database for valid alpha numeric strings, emails, years and hyperlinks.
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
        return value != null && value.matches("^[\\p{L}0-9 .,''’\\-()\\[\\]/&;:!%€$@#\\?\\r\\n]*$");
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
