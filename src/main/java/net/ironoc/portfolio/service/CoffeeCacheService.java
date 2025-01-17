package net.ironoc.portfolio.service;

import jakarta.annotation.PreDestroy;
import net.ironoc.portfolio.domain.CoffeeDomain;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoffeeCacheService implements CoffeesCache {

    private static final String BREWS_KEY = "brews-key";
    private final Map<String, List<CoffeeDomain>> coffees;

    public CoffeeCacheService() {
        this.coffees = new HashMap<>();
    }

    @Override
    public void put(List<CoffeeDomain> repositoryIssueDomains) {
        coffees.put(BREWS_KEY, repositoryIssueDomains);
    }

    @Override
    public List<CoffeeDomain> get() {
        return coffees.get(BREWS_KEY);
    }

    @Override
    public void remove() {
        coffees.remove(BREWS_KEY);
    }

    @PreDestroy
    public void tearDown() {
        coffees.clear();
    }
}

