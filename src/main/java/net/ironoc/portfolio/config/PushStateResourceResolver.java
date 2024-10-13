package net.ironoc.portfolio.config;

import net.ironoc.portfolio.logger.AbstractLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.io.IOException;
import java.util.List;

public class PushStateResourceResolver extends AbstractLogger implements ResourceResolver {

    private final Resource index;

    private final List<String> handledExtensions;

    private final List<String> ignoredPaths;

    public PushStateResourceResolver(List<String> handledExtensions, List<String> ignoredPaths) {
        this.handledExtensions = handledExtensions;
        this.ignoredPaths = ignoredPaths;
        this.index = new ClassPathResource("/static/index.html");
    }

    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath,
                                    List<? extends Resource> locations, ResourceResolverChain chain) {
        return resolve(requestPath, locations);
    }

    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations,
                                 ResourceResolverChain chain) {
        Resource resolvedResource = resolve(resourcePath, locations);
        if (resolvedResource == null) {
            return null;
        }
        try {
            return resolvedResource.getURL().toString();
        } catch (IOException e) {
            error("Unexpected error occurred resolving URL path.", e);
            return resolvedResource.getFilename();
        }
    }

    private Resource resolve(String requestPath, List<? extends Resource> locations) {
        if (isIgnored(requestPath)) {
            return null;
        }
        if (isHandled(requestPath)) {
            return locations.stream()
                    .map(loc -> createRelative(loc, requestPath))
                    .filter(resource -> resource != null && resource.exists())
                    .findFirst()
                    .orElseGet(null);
        }
        return index;
    }

    private Resource createRelative(Resource resource, String relativePath) {
        try {
            return resource.createRelative(relativePath);
        } catch (IOException e) {
            error("Unexpected error occurred creating relative path.", e);
            return null;
        }
    }

    private boolean isIgnored(String path) {
        return ignoredPaths.contains(path);
    }

    private boolean isHandled(String path) {
        String extension = StringUtils.getFilenameExtension(path);
        return handledExtensions.stream().anyMatch(ext -> ext.equals(extension));
    }
}
