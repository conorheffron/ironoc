package com.ironoc.portfolio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateIssueDomain {

    private String title;

    private String body;

    private String assignee;

    private String labels;
}
