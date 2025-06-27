package net.ironoc.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.server.webmvc.GraphiQlHandler;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GraphiQlConfiguration {

    @Bean
    @Order(0)
    public RouterFunction<ServerResponse> graphiQlRouterFunction() {
        RouterFunctions.Builder builder = RouterFunctions.route();
        ClassPathResource graphiQlPage = new ClassPathResource("graphiql/index.html");
        GraphiQlHandler graphiQLHandler = new GraphiQlHandler("/graphql", "", graphiQlPage);
        builder = builder.GET("/graphiql", graphiQLHandler::handleRequest);
        return builder.build();
    }
}
