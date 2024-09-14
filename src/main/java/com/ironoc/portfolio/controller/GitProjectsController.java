package com.ironoc.portfolio.controller;

import com.ironoc.portfolio.domain.RepositoryDetailDomain;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import com.ironoc.portfolio.logger.AbstractLogger;
import com.ironoc.portfolio.service.GitDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GitProjectsController extends AbstractLogger {

	@Autowired
	private final GitDetailsService gitDetailsService;

	public GitProjectsController(GitDetailsService gitDetailsService) {
		this.gitDetailsService = gitDetailsService;
	}

	@GetMapping(value = {"/get-repo-detail/{username}/"}, produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RepositoryDetailDomain>> getReposByUsernamePathVar(HttpServletRequest request,
															  @PathVariable(value = "username") String username) {
		return getReposByUsername(request, username);
	}

	@GetMapping(value = {"/get-repo-detail"}, produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RepositoryDetailDomain>> getReposByUsernameReqParam(HttpServletRequest request,
															  @RequestParam(value = "username") String username) {
		return getReposByUsername(request, username);
	}

	private ResponseEntity<List<RepositoryDetailDomain>> getReposByUsername(HttpServletRequest request, String username) {
		info("Github get repositories by username={} for request, host={}, uri={}, user-agent={}",
				username,
				request.getHeader("host"),
				request.getRequestURI(),
				request.getHeader("user-agent"));
		List<RepositoryDetailDto> repositories = gitDetailsService.getRepoDetails(username);
		info("The repository details for user={} are: {}", username, repositories);
		return ResponseEntity.status(HttpStatus.OK)
				.body(gitDetailsService.mapRepositoriesToResponse(repositories));
	}
}
