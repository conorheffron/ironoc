package net.ironoc.portfolio.job;

import net.ironoc.portfolio.service.GitProjectCache;
import net.ironoc.portfolio.service.GitRepoCache;
import net.ironoc.portfolio.service.GitDetails;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GitDetailsJob {

    private final GitRepoCache gitRepoCache;

    private final GitProjectCache gitProjectCache;

    private final GitDetails gitDetails;

    @Autowired
    public GitDetailsJob(GitDetails gitDetails, GitRepoCache gitRepoCache, GitProjectCache gitProjectCache) {
        this.gitRepoCache = gitRepoCache;
        this.gitDetails = gitDetails;
        this.gitProjectCache = gitProjectCache;
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
        // run background process to update cache
        GitDetailsRunnable runnable = new GitDetailsRunnable(gitRepoCache, gitProjectCache, gitDetails);
        runnable.run();
    }
}
