package net.ironoc.portfolio.controller;

import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import net.ironoc.portfolio.domain.RepositoryIssueDomain;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.dto.RepositoryIssueDto;
import net.ironoc.portfolio.logger.AbstractLogger;
import net.ironoc.portfolio.service.GitDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
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

	@Operation(summary = "Get project issues by GitHub username & repository (project name).",
			description = "Returns a list of Github project bugs/issues per 'username' & 'repository' path variables.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Successfully retrieved GitHub issues for username & repository path variables.")
	})
	@GetMapping(value = {"/get-repo-issue/{username}/{repository}/"}, produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RepositoryIssueDomain>> getIssuesByUsernameAndRepoPathVars(HttpServletRequest request,
																				  @PathVariable(value = "username") String username,
																				  @PathVariable(value = "repository") String repository) {
		return getIssuesByUsernameAndRepo(request, username, repository);
	}

	private ResponseEntity<List<RepositoryIssueDomain>> getIssuesByUsernameAndRepo(HttpServletRequest request,
																			 String username,
																			 String repository) {
		// user & repo name validation (must contain only letters, numbers and/or dash chars)
		String userId;
		String repo;
		if (!StringUtils.isNoneBlank(username, repository)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
		} else if (!StringUtils.isAlphanumericSpace(sanitizeValue(username)
				.replaceAll("-", " "))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
		} else if (!StringUtils.isAlphanumericSpace(sanitizeValue(repository)
				.replaceAll("-", " "))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
		} else {
			List<String> pathVars = sanitizeValues(username, repository);
			userId = pathVars.get(0);
			repo = pathVars.get(1);
		}
		info("Github list issues by username={} and repo={} for request, host={}, uri={}, user-agent={}",
				userId, repo,
				request.getHeader("host"),
				request.getRequestURI(),
				request.getHeader("user-agent"));
		List<RepositoryIssueDto> repositoryIssueDtos = gitDetailsService.getIssues(userId, repo, false);
		info("The repository issues for user={} and repo={} are: {}", userId, repo, repositoryIssueDtos);
		return ResponseEntity.status(HttpStatus.OK)
				.body(gitDetailsService.mapIssuesToResponse(repositoryIssueDtos));
	}

	private ResponseEntity<List<RepositoryDetailDomain>> getReposByUsername(HttpServletRequest request,
																			String username) {
		// username validation (must contain only letters, numbers and/or dash chars)
		String userId;
		if (StringUtils.isBlank(username)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
		} else if (!StringUtils.isAlphanumericSpace(sanitizeValue(username)
				.replaceAll("-", " "))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
		} else {
			userId = sanitizeValue(username);
		}

		info("Github get repositories by username={} for request, host={}, uri={}, user-agent={}",
				userId,
				request.getHeader("host"),
				request.getRequestURI(),
				request.getHeader("user-agent"));
		List<RepositoryDetailDto> repositories = gitDetailsService.getRepoDetails(userId, false);
		info("The repository details for user={} are: {}", userId, repositories);
		return ResponseEntity.status(HttpStatus.OK)
				.body(gitDetailsService.mapRepositoriesToResponse(repositories));
	}

	private List<String> sanitizeValues(String... values) {
		// trim leading and trailing whitespace
		String sanitizedValueUserId = sanitizeValue(values[0]);
		String sanitizedValueRepo = sanitizeValue(values[1]);
		return List.of(sanitizedValueUserId, sanitizedValueRepo);
	}

	private String sanitizeValue(String value) {
		// trim leading and trailing whitespace
		String sanitizedValue = value.trim();
		// remove unwanted characters & accents etc
		return sanitizedValue.replaceAll("\\p{M}", "");
	}
}
