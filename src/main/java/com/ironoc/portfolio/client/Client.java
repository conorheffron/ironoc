package com.ironoc.portfolio.client;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface Client {

    <T> List<T> callGitHubApi(String apiUri, String uri, Class<T> type, String httpMethod);

    void postGitHubApi(String apiUri, String uri, String httpMethod, String jsonBody) throws Exception;

    HttpsURLConnection createConn(String url, String baseUrl, String httpMethod) throws IOException;

    InputStream readInputStream(HttpsURLConnection conn) throws IOException;

    void closeConn(InputStream inputStream) throws IOException;
}
