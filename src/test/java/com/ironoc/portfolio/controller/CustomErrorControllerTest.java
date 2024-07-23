package com.ironoc.portfolio.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.RedirectView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomErrorControllerTest {

    @InjectMocks
    private CustomErrorController customErrorController;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private HttpServletResponse httpServletResponseMock;

    @Test
    public void test_error_view_success() {
        // when
        RedirectView result = customErrorController.error(httpServletRequestMock, httpServletResponseMock);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result, isA(RedirectView.class));
    }

    @Test
    public void test_getErrorPath_success() {
        // when
        String result = customErrorController.getErrorPath();

        // then
        assertThat(result, is("/error"));
    }
}
