package net.ironoc.portfolio.config;

public interface PropertyKeyI {

    String getGitApiEndpointRepos();

    String getGitTimeoutConnect();

    String getGitTimeoutRead();

    String getGitInstanceFollowRedirects();

    String getGitFollowRedirects();

    String getGitApiEndpointIssues();

    String getStaticConfIgnorePaths();

    String getStaticConfHandleExt();

    String getStaticConfResourceHandler();

    String getStaticConfResourceLoc();

    String getGitApiEndpointUserIdsCache();

    String getGitApiEndpointProjectsCache();

    String isCacheJobEnabled();

    String getBrewApiEndpointHot();

    String getBrewApiEndpointIce();

    String getBrewGraphEndpoint();

    String isBrewCacheJobEnabled();
}
