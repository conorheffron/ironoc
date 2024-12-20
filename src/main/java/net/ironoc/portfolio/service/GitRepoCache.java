package net.ironoc.portfolio.service;

import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GitRepoCache {

    void put(String userId, List<RepositoryDetailDomain> repositoryDetails);

    List<RepositoryDetailDomain> get(String userId);

    void tearDown();
}
