package net.ironoc.portfolio.client;

import module java.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import net.ironoc.portfolio.aws.SecretManager;
import net.ironoc.portfolio.config.PropertyConfigI;
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

@Component
@Slf4j
public class GitClient extends AbstractLogger implements Client {

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
    public <T> List<T> callGitHubApi(String apiUri, String uri, Class<T> type, String httpMethod) {
        info("Triggering GET request: url={}", apiUri);
        List<T> dtos = Collections.emptyList();
        try {
            URL urlBase = new URL(uri);
            String base = urlBase.getProtocol() + "://" + urlBase.getHost();
            if (!urlUtils.isValidURL(apiUri) || !apiUri.startsWith(base)) {
                log.error("The url is not valid for GIT client connection, url={}", apiUri);
                return Collections.emptyList();
            }
            HttpHeaders headers = new HttpHeaders();
            String token = secretManager.getGitSecret();
            if (StringUtils.isBlank(token)) {
                log.warn("GIT token not set, the lower request rate will apply");
            } else {
                headers.set("Authorization", token);
            }
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUri, HttpMethod.valueOf(httpMethod), entity, String.class);
            List<String> linkHeader = response.getHeaders().get("Link");
            if (linkHeader != null && !linkHeader.isEmpty()) {
                info("Link.Header: {}", linkHeader);
            }
            if (StringUtils.isBlank(response.getBody())) {
                error("Failed to created connection");
                return Collections.emptyList();
            }
            dtos = readJsonResponse(response.getBody(), type);
        } catch (Exception ex) {
            error("Unexpected error occurred while retrieving data.", ex);
        }
        return dtos;
    }

    private <T> List<T> readJsonResponse(String jsonResponse, Class<T> type) throws Exception {
        List<T> items;
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, type);
        items = objectMapper.readValue(jsonResponse, listType);
        debug("List.of(DTO)={}", items);
        return items;
    }
}
