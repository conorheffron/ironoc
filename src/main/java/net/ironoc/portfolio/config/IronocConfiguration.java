package net.ironoc.portfolio.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ironoc.portfolio.resolver.PushStateResourceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = { "net.ironoc.portfolio" })
public class IronocConfiguration implements WebMvcConfigurer {

    private final PropertyConfigI propertyConfig;

    @Autowired
    public IronocConfiguration(PropertyConfigI propertyConfig) {
        this.propertyConfig = propertyConfig;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        List<String> ignorePaths = List.of(propertyConfig.getStaticConfIgnorePaths());
        List<String> handledExtensions = propertyConfig.getStaticConfHandleExt();
        registry.addResourceHandler(propertyConfig.getStaticConfResourceHandler())
                .addResourceLocations(propertyConfig.getStaticConfResourceLoc())
                .resourceChain(false)
                .addResolver(new PushStateResourceResolver(handledExtensions, ignorePaths));
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> enableDefaultServlet() {
        return (factory) -> factory.setRegisterDefaultServlet(true);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedMethods(HttpMethod.GET.name());
    }
}
