package com.ironoc.portfolio.client;

import com.ironoc.portfolio.aws.SecretManager;
import com.ironoc.portfolio.config.PropertyConfigI;
import com.ironoc.portfolio.utils.UrlUtils;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
@Slf4j
public class GitClient implements Client {

    private final PropertyConfigI propertyConfig;

    private final SecretManager secretManager;

    private final UrlUtils urlUtils;

    public GitClient(PropertyConfigI propertyConfig,
                     SecretManager secretManager,
                     UrlUtils urlUtils) {
        this.propertyConfig = propertyConfig;
        this.secretManager = secretManager;
        this.urlUtils = urlUtils;
    }

    @Override
    public HttpsURLConnection createConn(String url) throws IOException {
        if (!urlUtils.isValidURL(url)) {
            log.error("The url is not valid for GIT client connection, url={}", url);
            return null;
        }
        URL apiUrlEndpoint = new URL(UriComponentsBuilder.fromHttpUrl(url).toUriString());
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
}
