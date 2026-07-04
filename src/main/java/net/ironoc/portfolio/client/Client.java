package net.ironoc.portfolio.client;

import module java.base;

import net.ironoc.portfolio.dto.RepositoryIssueCreateDto;
import net.ironoc.portfolio.dto.RepositoryIssueDto;

public interface Client {

    <T> List<T> callGitHubApi(String uri, Class<T> type, String httpMethod, Map<String, Object> uriVariables);

    RepositoryIssueDto createGitHubIssue(String uri, RepositoryIssueCreateDto requestBody,
                                         Map<String, Object> uriVariables);
}
