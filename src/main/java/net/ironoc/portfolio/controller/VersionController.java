package net.ironoc.portfolio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VersionController {

    @Autowired
    private final BuildProperties buildProperties;

    public VersionController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
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
}
