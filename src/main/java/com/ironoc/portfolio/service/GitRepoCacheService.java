package com.ironoc.portfolio.service;

import com.ironoc.portfolio.domain.RepositoryDetailDomain;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GitRepoCacheService implements GitRepoCache {

    private final Map<String, List<RepositoryDetailDomain>> userGitDetails;

    public GitRepoCacheService() {
        this.userGitDetails = new HashMap<>();
    }

    @Override
    public void put(String userId, List<RepositoryDetailDomain> repositoryDetails) {
        userGitDetails.put(userId, repositoryDetails);
    }

    @Override
    public List<RepositoryDetailDomain> get(String userId) {
        return userGitDetails.get(userId);
    }

    @PreDestroy
    public void tearDown() {
        this.userGitDetails.clear();
    }
}
