package com.ironoc.portfolio.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.RedirectView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomErrorControllerTest {

    @InjectMocks
    private CustomErrorController customErrorController;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Test
    public void test_error_view_success() {
        // when
        RedirectView result = customErrorController.error(httpServletRequestMock);

        // then
        verify(httpServletRequestMock, times(3)).getAttribute(anyString());
        verify(httpServletRequestMock).getAttribute(RequestDispatcher.ERROR_MESSAGE);
        verify(httpServletRequestMock).getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        verify(httpServletRequestMock).getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        assertThat(result, is(notNullValue()));
        assertThat(result, isA(RedirectView.class));
    }

    @Test
    public void test_getErrorPath_success() {
        // when
        String result = CustomErrorController.PATH;

        // then
        assertThat(result, is("/error"));
    }
}
