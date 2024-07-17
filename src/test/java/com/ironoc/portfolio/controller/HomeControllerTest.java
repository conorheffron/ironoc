package com.ironoc.portfolio.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Test
    public void test_index_success() {
        // when
        String result = homeController.index(httpServletRequestMock);

        // then
        verify(httpServletRequestMock).getRequestURI();

        assertThat(result, is("index"));
    }
}
