package net.ironoc.portfolio.client;

import module java.base;

import net.ironoc.portfolio.aws.SecretManager;
import net.ironoc.portfolio.config.PropertyConfigI;
import net.ironoc.portfolio.dto.RepositoryDetailDto;
import net.ironoc.portfolio.utils.UrlUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import javax.net.ssl.HttpsURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.emptyIterable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GitClientTest {

    @InjectMocks
    private GitClient gitClient;

    @Mock
    private SecretManager secretManagerMock;

    @Mock
    private PropertyConfigI propertyConfigMock;

    @Mock
    private UrlUtils urlUtilsMock;

    @Mock
    private HttpsURLConnection httpsURLConnectionMock;

    @Mock
    private InputStream inputStreamMock;

    private static final String TEST_URL = "https://unittest.github.com/users/conorheffron/repos";

    @Test
    public void test_callGitHubApi_fail() {
        // when
        Collection<RepositoryDetailDto> result = gitClient
                .callGitHubApi(TEST_URL, TEST_URL, RepositoryDetailDto.class, HttpMethod.GET.name());

        // then
        assertThat(result, is(emptyIterable()));
    }

    @Test
    public void test_readInputStream_fail() throws IOException {
        // when
        InputStream result = gitClient.readInputStream(httpsURLConnectionMock);

        // then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void test_readInputStream_success() throws IOException {
        // given
        when(httpsURLConnectionMock.getInputStream()).thenReturn(inputStreamMock);

        // when
        InputStream result = gitClient.readInputStream(httpsURLConnectionMock);

        // then
        verify(httpsURLConnectionMock).getInputStream();

        assertThat(result, is(inputStreamMock));
    }

    @Test
    public void test_close_success() throws IOException {
        // when
        gitClient.closeConn(inputStreamMock);

        // then
        verify(inputStreamMock).close();
    }

    @Test
    public void test_createConn_without_token_success() throws IOException {
        // given
        when(urlUtilsMock.isValidURL(TEST_URL)).thenReturn(true);

        // when
        HttpsURLConnection result = gitClient.createConn(TEST_URL, TEST_URL, HttpMethod.GET.name());

        // then
        verify(urlUtilsMock).isValidURL(TEST_URL);
        verify(propertyConfigMock).getGitFollowRedirects();
        verify(propertyConfigMock).getGitTimeoutConnect();
        verify(propertyConfigMock).getGitTimeoutRead();
        verify(propertyConfigMock).getGitInstanceFollowRedirects();
        verify(secretManagerMock).getGitSecret();

        assertThat(result, notNullValue());
    }

    @Test
    public void test_createConn_with_token_success() throws IOException {
        // given
        when(urlUtilsMock.isValidURL(TEST_URL)).thenReturn(true);
        when(secretManagerMock.getGitSecret()).thenReturn("test_fake_token");

        // when
        HttpsURLConnection result = gitClient.createConn(TEST_URL, TEST_URL, HttpMethod.GET.name());

        // then
        verify(urlUtilsMock).isValidURL(TEST_URL);
        verify(propertyConfigMock).getGitFollowRedirects();
        verify(propertyConfigMock).getGitTimeoutConnect();
        verify(propertyConfigMock).getGitTimeoutRead();
        verify(propertyConfigMock).getGitInstanceFollowRedirects();
        verify(secretManagerMock).getGitSecret();

        assertThat(result, notNullValue());
    }

    @Test
    public void test_createConn_invalid_url_fail() throws IOException {
        // given
        when(urlUtilsMock.isValidURL(TEST_URL)).thenReturn(false);

        // when
        HttpsURLConnection result = gitClient.createConn(TEST_URL, TEST_URL, HttpMethod.GET.name());

        // then
        verify(urlUtilsMock).isValidURL(TEST_URL);
        verify(propertyConfigMock, never()).getGitFollowRedirects();
        verify(propertyConfigMock, never()).getGitTimeoutConnect();
        verify(propertyConfigMock, never()).getGitTimeoutRead();
        verify(propertyConfigMock, never()).getGitInstanceFollowRedirects();
        verify(secretManagerMock, never()).getGitSecret();

        assertThat(result, is(nullValue()));
    }

    @Test
    public void test_convertInputStreamToString_success() throws Exception {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_response.json");

        // when
        String result = gitClient.convertInputStreamToString(jsonInputStream);


        // then
        assertThat(result, is("""
                [
                  {
                    "id": 737159069,
                    "node_id": "R_kgDOK_AnnQ",
                    "name": "bio-cell-red-edge",
                    "full_name": "conorheffron/bio-cell-red-edge",
                    "private": false,
                    "owner": {
                      "login": "conorheffron",
                      "id": 8218626,
                      "node_id": "MDQ6VXNlcjgyMTg2MjY=",
                      "avatar_url": "https://avatars.githubusercontent.com/u/8218626?v=4",
                      "gravatar_id": "",
                      "url": "https://api.github.com/users/conorheffron",
                      "html_url": "https://github.com/conorheffron",
                      "followers_url": "https://api.github.com/users/conorheffron/followers",
                      "following_url": "https://api.github.com/users/conorheffron/following{/other_user}",
                      "gists_url": "https://api.github.com/users/conorheffron/gists{/gist_id}",
                      "starred_url": "https://api.github.com/users/conorheffron/starred{/owner}{/repo}",
                      "subscriptions_url": "https://api.github.com/users/conorheffron/subscriptions",
                      "organizations_url": "https://api.github.com/users/conorheffron/orgs",
                      "repos_url": "https://api.github.com/users/conorheffron/repos",
                      "events_url": "https://api.github.com/users/conorheffron/events{/privacy}",
                      "received_events_url": "https://api.github.com/users/conorheffron/received_events",
                      "type": "User",
                      "site_admin": false
                    },
                    "html_url": "https://github.com/conorheffron/bio-cell-red-edge",
                    "description": "Edge Detection of Biological Cell (Image Processing Script)",
                    "fork": false,
                    "url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge",
                    "forks_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/forks",
                    "keys_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/keys{/key_id}",
                    "collaborators_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/collaborators{/collaborator}",
                    "teams_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/teams",
                    "hooks_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/hooks",
                    "issue_events_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/issues/events{/number}",
                    "events_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/events",
                    "assignees_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/assignees{/user}",
                    "branches_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/branches{/branch}",
                    "tags_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/tags",
                    "blobs_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/git/blobs{/sha}",
                    "git_tags_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/git/tags{/sha}",
                    "git_refs_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/git/refs{/sha}",
                    "trees_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/git/trees{/sha}",
                    "statuses_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/statuses/{sha}",
                    "languages_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/languages",
                    "stargazers_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/stargazers",
                    "contributors_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/contributors",
                    "subscribers_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/subscribers",
                    "subscription_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/subscription",
                    "commits_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/commits{/sha}",
                    "git_commits_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/git/commits{/sha}",
                    "comments_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/comments{/number}",
                    "issue_comment_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/issues/comments{/number}",
                    "contents_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/contents/{+path}",
                    "compare_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/compare/{base}...{head}",
                    "merges_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/merges",
                    "archive_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/{archive_format}{/ref}",
                    "downloads_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/downloads",
                    "issues_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/issues{/number}",
                    "pulls_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/pulls{/number}",
                    "milestones_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/milestones{/number}",
                    "notifications_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/notifications{?since,all,participating}",
                    "labels_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/labels{/name}",
                    "releases_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/releases{/id}",
                    "deployments_url": "https://api.github.com/repos/conorheffron/bio-cell-red-edge/deployments",
                    "created_at": "2023-12-30T02:40:43Z",
                    "updated_at": "2024-07-25T23:06:48Z",
                    "pushed_at": "2024-07-25T23:10:52Z",
                    "git_url": "git://github.com/conorheffron/bio-cell-red-edge.git",
                    "ssh_url": "git@github.com:conorheffron/bio-cell-red-edge.git",
                    "clone_url": "https://github.com/conorheffron/bio-cell-red-edge.git",
                    "svn_url": "https://github.com/conorheffron/bio-cell-red-edge",
                    "homepage": "https://conorheffron.github.io/bio-cell-red-edge/",
                    "size": 11698,
                    "stargazers_count": 1,
                    "watchers_count": 1,
                    "language": "Python",
                    "has_issues": true,
                    "has_projects": true,
                    "has_downloads": true,
                    "has_wiki": true,
                    "has_pages": true,
                    "has_discussions": false,
                    "forks_count": 0,
                    "mirror_url": null,
                    "archived": false,
                    "disabled": false,
                    "open_issues_count": 0,
                    "license": {
                      "key": "gpl-3.0",
                      "name": "GNU General Public License v3.0",
                      "spdx_id": "GPL-3.0",
                      "url": "https://api.github.com/licenses/gpl-3.0",
                      "node_id": "MDc6TGljZW5zZTk="
                    },
                    "allow_forking": true,
                    "is_template": false,
                    "web_commit_signoff_required": false,
                    "topics": [
                      "biology",
                      "computer-vision",
                      "image-processing",
                      "scikitlearn-machine-learning"
                    ],
                    "visibility": "public",
                    "forks": 0,
                    "open_issues": 0,
                    "watchers": 1,
                    "default_branch": "main"
                  },
                  {
                    "id": 850363843,
                    "node_id": "R_kgDOMq-Fww",
                    "name": "booking-sys",
                    "full_name": "conorheffron/booking-sys",
                    "private": true,
                    "html_url": "https://github.com/conorheffron/booking-sys",
                    "description": "Sample Reservations & Bookings Viewer System",
                    "homepage": "https://booking-sys-ebgefrdmh3afbhee.northeurope-01.azurewebsites.net/book/"
                  }
                ]"""));
        assertThat(jsonInputStream, is(notNullValue()));
        jsonInputStream.close();
    }
}
