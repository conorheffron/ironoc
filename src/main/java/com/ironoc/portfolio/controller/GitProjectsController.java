package com.ironoc.portfolio.controller;

import com.ironoc.portfolio.domain.RepositoryDetailDomain;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import com.ironoc.portfolio.logger.AbstractLogger;
import com.ironoc.portfolio.service.GitDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class GitProjectsController extends AbstractLogger {

	@Autowired
	private final GitDetailsService gitDetailsService;

	public GitProjectsController(GitDetailsService gitDetailsService) {
		this.gitDetailsService = gitDetailsService;
	}

	@Operation(summary = "Get repository details by GitHub username",
			description = "Returns a list of Github repository details per username path variable.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successfully retrieved GitHub projects for username path variable.")
	})
	@GetMapping(value = {"/get-repo-detail/{username}/"}, produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RepositoryDetailDomain>> getReposByUsernamePathVar(HttpServletRequest request,
															  @PathVariable(value = "username") String username) {
		return getReposByUsername(request, username);
	}

	@Operation(summary = "Get repository details by GitHub username",
			description = "Returns a list of Github repository details per 'username' request parameter.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successfully retrieved GitHub projects for username request parameter.")
	})
	@GetMapping(value = {"/get-repo-detail"}, produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RepositoryDetailDomain>> getReposByUsernameReqParam(HttpServletRequest request,
															  @RequestParam(value = "username") String username) {
		return getReposByUsername(request, username);
	}

	private ResponseEntity<List<RepositoryDetailDomain>> getReposByUsername(HttpServletRequest request,
																			String username) {
		// username validation (must contain only letters, numbers and/or dash chars)
		String userId = "";
		if (StringUtils.isBlank(username)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
		} else if (!StringUtils.isAlphanumericSpace(sanitizeUsername(username)
				.replaceAll("-", " "))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
		} else {
			userId = sanitizeUsername(username);
		}

		info("Github get repositories by username={} for request, host={}, uri={}, user-agent={}",
				userId,
				request.getHeader("host"),
				request.getRequestURI(),
				request.getHeader("user-agent"));
		List<RepositoryDetailDto> repositories = gitDetailsService.getRepoDetails(userId);
		info("The repository details for user={} are: {}", userId, repositories);
		return ResponseEntity.status(HttpStatus.OK)
				.body(gitDetailsService.mapRepositoriesToResponse(repositories));
	}

	private String sanitizeUsername(String username) {
		// trim leading and trailing whitespace
		String sanitizedUserId = username.trim();
		// remove unwanted characters & accents etc
		return sanitizedUserId.replaceAll("\\p{M}", "");
	}
}
