package com.ironoc.portfolio.config;

public interface PropertyConfigI {

    String getGitApiEndpoint();

    String getGitReposUri();

    Integer getGitTimeoutConnect();

    Integer getGitTimeoutRead();

    Boolean getGitInstanceFollowRedirects();

    Boolean getGitFollowRedirects();

    String getGitToken();
}