package com.ironoc.portfolio.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RepositoryDetailDomain {

    @Schema(name= "name", description = "Name of GitHub Repository.", example = "ironoc-db", required = true)
    private String name;

    @Schema(name= "fullName", description = "Full Name of GitHub Repository (Format is: username/project_name).",
            example = "conorheffron/ironoc-db", required = true)
    private String fullName;

    @Schema(name= "description", description = "Description of GitHub project.", example = "Personal Portfolio Website.",
            required = false)
    private String description;

    @Schema(name= "appHome", description = "Normally this value is the link to the project/app home page.",
            example = "http://ironoc.com", required = false)
    private String appHome;

    @Schema(name = "repoUrl", description = "This is the home page URL of the project.",
            example = "https://github.com/conorheffron/ironoc-db", required = true)
    private String repoUrl;

    @Schema(name = "topics", description = "Labels or topics associated with the GitHub repository project.",
            example = "[aws, jdk21, maven, personal, portfolio, spring-boot-3]", required = false)
    private String topics;
}
