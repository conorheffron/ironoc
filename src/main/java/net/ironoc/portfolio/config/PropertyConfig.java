package net.ironoc.portfolio.config;

import module java.base;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyConfig implements PropertyConfigI {

    private final Environment environment;

    private final PropertyKeyI propertyKey;

    public PropertyConfig(Environment environment, PropertyKeyI propertyKey) {
        this.environment = environment;
        this.propertyKey = propertyKey;
    }

    @Override
    public String getGitApiEndpointRepos() {
        return environment.getRequiredProperty(propertyKey.getGitApiEndpointRepos());
    }

    @Override
    public String getGitApiEndpointIssues() {
        return environment.getRequiredProperty(propertyKey.getGitApiEndpointIssues());
    }

    @Override
    public Integer getGitTimeoutConnect() {
        return Integer.valueOf(environment.getRequiredProperty(propertyKey.getGitTimeoutConnect()));
    }

    @Override
    public Integer getGitTimeoutRead() {
        return Integer.valueOf(environment.getRequiredProperty(propertyKey.getGitTimeoutRead()));
    }

    @Override
    public Boolean getGitInstanceFollowRedirects() {
        return Boolean.valueOf(environment.getRequiredProperty(propertyKey.getGitInstanceFollowRedirects()));
    }

    @Override
    public Boolean getGitFollowRedirects() {
        return Boolean.valueOf(environment.getRequiredProperty(propertyKey.getGitFollowRedirects()));
    }

    @Override
    public String getStaticConfIgnorePaths() {
        return environment.getRequiredProperty(propertyKey.getStaticConfIgnorePaths());
    }

    @Override
    public List<String> getStaticConfHandleExt() {
        String handleExt = environment.getRequiredProperty(propertyKey.getStaticConfHandleExt());
        return extractValues(handleExt);
    }

    @Override
    public String getStaticConfResourceHandler() {
        return environment.getRequiredProperty(propertyKey.getStaticConfResourceHandler());

    }

    @Override
    public String getStaticConfResourceLoc() {
        return environment.getRequiredProperty(propertyKey.getStaticConfResourceLoc());
    }

    @Override
    public List<String>  getGitApiEndpointUserIdsCache() {
        String userIds = environment.getRequiredProperty(propertyKey.getGitApiEndpointUserIdsCache());
        return extractValues(userIds);
    }

    @Override
    public List<String> getGitApiEndpointProjectsCache() {
        return List.of(environment.getRequiredProperty(propertyKey.getGitApiEndpointProjectsCache(), String[].class));
    }

    @Override
    public boolean isCacheJobEnabled() {
        return Boolean.parseBoolean(environment.getRequiredProperty(propertyKey.isCacheJobEnabled()));
    }

    @Override
    public String getBrewApiEndpointHot() {
        return environment.getRequiredProperty(propertyKey.getBrewApiEndpointHot());
    }

    @Override
    public String getBrewApiEndpointIce() {
        return environment.getRequiredProperty(propertyKey.getBrewApiEndpointIce());
    }

    @Override
    public String getBrewGraphEndpoint() {
        return environment.getRequiredProperty(propertyKey.getBrewGraphEndpoint());
    }

    @Override
    public boolean isBrewsCacheJobEnabled() {
        return Boolean.parseBoolean(environment.getRequiredProperty(propertyKey.isBrewCacheJobEnabled()));
    }

    private List<String> extractValues(String valuesStr) {
        return Arrays.stream(StringUtils.split(valuesStr, ",")).toList();
    }
}
