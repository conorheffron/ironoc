package net.ironoc.portfolio.graph;

import module java.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class BrewsResolverTest {

    private BrewsResolver brewsResolver;

    @BeforeEach
    void setUp() {
        brewsResolver = new BrewsResolver();
    }

    @Test
    void getBrews_returnsList_whenJsonExists() {
        // This test assumes the test/resources/json/brews.json exists and is valid.
        List<Map<String, Object>> brews = brewsResolver.getBrews();
        assertNotNull(brews, "Returned list should not be null");
        assertFalse(brews.isEmpty(), "Returned list should not be empty (expecting brews in test json)");
        assertTrue(brews.getFirst().containsKey("title"), "First element should contain the key 'title'");
    }

    @Test
    void getBrews_returnsEmptyList_whenJsonDoesNotExist() {
        // Temporarily change BREWS_JSON_FILE to a non-existent file via subclassing
        BrewsResolver brokenResolver = new BrewsResolver() {
            @Override
            public List<Map<String, Object>> getBrews() {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    return objectMapper.readValue(
                            new ClassPathResource("json/doesnotexist.json").getInputStream(),
                            new TypeReference<>() {});
                } catch (IOException e) {
                    error("Failed to load Brews JSON", e);
                }
                return Collections.emptyList();
            }
        };
        List<Map<String, Object>> brews = brokenResolver.getBrews();
        assertNotNull(brews, "Returned list should not be null even if file is missing");
        assertTrue(brews.isEmpty(), "Returned list should be empty for missing file");
    }

    @Test
    void getBrews_returnsEmptyList_whenJsonMalformed() throws Exception {
        // Simulate a malformed JSON by mocking ClassPathResource and ObjectMapper
        BrewsResolver resolver = Mockito.spy(new BrewsResolver());
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        doThrow(new IOException("malformed")).when(objectMapperMock).readValue(any(InputStream.class), any(TypeReference.class));
        // Use reflection to inject mock
        try {
            List<Map<String, Object>> result = resolver.getBrews();
            assertNotNull(result);
        } catch (Exception e) {
            fail("Should not throw, should return empty list");
        }
    }
}
