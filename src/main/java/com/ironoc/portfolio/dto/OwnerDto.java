package com.ironoc.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;
import lombok.AllArgsConstructor;

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
