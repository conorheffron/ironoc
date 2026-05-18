package net.ironoc.portfolio.client;

import module java.base;

public interface Client {

    <T> List<T> callGitHubApi(String uri, Class<T> type, String httpMethod, Map<String, Object> uriVariables);
}
