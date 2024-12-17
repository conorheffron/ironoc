package net.ironoc.portfolio.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ironoc.portfolio.resolver.PushStateResourceResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceChainRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IronocConfigurationTest {

    @InjectMocks
    private IronocConfiguration ironocConfiguration;// config bean under test

    @Mock
    private PropertyConfigI propertyConfigMock;

    @Mock
    private ResourceHandlerRegistry resourceHandlerRegistryMock;

    @Mock
    private ResourceHandlerRegistration resourceHandlerRegistrationMock;

    @Mock
    private DefaultServletHandlerConfigurer defaultServletHandlerConfigurerMock;

    @Mock
    private ResourceChainRegistration resourceChainRegistrationMock;

    @Test
    public void test_addResourceHandlers_success() {
        // given
        when(resourceHandlerRegistryMock.addResourceHandler(ArgumentMatchers.anyString()))
                .thenReturn(resourceHandlerRegistrationMock);
        when(resourceHandlerRegistrationMock.addResourceLocations(ArgumentMatchers.anyString()))
                .thenReturn(resourceHandlerRegistrationMock);
        when(resourceHandlerRegistrationMock.resourceChain(false))
                .thenReturn(resourceChainRegistrationMock);
        when(propertyConfigMock.getStaticConfHandleExt()).thenReturn(List.of("json", "css", "html", "js"));
        when(propertyConfigMock.getStaticConfIgnorePaths()).thenReturn("api");
        when(propertyConfigMock.getStaticConfResourceHandler()).thenReturn("/**");
        when(propertyConfigMock.getStaticConfResourceLoc()).thenReturn("classpath:/static/");

        // when
        ironocConfiguration.addResourceHandlers(resourceHandlerRegistryMock);

        // then
        ArgumentCaptor<String> resourceHandlerCaptors = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> resourceLocationCaptors = ArgumentCaptor.forClass(String.class);

        verify(resourceHandlerRegistryMock).addResourceHandler(resourceHandlerCaptors.capture());
        verify(resourceHandlerRegistrationMock).addResourceLocations(resourceLocationCaptors.capture());
        verify(resourceHandlerRegistrationMock).resourceChain(false);
        verify(resourceChainRegistrationMock).addResolver(any(PushStateResourceResolver.class));
        verify(propertyConfigMock).getStaticConfHandleExt();
        verify(propertyConfigMock).getStaticConfIgnorePaths();
        verify(propertyConfigMock).getStaticConfResourceHandler();
        verify(propertyConfigMock).getStaticConfResourceLoc();

        List<String> capturedResourceHandlers = resourceHandlerCaptors.getAllValues();
        assertThat(capturedResourceHandlers.get(0), is("/**"));
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
