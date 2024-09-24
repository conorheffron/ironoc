package com.ironoc.portfolio.service;

import com.ironoc.portfolio.dto.RepositoryDetailDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GitRepoCache {

    void put(String userId, List<RepositoryDetailDto> repositoryDetails);

    List<RepositoryDetailDto> get(String userId);
}
