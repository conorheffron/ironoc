package com.ironoc.portfolio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironoc.portfolio.client.Client;
import com.ironoc.portfolio.config.PropertyConfigI;
import com.ironoc.portfolio.domain.RepositoryDetailDomain;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.emptyString;
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
    private ObjectMapper objectMapperMock;

    @Mock
    private Client gitClient;

    @Mock
    private HttpsURLConnection httpsURLConnectionMock;

    @Mock
    private InputStream inputStreamMock;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test_get_repos_success() throws IOException {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_response.json");
        String testName = "bio-cell-red-edge";
        String testDesc = "Edge Detection of Biological Cell (Image Processing Script)";
        List<String> testTop = Arrays.asList("biology", "computer-vision", "image-processing",
                "scikitlearn-machine-learning");
        String testApp = "https://conorheffron.github.io/bio-cell-red-edge/";
        String testHtmlUrl = "https://github.com/conorheffron/bio-cell-red-edge";
        RepositoryDetailDto expected = RepositoryDetailDto.builder()
                .name(testName)
                .description(testDesc)
                .topics(testTop)
                .htmlUrl(testHtmlUrl)
                .homePage(testApp)
                .isPrivate(false)
                .build();

        when(gitClient.createConn(anyString())).thenReturn(httpsURLConnectionMock);
        when(gitClient.readInputStream(httpsURLConnectionMock)).thenReturn(jsonInputStream);
        when(objectMapperMock.readValue(jsonInputStream, RepositoryDetailDto[].class))
                .thenReturn(objectMapper.readValue(jsonInputStream, RepositoryDetailDto[].class));

        // when
        List<RepositoryDetailDto> results = gitDetailsService.getRepoDetails(testName);

        // then
        verify(propertyConfigMock).getGitApiEndpoint();
        verify(propertyConfigMock).getGitReposUri();
        verify(gitClient).createConn(anyString());
        verify(gitClient).readInputStream(any(HttpsURLConnection.class));
        verify(objectMapperMock).readValue(any(InputStream.class), any(Class.class));
        verify(gitClient).closeConn(any(InputStream.class));
;
        assertThat(results, is(hasSize(2)));
        Optional<RepositoryDetailDto> result = results.stream().findFirst();
        assertThat(result.get().getName(), is(expected.getName()));
        assertThat(result.get().getDescription(), is(expected.getDescription()));
        assertThat(result.get().getTopics(), is(expected.getTopics()));
        assertThat(result.get().getHomePage(), is(expected.getHomePage()));
        assertThat(result.get().getHtmlUrl(), is(expected.getHtmlUrl()));
        assertThat(result.get().isPrivate(), is(expected.isPrivate()));
        RepositoryDetailDto result2 = results.get(1);
        assertThat(result2.getName(), is("booking-sys"));
        assertThat(result2.getDescription(), is("Sample Reservations & Bookings Viewer System"));
        assertThat(result2.getTopics(), is(nullValue()));
        assertThat(result2.getHomePage(),
                is("https://booking-sys-ebgefrdmh3afbhee.northeurope-01.azurewebsites.net/book/"));
        assertThat(result2.getHtmlUrl(), is("https://github.com/conorheffron/booking-sys"));
        assertThat(result2.isPrivate(), is(Boolean.TRUE));
    }

    @Test
    public void test_mapRepositoriesToResponse_success() throws IOException {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_response.json");

        // when
        List<RepositoryDetailDomain> results = gitDetailsService.mapRepositoriesToResponse(
                Arrays.asList(objectMapper.readValue(jsonInputStream, RepositoryDetailDto[].class)));

        assertThat(results, is(hasSize(2)));
        Optional<RepositoryDetailDomain> result = results.stream().findFirst();
        assertThat(result.get().getName(), is("bio-cell-red-edge"));
        assertThat(result.get().getDescription(), is("Edge Detection of Biological Cell (Image Processing Script)"));
        assertThat(result.get().getTopics(), is("[biology, computer-vision, image-processing, scikitlearn-machine-learning]"));
        assertThat(result.get().getAppHome(), is("https://conorheffron.github.io/bio-cell-red-edge/"));
        assertThat(result.get().getRepoUrl(), is("https://github.com/conorheffron/bio-cell-red-edge"));
        RepositoryDetailDomain result2 = results.get(1);
        assertThat(result2.getName(), is("booking-sys"));
        assertThat(result2.getDescription(), is("Sample Reservations & Bookings Viewer System"));
        assertThat(result2.getTopics(), is(emptyString()));
        assertThat(result2.getAppHome(),
                is("https://booking-sys-ebgefrdmh3afbhee.northeurope-01.azurewebsites.net/book/"));
        assertThat(result2.getRepoUrl(), is("https://github.com/conorheffron/booking-sys"));

        jsonInputStream.close();
    }
}
