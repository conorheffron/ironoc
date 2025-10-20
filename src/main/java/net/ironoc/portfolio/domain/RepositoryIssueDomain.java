package net.ironoc.portfolio.domain;

import module java.base;

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

    @Schema(name= "state", description = "Issue State.",
            example = "Open / Closed etc.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String state;

    @Schema(name= "labels", description = "Issue Labels / Tags.",
            example = "bug, java, ui etc.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> labels;
}
