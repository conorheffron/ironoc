package net.ironoc.portfolio.service;

import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GitRepoCache {

    void put(String userId, List<RepositoryDetailDomain> repositoryDetails);

    List<RepositoryDetailDomain> get(String userId);

    void clear();
}
