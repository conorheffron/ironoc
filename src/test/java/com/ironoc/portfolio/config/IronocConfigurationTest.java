package com.ironoc.portfolio.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class IronocConfigurationTest {

    @InjectMocks
    private IronocConfiguration ironocConfiguration;// config bean under test

    @Mock
    private ResourceHandlerRegistry resourceHandlerRegistryMock;

    @Mock
    private ResourceHandlerRegistration resourceHandlerRegistrationMock;

    @Mock
    private DefaultServletHandlerConfigurer defaultServletHandlerConfigurerMock;

    @Test
    public void test_getViewResolver_success() {
        // when
        ViewResolver result = ironocConfiguration.getViewResolver();

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
        ironocConfiguration.addResourceHandlers(resourceHandlerRegistryMock);

        // then
        ArgumentCaptor<String> resourceHandlerCaptors = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> resourceLocationCaptors = ArgumentCaptor.forClass(String.class);

        verify(resourceHandlerRegistryMock, times(4)).addResourceHandler(resourceHandlerCaptors.capture());
        verify(resourceHandlerRegistrationMock, times(4)).addResourceLocations(resourceLocationCaptors.capture());

        List<String> capturedResourceHandlers = resourceHandlerCaptors.getAllValues();
        assertThat(IronocConfiguration.RESOURCES_HANDLER, is(capturedResourceHandlers.get(0)));
        assertThat(IronocConfiguration.FAV_ICON, is(capturedResourceHandlers.get(1)));
        assertThat(IronocConfiguration.SITE_MAP, is(capturedResourceHandlers.get(2)));
        assertThat(IronocConfiguration.ROBOTS_TEXT, is(capturedResourceHandlers.get(3)));

        List<String> capturedLocations = resourceLocationCaptors.getAllValues();
        assertThat(IronocConfiguration.STATIC_LOC, is(capturedLocations.get(0)));
        assertThat(IronocConfiguration.IMAGES_LOC, is(capturedLocations.get(1)));
        assertThat(IronocConfiguration.STATIC_CONF_LOC, is(capturedLocations.get(2)));
        assertThat(IronocConfiguration.STATIC_CONF_LOC, is(capturedLocations.get(3)));
    }

    @Test
    public void test_configureDefaultServletHandling_success() {
        // when
        ironocConfiguration.configureDefaultServletHandling(defaultServletHandlerConfigurerMock);

        // then
        verify(defaultServletHandlerConfigurerMock).enable();
    }

    @Test
    public void test_enableDefaultServlet_success() {
        // when
        WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> result = ironocConfiguration.enableDefaultServlet();

        // then
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void test_objectMapper_success() {
        // when
        ObjectMapper result = ironocConfiguration.objectMapper();

        // then
        assertThat(result, is(notNullValue()));
    }
}
