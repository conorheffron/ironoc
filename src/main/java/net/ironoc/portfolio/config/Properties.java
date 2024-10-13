package net.ironoc.portfolio.config;

import lombok.Getter;

@Getter
public enum Properties {

    GIT_API_ENDPOINT_REPOS("net.ironoc.portfolio.github.api.endpoint.repos"),
    GIT_API_ENDPOINT_ISSUES("net.ironoc.portfolio.github.api.endpoint.issues"),
    GIT_TIMEOUT_CONNECT ("net.ironoc.portfolio.github.timeout.connect"),
    GIT_TIMEOUT_READ("net.ironoc.portfolio.github.timeout.read"),
    GIT_INSTANCE_FOLLOW_REDIRECTS("net.ironoc.portfolio.github.instance-follow-redirects"),
    GIT_FOLLOW_REDIRECTS("net.ironoc.portfolio.github.follow-redirects");

    private String key;

    Properties(String key) {
        this.key = key;
    }
}
