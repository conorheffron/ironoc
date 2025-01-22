package net.ironoc.portfolio.utils;

import net.ironoc.portfolio.domain.CoffeeDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRequestResponseUtils {
    public static  List<CoffeeDomain> getSampleCoffeeDomainList() {
        List<CoffeeDomain> coffeeDomains = new ArrayList<>();
        coffeeDomains.add(CoffeeDomain.builder()
                .id(1)
                .ingredients(List.of("Coffee", "Ice"))
                .image("image_url_1")
                .title("Iced Coffee").build());
        coffeeDomains.add(CoffeeDomain.builder()
                .id(2)
                .ingredients(List.of("Espresso", "Milk"))
                .image("image_url_2")
                .title("Latte").build());
        return coffeeDomains;
    }

    public static Map<String, Object> getSampleResponse(boolean withInvalidIngredientsItem) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> allHots = new ArrayList<>();
        List<Map<String, Object>> allIce = new ArrayList<>();

        Map<String, Object> hotCoffee = new HashMap<>();
        hotCoffee.put("id", "1");
        hotCoffee.put("ingredients", List.of("Coffee"));
        hotCoffee.put("image", "image_url_1");
        hotCoffee.put("title", "Black Coffee");
        allHots.add(hotCoffee);

        Map<String, Object> icedCoffee = new HashMap<>();
        icedCoffee.put("id", "2");
        icedCoffee.put("ingredients", List.of("Espresso", "Milk"));
        icedCoffee.put("image", "image_url_2");
        icedCoffee.put("title", "Latte");
        allIce.add(icedCoffee);

        if (withInvalidIngredientsItem) {
            Map<String, Object> icedCoffee2 = new HashMap<>();
            icedCoffee2.put("id", "3");
            icedCoffee2.put("ingredients", "invalid_array");
            icedCoffee2.put("image", "image_url_3");
            icedCoffee2.put("title", "Ice Black");
            allIce.add(icedCoffee2);
        }

        response.put("allHots", allHots);
        response.put("allIceds", allIce);
        return response;
    }
}
