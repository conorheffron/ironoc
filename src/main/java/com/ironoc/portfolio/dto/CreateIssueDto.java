package com.ironoc.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateIssueDto {

    private String owner;

    private String repo;

    private String title;

    private String body;

    private List<String> assignees;

    private List<String> labels;

    private Integer milestone;

    private Map<String, String> headers;
}
