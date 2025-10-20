package net.ironoc.portfolio.service;

import module java.base;

import net.ironoc.portfolio.domain.RepositoryIssueDomain;

public interface GitProjectCache {

    void put(String userId, String project, List<RepositoryIssueDomain> repositoryIssueDomains);

    List<RepositoryIssueDomain> get(String userId, String project);

    void remove(String key);
}
