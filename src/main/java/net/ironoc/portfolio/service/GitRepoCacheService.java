package net.ironoc.portfolio.service;

import module java.base;

import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public final class GitRepoCacheService extends AbstractGitCache implements GitRepoCache {

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

    @Override
    public void remove(String key) {
        userGitDetails.remove(key);
    }

    @PreDestroy
    public void tearDown() {
        this.clear(userGitDetails);
    }
}
