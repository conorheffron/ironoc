package net.ironoc.portfolio.controller;

import net.ironoc.portfolio.dto.Brew;
import net.ironoc.portfolio.graph.BrewsResolver;
import net.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class BrewGraphqlController extends AbstractLogger {

    @Autowired
    private final BrewsResolver brewsResolver;

    public BrewGraphqlController(BrewsResolver brewsResolver) {
        this.brewsResolver = brewsResolver;
    }

    @QueryMapping
    public Collection<Map<String, Object>> brews() {
        return brewsResolver.getBrews();
    }

    @SchemaMapping(typeName = "Query", field = "brewsSchemaMapping")
    public Collection<Brew> brewsSchemaMapping() {
        List<Map<String, Object>> brews = brewsResolver.getBrews();
        return mapBrewsToCoffeesList(brews);
    }

    private List<Brew> mapBrewsToCoffeesList(List<Map<String, Object>> brews) {
        List<Brew> coffees = new ArrayList<>();
        for (Map<String, Object> d : brews) {
            if (d != null) {
                Brew coffee = Brew.builder()
                        .title(parseValue(d, "title"))
                        .description(parseValue(d, "description"))
                        .ingredients(parseValue(d, "ingredients").split(", "))
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
