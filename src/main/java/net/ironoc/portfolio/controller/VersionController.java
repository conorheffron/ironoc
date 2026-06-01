package net.ironoc.portfolio.controller;

import module java.base;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class VersionController {

    private static final String PROD_PROFILE = "prod";
    private static final String OPEN_API_PATH = "/api-docs";
    private static final String SWAGGER_UI_PATH = "/swagger-ui-ironoc.html";

    private final BuildProperties buildProperties;
    private final Environment environment;

    public VersionController(@Autowired BuildProperties buildProperties,
                             @Autowired Environment environment) {
        this.buildProperties = buildProperties;
        this.environment = environment;
    }

    @Operation(summary = "Get ironoc Application Version",
            description = "Returns a string value that represents the version of the ironoc application currently running")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved ironoc application version.")
    })
    @GetMapping(value = {"/application/version"}, produces= MediaType.TEXT_PLAIN_VALUE)
    public String getApplicationVersion() {
        return "Version: " + this.buildProperties.getVersion();
    }

    @Operation(summary = "Get ironoc API documentation endpoint",
            description = "Returns the fully qualified Swagger/OpenAPI documentation URL based on the active run profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved ironoc API documentation endpoint.")
    })
    @GetMapping(value = {"/application/openapi-endpoint"}, produces= MediaType.TEXT_PLAIN_VALUE)
    public String getOpenApiDocumentationEndpoint(HttpServletRequest request) {
        String endpointPath = isProdProfileActive() ? OPEN_API_PATH : SWAGGER_UI_PATH;
        return ServletUriComponentsBuilder.fromContextPath(request)
                .path(endpointPath)
                .toUriString();
    }

    private boolean isProdProfileActive() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(PROD_PROFILE::equalsIgnoreCase);
    }
}
