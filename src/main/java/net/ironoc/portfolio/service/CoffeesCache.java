package net.ironoc.portfolio.service;

import module java.base;

import net.ironoc.portfolio.domain.CoffeeDomain;

public interface CoffeesCache {
    void put(List<CoffeeDomain> repositoryIssueDomains);

    List<CoffeeDomain> get();

    void remove();

    void tearDown();
}
