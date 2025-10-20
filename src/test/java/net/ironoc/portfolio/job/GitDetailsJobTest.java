package net.ironoc.portfolio.job;

import module java.base;

import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.dto.RepositoryIssueDto;
import net.ironoc.portfolio.service.GitDetails;
import net.ironoc.portfolio.service.GitProjectCache;
import net.ironoc.portfolio.service.GitRepoCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class GitDetailsJobTest {

    @InjectMocks
    private GitDetailsJob gitDetailsJob;

    @Mock
    private GitDetails gitDetailsMock;

    @Mock
    private GitRepoCache gitRepoCache;

    @Mock
    private GitProjectCache gitProjectCache;

    @Mock
    private RepositoryIssueDto repositoryIssueDtoMock;

    @Mock
    private RepositoryDetailDto repositoryDetailDtoMock;

    @Mock
    private PropertyConfigI propertyConfigMock;

    @Test
    public void test_triggerGitDetailsJob_success() {
        // given
        when(propertyConfigMock.isCacheJobEnabled()).thenReturn(true);
        when(propertyConfigMock.getGitApiEndpointUserIdsCache()).thenReturn(List.of("conorheffron"));
        when(propertyConfigMock.getGitApiEndpointProjectsCache())
                .thenReturn(List.of("ironoc", "booking-sys"));
        when(gitDetailsMock.getRepoDetails(anyString(), anyBoolean()))
                .thenReturn(Collections.singletonList(repositoryDetailDtoMock));
        when(gitDetailsMock.getIssues(anyString(), anyString(), anyBoolean()))
                .thenReturn(Collections.singletonList(repositoryIssueDtoMock));

        // when
        gitDetailsJob.triggerGitDetailsJob();

        // then
        verify(propertyConfigMock).isCacheJobEnabled();
        verify(propertyConfigMock).getGitApiEndpointProjectsCache();
        verify(propertyConfigMock).getGitApiEndpointUserIdsCache();
        verify(gitDetailsMock).getRepoDetails(anyString(), anyBoolean());
        verify(gitRepoCache).put(anyString(), anyList());
        verify(gitDetailsMock, times(2)).getIssues(anyString(), anyString(), anyBoolean());
        verify(gitProjectCache, times(2)).put(anyString(), anyString(), anyList());
    }

    @Test
    public void test_populateCache_success() {
        // given
        when(propertyConfigMock.isCacheJobEnabled()).thenReturn(true);
        when(propertyConfigMock.getGitApiEndpointUserIdsCache()).thenReturn(List.of("ironoc-test-id"));
        when(propertyConfigMock.getGitApiEndpointProjectsCache())
                .thenReturn(List.of("ironoc", "booking-sys", "ironoc-db", "nba-stats"));
        when(gitDetailsMock.getRepoDetails(anyString(), anyBoolean()))
                .thenReturn(Collections.singletonList(repositoryDetailDtoMock));
        when(gitDetailsMock.getIssues(anyString(), anyString(), anyBoolean()))
                .thenReturn(Collections.singletonList(repositoryIssueDtoMock));

        // when
        gitDetailsJob.populateCache();

        // then
        verify(propertyConfigMock).isCacheJobEnabled();
        verify(propertyConfigMock).getGitApiEndpointProjectsCache();
        verify(propertyConfigMock).getGitApiEndpointUserIdsCache();
        verify(gitDetailsMock).getRepoDetails(anyString(), anyBoolean());
        verify(gitRepoCache).put(anyString(), anyList());
        verify(gitDetailsMock, times(4)).getIssues(anyString(), anyString(), anyBoolean());
        verify(gitProjectCache, times(4)).put(anyString(), anyString(), anyList());
    }

    @Test
    public void test_triggerGitDetailsJob_fail() {
        // given
        when(propertyConfigMock.isCacheJobEnabled()).thenReturn(false);

        // when
        gitDetailsJob.triggerGitDetailsJob();

        // then
        verify(propertyConfigMock).isCacheJobEnabled();
        verify(propertyConfigMock, never()).getGitApiEndpointProjectsCache();
        verify(propertyConfigMock, never()).getGitApiEndpointUserIdsCache();
        verify(gitDetailsMock, never()).getRepoDetails(anyString(), anyBoolean());
        verify(gitRepoCache, never()).put(anyString(), anyList());
        verify(gitDetailsMock, never()).getIssues(anyString(), anyString(), anyBoolean());
        verify(gitProjectCache, never()).put(anyString(), anyString(), anyList());
    }

    @Test
    public void test_populateCache_fail() {
        // given
        when(propertyConfigMock.isCacheJobEnabled()).thenReturn(false);

        // when
        gitDetailsJob.populateCache();

        // then
        verify(propertyConfigMock).isCacheJobEnabled();
        verify(propertyConfigMock, never()).getGitApiEndpointProjectsCache();
        verify(propertyConfigMock, never()).getGitApiEndpointUserIdsCache();
        verify(gitDetailsMock, never()).getRepoDetails(anyString(), anyBoolean());
        verify(gitRepoCache, never()).put(anyString(), anyList());
        verify(gitDetailsMock, never()).getIssues(anyString(), anyString(), anyBoolean());
        verify(gitProjectCache, never()).put(anyString(), anyString(), anyList());
    }
}
