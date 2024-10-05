package com.ironoc.portfolio.job;

import com.ironoc.portfolio.service.GitRepoCache;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import com.ironoc.portfolio.service.GitDetails;
import jakarta.annotation.PreDestroy;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class GitDetailsRunnable implements Runnable {

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
        for (String userId : userIds) {
            List<RepositoryDetailDto> dtos = gitDetails.getRepoDetails(userId);
            gitRepoCache.put(userId, gitDetails.mapRepositoriesToResponse(dtos));
        }
    }

    @PreDestroy
    public void tearDown() {
        this.userIds.clear();
    }
}
