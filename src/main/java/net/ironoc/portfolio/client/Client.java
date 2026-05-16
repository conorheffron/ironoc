package net.ironoc.portfolio.client;

import module java.base;

import net.ironoc.portfolio.dto.RepositoryIssueCreateDto;
import net.ironoc.portfolio.dto.RepositoryIssueDto;

public interface Client {

    <T> List<T> callGitHubApi(String apiUri, String uri, Class<T> type, String httpMethod);

    RepositoryIssueDto createGitHubIssue(String apiUri, String uri, RepositoryIssueCreateDto requestBody);

    HttpsURLConnection createConn(String url, String baseUrl, String httpMethod) throws IOException;

    InputStream readInputStream(HttpsURLConnection conn) throws IOException;

    void closeConn(InputStream inputStream) throws IOException;
}
