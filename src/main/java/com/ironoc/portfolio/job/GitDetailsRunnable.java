package com.ironoc.portfolio.job;

import com.ironoc.portfolio.logger.AbstractLogger;
import com.ironoc.portfolio.service.GitRepoCache;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import com.ironoc.portfolio.service.GitDetails;
import jakarta.annotation.PreDestroy;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class GitDetailsRunnable extends AbstractLogger implements Runnable {

    private final GitRepoCache gitRepoCache;

    private final GitDetails gitDetails;

    public final Set<String> userIds;

    public static final String USERNAME_HOME_PAGE = "conorheffron";

    public GitDetailsRunnable(GitRepoCache gitRepoCache,
                              GitDetails gitDetails) {
        this.gitRepoCache = gitRepoCache;
        this.gitDetails = gitDetails;
        this.userIds = populateUserIds();
    }

    private Set<String> populateUserIds() {
        // set user ID list
        Set<String> userIds = new HashSet<>();
        userIds.add(USERNAME_HOME_PAGE);
        return userIds;
    }

    @Override
    public void run() {
        info("GitDetailsRunnable running for userIds={}", getUserIds());
        for (String userId : userIds) {
            List<RepositoryDetailDto> dtos = gitDetails.getRepoDetails(userId);
            info("-----Running GIT details job for userIds={}, repositoryDetailDtos={}", getUserIds(), dtos);
            gitRepoCache.put(userId, gitDetails.mapRepositoriesToResponse(dtos));
        }
    }

    @PreDestroy
    public void tearDown() {
        info("Entering GitDetailsRunnable.tearDown for userIds={}", getUserIds());
        this.userIds.clear();
    }
}
