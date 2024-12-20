package net.ironoc.portfolio.job;

import net.ironoc.portfolio.service.GitDetails;
import net.ironoc.portfolio.service.GitProjectCache;
import net.ironoc.portfolio.service.GitRepoCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GitDetailsJobTest {

    @InjectMocks
    private GitDetailsJob gitDetailsJob;

    @Mock
    private GitDetails gitDetails;

    @Mock
    private GitRepoCache gitRepoCache;

    @Mock
    private GitProjectCache gitProjectCache;

    @Test
    public void test_triggerGitDetailsJob_success() {
        // when
        gitDetailsJob.triggerGitDetailsJob();

        // then
        verify(gitDetails).getRepoDetails(anyString());
        verify(gitRepoCache).put(anyString(), anyList());
    }

    @Test
    public void test_populateCache_success() {
        // when
        gitDetailsJob.populateCache();

        // then
        verify(gitDetails).getRepoDetails(anyString());
        verify(gitRepoCache).put(anyString(), anyList());
    }
}
