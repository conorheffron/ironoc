package net.ironoc.portfolio.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RepositoryDetailDomain {

    @Schema(name= "name", description = "Name of GitHub Repository.", example = "ironoc-db",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(name= "fullName", description = "Full Name of GitHub Repository (Format is: username/project_name).",
            example = "conorheffron/ironoc-db", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;

    @Schema(name= "description", description = "Description of GitHub project.", example = "Personal Portfolio Website.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(name= "appHome", description = "Normally this value is the link to the project/app home page.",
            example = "http://ironoc.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String appHome;

    @Schema(name = "repoUrl", description = "This is the home page URL of the project.",
            example = "https://github.com/conorheffron/ironoc-db", requiredMode = Schema.RequiredMode.REQUIRED)
    private String repoUrl;

    @Schema(name = "topics", description = "Labels or topics associated with the GitHub repository project.",
            example = "[aws, jdk21, maven, personal, portfolio, spring-boot-3]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String topics;

    @Schema(name= "issueCount", description = "Number of associated issues.", example = "3")
    private int issueCount;
}
