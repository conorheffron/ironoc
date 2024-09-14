package com.ironoc.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@ToString(includeFieldNames = true)
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwnerDto {

    private int id;

    private String login;

    @JsonProperty("html_url")
    private String htmlUrl;
}
