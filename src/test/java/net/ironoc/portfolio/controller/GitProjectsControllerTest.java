package net.ironoc.portfolio.controller;

import module java.base;

import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import net.ironoc.portfolio.domain.RepositoryIssueCreateDomain;
import net.ironoc.portfolio.domain.RepositoryIssueDomain;
import net.ironoc.portfolio.dto.RepositoryIssueDto;
import net.ironoc.portfolio.service.GitDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.emptyIterable;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GitProjectsControllerTest {

    @InjectMocks
    private GitProjectsController gitProjectsController;

    @Mock
    private GitDetailsService gitDetailsServiceMock;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    private static final String TEST_USERNAME_ALPHA = "conorheffron";
    private static final String TEST_USERNAME_ALPHA_NUMERIC = "conor123";
    private static final String TEST_REPO = "booking-sys";

    @Test
    public void test_getReposByUsernamePathVar_success() {
        // when
        ResponseEntity<List<RepositoryDetailDomain>> result = gitProjectsController
                .getReposByUsernamePathVar(httpServletRequestMock, TEST_USERNAME_ALPHA);

        // then
        verify(httpServletRequestMock).getRequestURI();
        verify(httpServletRequestMock, times(2)).getHeader(anyString());
        verify(gitDetailsServiceMock).mapRepositoriesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void test_getReposByUsernameReqParam_success() {
        // when
        ResponseEntity<List<RepositoryDetailDomain>> result = gitProjectsController
                .getReposByUsernameReqParam(httpServletRequestMock, TEST_USERNAME_ALPHA_NUMERIC);

        // then
        verify(httpServletRequestMock).getRequestURI();
        verify(httpServletRequestMock, times(2)).getHeader(anyString());
        verify(gitDetailsServiceMock).mapRepositoriesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void test_getReposByUsernameReqParam_withDash_success() {
        // when
        ResponseEntity<List<RepositoryDetailDomain>> result = gitProjectsController
                .getReposByUsernameReqParam(httpServletRequestMock, "-" + TEST_USERNAME_ALPHA_NUMERIC + "-");

        // then
        verify(httpServletRequestMock).getRequestURI();
        verify(httpServletRequestMock, times(2)).getHeader(anyString());
        verify(gitDetailsServiceMock).mapRepositoriesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void test_getReposByUsernameReqParam_username_fail() {
        // when
        ResponseEntity<List<RepositoryDetailDomain>> result = gitProjectsController
                .getReposByUsernameReqParam(httpServletRequestMock, "conor123%^*$£'");

        // then
        verify(httpServletRequestMock, never()).getRequestURI();
        verify(httpServletRequestMock, never()).getHeader(anyString());
        verify(gitDetailsServiceMock, never()).mapRepositoriesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getBody(), is(emptyIterable()));
        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void test_getReposByUsernameReqParam_username_blank_fail() {
        // when
        ResponseEntity<List<RepositoryDetailDomain>> result = gitProjectsController
                .getReposByUsernameReqParam(httpServletRequestMock, null);

        // then
        verify(httpServletRequestMock, never()).getRequestURI();
        verify(httpServletRequestMock, never()).getHeader(anyString());
        verify(gitDetailsServiceMock, never()).mapRepositoriesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getBody(), is(emptyIterable()));
        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void test_getIssuesByUsernameAndRepoPathVars_success() {
        // when
        ResponseEntity<List<RepositoryIssueDomain>> result = gitProjectsController
                .getIssuesByUsernameAndRepoPathVars(httpServletRequestMock, TEST_USERNAME_ALPHA, TEST_REPO);

        // then
        verify(httpServletRequestMock).getRequestURI();
        verify(httpServletRequestMock, times(2)).getHeader(anyString());
        verify(gitDetailsServiceMock).getIssues(anyString(), anyString(), anyBoolean());
        verify(gitDetailsServiceMock).mapIssuesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void test_getIssuesByUsernameAndRepoPathVars_username_invalid_fail() {
        // when
        ResponseEntity<List<RepositoryIssueDomain>> result = gitProjectsController
                .getIssuesByUsernameAndRepoPathVars(httpServletRequestMock, "service-accnt-123%^*$£'", TEST_REPO);

        // then
        verify(httpServletRequestMock, never()).getRequestURI();
        verify(httpServletRequestMock, never()).getHeader(anyString());
        verify(gitDetailsServiceMock, never()).getIssues(anyString(), anyString(), anyBoolean());
        verify(gitDetailsServiceMock, never()).mapIssuesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getBody(), is(emptyIterable()));
        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void test_getIssuesByUsernameAndRepoPathVars_project_invalid_fail() {
        // when
        ResponseEntity<List<RepositoryIssueDomain>> result = gitProjectsController
                .getIssuesByUsernameAndRepoPathVars(httpServletRequestMock, TEST_USERNAME_ALPHA, "project-123%^*$£'");

        // then
        verify(httpServletRequestMock, never()).getRequestURI();
        verify(httpServletRequestMock, never()).getHeader(anyString());
        verify(gitDetailsServiceMock, never()).getIssues(anyString(), anyString(), anyBoolean());
        verify(gitDetailsServiceMock, never()).mapIssuesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getBody(), is(emptyIterable()));
        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void test_getIssuesByUsernameAndRepoPathVars_missing_path_vars_fail() {
        // when
        ResponseEntity<List<RepositoryIssueDomain>> result = gitProjectsController
                .getIssuesByUsernameAndRepoPathVars(httpServletRequestMock, null, null);

        // then
        verify(httpServletRequestMock, never()).getRequestURI();
        verify(httpServletRequestMock, never()).getHeader(anyString());
        verify(gitDetailsServiceMock, never()).getIssues(anyString(), anyString(), anyBoolean());
        verify(gitDetailsServiceMock, never()).mapIssuesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getBody(), is(emptyIterable()));
        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void test_createIssueByUsernameAndRepoPathVars_success() {
        // given
        RepositoryIssueCreateDomain request = RepositoryIssueCreateDomain.builder()
                .title("Found a bug")
                .body("I am having a problem with this.")
                .labels(List.of("bug"))
                .build();
        RepositoryIssueDto createdIssue = RepositoryIssueDto.builder()
                .number("123")
                .title("Found a bug")
                .body("I am having a problem with this.")
                .build();
        RepositoryIssueDomain expectedResponse = RepositoryIssueDomain.builder()
                .number("123")
                .title("Found a bug")
                .body("I am having a problem with this.")
                .labels(Collections.emptyList())
                .build();
        when(gitDetailsServiceMock.createIssue(anyString(), anyString(), any())).thenReturn(createdIssue);
        when(gitDetailsServiceMock.mapIssuesToResponse(anyList())).thenReturn(List.of(expectedResponse));

        // when
        ResponseEntity<RepositoryIssueDomain> result = gitProjectsController
                .createIssueByUsernameAndRepoPathVars(httpServletRequestMock, TEST_USERNAME_ALPHA, TEST_REPO, request);

        // then
        verify(httpServletRequestMock).getRequestURI();
        verify(httpServletRequestMock, times(2)).getHeader(anyString());
        verify(gitDetailsServiceMock).createIssue(anyString(), anyString(), any());
        verify(gitDetailsServiceMock).mapIssuesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.CREATED));
    }

    @Test
    public void test_createIssueByUsernameAndRepoPathVars_missing_title_fail() {
        // when
        ResponseEntity<RepositoryIssueDomain> result = gitProjectsController
                .createIssueByUsernameAndRepoPathVars(httpServletRequestMock, TEST_USERNAME_ALPHA, TEST_REPO,
                        RepositoryIssueCreateDomain.builder().build());

        // then
        verify(httpServletRequestMock, never()).getRequestURI();
        verify(httpServletRequestMock, never()).getHeader(anyString());
        verify(gitDetailsServiceMock, never()).createIssue(anyString(), anyString(), any());
        verify(gitDetailsServiceMock, never()).mapIssuesToResponse(anyList());

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}
