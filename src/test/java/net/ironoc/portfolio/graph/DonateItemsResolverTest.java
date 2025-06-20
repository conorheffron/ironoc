package net.ironoc.portfolio.graph;

import net.ironoc.portfolio.dto.Donate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;

class DonateItemsResolverTest {

    private DonateItemsResolver donateItemsResolver;

    public static List<Map<String, Object>> donateItems = List.of(
            Map.of(
                    "donate", "https://vi.ie/supporting-us/donate-now/",
                    "link", "https://linktr.ee/vision_ireland",
                    "img", "red",
                    "alt", "red2",
                    "name", "Vision Ireland, the new name for NCBI",
                    "overview", "Vision Ireland, the name for NCBI is Ireland’s national charity working " +
                            "for the rising number of people affected by sight loss. Our practical and emotional " +
                            "advice and support help 8,000 people and their families confidently face their " +
                            "futures every year.",
                    "founded", 1931,
                    "phone", "+353 (0)1 830 7033"
            ),
            Map.of(
                    "donate", "https://www.childrenshealth.ie/donate/",
                    "link", "https://www.childrenshealthireland.ie/",
                    "img", "red",
                    "alt", "red3",
                    "name", "Temple Street Children’s University Hospital",
                    "overview", "Temple Street, is an acute national paediatric hospital. " +
                            "Seven major specialties at Temple Street today include neonatal & paediatric surgery, " +
                            "neurology, neurosurgery, nephrology, orthopaedics, ENT & plastic surgery. " +
                            "Temple Street cares for 145,000 children per year including 45,000 who attend the " +
                            "Emergency Department (ED) every year. A staff of 85 Consultants& over 950 other full-time " +
                            "& part-time HSCPs (Health & Social Care Professionals) % other staff deliver care & services.",
                    "founded", 1872,
                    "phone", "<br/><b>Temple Street:</b> +353 01 878 4200,\n                        " +
                            "<b>Tallaght:</b> +353 01 693 7500,<br />\n                        " +
                            "<b>Crumlin:</b> +353 01 409 6100,\n                        <b>Connolly:" +
                            "</b> +353 01 640 7500"
            ),
            Map.of(
                    "donate", "https://donors.cancer.ie/page/FUNDZAZBHHS",
                    "link", "https://www.cancer.ie/",
                    "img", "red",
                    "alt", "red4",
                    "name", "Irish Cancer Society",
                    "overview", "We are a community of patients, survivors, volunteers, supporters, " +
                            "health and social care professionals and researchers. Together we are transforming " +
                            "the experiences and outcomes of people affected by cancer through our advocacy, " +
                            "support services and research.",
                    "founded", 1963,
                    "phone", "+353 (0) 1800200700"
            ),
            Map.of(
                    "donate", "https://www.svp.ie/donate/",
                    "link", "https://www.svp.ie/",
                    "img", "red",
                    "alt", "red5",
                    "name", "The Society of Saint Vincent de Paul",
                    "overview", "The Society of Saint Vincent de Paul exists to fight poverty. " +
                            "The Society of St. Vincent de Paul is the largest, voluntary, charitable " +
                            "organisation in Ireland. Its membership of over 11,000 volunteer members throughout " +
                            "the country are supported by 600 staff, working for social justice and the creation " +
                            "of a more just, caring nation. This unique network of social concern also gives " +
                            "practical support to those experiencing poverty and social exclusion, by providing " +
                            "a wide range of services to people in need.",
                    "founded", 1844,
                    "phone", "+353 1 884 8200"
            ),
            Map.of(
                    "donate", "https://www.dubsimon.ie/donate",
                    "link", "https://www.dubsimon.ie/",
                    "img", "red",
                    "alt", "red6",
                    "name", "Dublin Simon Community",
                    "overview", "Covering counties Meath, Cavan, Monaghan & Louth. We provide services " +
                            "at all stages of homelessness and enable people to move to a place they can call home. " +
                            "At Simon, we listen to people who turn to us for help and do everything we can to " +
                            "support them to move out of homelessness into independent living. " +
                            "We strive to empower people to access, secure and retain a home of their own by reducing " +
                            "the reliance on short-term emergency accommodation and providing permanent " +
                            "supported housing for people to sustain a home in their local community. Moving " +
                            "people into supported housing produces life-enhancing and life-saving results " +
                            "and is more cost-effective in the long run.",
                    "founded", 1969,
                    "phone", "+353 (01) 671 5551"
            ),
            Map.of(
                    "donate", "https://www.debra.ie/donate/",
                    "link", "https://www.debra.ie/",
                    "img", "red",
                    "alt", "red7",
                    "name", "Debra Ireland",
                    "overview", "We are dedicated to transforming the lives of people living with " +
                            "Epidermolysis Bullosa (EB), caring for someone with EB, or bereaved by EB, " +
                            "through care, research and advocacy. EB is an incurable genetic condition that " +
                            "affects the body's largest organ; the skin. People living with EB are missing the " +
                            "essential proteins that bind the skin's layers together, so any minor friction, " +
                            "movement or trauma causes it to break, tear, and blister. It is as fragile as a " +
                            "butterfly wing. That's why we're here. To be a positive force for all those living " +
                            "with EB and all whose lives have been impacted by EB.",
                    "founded", 1988,
                    "phone", "+353 (0)1 4126924"
            )
    );

