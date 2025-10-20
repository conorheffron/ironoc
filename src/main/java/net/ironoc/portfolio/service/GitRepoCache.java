package net.ironoc.portfolio.service;

import module java.base;

import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import org.springframework.stereotype.Service;

@Service
public interface GitRepoCache {

    void put(String userId, List<RepositoryDetailDomain> repositoryDetails);

    List<RepositoryDetailDomain> get(String userId);

    void remove(String key);
}
