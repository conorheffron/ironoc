package net.ironoc.portfolio.service;

import module java.base;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface GraphQLClient {
    Map<String, Object> fetchCoffeeDetails() throws JsonProcessingException;

    List<Map<String, Object>> getAllIcedCoffees(Map<String, Object> response);

    List<Map<String, Object>> getAllHotCoffees(Map<String, Object> response);
}
