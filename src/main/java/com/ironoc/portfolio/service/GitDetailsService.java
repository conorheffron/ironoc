package com.ironoc.portfolio.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironoc.portfolio.client.Client;
import com.ironoc.portfolio.config.PropertyConfigI;
import com.ironoc.portfolio.domain.RepositoryDetailDomain;
import com.ironoc.portfolio.domain.RepositoryIssueDomain;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import com.ironoc.portfolio.dto.RepositoryIssueDto;
import com.ironoc.portfolio.job.GitDetailsJob;
import com.ironoc.portfolio.logger.AbstractLogger;
import com.ironoc.portfolio.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitDetailsService extends AbstractLogger implements GitDetails {

    private final PropertyConfigI propertyConfig;

    private final ObjectMapper objectMapper;

    private final Client gitClient;

    private final GitRepoCache gitRepoCache;

    private final UrlUtils urlUtils;

    @Autowired
    public GitDetailsService(PropertyConfigI propertyConfig,
                             ObjectMapper objectMapper,
                             Client gitClient,
                             GitRepoCache gitRepoCache,
                             UrlUtils urlUtils) {
        this.propertyConfig = propertyConfig;
        this.objectMapper = objectMapper;
        this.gitClient = gitClient;
        this.urlUtils = urlUtils;
        this.gitRepoCache = gitRepoCache;
;    }

    @Override
    public List<RepositoryDetailDto> getRepoDetails(String username) {
        // check cache for home page user ID
        if (username.toLowerCase().equals(GitDetailsJob.USERNAME_HOME_PAGE)) {
            List<RepositoryDetailDomain> repoDetails = gitRepoCache.get(GitDetailsJob.USERNAME_HOME_PAGE);
            if (repoDetails != null) {
                return this.mapResponseToRepositories(repoDetails);
            }
        }
        // further end-point validation (contains User ID)
        String uri = propertyConfig.getGitApiEndpointRepos();
        String apiUri = UriComponentsBuilder.fromHttpUrl(uri)
                .buildAndExpand(username)
                .toUriString();
        if (StringUtils.isBlank(apiUri) | StringUtils.isBlank(apiUri)
                | !urlUtils.isValidURL(apiUri)) {
            warn("URL is not valid: url={}", apiUri);
            return Collections.emptyList();
        }
        info("Triggering repositories GET request: url={}", apiUri);
        List<RepositoryDetailDto> repositoryDetailDtos = new ArrayList<>();
        InputStream inputStream = null;
        try {
            HttpsURLConnection conn = gitClient.createConn(apiUri, uri);
            inputStream = gitClient.readInputStream(conn);
            repositoryDetailDtos = Arrays.asList(objectMapper.readValue(inputStream,
                    RepositoryDetailDto[].class));
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
    public List<RepositoryDetailDomain> mapRepositoriesToResponse(
            List<RepositoryDetailDto> repositoryDetailDtos) {
        return repositoryDetailDtos.stream()
                .map(repositoryDetailDto -> RepositoryDetailDomain.builder()
                        .name(repositoryDetailDto.getName())
                        .fullName(parseNull(repositoryDetailDto.getFullName()))
                        .description(parseNull(repositoryDetailDto.getDescription()))
                        .appHome(parseNull(repositoryDetailDto.getHomePage()))
                        .topics(StringUtils.joinWith(", ", repositoryDetailDto.getTopics()))
                        .repoUrl(parseNull(repositoryDetailDto.getHtmlUrl()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<RepositoryDetailDto> mapResponseToRepositories(
            List<RepositoryDetailDomain> repositoryDetailDomains) {
        return repositoryDetailDomains.stream()
                .map(repositoryDetailDomain -> RepositoryDetailDto.builder()
                        .name(repositoryDetailDomain.getName())
                        .fullName(parseNull(repositoryDetailDomain.getFullName()))
                        .description(parseNull(repositoryDetailDomain.getDescription()))
                        .homePage(parseNull(repositoryDetailDomain.getAppHome()))
                        .topics(StringUtils.isNotBlank(repositoryDetailDomain.getTopics()) ?
                                Arrays.asList(repositoryDetailDomain.getTopics().split(", ")) : Collections.emptyList())
                        .htmlUrl(repositoryDetailDomain.getRepoUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<RepositoryIssueDto> getIssues(String userId, String repo) {
        // further end-point validation (contains User ID)
        String uri = propertyConfig.getGitApiEndpointIssues();
        String apiUri = UriComponentsBuilder.fromHttpUrl(uri)
                .buildAndExpand(userId, repo)
                .toUriString();
        if (StringUtils.isBlank(apiUri) | StringUtils.isBlank(apiUri)
                | !urlUtils.isValidURL(apiUri)) {
            warn("URL is not valid: url={}", apiUri);
            return Collections.emptyList();
        }
        info("Triggering issues GET request: url={}", apiUri);
        List<RepositoryIssueDto> repositoryIssueDtos = new ArrayList<>();
        InputStream inputStream = null;
        try {
            HttpsURLConnection conn = gitClient.createConn(apiUri, uri);
            inputStream = gitClient.readInputStream(conn);
            repositoryIssueDtos = Arrays.asList(objectMapper.readValue(inputStream,
                    RepositoryIssueDto[].class));
            debug("repositoryIssueDtos={}", repositoryIssueDtos);
        } catch(IOException ex) {
            error("Unexpected error occurred while retrieving data.", ex);
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
        return repositoryIssueDtos;
    }

    @Override
    public List<RepositoryIssueDomain> mapIssuesToResponse(List<RepositoryIssueDto> repositoryIssueDtos) {
        return repositoryIssueDtos.stream()
                .map(repositoryIssueDto -> RepositoryIssueDomain.builder()
                        .number(repositoryIssueDto.getNumber())
                        .title(repositoryIssueDto.getTitle())
                        .body(repositoryIssueDto.getBody())
                        .build())
                .collect(Collectors.toList());
    }

    private String parseNull(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }
}
