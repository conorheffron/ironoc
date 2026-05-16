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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GitClient extends AbstractLogger implements Client {

    private final PropertyConfigI propertyConfig;

    private final SecretManager secretManager;

    private final UrlUtils urlUtils;

    private final ObjectMapper objectMapper;

    public GitClient(PropertyConfigI propertyConfig,
                     SecretManager secretManager,
                     UrlUtils urlUtils,
                     ObjectMapper objectMapper) {
        this.propertyConfig = propertyConfig;
        this.secretManager = secretManager;
        this.urlUtils = urlUtils;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> List<T> callGitHubApi(String apiUri, String uri, Class<T> type, String httpMethod) {
        info("Triggering GET request: url={}", apiUri);
        List<T> dtos = new ArrayList<>();
        InputStream inputStream = null;
        try {
            HttpsURLConnection conn = this.createConn(apiUri, uri, httpMethod);
            if (conn == null) {
                error("Failed to create connection");
                return Collections.emptyList();
            }
            inputStream = this.readInputStream(conn);
            Map<String, List<String>> map = conn.getHeaderFields();
            List<String> linkHeader = map.get("Link");
            if (linkHeader != null && !linkHeader.isEmpty()) {
                info("Link.Header: {}", linkHeader);
            }
            dtos = readJsonResponse(inputStream, type);
        } catch (Exception ex) {
            error("Unexpected error occurred while retrieving data.", ex);
        } finally {
            try {
                if (inputStream != null) {
                    this.closeConn(inputStream);
                } else {
                    warn("Input stream already closed.");
                }
            } catch (IOException ex) {
                error("Unexpected error occurred while closing input stream.", ex);
            }
        }
        return dtos;
    }

    @Override
    public RepositoryIssueDto createGitHubIssue(String apiUri, String uri, RepositoryIssueCreateDto requestBody) {
        info("Triggering POST request: url={}", apiUri);
        InputStream inputStream = null;
        try {
            HttpsURLConnection conn = this.createConn(apiUri, uri, HttpMethod.POST.name());
            if (conn == null) {
                error("Failed to create connection");
                return null;
            }
            conn.setRequestProperty("Accept", "application/vnd.github+json");
            conn.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            try (OutputStream outputStream = conn.getOutputStream()) {
                outputStream.write(objectMapper.writeValueAsBytes(requestBody));
                outputStream.flush();
            }
            inputStream = this.readInputStream(conn);
            return objectMapper.readValue(this.convertInputStreamToString(inputStream), RepositoryIssueDto.class);
        } catch (Exception ex) {
            error("Unexpected error occurred while creating issue.", ex);
        } finally {
            try {
                if (inputStream != null) {
                    this.closeConn(inputStream);
                } else {
                    warn("Input stream already closed.");
                }
            } catch (IOException ex) {
                error("Unexpected error occurred while closing input stream.", ex);
            }
        }
        return null;
    }

    private <T> List<T> readJsonResponse(InputStream inputStream, Class<T> type) throws Exception {
        List<T> items;
        String jsonResponse = convertInputStreamToString(inputStream);
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, type);
        items = objectMapper.readValue(jsonResponse, listType);
        debug("List.of(DTO)={}", items);
        return items;
    }

    @Override
    public HttpsURLConnection createConn(String url, String baseUrl, String httpMethod) throws IOException {
        URL urlBase = new URL(baseUrl);
        URL urlToValidate = new URL(url);
        int basePort = urlBase.getPort() == -1 ? urlBase.getDefaultPort() : urlBase.getPort();
        int targetPort = urlToValidate.getPort() == -1 ? urlToValidate.getDefaultPort() : urlToValidate.getPort();
        boolean isMatchingHost = StringUtils.equalsIgnoreCase(urlToValidate.getProtocol(), urlBase.getProtocol())
                && StringUtils.equalsIgnoreCase(urlToValidate.getHost(), urlBase.getHost())
                && basePort == targetPort;
        if (!urlUtils.isValidURL(url) || !isMatchingHost) {
            log.error("The url is not valid for GIT client connection, url={}", url);
            return null;
        }
        URL apiUrlEndpoint = new URL(urlBase.getProtocol(), urlBase.getHost(),
                targetPort, urlToValidate.getFile());
        HttpsURLConnection conn = (HttpsURLConnection) apiUrlEndpoint.openConnection();
        String token = secretManager.getGitSecret();
        if (StringUtils.isBlank(token)) {
            log.warn("GIT token not set, the lower request rate will apply");
        } else {
            conn.setRequestProperty("Authorization", this.buildAuthorizationHeader(token));
        }
        conn.setRequestMethod(httpMethod);
        HttpURLConnection.setFollowRedirects(propertyConfig.getGitFollowRedirects());
        conn.setConnectTimeout(propertyConfig.getGitTimeoutConnect());
        conn.setReadTimeout(propertyConfig.getGitTimeoutRead());
        conn.setInstanceFollowRedirects(propertyConfig.getGitInstanceFollowRedirects());
        return conn;
    }

    @Override
    public InputStream readInputStream(HttpsURLConnection conn) throws IOException {
        return conn.getInputStream();
    }

    @Override
    public void closeConn(InputStream inputStream) throws IOException {
        inputStream.close();
    }

    protected String convertInputStreamToString(InputStream inputStream) throws Exception {
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    protected String buildAuthorizationHeader(String token) {
        String trimmedToken = StringUtils.trimToEmpty(token);
        return StringUtils.startsWithIgnoreCase(trimmedToken, "Bearer ")
                ? trimmedToken
                : "Bearer " + trimmedToken;
    }
}
