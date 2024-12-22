package net.ironoc.portfolio.job;

import net.ironoc.portfolio.config.PropertyConfigI;
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

    private final PropertyConfigI propertyConfig;

    @Autowired
    public GitDetailsJob(GitDetails gitDetails,
                         GitRepoCache gitRepoCache,
                         GitProjectCache gitProjectCache,
                         PropertyConfigI propertyConfig) {
        this.gitRepoCache = gitRepoCache;
        this.gitDetails = gitDetails;
        this.gitProjectCache = gitProjectCache;
        this.propertyConfig = propertyConfig;
    }

    @PostConstruct
    public void populateCache() {
        triggerJob();
    }

    @Scheduled(cron = "${net.ironoc.portfolio.github.cron-job}")
    public void triggerGitDetailsJob() {
        triggerJob();
    }

    private void triggerJob() {
        // run background process to update cache
        GitDetailsRunnable runnable = new GitDetailsRunnable(gitRepoCache, gitProjectCache, gitDetails, propertyConfig);
        runnable.run();
    }
}
