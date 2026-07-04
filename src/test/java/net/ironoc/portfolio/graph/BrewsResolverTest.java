package net.ironoc.portfolio.graph;

import module java.base;

import net.ironoc.portfolio.exception.IronocJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BrewsResolverTest {

    private BrewsResolver brewsResolver;

    @BeforeEach
    void setUp() {
        brewsResolver = new BrewsResolver();
    }

    @Test
    void getBrews_returnsList_whenJsonExists() {
        List<Map<String, Object>> brews = brewsResolver.getBrews();
        assertNotNull(brews, "Returned list should not be null");
        assertFalse(brews.isEmpty(), "Returned list should not be empty (expecting brews in test json)");
        assertTrue(brews.getFirst().containsKey("title"), "First element should contain the key 'title'");
    }

    @Test
    void getBrews_throwsException_whenJsonDoesNotExist() {
        BrewsResolver brokenResolver = new BrewsResolver() {
            @Override
            protected InputStream getBrewsInputStream() throws IOException {
                throw new IOException("missing");
            }
        };

        IronocJsonException exception = assertThrows(IronocJsonException.class, brokenResolver::getBrews);

        assertThat(exception.getMessage(), is("Failed to load brews JSON"));
    }

    @Test
    void getBrews_throwsException_whenJsonMalformed() {
        BrewsResolver malformedResolver = new BrewsResolver() {
            @Override
            protected InputStream getBrewsInputStream() {
                return new ByteArrayInputStream("{invalid-json}".getBytes(StandardCharsets.UTF_8));
            }
        };

        IronocJsonException exception = assertThrows(IronocJsonException.class, malformedResolver::getBrews);

        assertThat(exception.getMessage(), is("Failed to load brews JSON"));
    }
}
