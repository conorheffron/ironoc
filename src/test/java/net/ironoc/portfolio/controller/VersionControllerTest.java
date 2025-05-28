package net.ironoc.portfolio.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.info.BuildProperties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VersionControllerTest {

    private static final String TEST_VERSION = "2.2-RELEASE";

    @Mock
    private BuildProperties buildPropertiesMock;

    @InjectMocks
    private VersionController versionController;

    @Test
    public void test_getApplicationVersion_success() {
        // given
        when(buildPropertiesMock.getVersion()).thenReturn(TEST_VERSION);

        // when
        String response = versionController.getApplicationVersion();

        // then
        verify(buildPropertiesMock).getVersion();

        assertThat(response, is("Version: " + TEST_VERSION));
    }
}
