package com.ironoc.portfolio.config;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PushStateResourceResolverTest {

    private PushStateResourceResolver pushStateResourceResolver;// under test

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private ResourceResolverChain resourceResolverChainMock;

    @Mock
    private Resource resourceMock;

    @Mock
    private URL urlMock;

    // test variables
    List<String> ignorePaths = List.of("api");
    List<String> handledExtensions = List.of("html", "js");
    private static final String TEST_URL = "ironoc.net/about";
    private static final String INDEX_HTML_CONTENT =
            "<!doctype html>" +
            "<html lang=\"en\">" +
            "<head>" +
                "<meta charset=\"utf-8\"/>" +
                "<link rel=\"icon\" href=\"/favicon.ico\"/><meta name=\"viewport\" " +
                "content=\"width=device-width,initial-scale=1\"/><meta name=\"theme-color\" " +
                "content=\"#000000\"/>" +
                "<meta name=\"Personal Portfolio for Conor Heffron\" " +
                "content=\"Web Application Development | Software Engineering | Data Engineering | Cloud Deployments " +
                "| DevOps\"/><link rel=\"apple-touch-icon\" href=\"/robot-logo.png\"/>" +
                "<link rel=\"manifest\" " +
                "href=\"/manifest.json\"/>" +
                "<title>iRonoc React App | Portfolio | Software Engineer | DevOps " +
                "| Data Analytics | Conor Heffron</title><script defer=\"defer\" src=\"/static/js/main.6f0b1ec4.js\">" +
                "</script><link href=\"/static/css/main.903d2f06.css\" rel=\"stylesheet\">" +
            "</head>" +
            "<body>" +
                "<noscript>You need to enable JavaScript to run this app.</noscript>" +
                "<div id=\"root\">" +
                "</div>" +
            "</body>" +
            "</html>";

    @BeforeEach
    public void setUp() {
        this.pushStateResourceResolver = new PushStateResourceResolver(handledExtensions, ignorePaths);
    }

    @Test
    public void test_resolveResource_success() throws IOException {
        // when
        Resource result = pushStateResourceResolver.resolveResource(httpServletRequestMock,
                "/", Collections.emptyList(),
                resourceResolverChainMock);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getFilename(), is("index.html"));
        assertThat(result.getDescription(), is("class path resource [static/index.html]"));
    }

    @Test
    public void test_resolveResource_home_success() throws IOException {
        // given
        when(resourceMock.createRelative(anyString())).thenReturn(resourceMock);
        when(resourceMock.exists()).thenReturn(true);

        // when
        Resource result = pushStateResourceResolver.resolveResource(httpServletRequestMock,
                "Home.js", List.of(resourceMock), resourceResolverChainMock);

        // then
        verify(resourceMock).createRelative(anyString());
        verify(resourceMock).exists();

        assertThat(result, is(resourceMock));
    }

    @Test
    public void test_resolveResource_ignoredPath_api_success() {
        // when
        Resource result = pushStateResourceResolver.resolveResource(httpServletRequestMock,
                "api", Collections.emptyList(),
                resourceResolverChainMock);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void test_resolveUrlPath_about_success() throws IOException {
        // given
        when(resourceMock.createRelative(anyString())).thenReturn(resourceMock);
        when(resourceMock.exists()).thenReturn(true);
        when(resourceMock.getURL()).thenReturn(urlMock);
        when(urlMock.toString()).thenReturn(TEST_URL);

        // when
        String result = pushStateResourceResolver.resolveUrlPath("About.js",
                List.of(resourceMock), resourceResolverChainMock);

        // then
        verify(resourceMock).createRelative(anyString());
        verify(resourceMock).exists();
        verify(resourceMock).getURL();

        assertThat(result, is(TEST_URL));
    }

    @Test
    public void test_resolveUrlPath_api_ignoredPath_success() {
        // when
        String result = pushStateResourceResolver.resolveUrlPath("api",
                List.of(resourceMock), resourceResolverChainMock);

        // then
        assertThat(result, is(nullValue()));
    }
}
