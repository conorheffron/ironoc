package net.ironoc.portfolio.controller;

<<<<<<< HEAD
=======
import module java.base;

>>>>>>> origin/main
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
<<<<<<< HEAD
=======
import org.springframework.core.env.Environment;
>>>>>>> origin/main
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VersionController {

<<<<<<< HEAD
    private final BuildProperties buildProperties;

    public VersionController(@Autowired BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
=======
    private static final String PROD_PROFILE = "prod";
    private static final String OPEN_API_PATH = "/api-docs";
    private static final String SWAGGER_UI_PATH = "/swagger-ui-ironoc.html";

    private final BuildProperties buildProperties;
    private final Environment environment;

    public VersionController(@Autowired BuildProperties buildProperties,
                             @Autowired Environment environment) {
        this.buildProperties = buildProperties;
        this.environment = environment;
>>>>>>> origin/main
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
<<<<<<< HEAD
=======

    @Operation(summary = "Get ironoc API documentation endpoint",
            description = "Returns the Swagger/OpenAPI documentation path based on the active run profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved ironoc API documentation endpoint.")
    })
    @GetMapping(value = {"/application/openapi-endpoint"}, produces= MediaType.TEXT_PLAIN_VALUE)
    public String getOpenApiDocumentationEndpoint() {
        return isProdProfileActive() ? OPEN_API_PATH : SWAGGER_UI_PATH;
    }

    private boolean isProdProfileActive() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(PROD_PROFILE::equalsIgnoreCase);
    }
>>>>>>> origin/main
}
