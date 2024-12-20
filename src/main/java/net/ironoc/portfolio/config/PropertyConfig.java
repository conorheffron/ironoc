package net.ironoc.portfolio.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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
        return Arrays.stream(StringUtils.split(handleExt, ",")).toList();
    }

    @Override
    public String getStaticConfResourceHandler() {
        return environment.getRequiredProperty(propertyKey.getStaticConfResourceHandler());

    }

    @Override
    public String getStaticConfResourceLoc() {
        return environment.getRequiredProperty(propertyKey.getStaticConfResourceLoc());

    }
}
