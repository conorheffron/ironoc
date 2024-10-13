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
            example = "45", required = true)
    private String number;

    @Schema(name= "title", description = "Issue Title Text.",
            example = "The UI is not rendering the GitHub Repo Details View", required = true)
    private String title;

    @Schema(name= "body", description = "Issue Content & Description.",
            example = "The app crashes when I visit the Repo Details View", required = false)
    private String body;
}
