package net.ironoc.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.dto.RepositoryIssueDto;
import net.ironoc.portfolio.service.GraphQLClient;
import net.ironoc.portfolio.service.GitDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
public class GitProjectsControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GitDetailsService gitDetailsServiceMock;

    @MockitoBean
    private GraphQLClient graphQLClientServiceMock;

    @MockitoBean
    private VersionController versionControllerMock;

    @MockitoBean
    private DonateGraphqlController donateGraphqlControllerMock;

    @MockitoBean
    private DonateRestController donateRestControllerMock;

    @InjectMocks
    private GitProjectsController gitProjectsController;// controller under test

    private static final String JSON_RESPONSE = "[{\"name\":\"bio-cell-red-edge\"," +
            "\"fullName\":\"conorheffron/bio-cell-red-edge\"," +
            "\"description\":\"Edge Detection of Biological Cell (Image Processing Script)\"," +
            "\"appHome\":\"https://conorheffron.github.io/bio-cell-red-edge/\"," +
            "\"repoUrl\":\"https://github.com/conorheffron/bio-cell-red-edge\"," +
            "\"topics\":\"[biology, computer-vision, image-processing, scikitlearn-machine-learning]\"}," +
            "{\"name\":\"booking-sys\",\"fullName\":\"conorheffron/booking-sys\"," +
            "\"description\":\"Sample Reservations & Bookings Viewer System\"," +
            "\"appHome\":\"https://booking-sys-ebgefrdmh3afbhee.northeurope-01.azurewebsites.net/book/\"," +
            "\"repoUrl\":\"https://github.com/conorheffron/booking-sys\",\"topics\":\"\"" +
            "}]";
    private static final String ISSUES_JSON_RESPONSE = "[{\"number\":\"62\"," +
            "\"title\":\"Re-write frontend with React <POC>\"," +
            "\"body\":\"Use React or Angular framework & JavaScript " +
            "or TypeScript as implementation language? \\r\\n- Research & select best option.\"}," +
            "{\"number\":\"57\"," +
            "\"title\":\"Setup LB, Support SSL\"," +
            "\"body\":\"- [x] 1. Setup LB\\r\\n" +
            "- [ ] 2. Support SSL\\r\\n" +
            "- [ ] 3. Setup domain, map to AWS LB\"}" +
            "]";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void test_getReposByUsernamePathVar_success() throws Exception {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_response.json");
        List<RepositoryDetailDto> dtos = List.of(objectMapper.readValue(jsonInputStream, RepositoryDetailDto[].class));

        when(gitDetailsServiceMock.getRepoDetails("conorheffron", false)).thenReturn(dtos);
        when(gitDetailsServiceMock.mapRepositoriesToResponse(anyList()))
                .thenReturn(new GitDetailsService(null, null,
                        null,null,null)
                        .mapRepositoriesToResponse(dtos));

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/get-repo-detail/conorheffron/")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        verify(gitDetailsServiceMock).getRepoDetails("conorheffron", false);
        verify(gitDetailsServiceMock).mapRepositoriesToResponse(anyList());

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is(JSON_RESPONSE));
    }

    @Test
    public void test_getReposByUsernamePathVar_empty_response_success() throws Exception {
        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/get-repo-detail/test-user/")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("[]"));
    }

    @Test
    public void test_getReposByUsernameReqParam_success() throws Exception {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_response.json");
        List<RepositoryDetailDto> dtos = List.of(objectMapper.readValue(jsonInputStream, RepositoryDetailDto[].class));

        when(gitDetailsServiceMock.getRepoDetails("conorheffron", false)).thenReturn(dtos);
        when(gitDetailsServiceMock.mapRepositoriesToResponse(anyList()))
                .thenReturn(new GitDetailsService(null, null, null,
                        null, null)
                        .mapRepositoriesToResponse(dtos));

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/get-repo-detail?username=conorheffron")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        verify(gitDetailsServiceMock).getRepoDetails("conorheffron", false);
        verify(gitDetailsServiceMock).mapRepositoriesToResponse(anyList());

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is(JSON_RESPONSE));
    }

    @Test
    public void test_getReposByUsernameReqParam_empty_response_success() throws Exception {
        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/get-repo-detail?username=test-user")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("[]"));
    }

    @Test
    public void test_getIssuesByUsernameAndRepoPathVars_empty_response_success() throws Exception {
        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/get-repo-issue/test-user/test-repo/")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is("[]"));
    }

    @Test
    public void test_getIssuesByUsernameAndRepoPathVars_success() throws Exception {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_issues_response.json");
        List<RepositoryIssueDto> dtos = List.of(objectMapper.readValue(jsonInputStream, RepositoryIssueDto[].class));

        when(gitDetailsServiceMock.getIssues("test-user", "test-repo", false)).thenReturn(dtos);
        when(gitDetailsServiceMock.mapIssuesToResponse(anyList()))
                .thenReturn(new GitDetailsService(null, null, null,
                        null, null)
                        .mapIssuesToResponse(dtos));

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/api/get-repo-issue/test-user/test-repo/")
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        verify(gitDetailsServiceMock).getIssues("test-user", "test-repo", false);
        verify(gitDetailsServiceMock).mapIssuesToResponse(anyList());

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is(ISSUES_JSON_RESPONSE));
    }
}
