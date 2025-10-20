package net.ironoc.portfolio.controller;

import module java.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.ironoc.portfolio.graph.BrewsResolver;
import net.ironoc.portfolio.service.GraphQLClient;
import net.ironoc.portfolio.logger.AbstractLogger;
import net.ironoc.portfolio.domain.CoffeeDomain;
import net.ironoc.portfolio.service.Coffees;
import net.ironoc.portfolio.service.CoffeesCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CoffeeController extends AbstractLogger {

    private final Coffees coffeesService;

    private final CoffeesCache coffeesCache;

    private final GraphQLClient graphQLClient;

    private final BrewsResolver brewsResolver;

    @Autowired
    public CoffeeController(Coffees coffeesService, CoffeesCache coffeesCache, GraphQLClient graphQLClient,
                            BrewsResolver brewsResolver) {
        this.coffeesService = coffeesService;
        this.coffeesCache = coffeesCache;
        this.graphQLClient = graphQLClient;
        this.brewsResolver = brewsResolver;
    }

    @Operation(summary = "Put local storage brews into Coffees Cache",
            description = "Returns a list of Coffee Graphics & Recipes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully cached coffee brew details.")
    })
    @PutMapping(value = {"/coffees"}, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CoffeeDomain>> putCoffeesIntoMemoryStorage() {
        List<CoffeeDomain> cachedResults = coffeesCache.get();
        if (cachedResults != null && !cachedResults.isEmpty()) {
            return ResponseEntity.ok(cachedResults);
        } else {
            coffeesCache.tearDown();
            List<Map<String, Object>> brews = brewsResolver.getBrews();
            List<CoffeeDomain> coffeeDomains = mapBrewsToCoffeesList(brews);
            coffeesCache.put(coffeeDomains);
            return ResponseEntity.ok(coffeeDomains);
        }
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
            debug("Returning cached brews, cachedResults={}", cachedResults);
            return ResponseEntity.ok(cachedResults);
        }
    }

    List<CoffeeDomain> mapBrewsToCoffeesList(List<Map<String, Object>> brews) {
        List<CoffeeDomain> coffees = new ArrayList<>();
        for (Map<String, Object> d : brews) {
            if (d != null) {
                CoffeeDomain coffee = CoffeeDomain.builder()
                        .title(parseValue(d, "title"))
                        .description(parseValue(d, "description"))
                        .ingredients(List.of(parseValue(d, "ingredients").split(", ")))
                        .image(parseValue(d, "image"))
                        .id(Integer.parseInt(parseValue(d, "id")))
                        .build();
                coffees.add(coffee);
                info("Completed mapping of Brew item, coffee={}", coffee);
            }
        }
        return coffees;
    }

    private String parseValue(Map<String, Object> d, String key) {
        return d.get(key) == null ? null : d.get(key).toString();
    }
}
