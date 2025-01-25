package net.ironoc.portfolio.config;

import lombok.Getter;

@Getter
public enum Properties {

    STATIC_CONF_IGNORE_PATHS("net.ironoc.portfolio.config.ignore-paths"),
    STATIC_CONF_HANDLE_EXT("net.ironoc.portfolio.config.handle-extensions"),
    STATIC_CONF_RESOURCE_HANDLER("net.ironoc.portfolio.config.resource-handler"),
    STATIC_CONF_RESOURCE_LOC("net.ironoc.portfolio.config.resource-loc"),
    IS_GITHUB_JOB_ENABLED("net.ironoc.portfolio.github.job-enable"),
    BREWS_GRAPH_ENDPOINT("net.ironoc.portfolio.brew.graph.endpoint"),
    BREWS_API_ENDPOINT_HOT("net.ironoc.portfolio.brew.api.endpoint.hot"),
    BREWS_API_ENDPOINT_ICE("net.ironoc.portfolio.brew.api.endpoint.ice"),
    GIT_API_ENDPOINT_REPOS("net.ironoc.portfolio.github.api.endpoint.repos"),
    GIT_API_ENDPOINT_REPOS_PARAM_CACHE("net.ironoc.portfolio.github.api.endpoint.user-ids-cache"),
    GIT_API_ENDPOINT_ISSUES("net.ironoc.portfolio.github.api.endpoint.issues"),
    GIT_API_ENDPOINT_ISSUES_PARAM_CACHE("net.ironoc.portfolio.github.api.endpoint.projects-cache"),
    GIT_TIMEOUT_CONNECT ("net.ironoc.portfolio.github.timeout.connect"),
    GIT_TIMEOUT_READ("net.ironoc.portfolio.github.timeout.read"),
    GIT_INSTANCE_FOLLOW_REDIRECTS("net.ironoc.portfolio.github.instance-follow-redirects"),
    GIT_FOLLOW_REDIRECTS("net.ironoc.portfolio.github.follow-redirects");

    private final String key;

    Properties(String key) {
        this.key = key;
    }
}
