package com.ironoc.portfolio.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Test
    public void test_index_success() {
        // given
        String homeView = "index";

        // when
        String result = homeController.index(httpServletRequestMock);

        // then
        verify(httpServletRequestMock).getRequestURI();
        verify(httpServletRequestMock, times(2)).getHeader(anyString());

        assertThat(result, is(homeView));
    }
}
