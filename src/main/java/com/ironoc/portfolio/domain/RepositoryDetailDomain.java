package com.ironoc.portfolio.domain;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
public class RepositoryDetailDomain {

    private String name;

    private String description;

    private String appHome;

    private String repoUrl;

    private String topics;
}
