package com.ironoc.portfolio.job;

import com.ironoc.portfolio.service.GitRepoCache;
import com.ironoc.portfolio.service.GitDetails;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GitDetailsJob {

    private final GitRepoCache gitRepoCache;

    private final GitDetails gitDetails;

    public static final String USERNAME_HOME_PAGE = "conorheffron";

    @Autowired
    public GitDetailsJob(GitDetails gitDetails, GitRepoCache gitRepoCache) {
        this.gitRepoCache = gitRepoCache;
        this.gitDetails = gitDetails;
    }

    @PostConstruct
    public void populateCache() {
        triggerJob();
    }

    @Scheduled(cron = "0 1 1 ? * *")
    public void triggerGitDetailsJob() {
        triggerJob();
    }

    private void triggerJob() {
        // set user ID list
        Set<String> userIds = new HashSet<>();
        userIds.add(USERNAME_HOME_PAGE);

        // run background process to update cache
        GitDetailsRunnable runnable = new GitDetailsRunnable(gitRepoCache, gitDetails, userIds);
        runnable.run();
    }
}
