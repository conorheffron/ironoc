package net.ironoc.portfolio.controller;

import lombok.extern.slf4j.Slf4j;
import net.ironoc.portfolio.dto.Donate;
import net.ironoc.portfolio.dto.DonateItemOrder;
import net.ironoc.portfolio.graph.DonateItemsResolver;
import net.ironoc.portfolio.logger.AbstractLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
@Slf4j
public class DonateGraphqlController extends AbstractLogger {

    @Autowired
    private final DonateItemsResolver donateItemsResolver;

    public DonateGraphqlController(DonateItemsResolver donateItemsResolver) {
        this.donateItemsResolver = donateItemsResolver;
    }

    @QueryMapping
    public Collection<Map<String, Object>> donateItems(@Argument("orderBy") DonateItemOrder donateItemOrder) {
        log.info("Entering donateItems with donateItemOrder={}", donateItemOrder);
        if (donateItemOrder == null) {
            return donateItemsResolver.getDonateItems();
        }
        return donateItemsResolver.getDonateItemsByOrder(donateItemOrder);
    }

    @SchemaMapping(typeName = "Query", field = "donateItemsSchemaMapping")
    public Collection<Donate> donateItemsSchemaMapping() {
        List<Map<String, Object>> donateItems = donateItemsResolver.getDonateItems();
        return mapDonateItemsToCharityOptions(donateItems);
    }

    @QueryMapping
    public Donate charityOptionByFounded(@Argument("founded") Integer founded) {
        info("Entering method charityByFounded, founded={}", founded);
        List<Map<String, Object>> donateItems = donateItemsResolver.getDonateItems();
        List<Donate> charityOptions = mapDonateItemsToCharityOptions(donateItems);
        Optional<Donate> donateOptional = charityOptions.stream()
                .filter(donate -> founded.equals(donate.getFounded()))
                .findFirst();
        info("Leaving method charityByFounded, donateOptional={}", donateOptional);
        return donateOptional.orElseGet(() -> Donate.builder().build());
    }

    @QueryMapping
    public Donate charityOptionByDonateLink(@Argument("link") String donateLink) {
        List<Map<String, Object>> donateItems = donateItemsResolver.getDonateItems();
        List<Donate> charityOptions = mapDonateItemsToCharityOptions(donateItems);

        Optional<Donate> donateOptional = charityOptions.stream()
                .filter(donate -> donateLink.equals(donate.getDonate()))
                .findFirst();

        return donateOptional.orElseGet(() -> Donate.builder().build());
    }

    @SchemaMapping
    public List<Donate> charityOptions(Donate donateArg) {
        List<Map<String, Object>> donateItems = donateItemsResolver.getDonateItems();
        List<Donate> charityOptions = mapDonateItemsToCharityOptions(donateItems);

        Stream<Donate> donateOptional = charityOptions.stream()
                .filter(donateArg::equals);
        info("Leaving method charityOptions, donateOptional={}", donateOptional);
        return donateOptional.toList();
    }

    @MutationMapping
    public Donate addCharityOption(
            @Argument("alt") String alt,
            @Argument("name") String name,
            @Argument("link") String link,
            @Argument("donate") String donate,
            @Argument("img") String img,
            @Argument("overview") String overview,
            @Argument("founded") Integer founded,
            @Argument("phone") String phone
    ) {
        Donate newDonate = Donate.builder()
                .alt(alt)
                .name(name)
                .link(link)
                .donate(donate)
                .img(img)
                .overview(overview)
                .founded(founded)
                .phone(phone)
                .build();

        // Add to resolver or data source
        donateItemsResolver.addDonateItem(newDonate);

        info("Added new charity option: {}", newDonate);
        return newDonate;
    }

    private List<Donate> mapDonateItemsToCharityOptions(List<Map<String, Object>> donateItems) {
        List<Donate> charityOptions = new ArrayList<>();
        for (Map<String, Object> d : donateItems) {
            if (d != null) {
                Donate donate = Donate.builder()
                        .alt(parseValue(d, "alt"))
                        .name(parseValue(d, "name"))
                        .link(parseValue(d, "link"))
                        .donate(parseValue(d, "donate"))
                        .img(parseValue(d, "img"))
                        .overview(parseValue(d, "overview"))
                        .founded(Integer.parseInt(parseValue(d, "founded")))
                        .phone(parseValue(d, "phone"))
                        .build();
                charityOptions.add(donate);
                info("Completed mapping of Donate item, donate={}", donate);
            }
        }
        return charityOptions;
    }

    private String parseValue(Map<String, Object> d, String key) {
        return d.get(key) == null ? null : d.get(key).toString();
    }
}
