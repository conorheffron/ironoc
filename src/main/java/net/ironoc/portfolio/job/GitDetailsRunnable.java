package net.ironoc.portfolio.job;

import net.ironoc.portfolio.dto.RepositoryIssueDto;
import net.ironoc.portfolio.logger.AbstractLogger;
import net.ironoc.portfolio.service.GitProjectCache;
import net.ironoc.portfolio.service.GitRepoCache;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.service.GitDetails;
import jakarta.annotation.PreDestroy;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class GitDetailsRunnable extends AbstractLogger implements Runnable {

    private final GitRepoCache gitRepoCache;

    private final GitProjectCache gitProjectCache;

    private final GitDetails gitDetails;

    public final Set<String> userIds;

    // TODO move to configurable, create project set & populate
    public static final String IRONOC_GIT_USER = "conorheffron";
    public static final String IRONOC_GIT_PROJECT = "ironoc";

    public GitDetailsRunnable(GitRepoCache gitRepoCache,
                              GitProjectCache gitProjectCache,
                              GitDetails gitDetails) {
        this.gitRepoCache = gitRepoCache;
        this.gitProjectCache = gitProjectCache;
        this.gitDetails = gitDetails;
        this.userIds = populateUserIds();
    }

    private Set<String> populateUserIds() {
        // set user ID list
        Set<String> userIds = new HashSet<>();
        userIds.add(IRONOC_GIT_USER);
        return userIds;
    }

    @Override
    public void run() {
        info("GitDetailsRunnable running for userIds={}", getUserIds());
        gitRepoCache.tearDown();
        gitProjectCache.tearDown();
        for (String userId : userIds) {
            List<RepositoryDetailDto> repositoryDetailDtos = gitDetails.getRepoDetails(userId);
            info("Running GIT details job for userIds={}, repositoryDetailDtos={}",
                    userId, repositoryDetailDtos);
            gitRepoCache.put(userId, gitDetails.mapRepositoriesToResponse(repositoryDetailDtos));

            List<RepositoryIssueDto> issuesDtos = gitDetails.getIssues(userId, IRONOC_GIT_PROJECT);
            info("Running GIT details job for userIds={}, project={}, repositoryDetailDtos={}", userId,
                    IRONOC_GIT_PROJECT, issuesDtos);
            gitProjectCache.put(userId, IRONOC_GIT_PROJECT, gitDetails.mapIssuesToResponse(issuesDtos));

        }
        info("GitDetailsRunnable completed for userIds={}", getUserIds());
    }

    @PreDestroy
    public void tearDown() {
        info("Entering GitDetailsRunnable.tearDown for userIds={}", getUserIds());
        this.userIds.clear();
    }
}
