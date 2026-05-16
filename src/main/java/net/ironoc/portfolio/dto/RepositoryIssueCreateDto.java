package net.ironoc.portfolio.dto;

import module java.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RepositoryIssueCreateDto {

    private String title;

    private String body;

    private List<String> assignees;

    private Integer milestone;

    private List<String> labels;
}
