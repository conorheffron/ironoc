package net.ironoc.portfolio.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class PropertyKeyTest {

    @InjectMocks
    private PropertyKey propertyKey;

    @Test
    void test_getGitApiEndpointIssues_success() {
        // when
        String result = propertyKey.getGitApiEndpointIssues();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.api.endpoint.issues"));
    }

    @Test
    void test_getGitApiEndpointRepos_success() {
        // when
        String result = propertyKey.getGitApiEndpointRepos();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.api.endpoint.repos"));
    }

    @Test
    void test_getGitFollowRedirects_success() {
        // when
        String result = propertyKey.getGitFollowRedirects();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.follow-redirects"));
    }

    @Test
    void test_getGitTimeoutRead_success() {
        // when
        String result = propertyKey.getGitTimeoutRead();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.timeout.read"));
    }

    @Test
    void test_getGitTimeoutConnect_success() {
        // when
        String result = propertyKey.getGitTimeoutConnect();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.timeout.connect"));
    }

    @Test
    void test_getGitInstanceFollowRedirects_success() {
        // when
        String result = propertyKey.getGitInstanceFollowRedirects();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.instance-follow-redirects"));
    }

    @Test
    void test_getStaticConfIgnorePaths_success() {
        // when
        String result = propertyKey.getStaticConfIgnorePaths();

        // then
        assertThat(result, is("net.ironoc.portfolio.config.ignore-paths"));
    }

    @Test
    void test_getStaticConfHandleExt_success() {
        // when
        String result = propertyKey.getStaticConfHandleExt();

        // then
        assertThat(result, is("net.ironoc.portfolio.config.handle-extensions"));
    }

    @Test
    void test_getStaticConfResourceHandler_success() {
        // when
        String result = propertyKey.getStaticConfResourceHandler();

        // then
        assertThat(result, is("net.ironoc.portfolio.config.resource-handler"));
    }

    @Test
    void test_getStaticConfResourceLoc_success() {
        // when
        String result = propertyKey.getStaticConfResourceLoc();

        // then
        assertThat(result, is("net.ironoc.portfolio.config.resource-loc"));
    }

    @Test
    void test_getGitApiEndpointProjectsCache_success() {
        // when
        String result = propertyKey.getGitApiEndpointProjectsCache();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.api.endpoint.projects-cache"));
    }

    @Test
    void test_getGitApiEndpointUserIdsCache_success() {
        // when
        String result = propertyKey.getGitApiEndpointUserIdsCache();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.api.endpoint.user-ids-cache"));
    }

    @Test
    void test_isCacheJobEnabled_success() {
        // when
        String result = propertyKey.isCacheJobEnabled();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.job-enable"));
    }

    @Test
    void test_getBrewGraphEndpoint_success() {
        // when
        String result = propertyKey.getBrewGraphEndpoint();

        // then
        assertThat(result, is("net.ironoc.portfolio.brew.graph.endpoint"));
    }

    @Test
    void test_getBrewApiEndpointHot_success() {
        // when
        String result = propertyKey.getBrewApiEndpointHot();

        // then
        assertThat(result, is("net.ironoc.portfolio.brew.api.endpoint.hot"));
    }

    @Test
    void test_getBrewApiEndpointIce_success() {
        // when
        String result = propertyKey.getBrewApiEndpointIce();

        // then
        assertThat(result, is("net.ironoc.portfolio.brew.api.endpoint.ice"));
    }

    @Test
    void test_isBrewsCacheJobEnabled_success() {
        // when
        String result = propertyKey.isBrewCacheJobEnabled();

        // then
        assertThat(result, is("net.ironoc.portfolio.brew.job-enable"));
    }
}
