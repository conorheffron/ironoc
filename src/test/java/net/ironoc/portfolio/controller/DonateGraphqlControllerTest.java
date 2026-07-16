package net.ironoc.portfolio.controller;

import module java.base;

import net.ironoc.portfolio.dto.Donate;
import net.ironoc.portfolio.dto.DonateItemOrder;
import net.ironoc.portfolio.enums.SortingOrder;
import net.ironoc.portfolio.graph.DonateItemsResolver;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.Disposable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class DonateGraphqlControllerTest {

    @Mock
    private DonateItemsResolver donateItemsResolver;

    @InjectMocks
    private DonateGraphqlController donateGraphqlController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDonateItems() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("name", "Charity A");
        item.put("founded", 2000);
        mockDonateItems.add(item);
        when(donateItemsResolver.getDonateItems()).thenReturn(mockDonateItems);

        // Act
        Collection<Map<String, Object>> result = donateGraphqlController.donateItems(null);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(1));
        assertThat(result.iterator().next(), hasEntry("name", "Charity A"));
        verify(donateItemsResolver, times(1)).getDonateItems();
    }

    @Test
    void test_getDonateItemsByOrder_founded_desc() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = getMockDonateItems();
        DonateItemOrder donateItemOrder = new DonateItemOrder();
        donateItemOrder.setFounded(SortingOrder.DESC);
        when(donateItemsResolver.getDonateItemsByOrder(donateItemOrder)).thenReturn(mockDonateItems);

        // Act
        Collection<Map<String, Object>> result = donateGraphqlController.donateItems(donateItemOrder);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(3));
        assertThat(result.iterator().next(), hasEntry("name", "Charity A"));
        verify(donateItemsResolver, never()).getDonateItems();
        verify(donateItemsResolver).getDonateItemsByOrder(any(DonateItemOrder.class));
    }

    @Test
    void testDonateItemsSchemaMapping() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("alt", "Alt Text");
        item.put("name", "Charity B");
        item.put("link", "https://example.com");
        item.put("donate", "https://donate.example.com");
        item.put("img", "image.png");
        item.put("overview", "An overview");
        item.put("founded", 1999);
        item.put("phone", "+123456789");
        mockDonateItems.add(item);
        when(donateItemsResolver.getDonateItems()).thenReturn(mockDonateItems);

        // Act
        Collection<Donate> result = donateGraphqlController.donateItemsSchemaMapping();

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(1));
        Donate donate = result.iterator().next();
        assertThat(donate.getName(), is("Charity B"));
        assertThat(donate.getFounded(), is(1999));
        verify(donateItemsResolver, never()).getDonateItemsByOrder(any(DonateItemOrder.class));
        verify(donateItemsResolver, times(1)).getDonateItems();
    }

    @Test
    void testCharityOptionByFounded() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("name", "Charity C");
        item.put("founded", 2005);
        mockDonateItems.add(item);
        when(donateItemsResolver.getDonateItems()).thenReturn(mockDonateItems);

        // Act
        Donate result = donateGraphqlController.charityOptionByFounded(2005);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is("Charity C"));
        assertThat(result.getFounded(), is(2005));
        verify(donateItemsResolver, times(1)).getDonateItems();
    }

    @Test
    void testCharityOptionByDonateLink() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("name", "Charity D");
        item.put("donate", "https://donate.charityd.com");
        item.put("founded", 1987);
        mockDonateItems.add(item);
        when(donateItemsResolver.getDonateItems()).thenReturn(mockDonateItems);

        // Act
        Donate result = donateGraphqlController.charityOptionByDonateLink("https://donate.charityd.com");

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is("Charity D"));
        assertThat(result.getDonate(), is("https://donate.charityd.com"));
        verify(donateItemsResolver, times(1)).getDonateItems();
    }

    @Test
    void testCharityOptionByName() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = getMockDonateItems();
        when(donateItemsResolver.getDonateItems()).thenReturn(mockDonateItems);

        // Act
        Donate result = donateGraphqlController.charityOptionByName("Charity B");

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is("Charity B"));
        assertThat(result.getFounded(), is(1));
        verify(donateItemsResolver, times(1)).getDonateItems();
    }

    @Test
    void testCharityOptions() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("name", "Charity E");
        item.put("founded", 2010);
        mockDonateItems.add(item);
        when(donateItemsResolver.getDonateItems()).thenReturn(mockDonateItems);

        Donate donateArg = Donate.builder()
                .name("Charity E")
                .founded(2010)
                .build();

        // Act
        List<Donate> result = donateGraphqlController.charityOptions(donateArg);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(1));
        Donate donate = result.getFirst();
        assertThat(donate.getName(), is("Charity E"));
        assertThat(donate.getFounded(), is(2010));
        verify(donateItemsResolver, times(1)).getDonateItems();
    }

    @Test
    void testDonateItemsSubscription_ReturnsExistingItems() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("alt", "Alt Text");
        item.put("name", "Charity Sub");
        item.put("link", "https://charity-sub.org");
        item.put("donate", "https://charity-sub.org/donate");
        item.put("img", "sub.png");
        item.put("overview", "Subscription item");
        item.put("founded", 2001);
        item.put("phone", "+353111111111");
        mockDonateItems.add(item);
        when(donateItemsResolver.getDonateItems()).thenReturn(mockDonateItems);

        // Act
        List<Donate> result = donateGraphqlController.donateItemsSubscription()
                .take(1)
                .collectList()
                .block(java.time.Duration.ofSeconds(1));

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(1));
        assertThat(result.getFirst().getName(), is("Charity Sub"));
        verify(donateItemsResolver, times(1)).getDonateItems();
    }

    @Test
    void testAddCharityOption_EmitsToSubscriptionWhenAdded() {
        // Arrange
        when(donateItemsResolver.getDonateItems()).thenReturn(new ArrayList<>());
        when(donateItemsResolver.addDonateItem(any(Donate.class))).thenReturn(true);

        List<Donate> emittedItems = new ArrayList<>();
        Disposable subscription = donateGraphqlController.donateItemsSubscription().subscribe(emittedItems::add);

        // Act
        Donate result = donateGraphqlController.addCharityOption(
                "Alt",
                "Charity New",
                "https://charity-new.org",
                "https://charity-new.org/donate",
                "new.png",
                "New charity",
                2022,
                "+353123456789"
        );

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is("Charity New"));
        assertThat(emittedItems, hasSize(1));
        assertThat(emittedItems.getFirst().getName(), is("Charity New"));
        verify(donateItemsResolver, times(1)).addDonateItem(any(Donate.class));

        subscription.dispose();
    }

    @Test
    void testAddCharityOption_DoesNotEmitToSubscriptionWhenNotAdded() {
        // Arrange
        when(donateItemsResolver.getDonateItems()).thenReturn(new ArrayList<>());
        when(donateItemsResolver.addDonateItem(any(Donate.class))).thenReturn(false);

        List<Donate> emittedItems = new ArrayList<>();
        Disposable subscription = donateGraphqlController.donateItemsSubscription().subscribe(emittedItems::add);

        // Act & Assert
        assertThrows(graphql.GraphQLException.class, () -> donateGraphqlController.addCharityOption(
                "Alt",
                "Charity Reject",
                "https://charity-reject.org",
                "https://charity-reject.org/donate",
                "reject.png",
                "Rejected charity",
                2024,
                "+353987654321"
        ));

        // Assert
        assertThat(emittedItems, hasSize(0));
        verify(donateItemsResolver, times(1)).addDonateItem(any(Donate.class));

        subscription.dispose();
    }

    @Test
    void testCharityOptionByFounded_NotFound() {
        // Arrange
        when(donateItemsResolver.getDonateItems()).thenReturn(new ArrayList<>());

        // Act
        Donate result = donateGraphqlController.charityOptionByFounded(2005);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(org.hamcrest.Matchers.nullValue()));
    }

    @Test
    void testCharityOptionByDonateLink_NotFound() {
        // Arrange
        when(donateItemsResolver.getDonateItems()).thenReturn(new ArrayList<>());

        // Act
        Donate result = donateGraphqlController.charityOptionByDonateLink("https://donate.notfound.com");

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(org.hamcrest.Matchers.nullValue()));
    }

    @Test
    void testCharityOptionByName_NotFound() {
        // Arrange
        when(donateItemsResolver.getDonateItems()).thenReturn(new ArrayList<>());

        // Act
        Donate result = donateGraphqlController.charityOptionByName("NotFound");

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(org.hamcrest.Matchers.nullValue()));
    }

    @Test
    void test_mapDonateItemsToCharityOptions_withNullAndEmptyValues() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("alt", null);
        item.put("name", "Charity Nulls");
        item.put("link", "https://example.com");
        item.put("donate", "https://donate.example.com");
        item.put("img", "image.png");
        item.put("overview", "An overview");
        item.put("founded", 2000);
        item.put("phone", null);
        mockDonateItems.add(item);
        when(donateItemsResolver.getDonateItems()).thenReturn(mockDonateItems);

        // Act
        Collection<Donate> result = donateGraphqlController.donateItemsSchemaMapping();

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(1));
        Donate donate = result.iterator().next();
        assertThat(donate.getAlt(), is(org.hamcrest.Matchers.nullValue()));
        assertThat(donate.getPhone(), is(org.hamcrest.Matchers.nullValue()));
    }

    @Test
    void test_mapDonateItemsToCharityOptions_withNullItemInList() {
        // Arrange
        List<Map<String, Object>> mockDonateItems = new ArrayList<>();
        mockDonateItems.add(null);
        when(donateItemsResolver.getDonateItems()).thenReturn(mockDonateItems);

        // Act
        Collection<Donate> result = donateGraphqlController.donateItemsSchemaMapping();

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(0));
    }

    @Test
    void testAddCharityOption_NoSubscribers() {
        // Arrange
        when(donateItemsResolver.addDonateItem(any(Donate.class))).thenReturn(true);

        // Act
        Donate result = donateGraphqlController.addCharityOption(
                "Alt",
                "Charity No Sub",
                "https://charity-nosub.org",
                "https://charity-nosub.org/donate",
                "nosub.png",
                "No subscribers",
                2023,
                "+353123456789"
        );

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is("Charity No Sub"));
        verify(donateItemsResolver, times(1)).addDonateItem(any(Donate.class));
    }

    @NotNull
    private static List<Map<String, Object>> getMockDonateItems() {
        List<Map<String, Object>> mockDonateItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("name", "Charity A");
        item.put("founded", 6);
        mockDonateItems.add(item);
        Map<String, Object> item2 = new HashMap<>();
        item2.put("name", "Charity B");
        item2.put("founded", 1);
        mockDonateItems.add(item2);
        Map<String, Object> item3 = new HashMap<>();
        item3.put("name", "Charity C");
        item3.put("founded", 3);
        mockDonateItems.add(item3);
        return mockDonateItems;
    }
}
