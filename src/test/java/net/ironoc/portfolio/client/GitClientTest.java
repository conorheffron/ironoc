package net.ironoc.portfolio.client;

import module java.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ironoc.portfolio.aws.SecretManager;
import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.utils.UrlUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GitClientTest {

    private GitClient gitClient;

    @Mock
    private SecretManager secretManagerMock;

    @Mock
    private PropertyConfigI propertyConfigMock;

    @Mock
    private UrlUtils urlUtilsMock;

    @Mock
    private RestTemplateBuilder restTemplateBuilderMock;

    @Mock
    private RestTemplate restTemplateMock;

    private static final String TEST_URL = "https://unittest.github.com/users/conorheffron/repos";

    @BeforeEach
    void setUp() {
        when(restTemplateBuilderMock.connectTimeout(any())).thenReturn(restTemplateBuilderMock);
        when(restTemplateBuilderMock.readTimeout(any())).thenReturn(restTemplateBuilderMock);
        when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
        when(propertyConfigMock.getGitTimeoutConnect()).thenReturn(5000);
        when(propertyConfigMock.getGitTimeoutRead()).thenReturn(5000);
        gitClient = new GitClient(restTemplateBuilderMock, propertyConfigMock,
                secretManagerMock, urlUtilsMock, new ObjectMapper());
    }

    @Test
    public void test_callGitHubApi_success_with_token() {
        // given
        String responseBody = """
                [{
                  "id": 123,
                  "name": "ironoc",
                  "full_name": "conorheffron/ironoc"
                }]""";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", "<https://api.github.com?page=2>; rel=\"next\"");
        when(urlUtilsMock.isValidURL(TEST_URL)).thenReturn(true);
        when(secretManagerMock.getGitSecret()).thenReturn("Bearer test_fake_token");
        when(restTemplateMock.exchange(eq(URI.create(TEST_URL)), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(responseBody, headers, HttpStatusCode.valueOf(200)));

        // when
        List<RepositoryDetailDto> result = gitClient
                .callGitHubApi(TEST_URL, RepositoryDetailDto.class, HttpMethod.GET.name());

        // then
        ArgumentCaptor<HttpEntity<Void>> requestCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplateMock).exchange(eq(URI.create(TEST_URL)), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));
        verify(urlUtilsMock).isValidURL(TEST_URL);
        verify(secretManagerMock).getGitSecret();
        assertThat(result, is(notNullValue()));
        assertThat(result.get(0).getName(), is("ironoc"));
        assertThat(requestCaptor.getValue().getHeaders().getFirst("Authorization"), is("Bearer test_fake_token"));
    }

    @Test
    public void test_callGitHubApi_invalid_url_fail() {
        // given
        when(urlUtilsMock.isValidURL(TEST_URL)).thenReturn(false);

        // when
        Collection<RepositoryDetailDto> result = gitClient
                .callGitHubApi(TEST_URL, RepositoryDetailDto.class, HttpMethod.GET.name());

        // then
        verify(urlUtilsMock).isValidURL(TEST_URL);
        verify(secretManagerMock, never()).getGitSecret();
        verify(restTemplateMock, never()).exchange(eq(URI.create(TEST_URL)), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
        assertThat(result, is(emptyIterable()));
    }

    @Test
    public void test_callGitHubApi_blank_response_body_fail() {
        // given
        when(urlUtilsMock.isValidURL(TEST_URL)).thenReturn(true);
        when(restTemplateMock.exchange(eq(URI.create(TEST_URL)), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("", new HttpHeaders(), HttpStatusCode.valueOf(200)));

        // when
        List<RepositoryDetailDto> result = gitClient
                .callGitHubApi(TEST_URL, RepositoryDetailDto.class, HttpMethod.GET.name());

        // then
        assertThat(result, is(emptyIterable()));
    }
}
