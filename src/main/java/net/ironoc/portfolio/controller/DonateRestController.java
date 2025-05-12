package net.ironoc.portfolio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import net.ironoc.portfolio.graph.DonateItemsResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DonateRestController {

    @Autowired
    private final DonateItemsResolver donateItemsResolver;

    public DonateRestController(DonateItemsResolver donateItemsResolver) {
        this.donateItemsResolver = donateItemsResolver;
    }

    @Operation(summary = "Get Details of Each Charity Option",
            description = "Returns a list of Charity Options & corresponding details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved Charity Options.")
    })
    @GetMapping(value = {"/donate-items"}, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> getDonateItems() {
        // Use the resolver to fetch the Donate/Charity option items
        List<Map<String, Object>> donateItems = donateItemsResolver.getDonateItems();
        return ResponseEntity.ok(donateItems);
    }
}
