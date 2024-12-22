package net.ironoc.portfolio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import net.ironoc.portfolio.client.Client;
import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import net.ironoc.portfolio.domain.RepositoryIssueDomain;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.dto.RepositoryIssueDto;
import net.ironoc.portfolio.utils.UrlUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.ironoc.portfolio.service.GitDetailsService.IRONOC_GIT_USER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class GitDetailsServiceTest {

    @InjectMocks
    private GitDetailsService gitDetailsService;

    @Mock
    private PropertyConfigI propertyConfigMock;

    @Mock
    private Client gitClientMock;

    @Mock
    private UrlUtils urlUtilsMock;

    @Mock
    private GitRepoCache gitRepoCacheMock;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEST_URI = "https://unittest.github.com/users/{username}/repos";

    @Test
    public void test_get_repos_success() throws IOException {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_response.json");
        String testUserId = "conorheffron-test";
        String testName = "bio-cell-red-edge";
        String testFullName = "conorheffron/bio-cell-red-edge";
        String testDesc = "Edge Detection of Biological Cell (Image Processing Script)";
        List<String> testTop = List.of("biology", "computer-vision", "image-processing",
                "scikitlearn-machine-learning");
        String testApp = "https://conorheffron.github.io/bio-cell-red-edge/";
        String testHtmlUrl = "https://github.com/conorheffron/bio-cell-red-edge";
        RepositoryDetailDto expected = RepositoryDetailDto.builder()
                .name(testName)
                .fullName(testFullName)
                .description(testDesc)
                .topics(testTop)
                .htmlUrl(testHtmlUrl)
                .homePage(testApp)
                .isPrivate(false)
                .build();

        when(propertyConfigMock.getGitApiEndpointRepos())
                .thenReturn(TEST_URI);
        when(urlUtilsMock.isValidURL(anyString())).thenReturn(true);
        when(propertyConfigMock.getGitApiEndpointRepos()).thenReturn(TEST_URI);
        when(urlUtilsMock.isValidURL(anyString())).thenReturn(true);
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, RepositoryDetailDto.class);
        when(gitClientMock.callGitHubApi(anyString(), anyString(), any(), anyString()))
                .thenReturn(objectMapper.readValue(jsonInputStream, listType));

        // when
        List<RepositoryDetailDto> results = gitDetailsService.getRepoDetails(testUserId);

        // then
        verify(propertyConfigMock).getGitApiEndpointRepos();
        verify(urlUtilsMock).isValidURL(anyString());
        verify(gitClientMock).callGitHubApi(anyString(), anyString(), any(), anyString());

        assertThat(results, is(hasSize(2)));
        Optional<RepositoryDetailDto> result = results.stream().findFirst();
        if (result.isPresent()) {
            assertThat(result.get().getName(), is(expected.getName()));
            assertThat(result.get().getFullName(), is(expected.getFullName()));
            assertThat(result.get().getDescription(), is(expected.getDescription()));
            assertThat(result.get().getTopics(), is(expected.getTopics()));
            assertThat(result.get().getHomePage(), is(expected.getHomePage()));
            assertThat(result.get().getHtmlUrl(), is(expected.getHtmlUrl()));
            assertThat(result.get().isPrivate(), is(expected.isPrivate()));
        }
        RepositoryDetailDto result2 = results.get(1);
        assertThat(result2.getName(), is("booking-sys"));
        assertThat(result2.getFullName(), is("conorheffron/booking-sys"));
        assertThat(result2.getDescription(), is("Sample Reservations & Bookings Viewer System"));
        assertThat(result2.getTopics(), is(nullValue()));
        assertThat(result2.getHomePage(),
                is("https://booking-sys-ebgefrdmh3afbhee.northeurope-01.azurewebsites.net/book/"));
        assertThat(result2.getHtmlUrl(), is("https://github.com/conorheffron/booking-sys"));
        assertThat(result2.isPrivate(), is(Boolean.TRUE));
    }

    @Test
    public void test_get_issues_success() throws IOException {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_issues_response.json");
        String testRepo = "bio-cell-red-edge";
        String testUserId = "conor-h";
        String testBody = "Use React or Angular framework & JavaScript or TypeScript as implementation language? " +
                "\r\n- Research & select best option.";
        String testTitle = "Re-write frontend with React <POC>";
        String testIssueNo = "62";
        RepositoryIssueDto expected = RepositoryIssueDto.builder()
                .number(testIssueNo)
                .title(testTitle)
                .body(testBody)
                .build();

        when(propertyConfigMock.getGitApiEndpointIssues()).thenReturn(TEST_URI);
        when(urlUtilsMock.isValidURL(anyString())).thenReturn(true);
        when(urlUtilsMock.isValidURL(anyString())).thenReturn(true);
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, RepositoryIssueDto.class);
        when(gitClientMock.callGitHubApi(anyString(), anyString(), any(), anyString()))
                .thenReturn(objectMapper.readValue(jsonInputStream, listType));

        // when
        List<RepositoryIssueDto> results = gitDetailsService.getIssues(testUserId, testRepo);

        // then
        verify(propertyConfigMock).getGitApiEndpointIssues();
        verify(urlUtilsMock).isValidURL(anyString());
        verify(gitClientMock).callGitHubApi(anyString(), anyString(), any(), anyString());

        assertThat(results, is(hasSize(2)));
        Optional<RepositoryIssueDto> result = results.stream().findFirst();
        assertThat(result.get().getNumber(), is(expected.getNumber()));
        assertThat(result.get().getTitle(), is(expected.getTitle()));
        assertThat(result.get().getBody(), is(expected.getBody()));
        RepositoryIssueDto result2 = results.get(1);
        assertThat(result2.getNumber(), is("57"));
        assertThat(result2.getTitle(), is("Setup LB, Support SSL"));
        assertThat(result2.getBody(), is("- [x] 1. Setup LB\r\n- [ ] 2. " +
                "Support SSL\r\n- [ ] 3. Setup domain, map to AWS LB"));
    }

    @Test
    public void test_get_repos_parseNull_values_success() throws IOException {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_parse_null_response.json");
        String testUserId = "conorheffron-test";
        String testName = "conorheffron/ironoc-test";
        List<String> testTop = Collections.emptyList();
        RepositoryDetailDto expected = RepositoryDetailDto.builder()
                .name(testName)
                .topics(testTop)
                .isPrivate(false)
                .build();

        when(propertyConfigMock.getGitApiEndpointRepos()).thenReturn(TEST_URI);
        when(urlUtilsMock.isValidURL(anyString())).thenReturn(true);
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, RepositoryDetailDto.class);
        when(gitClientMock.callGitHubApi(anyString(), anyString(), any(), anyString()))
                .thenReturn(objectMapper.readValue(jsonInputStream, listType));

        // when
        List<RepositoryDetailDto> results = gitDetailsService.getRepoDetails(testUserId);

        // then
        verify(propertyConfigMock).getGitApiEndpointRepos();
        verify(urlUtilsMock).isValidURL(anyString());
        verify(gitClientMock).callGitHubApi(anyString(), anyString(), any(), anyString());

        assertThat(results, is(hasSize(1)));
        Optional<RepositoryDetailDto> result = results.stream().findFirst();
        assertThat(result.get().getName(), is(expected.getName()));
        assertThat(result.get().getFullName(), is(expected.getFullName()));
        assertThat(result.get().getDescription(), is(expected.getDescription()));
        assertThat(result.get().getTopics(), is(emptyIterable()));
        assertThat(result.get().getHomePage(), is(expected.getHomePage()));
        assertThat(result.get().getHtmlUrl(), is(expected.getHtmlUrl()));
        assertThat(result.get().isPrivate(), is(expected.isPrivate()));
    }

    @Test
    public void test_getRepoDetails_result_cached_success() {
        // given
        String testUserId = "conorheffron";

        // when
        List<RepositoryDetailDto> results = gitDetailsService.getRepoDetails(testUserId);

        // then
        verify(gitRepoCacheMock).get(IRONOC_GIT_USER);

        assertThat(results, is(notNullValue()));
        assertThat(results, is(hasSize(0)));
    }

    @Test
    public void test_getRepoDetails_url_invalid_fail() {
        // given
        String testUserId = "conorheffron-test-id";
        when(propertyConfigMock.getGitApiEndpointRepos()).thenReturn(TEST_URI);

        // when
        List<RepositoryDetailDto> results = gitDetailsService.getRepoDetails(testUserId);

        // then
        verify(propertyConfigMock).getGitApiEndpointRepos();

        assertThat(results, is(notNullValue()));
        assertThat(results, is(hasSize(0)));
    }

    @Test
    public void test_mapRepositoriesToResponse_success() throws IOException {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_response.json");

        // when
        List<RepositoryDetailDomain> results = gitDetailsService.mapRepositoriesToResponse(
                List.of(objectMapper.readValue(jsonInputStream, RepositoryDetailDto[].class)));

        assertThat(results, is(hasSize(2)));
        Optional<RepositoryDetailDomain> result = results.stream().findFirst();
        assertThat(result.get().getName(), is("bio-cell-red-edge"));
        assertThat(result.get().getFullName(), is("conorheffron/bio-cell-red-edge"));
        assertThat(result.get().getDescription(),
                is("Edge Detection of Biological Cell (Image Processing Script)"));
        assertThat(result.get().getTopics(),
                is("[biology, computer-vision, image-processing, scikitlearn-machine-learning]"));
        assertThat(result.get().getAppHome(),
                is("https://conorheffron.github.io/bio-cell-red-edge/"));
        assertThat(result.get().getRepoUrl(),
                is("https://github.com/conorheffron/bio-cell-red-edge"));
        RepositoryDetailDomain result2 = results.get(1);
        assertThat(result2.getName(), is("booking-sys"));
        assertThat(result2.getFullName(), is("conorheffron/booking-sys"));
        assertThat(result2.getDescription(), is("Sample Reservations & Bookings Viewer System"));
        assertThat(result2.getTopics(), is(emptyString()));
        assertThat(result2.getAppHome(),
                is("https://booking-sys-ebgefrdmh3afbhee.northeurope-01.azurewebsites.net/book/"));
        assertThat(result2.getRepoUrl(), is("https://github.com/conorheffron/booking-sys"));
        jsonInputStream.close();
    }

    @Test
    public void test_mapResponseToRepositories_success() throws IOException {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_repo_detail_response.json");

        // when
        List<RepositoryDetailDto> results = gitDetailsService.mapResponseToRepositories(
                List.of(objectMapper.readValue(jsonInputStream, RepositoryDetailDomain[].class)));

        assertThat(results, is(hasSize(2)));
        Optional<RepositoryDetailDto> result = results.stream().findFirst();
        assertThat(result.get().getName(), is("bio-cell-red-edge"));
        assertThat(result.get().getFullName(), is("conorheffron/bio-cell-red-edge"));
        assertThat(result.get().getDescription(),
                is("Edge Detection of Biological Cell (Image Processing Script)"));
        String testTopics = "[Biology, computer-vision, image-processing, scikitlearn-machine-learning]";
        assertThat(result.get().getTopics(),
                is(List.of(testTopics.substring(1, testTopics.length() - 1)
                        .split(", "))));
        assertThat(result.get().getHomePage(),
                is("https://conorheffron.github.io/bio-cell-red-edge/"));
        assertThat(result.get().getHtmlUrl(),
                is("https://github.com/conorheffron/bio-cell-red-edge"));
        RepositoryDetailDto result2 = results.get(1);
        assertThat(result2.getName(), is("booking-sys"));
        assertThat(result2.getFullName(), is(emptyString()));
        assertThat(result2.getDescription(), is("python3 and django5 web app"));
        assertThat(result2.getTopics(), is(emptyIterable()));
        assertThat(result2.getHomePage(),
                is("https://booking-sys-ebgefrdmh3afbhee.northeurope-01.azurewebsites.net/book/"));
        assertThat(result2.getHtmlUrl(), is("https://github.com/conorheffron/booking-sys"));
        jsonInputStream.close();
    }

    @Test
    public void test_mapRepositoriesToResponse_parseNulls_success() throws IOException {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_parse_null_response.json");

        // when
        List<RepositoryDetailDomain> results = gitDetailsService.mapRepositoriesToResponse(
                List.of(objectMapper.readValue(jsonInputStream, RepositoryDetailDto[].class)));

        // then
        assertThat(results, is(hasSize(1)));
        Optional<RepositoryDetailDomain> result = results.stream().findFirst();
        assertThat(result.get().getName(), is("conorheffron/ironoc-test"));
        assertThat(result.get().getFullName(), is(emptyString()));
        assertThat(result.get().getDescription(), is(emptyString()));
        assertThat(result.get().getTopics(), is("[]"));
        assertThat(result.get().getAppHome(), is(emptyString()));
        assertThat(result.get().getRepoUrl(), is(emptyString()));
        jsonInputStream.close();
    }

    @Test
    public void test_mapIssuesToResponse_success() throws IOException {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_issues_response.json");

        // when
        List<RepositoryIssueDomain> results = gitDetailsService.mapIssuesToResponse(
                List.of(objectMapper.readValue(jsonInputStream, RepositoryIssueDto[].class)));

        // then
        assertThat(results, is(hasSize(2)));
        Optional<RepositoryIssueDomain> result = results.stream().findFirst();
        assertThat(result.get().getNumber(), is("62"));
        assertThat(result.get().getTitle(), is("Re-write frontend with React <POC>"));
        assertThat(result.get().getBody(), is("Use React or Angular framework & JavaScript " +
                "or TypeScript as implementation language? \r\n- Research & select best option."));
        RepositoryIssueDomain result2 = results.get(1);
        assertThat(result2.getNumber(), is("57"));
        assertThat(result2.getTitle(), is("Setup LB, Support SSL"));
        assertThat(result2.getBody(), is("- [x] 1. Setup LB\r\n- [ ] 2. " +
                "Support SSL\r\n- [ ] 3. Setup domain, map to AWS LB"));
        jsonInputStream.close();
    }

    @Test
    public void test_getIssues_url_invalid_fail() {
        // given
        String testUserId = "conorheffron-test-id";
        String testProject = "ironoc-test-project";
        when(propertyConfigMock.getGitApiEndpointIssues()).thenReturn(TEST_URI);

        // when
        List<RepositoryIssueDto> results = gitDetailsService.getIssues(testUserId, testProject);

        // then
        verify(propertyConfigMock).getGitApiEndpointIssues();

        assertThat(results, is(notNullValue()));
        assertThat(results, is(hasSize(0)));
    }
}
