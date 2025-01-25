package net.ironoc.portfolio.config;

import java.util.List;

public interface PropertyConfigI {

    String getGitApiEndpointRepos();

    Integer getGitTimeoutConnect();

    Integer getGitTimeoutRead();

    Boolean getGitInstanceFollowRedirects();

    Boolean getGitFollowRedirects();

    String getGitApiEndpointIssues();

    String getStaticConfIgnorePaths();

    List<String> getStaticConfHandleExt();

    String getStaticConfResourceHandler();

    String getStaticConfResourceLoc();

    List<String>  getGitApiEndpointUserIdsCache();

    List<String> getGitApiEndpointProjectsCache();

    boolean isCacheJobEnabled();

    String getBrewApiEndpointHot();

    String getBrewApiEndpointIce();

    String getBrewGraphEndpoint();
}
