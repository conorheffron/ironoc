package net.ironoc.portfolio.job;

import net.ironoc.portfolio.config.PropertyConfigI;
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

    private final PropertyConfigI propertyConfig;

    public final Set<String> userIds;

    public final Set<String> projects;

    public GitDetailsRunnable(GitRepoCache gitRepoCache,
                              GitProjectCache gitProjectCache,
                              GitDetails gitDetails,
                              PropertyConfigI propertyConfig) {
        this.gitRepoCache = gitRepoCache;
        this.gitProjectCache = gitProjectCache;
        this.gitDetails = gitDetails;
        this.propertyConfig = propertyConfig;
        this.userIds = populateUserIds();
        this.projects = populateProjects();
    }

    protected Set<String> populateUserIds() {
        // set user ID list
        return new HashSet<>(propertyConfig.getGitApiEndpointUserIdsCache());
    }

    protected Set<String> populateProjects() {
        // set project list
        return new HashSet<>(propertyConfig.getGitApiEndpointProjectsCache());
    }

    @Override
    public void run() {
        info("GitDetailsRunnable running for userIds={}", getUserIds());

        for (String userId : userIds) {
            List<RepositoryDetailDto> repositoryDetailDtos = gitDetails.getRepoDetails(userId);
            info("Running GIT details job for userIds={}, repositoryDetailDtos={}",
                    userId, repositoryDetailDtos);
            if (repositoryDetailDtos != null && !repositoryDetailDtos.isEmpty()) {
                gitRepoCache.remove(userId);
                gitRepoCache.put(userId, gitDetails.mapRepositoriesToResponse(repositoryDetailDtos));

                for(String project : getProjects()) {
                    List<RepositoryIssueDto> issuesDtos = gitDetails.getIssues(userId, project);
                    info("Running GIT details job for userIds={}, project={}, repositoryDetailDtos={}", userId,
                            project, issuesDtos);
                    if (issuesDtos != null && !issuesDtos.isEmpty()) {
                        gitProjectCache.remove(userId + project);
                        gitProjectCache.put(userId, project, gitDetails.mapIssuesToResponse(issuesDtos));
                    }
                }
            }
        }
        info("GitDetailsRunnable completed for userIds={}", getUserIds());
    }

    @PreDestroy
    public void tearDown() {
        info("Entering GitDetailsRunnable.tearDown for userIds={}", getUserIds());
        this.userIds.clear();
    }
}
