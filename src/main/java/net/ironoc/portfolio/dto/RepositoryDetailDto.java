package net.ironoc.portfolio.dto;

import module java.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class RepositoryDetailDto {

    private int id;

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("private")
    private boolean isPrivate;

    @JsonProperty("owner")
    private OwnerDto owner;

    @JsonProperty("html_url")
    private String htmlUrl;

    private String description;

    private String language;

    @JsonProperty("git_url")
    private String gitUrl;

    @JsonProperty("homepage")
    private String homePage;

    private List<String> topics;

    @Override
    public String toString() {
        return "name: '" + this.name + "'";
    }
}
