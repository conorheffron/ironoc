package com.ironoc.portfolio.controller;

import com.ironoc.portfolio.domain.RepositoryDetailDomain;
import com.ironoc.portfolio.service.GitDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GitProjectsControllerTest {

    @InjectMocks
    private GitProjectsController gitProjectsController;

    @Mock
    private GitDetailsService gitDetailsServiceMock;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    private static final String TEST_USERNAME = "conorheffron";

    @Test
    public void test_getReposByUsernamePathVar_success() {
        // when
        ResponseEntity<List<RepositoryDetailDomain>> result = gitProjectsController
                .getReposByUsernamePathVar(httpServletRequestMock, TEST_USERNAME);

        // then
        verify(httpServletRequestMock).getRequestURI();
        verify(httpServletRequestMock, times(2)).getHeader(anyString());
        verify(gitDetailsServiceMock).mapRepositoriesToResponse(anyList());

        assertThat(result, is(notNullValue()));
    }
    @Test
    public void test_getReposByUsernameReqParam_success() {
        // when
        ResponseEntity<List<RepositoryDetailDomain>> result = gitProjectsController
                .getReposByUsernameReqParam(httpServletRequestMock, TEST_USERNAME);

        // then
        verify(httpServletRequestMock).getRequestURI();
        verify(httpServletRequestMock, times(2)).getHeader(anyString());
        verify(gitDetailsServiceMock).mapRepositoriesToResponse(anyList());

        assertThat(result, is(notNullValue()));
    }
}
