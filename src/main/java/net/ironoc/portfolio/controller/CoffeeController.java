package net.ironoc.portfolio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.ironoc.portfolio.logger.AbstractLogger;
import net.ironoc.portfolio.domain.CoffeeDomain;
import net.ironoc.portfolio.service.Coffees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CoffeeController extends AbstractLogger {

    private final Coffees coffeesService;

    @Autowired
    public CoffeeController(Coffees coffeesService) {
        this.coffeesService = coffeesService;
    }

    @Operation(summary = "Get Hot/Iced Coffee Details",
            description = "Returns a list of Coffee Graphics & Recipes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved coffee brew details.")
    })
    @GetMapping(value = {"/coffees"}, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CoffeeDomain>> getCoffeeDetails() {
        List<CoffeeDomain> coffeeDomain = coffeesService.getCoffeeDetails();
        return ResponseEntity.ok(coffeeDomain);
    }
}
