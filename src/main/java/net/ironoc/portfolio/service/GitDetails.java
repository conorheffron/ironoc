package net.ironoc.portfolio.service;

import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import net.ironoc.portfolio.domain.RepositoryIssueDomain;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.dto.RepositoryIssueDto;

import java.util.List;

public interface GitDetails {

    List<RepositoryDetailDto> getRepoDetails(String username);

    List<RepositoryDetailDomain> mapRepositoriesToResponse(
            List<RepositoryDetailDto> repositoryDetailDtos);

    List<RepositoryDetailDto> mapResponseToRepositories(
            List<RepositoryDetailDomain> repositoryDetailDomains);

    List<RepositoryIssueDto> getIssues(String userId, String repo);

    List<RepositoryIssueDomain> mapIssuesToResponse(List<RepositoryIssueDto> repositoryIssueDtos);
}
