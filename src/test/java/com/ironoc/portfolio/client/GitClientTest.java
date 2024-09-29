package com.ironoc.portfolio.client;

import com.ironoc.portfolio.aws.SecretManager;
import com.ironoc.portfolio.config.PropertyConfigI;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import com.ironoc.portfolio.utils.UrlUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.emptyIterable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GitClientTest {

    @InjectMocks
    private GitClient gitClient;

    @Mock
    private SecretManager secretManagerMock;

    @Mock
    private PropertyConfigI propertyConfigMock;

    @Mock
    private UrlUtils urlUtilsMock;

    @Mock
    private HttpsURLConnection httpsURLConnectionMock;

    @Mock
    private InputStream inputStreamMock;

    private static final String TEST_URL = "https://unittest.github.com/users/conorheffron/repos";

    @Test
    public void test_callGitHubApi_fail() {
        // when
        Collection<RepositoryDetailDto> result = gitClient
                .callGitHubApi(TEST_URL, TEST_URL, RepositoryDetailDto.class);

        // then
        assertThat(result, is(emptyIterable()));
    }

    @Test
    public void test_readInputStream_fail() throws IOException {
        // when
        InputStream result = gitClient.readInputStream(httpsURLConnectionMock);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void test_readInputStream_success() throws IOException {
        // given
        when(httpsURLConnectionMock.getInputStream()).thenReturn(inputStreamMock);

        // when
        InputStream result = gitClient.readInputStream(httpsURLConnectionMock);

        // then
        verify(httpsURLConnectionMock).getInputStream();

        assertThat(result, is(inputStreamMock));
    }

    @Test
    public void test_close_success() throws IOException {
        // when
        gitClient.closeConn(inputStreamMock);

        // then
        verify(inputStreamMock).close();
    }

    @Test
    public void test_createConn_without_token_success() throws IOException {
        // given
        when(urlUtilsMock.isValidURL(TEST_URL)).thenReturn(true);

        // when
        HttpsURLConnection result = gitClient.createConn(TEST_URL, TEST_URL);

        // then
        verify(urlUtilsMock).isValidURL(TEST_URL);
        verify(propertyConfigMock).getGitFollowRedirects();
        verify(propertyConfigMock).getGitTimeoutConnect();
        verify(propertyConfigMock).getGitTimeoutRead();
        verify(propertyConfigMock).getGitInstanceFollowRedirects();
        verify(secretManagerMock).getGitSecret();

        assertThat(result, notNullValue());
    }

    @Test
    public void test_createConn_with_token_success() throws IOException {
        // given
        when(urlUtilsMock.isValidURL(TEST_URL)).thenReturn(true);
        when(secretManagerMock.getGitSecret()).thenReturn("test_fake_token");

        // when
        HttpsURLConnection result = gitClient.createConn(TEST_URL, TEST_URL);

        // then
        verify(urlUtilsMock).isValidURL(TEST_URL);
        verify(propertyConfigMock).getGitFollowRedirects();
        verify(propertyConfigMock).getGitTimeoutConnect();
        verify(propertyConfigMock).getGitTimeoutRead();
        verify(propertyConfigMock).getGitInstanceFollowRedirects();
        verify(secretManagerMock).getGitSecret();

        assertThat(result, notNullValue());
    }

    @Test
    public void test_createConn_invalid_url_fail() throws IOException {
        // given
        when(urlUtilsMock.isValidURL(TEST_URL)).thenReturn(false);

        // when
        HttpsURLConnection result = gitClient.createConn(TEST_URL, TEST_URL);

        // then
        verify(urlUtilsMock).isValidURL(TEST_URL);
        verify(propertyConfigMock, never()).getGitFollowRedirects();
        verify(propertyConfigMock, never()).getGitTimeoutConnect();
        verify(propertyConfigMock, never()).getGitTimeoutRead();
        verify(propertyConfigMock, never()).getGitInstanceFollowRedirects();
        verify(secretManagerMock, never()).getGitSecret();

        assertThat(result, is(nullValue()));
    }
}
