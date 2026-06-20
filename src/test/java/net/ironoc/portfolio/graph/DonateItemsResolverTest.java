package net.ironoc.portfolio.graph;

import module java.base;

import net.ironoc.portfolio.domain.CharityOption;
import net.ironoc.portfolio.dto.Donate;
import net.ironoc.portfolio.dto.DonateItemOrder;
import net.ironoc.portfolio.enums.SortingOrder;
import net.ironoc.portfolio.repository.CharityOptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonateItemsResolverTest {

    @Mock
    private CharityOptionRepository charityOptionRepository;

    private DonateItemsResolver donateItemsResolver;

    private List<CharityOption> repositoryStore;

    public static List<Map<String, Object>> donateItems = List.of(
            Map.of(
                    "donate", "https://vi.ie/supporting-us/donate-now/",
                    "link", "https://linktr.ee/vision_ireland",
                    "img", "red",
                    "alt", "red2",
                    "name", "Vision Ireland, the new name for NCBI",
                    "overview", "Vision Ireland, the name for NCBI is Ireland\u2019s national charity working " +
                            "for the rising number of people affected by sight loss. Our practical and emotional " +
                            "advice and support help 8,000 people and their families confidently face their " +
                            "futures every year.",
                    "founded", 1931,
                    "phone", "+353 (0)1 830 7033"
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

    @BeforeEach
    void setUp() {
        repositoryStore = new ArrayList<>();

        when(charityOptionRepository.save(any(CharityOption.class))).thenAnswer(inv -> {
            CharityOption entity = inv.getArgument(0);
            repositoryStore.add(entity);
            return entity;
        });

        when(charityOptionRepository.findAll()).thenAnswer(inv -> new ArrayList<>(repositoryStore));

        when(charityOptionRepository.existsByName(anyString())).thenAnswer(inv -> {
            String name = inv.getArgument(0);
            return repositoryStore.stream().anyMatch(e -> name.equals(e.getName()));
        });

        donateItemsResolver = new DonateItemsResolver(charityOptionRepository);
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
    void test_getDonateItemsByOrder_ValidJson_founded_DESC() {
        DonateItemOrder donateItemOrder = new DonateItemOrder(SortingOrder.DESC, null);
        List<Map<String, Object>> actualItems = donateItemsResolver.getDonateItemsByOrder(donateItemOrder);
        assertThat(actualItems.stream().findFirst().get().get("name"), is("The Jack and Jill Children\u2019s Foundation"));
        assertThat(actualItems.stream().skip(actualItems.size()-1).findFirst().get().get("name"), is("The Society of Saint Vincent de Paul"));
        for (Map<String, Object> expected : donateItems) {
            assertThat(actualItems, hasItem(equalTo(expected)));
        }
    }

    @Test
    void test_getDonateItemsByOrder_ValidJson_founded_ASC() {
        DonateItemOrder donateItemOrder = new DonateItemOrder(SortingOrder.ASC, null);
        List<Map<String, Object>> actualItems = donateItemsResolver.getDonateItemsByOrder(donateItemOrder);
        assertThat(actualItems.stream().findFirst().get().get("name"), is("The Society of Saint Vincent de Paul"));
        assertThat(actualItems.stream().skip(actualItems.size()-1).findFirst().get().get("name"), is("The Jack and Jill Children\u2019s Foundation"));
        for (Map<String, Object> expected : donateItems) {
            assertThat(actualItems, hasItem(equalTo(expected)));
        }
    }

    @Test
    void test_getDonateItemsByOrder_ValidJson_charity_name_ASC() {
        DonateItemOrder donateItemOrder = new DonateItemOrder(null, SortingOrder.ASC);
        List<Map<String, Object>> actualItems = donateItemsResolver.getDonateItemsByOrder(donateItemOrder);
        assertThat(actualItems.stream().findFirst().get().get("name"), is("Debra Ireland"));
        assertThat(actualItems.stream().skip(actualItems.size()-1).findFirst().get().get("name"), is("Vision Ireland, the new name for NCBI"));
        for (Map<String, Object> expected : donateItems) {
            assertThat(actualItems, hasItem(equalTo(expected)));
        }
    }

    @Test
    void test_getDonateItemsByOrder_ValidJson_charity_name_DESC() {
        DonateItemOrder donateItemOrder = new DonateItemOrder(null, SortingOrder.DESC);
        List<Map<String, Object>> actualItems = donateItemsResolver.getDonateItemsByOrder(donateItemOrder);
        assertThat(actualItems.stream().findFirst().get().get("name"), is("Vision Ireland, the new name for NCBI"));
        assertThat(actualItems.stream().skip(actualItems.size()-1).findFirst().get().get("name"), is("Debra Ireland"));
        for (Map<String, Object> expected : donateItems) {
            assertThat(actualItems, hasItem(equalTo(expected)));
        }
    }

    @Test
    void testAddDonateItem_DoesNotAddWhenNameAlreadyPresent() {
        int originalSize = donateItemsResolver.getDonateItems().size();
        // Use a name that is already present in the repositoryStore (loaded from JSON)
        Donate duplicateDonate = Donate.builder()
                .donate("https://another.example.org/donate")
                .link("https://another.example.org")
                .img("blue")
                .alt("blue1")
                .name("Vision Ireland, the new name for NCBI") // name already present
                .overview("A different overview but same name.")
                .founded(2023)
                .phone("+353 01 999 9999")
                .build();

        donateItemsResolver.addDonateItem(duplicateDonate);

        assertThat(donateItemsResolver.getDonateItems().size(), is(originalSize));
        assertThat(donateItemsResolver.getDonateItems(), not(hasItem(equalTo(Map.of(
                "donate", "https://another.example.org/donate",
                "link", "https://another.example.org",
                "img", "blue",
                "alt", "blue1",
                "name", "Vision Ireland, the new name for NCBI",
                "overview", "A different overview but same name.",
                "founded", 2023,
                "phone", "+353 01 999 9999"
        )))));
    }

    @Test
    void testAddDonateItem_DoesNotAddWhenNameNotAllowed() {
        int originalSize = donateItemsResolver.getDonateItems().size();
        Donate notAllowedDonate = Donate.builder()
                .donate("https://www.jackandjill.ie/professionals/ways-to-donate/")
                .link("https://www.jackandjill.ie")
                .img("red")
                .alt("red1")
                .name("Example Charity without valid name")
                .overview("The Jack and Jill Children's Foundation is a nationwide charity that " +
                        "funds and provides in-home nursing care and respite support for children with severe " +
                        "to profound neurodevelopmental delay, up to the age of 6. This may include children " +
                        "with brain injury, genetic diagnosis, cerebral palsy and undiagnosed conditions. " +
                        "Another key part of our service is end-of-life care for all children up to the age of 6, " +
                        "irrespective of diagnosis.")
                .founded(1997)
                .phone("+353 (045) 894 538")
                .build();

        donateItemsResolver.addDonateItem(notAllowedDonate);

        assertThat(donateItemsResolver.getDonateItems().size(), is(originalSize));
    }

    @Test
    void testAddDonateItem_AddsToRepositoryWhenValidAndAllowed() {
        int originalSize = donateItemsResolver.getDonateItems().size();
        // "Example Charity" is in charities.txt but NOT in test donate-items.json
        Donate newDonate = Donate.builder()
                .donate("https://example.org/donate")
                .link("https://example.org")
                .img("blue")
                .alt("blue1")
                .name("Example Charity")
                .overview("An example charity overview.")
                .founded(2020)
                .phone("+353 01 000 0000")
                .build();

        donateItemsResolver.addDonateItem(newDonate);

        assertThat(donateItemsResolver.getDonateItems().size(), is(originalSize + 1));
        assertThat(donateItemsResolver.getDonateItems(), hasItem(equalTo(Map.of(
                "donate", "https://example.org/donate",
                "link", "https://example.org",
                "img", "blue",
                "alt", "blue1",
                "name", "Example Charity",
                "overview", "An example charity overview.",
                "founded", 2020,
                "phone", "+353 01 000 0000"
        ))));
    }
}

