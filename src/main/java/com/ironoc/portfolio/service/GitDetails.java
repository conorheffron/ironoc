package com.ironoc.portfolio.service;

import com.ironoc.portfolio.domain.RepositoryDetailDomain;
import com.ironoc.portfolio.dto.RepositoryDetailDto;

import java.util.List;

public interface GitDetails {

    List<RepositoryDetailDto> getRepoDetails(String username);

    List<RepositoryDetailDomain> mapRepositoriesToResponse(List<RepositoryDetailDto> repositoryDetailDtos);
}
