package net.ironoc.portfolio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.ironoc.portfolio.service.GraphQLClient;
import net.ironoc.portfolio.logger.AbstractLogger;
import net.ironoc.portfolio.domain.CoffeeDomain;
import net.ironoc.portfolio.service.Coffees;
import net.ironoc.portfolio.service.CoffeesCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class CoffeeController extends AbstractLogger {

    private final Coffees coffeesService;

    private final CoffeesCache coffeesCache;

    private final GraphQLClient graphQLClient;

    @Autowired
    public CoffeeController(Coffees coffeesService, CoffeesCache coffeesCache, GraphQLClient graphQLClient) {
        this.coffeesService = coffeesService;
        this.coffeesCache = coffeesCache;
        this.graphQLClient = graphQLClient;
    }

    @Operation(summary = "Get Hot/Iced Coffee Details by REST API call",
            description = "Returns a list of Coffee Graphics & Recipes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved coffee brew details.")
    })
    @GetMapping(value = {"/coffees"}, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CoffeeDomain>> getCoffeeDetails() {
        List<CoffeeDomain> cachedResults = coffeesCache.get();
        if (cachedResults != null && !cachedResults.isEmpty()) {
            return ResponseEntity.ok(cachedResults);
        } else {
            List<CoffeeDomain> coffeeDomain = coffeesService.getCoffeeDetails();
            return ResponseEntity.ok(coffeeDomain);
        }
    }

    @Operation(summary = "Get Hot/Iced Coffee Details by GraphQL call",
            description = "Returns a list of Coffee Graphics & Recipes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved coffee brew details.")
    })
    @GetMapping(value = {"/coffees-graph-ql"}, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CoffeeDomain>> getCoffeeDetailsGraphQl() {
        List<CoffeeDomain> cachedResults = coffeesCache.get();
        if (cachedResults == null || cachedResults.isEmpty()) {
            try {
                // fetch brew details
                Map<String, Object> response = graphQLClient.fetchCoffeeDetails();
                List<Map<String, Object>> hot = graphQLClient.getAllHotCoffees(response);
                List<Map<String, Object>> ice = graphQLClient.getAllIcedCoffees(response);
                List<Map<String, Object>> mergedCoffees = new ArrayList<>();
                mergedCoffees.addAll(hot);
                mergedCoffees.addAll(ice);

                // map results
                List<CoffeeDomain> coffeeDomains = new ArrayList<>();
                for (Map<String, Object> coffeeMap : mergedCoffees) {
                    try {
                        CoffeeDomain coffeeDomain = new ObjectMapper().convertValue(coffeeMap, CoffeeDomain.class);
                        coffeeDomains.add(coffeeDomain);
                    } catch (Exception e) {
                        error("Error occurred mapping coffee domain object", e.getMessage());
                    }
                }

                // cache result set
                info("Retrieved brews from GraphQL query, coffeeDomains={}", coffeeDomains);
                coffeesCache.put(coffeeDomains);

                return ResponseEntity.ok(coffeeDomains);
            } catch (JsonProcessingException e) {
                error("Unexpected exception occurred loading GraphQL query, msg={}", e.getMessage());
            }
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            info("Returning cached brews, cachedResults={}", cachedResults);
            return ResponseEntity.ok(cachedResults);
        }
    }
}
