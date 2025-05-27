package net.ironoc.portfolio.controller;

import net.ironoc.portfolio.SeleniumConfig;
import net.ironoc.portfolio.config.TestIronocConfiguration;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@ContextConfiguration(classes = {TestIronocConfiguration.class, SeleniumConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {
        "net.ironoc.portfolio.config.ignore-paths=api",
        "net.ironoc.portfolio.config.handle-extensions=css,html",
        "net.ironoc.portfolio.config.resource-handler=/**",
        "net.ironoc.portfolio.config.resource-loc=\"classpath:/static/\"",
        "net.ironoc.portfolio.github.api.endpoint.user-ids-cache=conorheffron",
        "net.ironoc.portfolio.github.api.endpoint.projects-cache=\"ironoc,ironoc-db,booking-sys\"",
        "net.ironoc.portfolio.github.cron-job=0 1 1 ? * *",
        "net.ironoc.portfolio.github.job-enable=false",
        "net.ironoc.portfolio.brew.cron-job=0 0 2 ? * *",
        "net.ironoc.portfolio.brew.job-enable=false",
        "net.ironoc.portfolio.brew.api.endpoint.ice=https://api.sampleapis.com/coffee/iced",
        "net.ironoc.portfolio.brew.api.endpoint.hot=https://api.sampleapis.com/coffee/hot",
        "net.ironoc.portfolio.github.api.endpoint.repos=ironoc-db"
})
public class BaseControllerIntegrationTest {
}
