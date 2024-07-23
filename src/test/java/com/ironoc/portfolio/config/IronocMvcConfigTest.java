package com.ironoc.portfolio.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IronocMvcConfigTest {

    @InjectMocks
    private IronocMvcConfig ironocMvcConfig;// config bean under test

    @Mock
    private ResourceHandlerRegistry resourceHandlerRegistryMock;

    @Mock
    private ResourceHandlerRegistration resourceHandlerRegistrationMock;

    @Mock
    private DefaultServletHandlerConfigurer defaultServletHandlerConfigurerMock;

    @Test
    public void test_getViewResolver_success() {
        // when
        ViewResolver result = ironocMvcConfig.getViewResolver();

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getClass(), is(InternalResourceViewResolver.class));
    }

    @Test
    public void test_addResourceHandlers_success() {
        // given
        when(resourceHandlerRegistryMock.addResourceHandler(ArgumentMatchers.anyString()))
                .thenReturn(resourceHandlerRegistrationMock);

        // when
        ironocMvcConfig.addResourceHandlers(resourceHandlerRegistryMock);

        // then
        ArgumentCaptor<String> resourceHandlerCaptors = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> resourceLocationCaptors = ArgumentCaptor.forClass(String.class);

        verify(resourceHandlerRegistryMock, times(4)).addResourceHandler(resourceHandlerCaptors.capture());
        verify(resourceHandlerRegistrationMock, times(4)).addResourceLocations(resourceLocationCaptors.capture());

        List<String> capturedResourceHandlers = resourceHandlerCaptors.getAllValues();
        assertThat(IronocMvcConfig.RESOURCES_HANDLER, is(capturedResourceHandlers.get(0)));
        assertThat(IronocMvcConfig.FAV_ICON, is(capturedResourceHandlers.get(1)));
        assertThat(IronocMvcConfig.SITE_MAP, is(capturedResourceHandlers.get(2)));
        assertThat(IronocMvcConfig.ROBOTS_TEXT, is(capturedResourceHandlers.get(3)));

        List<String> capturedLocations = resourceLocationCaptors.getAllValues();
        assertThat(IronocMvcConfig.STATIC_LOC, is(capturedLocations.get(0)));
        assertThat(IronocMvcConfig.IMAGES_LOC, is(capturedLocations.get(1)));
        assertThat(IronocMvcConfig.STATIC_CONF_LOC, is(capturedLocations.get(2)));
        assertThat(IronocMvcConfig.STATIC_CONF_LOC, is(capturedLocations.get(3)));
    }

    @Test
    public void test_configureDefaultServletHandling_success() {
        // when
        ironocMvcConfig.configureDefaultServletHandling(defaultServletHandlerConfigurerMock);

        // then
        verify(defaultServletHandlerConfigurerMock).enable();
    }

    @Test
    public void test_enableDefaultServlet_success() {
        // when
        WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> result = ironocMvcConfig.enableDefaultServlet();

        // then
        assertThat(result, is(notNullValue()));
    }
}
