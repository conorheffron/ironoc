package net.ironoc.portfolio.service;

import net.ironoc.portfolio.domain.CoffeeDomain;

import java.util.List;

public interface CoffeesCache {
    void put(List<CoffeeDomain> repositoryIssueDomains);

    List<CoffeeDomain> get();

    void remove();
}
