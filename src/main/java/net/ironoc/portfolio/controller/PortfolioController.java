package net.ironoc.portfolio.controller;

import module java.base;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.ironoc.portfolio.service.PortfolioItemsResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PortfolioController {

    private final PortfolioItemsResolver portfolioItemsResolver;

    public PortfolioController(@Autowired PortfolioItemsResolver portfolioItemsResolver) {
        this.portfolioItemsResolver = portfolioItemsResolver;
    }

    @Operation(summary = "Get Details of Each Portfolio Project item",
            description = "Returns a list of Portfolio Project & corresponding details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved Portfolio Projects.")
    })
    @GetMapping(value = {"/portfolio-items"}, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> getPortfolioItems() {
        // Use the resolver to fetch the portfolio items
        List<Map<String, Object>> portfolioItems = portfolioItemsResolver.getPortfolioItems();
        return ResponseEntity.ok(portfolioItems);
    }
}
