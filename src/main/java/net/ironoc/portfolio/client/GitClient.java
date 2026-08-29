package net.ironoc.portfolio.client;

import module java.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import net.ironoc.portfolio.aws.SecretManager;
import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.dto.RepositoryIssueCreateDto;
import net.ironoc.portfolio.dto.RepositoryIssueDto;
import net.ironoc.portfolio.logger.AbstractLogger;
import net.ironoc.portfolio.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class GitClient extends AbstractLogger implements Client {

    private static final String GITHUB_API_HOST = "api.github.com";

    private final PropertyConfigI propertyConfig;

    private final SecretManager secretManager;

    private final UrlUtils urlUtils;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    public GitClient(RestTemplateBuilder restTemplateBuilder,
                     PropertyConfigI propertyConfig,
                     SecretManager secretManager,
                     UrlUtils urlUtils,
                     ObjectMapper objectMapper) {
        this.propertyConfig = propertyConfig;
        this.secretManager = secretManager;
        this.urlUtils = urlUtils;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder
                .connectTimeout(Duration.ofMillis(propertyConfig.getGitTimeoutConnect()))
                .readTimeout(Duration.ofMillis(propertyConfig.getGitTimeoutRead()))
                .build();
    }

    @Override
    public <T> List<T> callGitHubApi(String uri, Class<T> type, String httpMethod, Map<String, Object> uriVariables) {
        URI validatedApiUri = null;
        List<T> dtos = new ArrayList<>();
        try {
            validatedApiUri = getValidatedApiUri(uri, uriVariables);
            if (validatedApiUri == null) {
                return Collections.emptyList();
            }
            info("Triggering GET request: url={}", validatedApiUri);
            HttpHeaders headers = new HttpHeaders();
            String token = secretManager.getGitSecret();
            if (StringUtils.isBlank(token)) {
                log.warn("GIT token not set, the lower request rate will apply");
            } else {
                headers.set("Authorization", buildAuthorizationHeader(token));
            }
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    validatedApiUri, HttpMethod.valueOf(httpMethod), entity, String.class);
            if (response == null) {
                error("No response received from GitHub API: method={}, uri={}", httpMethod, validatedApiUri);
                return Collections.emptyList();
            }
            List<String> linkHeader = response.getHeaders().get("Link");
            if (linkHeader != null && !linkHeader.isEmpty()) {
                info("Link.Header: {}", linkHeader);
            }
            if (StringUtils.isBlank(response.getBody())) {
                error("Received blank response body from GitHub API");
                return Collections.emptyList();
            }
            dtos = readJsonResponse(response.getBody(), type);
        } catch (Exception ex) {
            error("Unexpected error occurred while retrieving data.", ex);
        }
        return dtos;
    }

    @Override
    public RepositoryIssueDto createGitHubIssue(String uri, RepositoryIssueCreateDto requestBody,
                                                Map<String, Object> uriVariables) {
        try {
            URI validatedApiUri = getValidatedApiUri(uri, uriVariables);
            if (validatedApiUri == null) {
                return null;
            }
            info("Triggering POST request: url={}", validatedApiUri);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/vnd.github+json");
            headers.set("X-GitHub-Api-Version", "2022-11-28");
            headers.set("Content-Type", "application/json");

            String token = secretManager.getGitSecret();
            if (StringUtils.isBlank(token)) {
                log.warn("GIT token not set, the lower request rate will apply");
            } else {
                headers.set("Authorization", buildAuthorizationHeader(token));
            }
            HttpEntity<RepositoryIssueCreateDto> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<RepositoryIssueDto> response = restTemplate.exchange(
                    validatedApiUri, HttpMethod.POST, entity, RepositoryIssueDto.class);
            if (response == null || response.getBody() == null) {
                error("No response body received from GitHub API for issue creation: uri={}", validatedApiUri);
                return null;
            }
            return response.getBody();
        } catch (Exception ex) {
            error("Unexpected error occurred while creating issue.", ex);
            return null;
        }
    }

    private URI getValidatedApiUri(String uri, Map<String, Object> uriVariables) {
        String trustedUriTemplate = propertyConfig.getGitApiEndpointIssues();
        if (StringUtils.isBlank(trustedUriTemplate) || !urlUtils.isValidURL(trustedUriTemplate)) {
            log.error("The configured Git endpoint is invalid, url={}", trustedUriTemplate);
            return null;
        }

        URI configuredBaseUri = UriComponentsBuilder.fromUriString(trustedUriTemplate).build().toUri();
        if (!StringUtils.equalsIgnoreCase("https", configuredBaseUri.getScheme())
                || StringUtils.isNotBlank(configuredBaseUri.getUserInfo())
                || configuredBaseUri.getFragment() != null
                || !StringUtils.equalsIgnoreCase(GITHUB_API_HOST, configuredBaseUri.getHost())) {
            log.error("The configured Git endpoint is not allowed, url={}", configuredBaseUri);
            return null;
        }

        Object username = uriVariables.get("username");
        Object repo = uriVariables.get("repo");
        if (!isValidGitPathVariable(username) || !isValidGitPathVariable(repo)) {
            log.error("Invalid URI path variables for GitHub API request, username={}, repo={}", username, repo);
            return null;
        }

        URI targetUri = UriComponentsBuilder.fromUriString(trustedUriTemplate)
                .buildAndExpand(uriVariables)
                .encode()
                .toUri();
        if (!urlUtils.isValidURL(targetUri.toString())) {
            log.error("The url is not valid for GIT client connection, url={}", targetUri);
            return null;
        }

        if (!StringUtils.equalsIgnoreCase("https", targetUri.getScheme())
                || StringUtils.isNotBlank(targetUri.getUserInfo())
                || targetUri.getFragment() != null
                || !StringUtils.equalsIgnoreCase(GITHUB_API_HOST, targetUri.getHost())) {
            log.error("The url is not valid for GIT client connection, url={}", targetUri);
            return null;
        }
        return targetUri;
    }

    private boolean isValidGitPathVariable(Object value) {
        if (value == null) {
            return false;
        }
        String text = StringUtils.trimToEmpty(String.valueOf(value));
        return StringUtils.isNotBlank(text) && text.matches("^[A-Za-z0-9-]+$");
    }

    private <T> List<T> readJsonResponse(String jsonResponse, Class<T> type) throws Exception {
        List<T> items;
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, type);
        items = objectMapper.readValue(jsonResponse, listType);
        debug("List.of(DTO)={}", items);
        return items;
    }

    protected String buildAuthorizationHeader(String token) {
        String trimmedToken = StringUtils.trimToEmpty(token);
        return StringUtils.startsWithIgnoreCase(trimmedToken, "Bearer ")
                ? trimmedToken
                : "Bearer " + trimmedToken;
    }
}
