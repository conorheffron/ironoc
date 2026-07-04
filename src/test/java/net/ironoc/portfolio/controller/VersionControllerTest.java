package net.ironoc.portfolio.controller;

import module java.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.info.BuildProperties;
<<<<<<< HEAD
=======
import org.springframework.core.env.Environment;
>>>>>>> origin/main

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VersionControllerTest {

    private static final String TEST_VERSION = "2.2-RELEASE";

    @Mock
    private BuildProperties buildPropertiesMock;

<<<<<<< HEAD
=======
    @Mock
    private Environment environmentMock;

>>>>>>> origin/main
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
<<<<<<< HEAD
=======

    @Test
    public void test_getOpenApiDocumentationEndpoint_nonProdProfile_success() {
        // given
        when(environmentMock.getActiveProfiles()).thenReturn(new String[]{"dev"});

        // when
        String response = versionController.getOpenApiDocumentationEndpoint();

        // then
        verify(environmentMock).getActiveProfiles();
        assertThat(response, is("/swagger-ui-ironoc.html"));
    }

    @Test
    public void test_getOpenApiDocumentationEndpoint_prodProfile_success() {
        // given
        when(environmentMock.getActiveProfiles()).thenReturn(new String[]{"prod"});

        // when
        String response = versionController.getOpenApiDocumentationEndpoint();

        // then
        verify(environmentMock).getActiveProfiles();
        assertThat(response, is("/api-docs"));
    }
>>>>>>> origin/main
}
