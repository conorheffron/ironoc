package com.ironoc.portfolio.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.ironoc.portfolio.aws.SecretManager;
import com.ironoc.portfolio.config.PropertyConfigI;
import com.ironoc.portfolio.logger.AbstractLogger;
import com.ironoc.portfolio.utils.UrlUtils;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    public <T> List<T> callGitHubApi(String username, String apiUri, String uri, Class<T> type) {
        info("Triggering GET request: url={}", apiUri);
        List<T> dtos = new ArrayList<>();
        InputStream inputStream = null;
        try {
            HttpsURLConnection conn = this.createConn(apiUri, uri);
            inputStream = this.readInputStream(conn);
            String jsonResponse = convertInputStreamToString(inputStream);
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            dtos = objectMapper.readValue(jsonResponse, listType);
            debug("dtos={}", dtos);
        } catch(Exception ex) {
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
    public HttpsURLConnection createConn(String url, String baseUrl) throws IOException {
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
        conn.setRequestMethod(HttpMethod.GET.name());
        conn.setFollowRedirects(propertyConfig.getGitFollowRedirects());
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

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
