package net.ironoc.portfolio.client;

import module java.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import net.ironoc.portfolio.aws.SecretManager;
import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.logger.AbstractLogger;
import net.ironoc.portfolio.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
                error("Failed to created connection");
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
        String base = urlBase.getProtocol() + "://" + urlBase.getHost();
        if (!urlUtils.isValidURL(url) || !url.startsWith(base)) {
            log.error("The url is not valid for GIT client connection, url={}", url);
            return null;
        }
        URL apiUrlEndpoint = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) apiUrlEndpoint.openConnection();
        String token = secretManager.getGitSecret();
        if (StringUtils.isBlank(token)) {
            log.warn("GIT token not set, the lower request rate will apply");
        } else {
            conn.setRequestProperty("Authorization", token);
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
}
