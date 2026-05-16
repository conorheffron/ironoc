package net.ironoc.portfolio.domain;

import module java.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RepositoryIssueCreateDomain {

    @Schema(name = "title", description = "Issue title text.",
            example = "Found a bug", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(name = "body", description = "Issue content and description.",
            example = "I'm having a problem with this.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String body;

    @Schema(name = "assignees", description = "Issue assignees.",
            example = "[\"octocat\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> assignees;

    @Schema(name = "milestone", description = "Issue milestone number.",
            example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer milestone;

    @Schema(name = "labels", description = "Issue labels.",
            example = "[\"bug\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> labels;
}
