package net.ironoc.portfolio.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface GraphQLClient {
    Map<String, Object> fetchCoffeeDetails() throws JsonProcessingException;

    List<Map<String, Object>> getAllIcedCoffees(Map<String, Object> response);

    List<Map<String, Object>> getAllHotCoffees(Map<String, Object> response);
}
