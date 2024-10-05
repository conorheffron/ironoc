package com.ironoc.portfolio.service;

import com.ironoc.portfolio.domain.RepositoryDetailDomain;
import com.ironoc.portfolio.domain.RepositoryIssueDomain;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import com.ironoc.portfolio.dto.RepositoryIssueDto;

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
