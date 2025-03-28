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
        "net.ironoc.portfolio.github.job-enable=true",
        "net.ironoc.portfolio.github.api.endpoint.repos=ironoc-db"
})
public class BaseControllerIntegrationTest {
}
