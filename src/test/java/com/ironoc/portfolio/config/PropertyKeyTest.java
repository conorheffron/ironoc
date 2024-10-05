package com.ironoc.portfolio.config;

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
        assertThat(result, is("com.ironoc.portfolio.github.api.endpoint.issues"));
    }

    @Test
    public void test_getGitApiEndpointRepos_success() {
        // when
        String result = propertyKey.getGitApiEndpointRepos();

        // then
        assertThat(result, is("com.ironoc.portfolio.github.api.endpoint.repos"));
    }

    @Test
    public void test_getGitFollowRedirects_success() {
        // when
        String result = propertyKey.getGitFollowRedirects();

        // then
        assertThat(result, is("com.ironoc.portfolio.github.follow-redirects"));
    }

    @Test
    public void test_getGitTimeoutRead_success() {
        // when
        String result = propertyKey.getGitTimeoutRead();

        // then
        assertThat(result, is("com.ironoc.portfolio.github.timeout.read"));
    }

    @Test
    public void test_getGitTimeoutConnect_success() {
        // when
        String result = propertyKey.getGitTimeoutConnect();

        // then
        assertThat(result, is("com.ironoc.portfolio.github.timeout.connect"));
    }

    @Test
    public void test_getGitInstanceFollowRedirects_success() {
        // when
        String result = propertyKey.getGitInstanceFollowRedirects();

        // then
        assertThat(result, is("com.ironoc.portfolio.github.instance-follow-redirects"));
    }
}
