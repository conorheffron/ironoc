package com.ironoc.portfolio.job;

import com.ironoc.portfolio.service.GitRepoCache;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import com.ironoc.portfolio.service.GitDetails;
import jakarta.annotation.PreDestroy;

import java.util.List;
import java.util.Set;

public class GitDetailsRunnable implements Runnable {

    private final GitRepoCache gitRepoCache;

    private final GitDetails gitDetails;

    private final Set<String> userIds;

    public GitDetailsRunnable(GitRepoCache gitRepoCache,
                              GitDetails gitDetails,
                              Set<String> usersIds) {
        this.gitRepoCache = gitRepoCache;
        this.gitDetails = gitDetails;
        this.userIds = usersIds;
    }

    @Override
    public void run() {
        for (String userId : userIds) {
            List<RepositoryDetailDto> repoDetails = gitDetails.getRepoDetails(userId);
            gitRepoCache.put(userId, repoDetails);
        }
    }

    @PreDestroy
    public void tearDown() {
        this.userIds.clear();
        this.tearDown();
    }
}
