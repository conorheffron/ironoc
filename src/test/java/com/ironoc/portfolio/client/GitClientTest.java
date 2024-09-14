package com.ironoc.portfolio.client;

import com.ironoc.portfolio.config.PropertyConfigI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GitClientTest {

    @InjectMocks
    private GitClient gitClient;

    @Mock
    private PropertyConfigI propertyConfigMock;

    @Mock
    private HttpsURLConnection httpsURLConnectionMock;

    @Mock
    private InputStream inputStreamMock;

    private static final String TEST_URL = "https://cloud-conor.com";

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
        // when
        HttpsURLConnection result = gitClient.createConn(TEST_URL);

        // then
        verify(propertyConfigMock).getGitToken();

        assertThat(result, notNullValue());
    }

    @Test
    public void test_createConn_with_token_success() throws IOException {
        // given
        when(propertyConfigMock.getGitToken()).thenReturn("test_fake_token");

        // when
        HttpsURLConnection result = gitClient.createConn(TEST_URL);

        // then
        verify(propertyConfigMock).getGitToken();

        assertThat(result, notNullValue());
    }
}
