package net.ironoc.portfolio.service;

import module java.base;

import jakarta.annotation.PostConstruct;
import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.domain.CoffeeDomain;
import net.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoffeesService extends AbstractLogger implements Coffees {

    private final RestTemplate restTemplate;

    private final PropertyConfigI propertyConfig;

    private final CoffeesCache coffeesCache;

    @Autowired
    public CoffeesService(RestTemplateBuilder restTemplateBuilder,
                          PropertyConfigI propertyConfig,
                          CoffeesCache coffeesCache) {
        this.restTemplate = restTemplateBuilder.build();
        this.propertyConfig = propertyConfig;
        this.coffeesCache = coffeesCache;
    }

    @PostConstruct
    public void populateBrewsCache() {
        if (propertyConfig.isBrewsCacheJobEnabled()) {
            getCoffeeDetails();
        } else {
            warn("The job to pre-populate the cache of Coffee Brews information is disabled.");
        }
    }

    @Scheduled(cron = "${net.ironoc.portfolio.brew.cron-job}")
    public void triggerBrewsCacheJob() {
        if (propertyConfig.isBrewsCacheJobEnabled()) {
            getCoffeeDetails();
        } else {
            warn("The job to update the cache of Coffee Brews information is disabled.");
        }
    }

    @Override
    public List<CoffeeDomain> getCoffeeDetails() {
        coffeesCache.tearDown();
        info("Entering CoffeesService.getCoffeeDetails");
        List<CoffeeDomain> hotCoffeeDomains = getBody(propertyConfig.getBrewApiEndpointHot());
        List<CoffeeDomain> coffeeDomains = new ArrayList<>(hotCoffeeDomains);
        info("Hot Coffee Details: hotCoffeeDtos={}", hotCoffeeDomains);
        List<CoffeeDomain> icedCoffeeDomains = getBody(propertyConfig.getBrewApiEndpointIce());
        coffeeDomains.addAll(icedCoffeeDomains);
        info("Iced Coffee Details: icedCoffeeDtos={}", icedCoffeeDomains);
        coffeesCache.put(coffeeDomains);
        return coffeeDomains;
    }

    private List<CoffeeDomain> getBody(String endpoint) {
        ResponseEntity<Object> response = restTemplate.getForEntity(endpoint, Object.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            if (response.getBody() instanceof List<?>) {
                return (List<CoffeeDomain>) response.getBody();
            } else {
                error("Error calling coffee API, object type does not match domain POJO: response={}", response);
                return Collections.emptyList();
            }
        } else {
            error("Error calling coffee API: response={}", response);
            return Collections.emptyList();
        }
    }
}
