package net.ironoc.portfolio.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class VersionControllerTest {

    private static final String TEST_VERSION = "2.2-RELEASE";

    private final VersionController versionController = new VersionController(TEST_VERSION);

    @Test
    public void test_getApplicationVersion_success() {
        // when
        String response = versionController.getApplicationVersion();

        // then
        assertThat(response, is("Version: " + TEST_VERSION));
    }
}
