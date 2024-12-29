package net.ironoc.portfolio.config;

import org.springframework.stereotype.Component;

@Component
public class PropertyKey implements PropertyKeyI {

    @Override
    public String getGitApiEndpointRepos() {
        return Properties.GIT_API_ENDPOINT_REPOS.getKey();
    }

    @Override
    public String getGitApiEndpointIssues() {
        return Properties.GIT_API_ENDPOINT_ISSUES.getKey();
    }

    @Override
    public String getGitTimeoutConnect() {
        return Properties.GIT_TIMEOUT_CONNECT.getKey();
    }

    @Override
    public String getGitTimeoutRead() {
        return Properties.GIT_TIMEOUT_READ.getKey();
    }

    @Override
    public String getGitInstanceFollowRedirects() {
        return Properties.GIT_INSTANCE_FOLLOW_REDIRECTS.getKey();
    }

    @Override
    public String getGitFollowRedirects() {
        return Properties.GIT_FOLLOW_REDIRECTS.getKey();
    }

    @Override
    public String getStaticConfIgnorePaths() {
        return Properties.STATIC_CONF_IGNORE_PATHS.getKey();
    }

    @Override
    public String getStaticConfHandleExt() {
        return Properties.STATIC_CONF_HANDLE_EXT.getKey();
    }

    @Override
    public String getStaticConfResourceHandler() {
        return Properties.STATIC_CONF_RESOURCE_HANDLER.getKey();
    }

    @Override
    public String getStaticConfResourceLoc() {
        return Properties.STATIC_CONF_RESOURCE_LOC.getKey();
    }

    @Override
    public String getGitApiEndpointUserIdsCache() {
        return Properties.GIT_API_ENDPOINT_REPOS_PARAM_CACHE.getKey();
    }

    @Override
    public String getGitApiEndpointProjectsCache() {
        return Properties.GIT_API_ENDPOINT_ISSUES_PARAM_CACHE.getKey();
    }

    @Override
    public String isCacheJobEnabled() {
        return Properties.IS_GITHUB_JOB_ENABLED.getKey();
    }
}
