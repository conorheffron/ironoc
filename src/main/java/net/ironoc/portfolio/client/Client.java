package net.ironoc.portfolio.client;

import module java.base;

public interface Client {

    <T> List<T> callGitHubApi(String apiUri, String uri, Class<T> type, String httpMethod);

    HttpsURLConnection createConn(String url, String baseUrl, String httpMethod) throws IOException;

    InputStream readInputStream(HttpsURLConnection conn) throws IOException;

    void closeConn(InputStream inputStream) throws IOException;
}
