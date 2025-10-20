package net.ironoc.portfolio.controller;

import module java.base;

import net.ironoc.portfolio.dto.Brew;
import net.ironoc.portfolio.graph.BrewsResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class BrewGraphqlControllerTest {

    @Mock
    private BrewsResolver brewsResolver;

    @InjectMocks
    private BrewGraphqlController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new BrewGraphqlController(brewsResolver);
    }

    @Test
    void testBrews_ReturnsCollectionOfMaps() {
        Map<String, Object> brew1 = new HashMap<>();
        brew1.put("title", "Coffee1");
        Map<String, Object> brew2 = new HashMap<>();
        brew2.put("title", "Coffee2");
        List<Map<String, Object>> brewsList = Arrays.asList(brew1, brew2);

        when(brewsResolver.getBrews()).thenReturn(brewsList);

        Collection<Map<String, Object>> result = controller.brews();
        assertEquals(2, result.size());
        assertTrue(result.contains(brew1));
        assertTrue(result.contains(brew2));
    }

    @Test
    void testBrewsSchemaMapping_ReturnsListOfBrew() {
        Map<String, Object> brewMap = new HashMap<>();
        brewMap.put("title", "Espresso");
        brewMap.put("description", "Strong coffee");
        brewMap.put("ingredients", "Coffee, Water");
        brewMap.put("image", "espresso.jpg");
        brewMap.put("id", "10");
        List<Map<String, Object>> brewsList = Collections.singletonList(brewMap);

        when(brewsResolver.getBrews()).thenReturn(brewsList);

        Collection<Brew> result = controller.brewsSchemaMapping();
        assertEquals(1, result.size());
        Brew brew = result.iterator().next();
        assertEquals("Espresso", brew.getTitle());
        assertEquals("Strong coffee", brew.getDescription());
        assertArrayEquals(new String[] {"Coffee", "Water"}, brew.getIngredients());
        assertEquals("espresso.jpg", brew.getImage());
        assertEquals(10, brew.getId());
    }

    @Test
    void testParseValue_NullKeyReturnsNull() {
        Map<String, Object> d = new HashMap<>();
        d.put("title", null);
        // Use reflection to access private method
        try {
            java.lang.reflect.Method m = BrewGraphqlController.class.getDeclaredMethod("parseValue", Map.class, String.class);
            m.setAccessible(true);
            String result = (String) m.invoke(controller, d, "title");
            assertNull(result);
        } catch (Exception e) {
            fail("Exception during reflection: " + e.getMessage());
        }
    }
}
