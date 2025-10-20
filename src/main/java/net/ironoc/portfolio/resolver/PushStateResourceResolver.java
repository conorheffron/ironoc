package net.ironoc.portfolio.resolver;

import module java.base;

import org.springframework.lang.Nullable;
import net.ironoc.portfolio.logger.AbstractLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

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
    public Resource resolveResource(@Nullable HttpServletRequest request, @Nullable String requestPath,
                                    @Nullable List<? extends Resource> locations, @Nullable ResourceResolverChain chain) {
        return resolve(requestPath, locations);
    }

    @Override
    public String resolveUrlPath(@Nullable String resourcePath, @Nullable List<? extends Resource> locations,
                                 @Nullable ResourceResolverChain chain) {
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
            warn("The ignored request path is: {}", requestPath);
            return null;
        }
        if (isHandled(requestPath)) {
            Optional<Resource> staticResource = locations.stream()
                    .map(loc -> createRelative(loc, requestPath))
                    .filter(resource -> resource != null && resource.exists())
                    .findFirst();
            debug("The request path is: {}", requestPath);
            if (staticResource.isPresent()) {
                return staticResource.get();
            } else {
                error("The request path {} does not exist.", requestPath);
                return null;
            }
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
