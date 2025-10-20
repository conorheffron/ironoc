package net.ironoc.portfolio.service;

import module java.base;

import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import net.ironoc.portfolio.domain.RepositoryIssueDomain;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.dto.RepositoryIssueDto;

public interface GitDetails {

    List<RepositoryDetailDto> getRepoDetails(String username, boolean isJob);

    List<RepositoryDetailDomain> mapRepositoriesToResponse(
            List<RepositoryDetailDto> repositoryDetailDtos);

    List<RepositoryDetailDto> mapResponseToRepositories(
            List<RepositoryDetailDomain> repositoryDetailDomains);

    List<RepositoryIssueDto> getIssues(String userId, String repo, boolean isJob);

    List<RepositoryIssueDomain> mapIssuesToResponse(List<RepositoryIssueDto> repositoryIssueDtos);
}
