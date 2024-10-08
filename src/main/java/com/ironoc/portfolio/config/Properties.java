package com.ironoc.portfolio.config;

import lombok.Getter;

@Getter
public enum Properties {

    GIT_API_ENDPOINT_REPOS("com.ironoc.portfolio.github.api.endpoint.repos"),
    GIT_API_ENDPOINT_ISSUES("com.ironoc.portfolio.github.api.endpoint.issues"),
    GIT_TIMEOUT_CONNECT ("com.ironoc.portfolio.github.timeout.connect"),
    GIT_TIMEOUT_READ("com.ironoc.portfolio.github.timeout.read"),
    GIT_INSTANCE_FOLLOW_REDIRECTS("com.ironoc.portfolio.github.instance-follow-redirects"),
    GIT_FOLLOW_REDIRECTS("com.ironoc.portfolio.github.follow-redirects");

    private String key;

    Properties(String key) {
        this.key = key;
    }
}
