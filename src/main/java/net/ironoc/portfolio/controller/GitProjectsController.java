package net.ironoc.portfolio.controller;

import module java.base;

import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import net.ironoc.portfolio.domain.RepositoryIssueCreateDomain;
import net.ironoc.portfolio.domain.RepositoryIssueDomain;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.dto.RepositoryIssueCreateDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class GitProjectsController extends AbstractLogger {

	private final GitDetailsService gitDetailsService;

	protected static final String IRONOC_GIT_USER = "conorheffron";
	private static final String OPEN_ISSUE_STATE = "open";

	// Cache for issue counts: key is "username/repo"
	private final ConcurrentHashMap<String, Integer> issueCountCache = new ConcurrentHashMap<>();

	public GitProjectsController(@Autowired GitDetailsService gitDetailsService) {
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
		return getReposSortedAndWithIssueCount(request, username);
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
		return getReposSortedAndWithIssueCount(request, username);
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

	@Operation(summary = "Create project issue by GitHub username & repository (project name).",
			description = "Creates a Github issue per 'username' & 'repository' path variables.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201",
					description = "Successfully created GitHub issue for username & repository path variables.")
	})
	@PostMapping(value = {"/create-repo-issue/{username}/{repository}/"},
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RepositoryIssueDomain> createIssueByUsernameAndRepoPathVars(HttpServletRequest request,
																					  @PathVariable(value = "username") String username,
																					  @PathVariable(value = "repository") String repository,
																					  @RequestBody RepositoryIssueCreateDomain issue) {
		return createIssueByUsernameAndRepo(request, username, repository, issue);
	}

	private ResponseEntity<List<RepositoryDetailDomain>> getReposSortedAndWithIssueCount(HttpServletRequest request, String username) {
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

		List<RepositoryDetailDomain> domains = gitDetailsService.mapRepositoriesToResponse(repositories);

		// Only for username 'conorheffron', fetch issue counts and sort
		if (IRONOC_GIT_USER.equalsIgnoreCase(userId)) {
			for (RepositoryDetailDomain domain : domains) {
				String cacheKey = userId + "/" + domain.getName();
				Integer cachedCount = issueCountCache.get(cacheKey);
				if (cachedCount == null) {
					List<RepositoryIssueDto> issues = gitDetailsService.getIssues(userId, domain.getName(), false);
					cachedCount = countOpenIssues(issues);
					issueCountCache.put(cacheKey, cachedCount);
				}
				domain.setIssueCount(cachedCount);
			}
			domains.sort(Comparator.comparingInt(RepositoryDetailDomain::getIssueCount).reversed());
		}

		return ResponseEntity.ok(domains);
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

	private ResponseEntity<RepositoryIssueDomain> createIssueByUsernameAndRepo(HttpServletRequest request,
																			   String username,
																			   String repository,
																			   RepositoryIssueCreateDomain issue) {
		String userId;
		String repo;
		if (!StringUtils.isNoneBlank(username, repository) || issue == null
				|| StringUtils.isBlank(issue.getTitle())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} else if (!StringUtils.isAlphanumericSpace(sanitizeValue(username)
				.replaceAll("-", " "))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} else if (!StringUtils.isAlphanumericSpace(sanitizeValue(repository)
				.replaceAll("-", " "))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} else {
			List<String> pathVars = sanitizeValues(username, repository);
			userId = pathVars.get(0);
			repo = pathVars.get(1);
		}

		info("Github create issue by username={} and repo={} for request, host={}, uri={}, user-agent={}",
				userId, repo,
				request.getHeader("host"),
				request.getRequestURI(),
				request.getHeader("user-agent"));
		RepositoryIssueDto createdIssue = gitDetailsService.createIssue(userId, repo,
				RepositoryIssueCreateDto.builder()
						.title(issue.getTitle())
						.body(issue.getBody())
						.assignees(issue.getAssignees())
						.milestone(issue.getMilestone())
						.labels(issue.getLabels())
						.build());
		if (createdIssue == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		List<RepositoryIssueDomain> issueDomain = gitDetailsService.mapIssuesToResponse(List.of(createdIssue));
		return ResponseEntity.status(HttpStatus.CREATED).body(issueDomain.get(0));
	}

	private List<String> sanitizeValues(String... values) {
		// trim leading and trailing whitespace
		String sanitizedValueUserId = sanitizeValue(values[0]);
		String sanitizedValueRepo = sanitizeValue(values[1]);
		return List.of(sanitizedValueUserId, sanitizedValueRepo);
	}

	private int countOpenIssues(List<RepositoryIssueDto> issues) {
		if (issues == null) {
			return 0;
		}
		return (int) issues.stream()
				.filter(Objects::nonNull)
				.map(RepositoryIssueDto::getState)
				.filter(Objects::nonNull)
				.filter(OPEN_ISSUE_STATE::equalsIgnoreCase)
				.count();
	}

	private String sanitizeValue(String value) {
		// trim leading and trailing whitespace
		String sanitizedValue = value.trim();
		// remove unwanted characters & accents etc
		return sanitizedValue.replaceAll("\\p{M}", "");
	}
}
