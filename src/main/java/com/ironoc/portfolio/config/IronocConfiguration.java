package com.ironoc.portfolio.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = { "com.ironoc.portfolio" })
public class IronocConfiguration implements WebMvcConfigurer {

    protected static final String RESOURCES_HANDLER = "/resources/**";
    protected static final String FAV_ICON = "/favicon.ico";
    protected static final String SITE_MAP = "/sitemap.xml";
    protected static final String ROBOTS_TEXT = "/robots.txt";
    protected static final String STATIC_LOC = "/static/";
    protected static final String IMAGES_LOC = STATIC_LOC + "imgs/";
    protected static final String STATIC_CONF_LOC = STATIC_LOC + "config/";

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCES_HANDLER).addResourceLocations(STATIC_LOC);
        registry.addResourceHandler(FAV_ICON).addResourceLocations(IMAGES_LOC);
        registry.addResourceHandler(SITE_MAP).addResourceLocations(STATIC_CONF_LOC);
        registry.addResourceHandler(ROBOTS_TEXT).addResourceLocations(STATIC_CONF_LOC);
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
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    @Bean
    public Docket ironocApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.ironoc.portfolio")).build();
    }
}
