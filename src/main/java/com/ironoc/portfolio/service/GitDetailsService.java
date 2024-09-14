package com.ironoc.portfolio.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironoc.portfolio.client.Client;
import com.ironoc.portfolio.config.PropertyConfigI;
import com.ironoc.portfolio.domain.RepositoryDetailDomain;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import com.ironoc.portfolio.logger.AbstractLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitDetailsService extends AbstractLogger implements GitDetails {

    private final PropertyConfigI propertyConfig;

    private final ObjectMapper objectMapper;

    private final Client gitClient;

    @Autowired
    public GitDetailsService(PropertyConfigI propertyConfig, ObjectMapper objectMapper, Client gitClient) {
        this.propertyConfig = propertyConfig;
        this.objectMapper = objectMapper;
        this.gitClient = gitClient;
;    }

    @Override
    public List<RepositoryDetailDto> getRepoDetails(String username) {
        String url = propertyConfig.getGitApiEndpoint() + username + propertyConfig.getGitReposUri();
        info("Triggering repositories GET request: url={}", url);
        List<RepositoryDetailDto> repositoryDetailDtos = new ArrayList<>();
        InputStream inputStream = null;
        try {
            HttpsURLConnection conn = gitClient.createConn(url);
            inputStream = gitClient.readInputStream(conn);
            repositoryDetailDtos = Arrays.asList(objectMapper.readValue(inputStream, RepositoryDetailDto[].class));
            debug("repositoryDetailDtos={}", repositoryDetailDtos);
        } catch(IOException ex) {
            error("Unexpected error occurred while retrieving repo details for user=" + username, ex);
        } finally {
            try {
                if (inputStream != null) {
                    gitClient.closeConn(inputStream);
                } else {
                    warn("Input stream already closed.");
                }
            } catch (IOException ex) {
                error("Unexpected error occurred while closing input stream.", ex);
            }
        }
        return repositoryDetailDtos;
    }

    @Override
    public List<RepositoryDetailDomain> mapRepositoriesToResponse(List<RepositoryDetailDto> repositoryDetailDtos) {
        return repositoryDetailDtos.stream()
                .map(repositoryDetailDto -> RepositoryDetailDomain.builder()
                        .name(repositoryDetailDto.getName())
                        .description(repositoryDetailDto.getDescription())
                        .appHome(repositoryDetailDto.getHomePage())
                        .topics(StringUtils.joinWith(", ", repositoryDetailDto.getTopics()))
                        .repoUrl(repositoryDetailDto.getHtmlUrl())
                        .build())
                .collect(Collectors.toList());
    }
}
