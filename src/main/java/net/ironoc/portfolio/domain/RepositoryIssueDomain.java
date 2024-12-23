package net.ironoc.portfolio.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RepositoryIssueDomain {

    @Schema(name= "number", description = "Project Issue Number.",
            example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
    private String number;

    @Schema(name= "title", description = "Issue Title Text.",
            example = "The UI is not rendering the GitHub Repo Details View", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(name= "body", description = "Issue Content & Description.",
            example = "The app crashes when I visit the Repo Details View", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String body;
}
