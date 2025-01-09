package net.ironoc.portfolio.service;

import net.ironoc.portfolio.dto.CoffeeDto;
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
    public List<CoffeeDto> getCoffeeDetails() {
        // TODO move URLs to configurable
        info("Entering CoffeesService.getCoffeeDetails");
        String urlHotDrinks = "https://api.sampleapis.com/coffee/hot";
        String urlIcedDrinks = "https://api.sampleapis.com/coffee/iced";
        List<CoffeeDto> coffeeDtos = new ArrayList<>();
        List<CoffeeDto> hotCoffeeDtos = List.of(Objects.requireNonNull(
                restTemplate.getForEntity(urlHotDrinks, CoffeeDto[].class).getBody()));
        coffeeDtos.addAll(hotCoffeeDtos);
        info("Hot Coffee Details: hotCoffeeDtos={}", hotCoffeeDtos);
        List<CoffeeDto> icedCoffeeDtos = List.of(Objects.requireNonNull(
                restTemplate.getForEntity(urlIcedDrinks, CoffeeDto[].class).getBody()));
        coffeeDtos.addAll(icedCoffeeDtos);
        info("Iced Coffee Details: icedCoffeeDtos={}", icedCoffeeDtos);
        return coffeeDtos;
    }
}
