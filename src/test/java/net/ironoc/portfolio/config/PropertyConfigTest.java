package net.ironoc.portfolio.config;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PropertyConfigTest {

    @InjectMocks
    private PropertyConfig propertyConfig;

    @Mock
    private Environment environmentMock;

    @Mock
    private PropertyKeyI propertyKeyMock;

    private static final String TEST_PROP_VAL = "test_val";
    private static final String TEST_PROPS_VAL = "htnl,css,js";

    @Test
    public void test_getGitApiEndpoint_Repos_success() {
        // given
        when(propertyKeyMock.getGitApiEndpointRepos()).thenReturn(Properties.GIT_API_ENDPOINT_REPOS.getKey());
        when(environmentMock.getRequiredProperty(Properties.GIT_API_ENDPOINT_REPOS.getKey())).thenReturn(TEST_PROP_VAL);

        // when
        String result = propertyConfig.getGitApiEndpointRepos();

        // then
        verify(propertyKeyMock).getGitApiEndpointRepos();
        verify(environmentMock).getRequiredProperty(Properties.GIT_API_ENDPOINT_REPOS.getKey());

        assertThat(result, is(TEST_PROP_VAL));
    }

    @Test
    public void test_getGitApiEndpointIssues_success() {
        // given
        when(propertyKeyMock.getGitApiEndpointIssues()).thenReturn(Properties.GIT_API_ENDPOINT_ISSUES.getKey());
        when(environmentMock.getRequiredProperty(Properties.GIT_API_ENDPOINT_ISSUES.getKey())).thenReturn(TEST_PROP_VAL);

        // when
        String result = propertyConfig.getGitApiEndpointIssues();

        // then
        verify(propertyKeyMock).getGitApiEndpointIssues();
        verify(environmentMock).getRequiredProperty(Properties.GIT_API_ENDPOINT_ISSUES.getKey());

        assertThat(result, is(TEST_PROP_VAL));
    }

    @Test
    public void test_getGitFollowRedirects_success() {
        // given
        when(propertyKeyMock.getGitFollowRedirects()).thenReturn(Properties.GIT_FOLLOW_REDIRECTS.getKey());
        when(environmentMock.getRequiredProperty(Properties.GIT_FOLLOW_REDIRECTS.getKey())).thenReturn("true");

        // when
        Boolean result = propertyConfig.getGitFollowRedirects();

        // then
        verify(propertyKeyMock).getGitFollowRedirects();
        verify(environmentMock).getRequiredProperty(Properties.GIT_FOLLOW_REDIRECTS.getKey());

        assertThat(result, is(Boolean.TRUE));
    }

    @Test
    public void test_getGitTimeoutRead_success() {
        // given
        when(propertyKeyMock.getGitTimeoutRead()).thenReturn(Properties.GIT_TIMEOUT_READ.getKey());
        when(environmentMock.getRequiredProperty(Properties.GIT_TIMEOUT_READ.getKey())).thenReturn("500");

        // when
        Integer result = propertyConfig.getGitTimeoutRead();

        // then
        verify(propertyKeyMock).getGitTimeoutRead();
        verify(environmentMock).getRequiredProperty(Properties.GIT_TIMEOUT_READ.getKey());

        assertThat(result, is(500));
    }
    @Test
    public void test_getGitTimeoutConnect_success() {
        // given
        when(propertyKeyMock.getGitTimeoutConnect()).thenReturn(Properties.GIT_TIMEOUT_CONNECT.getKey());
        when(environmentMock.getRequiredProperty(Properties.GIT_TIMEOUT_CONNECT.getKey())).thenReturn("631");

        // when
        Integer result = propertyConfig.getGitTimeoutConnect();

        // then
        verify(propertyKeyMock).getGitTimeoutConnect();
        verify(environmentMock).getRequiredProperty(Properties.GIT_TIMEOUT_CONNECT.getKey());

        assertThat(result, is(631));
    }

    @Test
    public void test_getGitInstanceFollowRedirects_success() {
        // given
        when(propertyKeyMock.getGitInstanceFollowRedirects()).thenReturn(Properties.GIT_INSTANCE_FOLLOW_REDIRECTS.getKey());
        when(environmentMock.getRequiredProperty(Properties.GIT_INSTANCE_FOLLOW_REDIRECTS.getKey())).thenReturn("false");

        // when
        Boolean result = propertyConfig.getGitInstanceFollowRedirects();

        // then
        verify(propertyKeyMock).getGitInstanceFollowRedirects();
        verify(environmentMock).getRequiredProperty(Properties.GIT_INSTANCE_FOLLOW_REDIRECTS.getKey());

        assertThat(result, is(Boolean.FALSE));
    }

    @Test
    public void test_getStaticConfIgnorePaths_success() {
        // given
        when(propertyKeyMock.getStaticConfIgnorePaths()).thenReturn(Properties.STATIC_CONF_IGNORE_PATHS.getKey());
        when(environmentMock.getRequiredProperty(Properties.STATIC_CONF_IGNORE_PATHS.getKey())).thenReturn(TEST_PROP_VAL);

        // when
        String result = propertyConfig.getStaticConfIgnorePaths();

        // then
        verify(propertyKeyMock).getStaticConfIgnorePaths();
        verify(environmentMock).getRequiredProperty(Properties.STATIC_CONF_IGNORE_PATHS.getKey());

        assertThat(result, is(TEST_PROP_VAL));
    }

    @Test
    public void test_getStaticConfHandleExt_success() {
        // given
        when(propertyKeyMock.getStaticConfHandleExt()).thenReturn(Properties.STATIC_CONF_HANDLE_EXT.getKey());
        when(environmentMock.getRequiredProperty(Properties.STATIC_CONF_HANDLE_EXT.getKey())).thenReturn(TEST_PROPS_VAL);

        // when
        List<String> result = propertyConfig.getStaticConfHandleExt();

        // then
        verify(propertyKeyMock).getStaticConfHandleExt();
        verify(environmentMock).getRequiredProperty(Properties.STATIC_CONF_HANDLE_EXT.getKey());

        assertThat(result, is(Arrays.stream(StringUtils.split(TEST_PROPS_VAL, ",")).toList()));
    }

    @Test
    public void test_getStaticConfResourceHandler_success() {
        // given
        when(propertyKeyMock.getStaticConfResourceHandler()).thenReturn(Properties.STATIC_CONF_RESOURCE_HANDLER.getKey());
        when(environmentMock.getRequiredProperty(Properties.STATIC_CONF_RESOURCE_HANDLER.getKey())).thenReturn(TEST_PROP_VAL);

        // when
        String result = propertyConfig.getStaticConfResourceHandler();

        // then
        verify(propertyKeyMock).getStaticConfResourceHandler();
        verify(environmentMock).getRequiredProperty(Properties.STATIC_CONF_RESOURCE_HANDLER.getKey());

        assertThat(result, is(TEST_PROP_VAL));
    }

    @Test
    public void test_getStaticConfResourceLoc_success() {
        // given
        when(propertyKeyMock.getStaticConfResourceLoc()).thenReturn(Properties.STATIC_CONF_RESOURCE_LOC.getKey());
        when(environmentMock.getRequiredProperty(Properties.STATIC_CONF_RESOURCE_LOC.getKey())).thenReturn(TEST_PROP_VAL);

        // when
        String result = propertyConfig.getStaticConfResourceLoc();

        // then
        verify(propertyKeyMock).getStaticConfResourceLoc();
        verify(environmentMock).getRequiredProperty(Properties.STATIC_CONF_RESOURCE_LOC.getKey());

        assertThat(result, is(TEST_PROP_VAL));
    }

    @Test
    public void test_getGitApiEndpointUserIdsCache_success() {
        // given
        when(propertyKeyMock.getGitApiEndpointUserIdsCache()).thenReturn(Properties.GIT_API_ENDPOINT_REPOS_PARAM_CACHE.getKey());
        when(environmentMock.getRequiredProperty(Properties.GIT_API_ENDPOINT_REPOS_PARAM_CACHE.getKey())).thenReturn(TEST_PROPS_VAL);

        // when
        List<String> result = propertyConfig.getGitApiEndpointUserIdsCache();

        // then
        verify(propertyKeyMock).getGitApiEndpointUserIdsCache();
        verify(environmentMock).getRequiredProperty(Properties.GIT_API_ENDPOINT_REPOS_PARAM_CACHE.getKey());

        assertThat(result, is(Arrays.stream(StringUtils.split(TEST_PROPS_VAL, ",")).toList()));
    }

    @Test
    public void test_getGitApiEndpointProjectsCache_success() {
        // given
        when(propertyKeyMock.getGitApiEndpointProjectsCache()).thenReturn(Properties.GIT_API_ENDPOINT_ISSUES_PARAM_CACHE.getKey());
        when(environmentMock.getRequiredProperty(Properties.GIT_API_ENDPOINT_ISSUES_PARAM_CACHE.getKey())).thenReturn(TEST_PROPS_VAL);

        // when
        List<String> result = propertyConfig.getGitApiEndpointProjectsCache();

        // then
        verify(propertyKeyMock).getGitApiEndpointProjectsCache();
        verify(environmentMock).getRequiredProperty(Properties.GIT_API_ENDPOINT_ISSUES_PARAM_CACHE.getKey());

        assertThat(result, is(Arrays.stream(StringUtils.split(TEST_PROPS_VAL, ",")).toList()));
    }

    @Test
    public void test_isCacheJobEnabled_success() {
        // given
        when(propertyKeyMock.isCacheJobEnabled()).thenReturn(Properties.IS_GITHUB_JOB_ENABLED.getKey());
        when(environmentMock.getRequiredProperty(Properties.IS_GITHUB_JOB_ENABLED.getKey())).thenReturn("true");

        // when
        boolean result = propertyConfig.isCacheJobEnabled();

        // then
        verify(propertyKeyMock).isCacheJobEnabled();
        verify(environmentMock).getRequiredProperty(Properties.IS_GITHUB_JOB_ENABLED.getKey());

        assertThat(result, is(Boolean.TRUE.booleanValue()));
    }
}
