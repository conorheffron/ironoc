package net.ironoc.portfolio.service;

import net.ironoc.portfolio.domain.RepositoryIssueDomain;

import java.util.List;

public interface GitProjectCache {

    void put(String userId, String project, List<RepositoryIssueDomain> repositoryIssueDomains);

    List<RepositoryIssueDomain> get(String userId, String project);

    void tearDown();
}
