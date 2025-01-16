package net.ironoc.portfolio.service;

import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.domain.CoffeeDomain;
import net.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CoffeesService extends AbstractLogger implements Coffees {

    private final RestTemplate restTemplate;

    private final PropertyConfigI propertyConfig;

    @Autowired
    public CoffeesService(RestTemplateBuilder restTemplateBuilder,
                          PropertyConfigI propertyConfig) {
        this.restTemplate = restTemplateBuilder.build();
        this.propertyConfig = propertyConfig;
    }

    @Override
    public List<CoffeeDomain> getCoffeeDetails() {
        info("Entering CoffeesService.getCoffeeDetails");
        List<CoffeeDomain> hotCoffeeDomains = List.of(Objects.requireNonNull(
                restTemplate.getForEntity(propertyConfig.getBrewApiEndpointHot(), CoffeeDomain[].class).getBody()));
        List<CoffeeDomain> coffeeDomains = new ArrayList<>(hotCoffeeDomains);
        info("Hot Coffee Details: hotCoffeeDtos={}", hotCoffeeDomains);
        List<CoffeeDomain> icedCoffeeDomains = List.of(Objects.requireNonNull(
                restTemplate.getForEntity(propertyConfig.getBrewApiEndpointIce(), CoffeeDomain[].class).getBody()));
        coffeeDomains.addAll(icedCoffeeDomains);
        info("Iced Coffee Details: icedCoffeeDtos={}", icedCoffeeDomains);
        return coffeeDomains;
    }
}
