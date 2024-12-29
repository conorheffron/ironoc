package net.ironoc.portfolio.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class PropertyKeyTest {

    @InjectMocks
    private PropertyKey propertyKey;

    @Test
    public void test_getGitApiEndpointIssues_success() {
        // when
        String result = propertyKey.getGitApiEndpointIssues();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.api.endpoint.issues"));
    }

    @Test
    public void test_getGitApiEndpointRepos_success() {
        // when
        String result = propertyKey.getGitApiEndpointRepos();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.api.endpoint.repos"));
    }

    @Test
    public void test_getGitFollowRedirects_success() {
        // when
        String result = propertyKey.getGitFollowRedirects();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.follow-redirects"));
    }

    @Test
    public void test_getGitTimeoutRead_success() {
        // when
        String result = propertyKey.getGitTimeoutRead();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.timeout.read"));
    }

    @Test
    public void test_getGitTimeoutConnect_success() {
        // when
        String result = propertyKey.getGitTimeoutConnect();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.timeout.connect"));
    }

    @Test
    public void test_getGitInstanceFollowRedirects_success() {
        // when
        String result = propertyKey.getGitInstanceFollowRedirects();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.instance-follow-redirects"));
    }

    @Test
    public void test_getStaticConfIgnorePaths_success() {
        // when
        String result = propertyKey.getStaticConfIgnorePaths();

        // then
        assertThat(result, is("net.ironoc.portfolio.config.ignore-paths"));
    }

    @Test
    public void test_getStaticConfHandleExt_success() {
        // when
        String result = propertyKey.getStaticConfHandleExt();

        // then
        assertThat(result, is("net.ironoc.portfolio.config.handle-extensions"));
    }

    @Test
    public void test_getStaticConfResourceHandler_success() {
        // when
        String result = propertyKey.getStaticConfResourceHandler();

        // then
        assertThat(result, is("net.ironoc.portfolio.config.resource-handler"));
    }

    @Test
    public void test_getStaticConfResourceLoc_success() {
        // when
        String result = propertyKey.getStaticConfResourceLoc();

        // then
        assertThat(result, is("net.ironoc.portfolio.config.resource-loc"));
    }

    @Test
    public void test_getGitApiEndpointProjectsCache_success() {
        // when
        String result = propertyKey.getGitApiEndpointProjectsCache();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.api.endpoint.projects-cache"));
    }

    @Test
    public void test_getGitApiEndpointUserIdsCache_success() {
        // when
        String result = propertyKey.getGitApiEndpointUserIdsCache();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.api.endpoint.user-ids-cache"));
    }

    @Test
    public void test_isCacheJobEnabled_success() {
        // when
        String result = propertyKey.isCacheJobEnabled();

        // then
        assertThat(result, is("net.ironoc.portfolio.github.job-enable"));
    }
}
