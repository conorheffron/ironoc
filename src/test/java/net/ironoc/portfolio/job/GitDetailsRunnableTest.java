package net.ironoc.portfolio.job;

import net.ironoc.portfolio.service.GitDetails;
import net.ironoc.portfolio.service.GitRepoCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
public class GitDetailsRunnableTest {

    @InjectMocks
    private GitDetailsRunnable gitDetailsRunnable;

    @Mock
    private GitDetails gitDetailsMock;

    @Mock
    private GitRepoCache gitRepoCacheMock;

    @Test
    public void test_run_tearDown_success() {
        // when
        gitDetailsRunnable.run();

        // then
        verify(gitDetailsMock).getRepoDetails(anyString());
        verify(gitRepoCacheMock).put(anyString(), anyList());
        verify(gitDetailsMock).mapRepositoriesToResponse(anyList());

        assertThat(gitDetailsRunnable.getUserIds(), hasSize(1));

        gitDetailsRunnable.tearDown();
        assertThat(gitDetailsRunnable.getUserIds(), emptyIterable());
    }
}

