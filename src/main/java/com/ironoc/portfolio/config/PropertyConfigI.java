package com.ironoc.portfolio.config;

public interface PropertyConfigI {

    String getGitApiEndpoint();

    Integer getGitTimeoutConnect();

    Integer getGitTimeoutRead();

    Boolean getGitInstanceFollowRedirects();

    Boolean getGitFollowRedirects();
}