    private static final Set<String> allowedCharities = Set.of(
            "Vision Ireland, the new name for NCBI",
            "Temple Street Children’s University Hospital",
            "Irish Cancer Society",
            "The Society of Saint Vincent de Paul",
            "Dublin Simon Community",
            "Debra Ireland",
            "The Jack and Jill Children’s Foundation"
    );

    @BeforeEach
    void setUp() {
        donateItemsResolver = new DonateItemsResolver() {{
            // Inject allowed charity names directly for test, bypassing file
            this.allowedCharityNames.clear();
            this.allowedCharityNames.addAll(allowedCharities);
        }};

        donateItemsResolver.loadDonateItems();
    }

    @Test
    void testGetDonateItems_ValidJson() {
        List<Map<String, Object>> actualItems = donateItemsResolver.getDonateItems();
        for (Map<String, Object> expected : donateItems) {
            assertThat(actualItems, hasItem(equalTo(expected)));
        }
    }

    @Test
    void testAddDonateItem_AddsToMemoryWhenNameInAllowedListAndNotPresent() {
        Map<String, Object> newDonateMap = Map.of(
                "donate", "https://example.org/donate",
                "link", "https://example.org",
                "img", "blue",
                "alt", "blue1",
                "name", "The Jack and Jill Children’s Foundation", // in allowed list, not present yet
                "overview", "An example charity overview.",
                "founded", 2020,
                "phone", "+353 01 000 0000"
        );
        Donate donateObj = Donate.builder()
                .donate((String)newDonateMap.get("donate"))
                .link((String)newDonateMap.get("link"))
                .img((String)newDonateMap.get("img"))
                .alt((String)newDonateMap.get("alt"))
                .name((String)newDonateMap.get("name"))
                .overview((String)newDonateMap.get("overview"))
                .founded((Integer)newDonateMap.get("founded"))
                .phone((String)newDonateMap.get("phone"))
                .build();

        int originalSize = donateItemsResolver.getDonateItems().size();
        donateItemsResolver.addDonateItem(donateObj);

        List<Map<String, Object>> actualItems = donateItemsResolver.getDonateItems();
        assertThat(actualItems.size(), is(originalSize));
        for (Map<String, Object> expected : donateItems) {
            assertThat(actualItems, hasItem(equalTo(expected)));
        }
    }

    @Test
    void testAddDonateItem_DoesNotAddWhenNameNotAllowed() {
        int originalSize = donateItemsResolver.getDonateItems().size();
        Map<String, Object> notAllowedDonateMap = Map.of(
                "donate", "https://www.jackandjill.ie/professionals/ways-to-donate/",
                "link", "https://www.jackandjill.ie",
                "img", "red",
                "alt", "red1",
                "name", "Example Charity without valid name",
                "overview", "The Jack and Jill Children’s Foundation is a nationwide charity that " +
                        "funds and provides in-home nursing care and respite support for children with severe " +
                        "to profound neurodevelopmental delay, up to the age of 6. This may include children " +
                        "with brain injury, genetic diagnosis, cerebral palsy and undiagnosed conditions. " +
                        "Another key part of our service is end-of-life care for all children up to the age of 6, " +
                        "irrespective of diagnosis.",
                "founded", 1997,
                "phone", "+353 (045) 894 538"
        );
        Donate notAllowedDonate = Donate.builder()
                .donate((String)notAllowedDonateMap.get("donate"))
                .link((String)notAllowedDonateMap.get("link"))
                .img((String)notAllowedDonateMap.get("img"))
                .alt((String)notAllowedDonateMap.get("alt"))
                .name((String)notAllowedDonateMap.get("name"))
                .overview((String)notAllowedDonateMap.get("overview"))
                .founded((Integer)notAllowedDonateMap.get("founded"))
                .phone((String)notAllowedDonateMap.get("phone"))
                .build();

        // Try to add (should NOT be added)
        donateItemsResolver.addDonateItem(notAllowedDonate);

        // List should be unchanged and not contain the attempted addition
        assertThat(donateItemsResolver.getDonateItems().size(), is(originalSize));
        assertThat(donateItemsResolver.getDonateItems(), not(hasItem(equalTo(notAllowedDonateMap))));
    }

    @Test
    void testAddDonateItem_DoesNotAddWhenNameAlreadyPresent() {
        int originalSize = donateItemsResolver.getDonateItems().size();
        // Use a name that is already present in the donateItems
        Map<String, Object> duplicateDonateMap = Map.of(
                "donate", "https://another.example.org/donate",
                "link", "https://another.example.org",
                "img", "blue",
                "alt", "blue1",
                "name", "Vision Ireland, the new name for NCBI", // name already present in donateItems
                "overview", "A different overview but same name.",
                "founded", 2023,
                "phone", "+353 01 999 9999"
        );
        Donate duplicateDonate = Donate.builder()
                .donate((String)duplicateDonateMap.get("donate"))
                .link((String)duplicateDonateMap.get("link"))
                .img((String)duplicateDonateMap.get("img"))
                .alt((String)duplicateDonateMap.get("alt"))
                .name((String)duplicateDonateMap.get("name"))
                .overview((String)duplicateDonateMap.get("overview"))
                .founded((Integer)duplicateDonateMap.get("founded"))
                .phone((String)duplicateDonateMap.get("phone"))
                .build();

        // Try to add (should NOT be added)
        donateItemsResolver.addDonateItem(duplicateDonate);

        // List should be unchanged and not contain the attempted addition
        assertThat(donateItemsResolver.getDonateItems().size(), is(originalSize));
        assertThat(donateItemsResolver.getDonateItems(), not(hasItem(equalTo(duplicateDonateMap))));
    }
}
