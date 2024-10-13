package net.ironoc.portfolio.config;

public interface PropertyConfigI {

    String getGitApiEndpointRepos();

    Integer getGitTimeoutConnect();

    Integer getGitTimeoutRead();

    Boolean getGitInstanceFollowRedirects();

    Boolean getGitFollowRedirects();

    String getGitApiEndpointIssues();
}