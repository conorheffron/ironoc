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
}
