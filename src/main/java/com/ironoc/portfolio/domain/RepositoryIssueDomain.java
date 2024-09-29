package com.ironoc.portfolio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RepositoryIssueDomain {

    private String number;

    private String title;

    private String body;
}
