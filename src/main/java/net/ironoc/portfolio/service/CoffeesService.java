package net.ironoc.portfolio.service;

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

    @Autowired
    public CoffeesService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public List<CoffeeDomain> getCoffeeDetails() {
        // TODO move URLs to configurable
        info("Entering CoffeesService.getCoffeeDetails");
        String urlHotDrinks = "https://api.sampleapis.com/coffee/hot";
        String urlIcedDrinks = "https://api.sampleapis.com/coffee/iced";
        List<CoffeeDomain> coffeeDomains = new ArrayList<>();
        List<CoffeeDomain> hotCoffeeDomains = List.of(Objects.requireNonNull(
                restTemplate.getForEntity(urlHotDrinks, CoffeeDomain[].class).getBody()));
        coffeeDomains.addAll(hotCoffeeDomains);
        info("Hot Coffee Details: hotCoffeeDtos={}", hotCoffeeDomains);
        List<CoffeeDomain> icedCoffeeDomains = List.of(Objects.requireNonNull(
                restTemplate.getForEntity(urlIcedDrinks, CoffeeDomain[].class).getBody()));
        coffeeDomains.addAll(icedCoffeeDomains);
        info("Iced Coffee Details: icedCoffeeDtos={}", icedCoffeeDomains);
        return coffeeDomains;
    }
}
