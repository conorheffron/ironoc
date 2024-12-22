package net.ironoc.portfolio.job;

import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.dto.RepositoryIssueDto;
import net.ironoc.portfolio.service.GitDetails;
import net.ironoc.portfolio.service.GitProjectCache;
import net.ironoc.portfolio.service.GitRepoCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GitDetailsRunnableTest {

    @Mock
    private GitDetails gitDetailsMock;

    @Mock
    private GitRepoCache gitRepoCacheMock;

    @Mock
    private GitProjectCache gitProjectCacheMock;

    @Mock
    private RepositoryIssueDto repositoryIssueDtoMock;

    @Mock
    private RepositoryDetailDto repositoryDetailDtoMock;

    @Mock
    private PropertyConfigI propertyConfigMock;

    private GitDetailsRunnable gitDetailsRunnable;

    @BeforeEach
    public void setup() {
        when(propertyConfigMock.getGitApiEndpointUserIdsCache()).thenReturn(List.of("conorheffron"));
        when(propertyConfigMock.getGitApiEndpointProjectsCache())
                .thenReturn(List.of("ironoc", "ironoc-db", "booking-sys"));

        gitDetailsRunnable = new GitDetailsRunnable(gitRepoCacheMock, gitProjectCacheMock, gitDetailsMock, propertyConfigMock);
    }

    @Test
    public void test_run_tearDown_success() {
        // given
        when(gitDetailsMock.getRepoDetails(anyString()))
                .thenReturn(Collections.singletonList(repositoryDetailDtoMock));
        when(gitDetailsMock.getIssues(anyString(), anyString()))
                .thenReturn(Collections.singletonList(repositoryIssueDtoMock));

        // when
        gitDetailsRunnable.run();

        // then
        verify(propertyConfigMock).getGitApiEndpointProjectsCache();
        verify(propertyConfigMock).getGitApiEndpointUserIdsCache();
        verify(gitDetailsMock).getRepoDetails(anyString());
        verify(gitRepoCacheMock).put(anyString(), anyList());
        verify(gitDetailsMock).mapRepositoriesToResponse(anyList());
        verify(gitProjectCacheMock, times(3)).put(anyString(), anyString(), anyList());

        assertThat(gitDetailsRunnable.getUserIds(), hasSize(1));

        gitDetailsRunnable.tearDown();
        assertThat(gitDetailsRunnable.getUserIds(), emptyIterable());
    }
}

