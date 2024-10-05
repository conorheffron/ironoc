package com.ironoc.portfolio.client;

import com.ironoc.portfolio.aws.SecretManager;
import com.ironoc.portfolio.config.PropertyConfigI;
import com.ironoc.portfolio.dto.RepositoryDetailDto;
import com.ironoc.portfolio.utils.UrlUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

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
        assertThat(result, is("[\n" +
                "  {\n" +
                "    \"id\": 737159069,\n" +
                "    \"node_id\": \"R_kgDOK_AnnQ\",\n" +
                "    \"name\": \"bio-cell-red-edge\",\n" +
                "    \"full_name\": \"conorheffron/bio-cell-red-edge\",\n" +
                "    \"private\": false,\n" +
                "    \"owner\": {\n" +
                "      \"login\": \"conorheffron\",\n" +
                "      \"id\": 8218626,\n" +
                "      \"node_id\": \"MDQ6VXNlcjgyMTg2MjY=\",\n" +
                "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/8218626?v=4\",\n" +
                "      \"gravatar_id\": \"\",\n" +
                "      \"url\": \"https://api.github.com/users/conorheffron\",\n" +
                "      \"html_url\": \"https://github.com/conorheffron\",\n" +
                "      \"followers_url\": \"https://api.github.com/users/conorheffron/followers\",\n" +
                "      \"following_url\": \"https://api.github.com/users/conorheffron/following{/other_user}\",\n" +
                "      \"gists_url\": \"https://api.github.com/users/conorheffron/gists{/gist_id}\",\n" +
                "      \"starred_url\": \"https://api.github.com/users/conorheffron/starred{/owner}{/repo}\",\n" +
                "      \"subscriptions_url\": \"https://api.github.com/users/conorheffron/subscriptions\",\n" +
                "      \"organizations_url\": \"https://api.github.com/users/conorheffron/orgs\",\n" +
                "      \"repos_url\": \"https://api.github.com/users/conorheffron/repos\",\n" +
                "      \"events_url\": \"https://api.github.com/users/conorheffron/events{/privacy}\",\n" +
                "      \"received_events_url\": \"https://api.github.com/users/conorheffron/received_events\",\n" +
                "      \"type\": \"User\",\n" +
                "      \"site_admin\": false\n" +
                "    },\n" +
                "    \"html_url\": \"https://github.com/conorheffron/bio-cell-red-edge\",\n" +
                "    \"description\": \"Edge Detection of Biological Cell (Image Processing Script)\",\n" +
                "    \"fork\": false,\n" +
                "    \"url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge\",\n" +
                "    \"forks_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/forks\",\n" +
                "    \"keys_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/keys{/key_id}\",\n" +
                "    \"collaborators_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/collaborators{/collaborator}\",\n" +
                "    \"teams_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/teams\",\n" +
                "    \"hooks_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/hooks\",\n" +
                "    \"issue_events_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/issues/events{/number}\",\n" +
                "    \"events_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/events\",\n" +
                "    \"assignees_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/assignees{/user}\",\n" +
                "    \"branches_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/branches{/branch}\",\n" +
                "    \"tags_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/tags\",\n" +
                "    \"blobs_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/git/blobs{/sha}\",\n" +
                "    \"git_tags_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/git/tags{/sha}\",\n" +
                "    \"git_refs_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/git/refs{/sha}\",\n" +
                "    \"trees_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/git/trees{/sha}\",\n" +
                "    \"statuses_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/statuses/{sha}\",\n" +
                "    \"languages_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/languages\",\n" +
                "    \"stargazers_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/stargazers\",\n" +
                "    \"contributors_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/contributors\",\n" +
                "    \"subscribers_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/subscribers\",\n" +
                "    \"subscription_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/subscription\",\n" +
                "    \"commits_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/commits{/sha}\",\n" +
                "    \"git_commits_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/git/commits{/sha}\",\n" +
                "    \"comments_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/comments{/number}\",\n" +
                "    \"issue_comment_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/issues/comments{/number}\",\n" +
                "    \"contents_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/contents/{+path}\",\n" +
                "    \"compare_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/compare/{base}...{head}\",\n" +
                "    \"merges_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/merges\",\n" +
                "    \"archive_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/{archive_format}{/ref}\",\n" +
                "    \"downloads_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/downloads\",\n" +
                "    \"issues_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/issues{/number}\",\n" +
                "    \"pulls_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/pulls{/number}\",\n" +
                "    \"milestones_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/milestones{/number}\",\n" +
                "    \"notifications_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/notifications{?since,all,participating}\",\n" +
                "    \"labels_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/labels{/name}\",\n" +
                "    \"releases_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/releases{/id}\",\n" +
                "    \"deployments_url\": \"https://api.github.com/repos/conorheffron/bio-cell-red-edge/deployments\",\n" +
                "    \"created_at\": \"2023-12-30T02:40:43Z\",\n" +
                "    \"updated_at\": \"2024-07-25T23:06:48Z\",\n" +
                "    \"pushed_at\": \"2024-07-25T23:10:52Z\",\n" +
                "    \"git_url\": \"git://github.com/conorheffron/bio-cell-red-edge.git\",\n" +
                "    \"ssh_url\": \"git@github.com:conorheffron/bio-cell-red-edge.git\",\n" +
                "    \"clone_url\": \"https://github.com/conorheffron/bio-cell-red-edge.git\",\n" +
                "    \"svn_url\": \"https://github.com/conorheffron/bio-cell-red-edge\",\n" +
                "    \"homepage\": \"https://conorheffron.github.io/bio-cell-red-edge/\",\n" +
                "    \"size\": 11698,\n" +
                "    \"stargazers_count\": 1,\n" +
                "    \"watchers_count\": 1,\n" +
                "    \"language\": \"Python\",\n" +
                "    \"has_issues\": true,\n" +
                "    \"has_projects\": true,\n" +
                "    \"has_downloads\": true,\n" +
                "    \"has_wiki\": true,\n" +
                "    \"has_pages\": true,\n" +
                "    \"has_discussions\": false,\n" +
                "    \"forks_count\": 0,\n" +
                "    \"mirror_url\": null,\n" +
                "    \"archived\": false,\n" +
                "    \"disabled\": false,\n" +
                "    \"open_issues_count\": 0,\n" +
                "    \"license\": {\n" +
                "      \"key\": \"gpl-3.0\",\n" +
                "      \"name\": \"GNU General Public License v3.0\",\n" +
                "      \"spdx_id\": \"GPL-3.0\",\n" +
                "      \"url\": \"https://api.github.com/licenses/gpl-3.0\",\n" +
                "      \"node_id\": \"MDc6TGljZW5zZTk=\"\n" +
                "    },\n" +
                "    \"allow_forking\": true,\n" +
                "    \"is_template\": false,\n" +
                "    \"web_commit_signoff_required\": false,\n" +
                "    \"topics\": [\n" +
                "      \"biology\",\n" +
                "      \"computer-vision\",\n" +
                "      \"image-processing\",\n" +
                "      \"scikitlearn-machine-learning\"\n" +
                "    ],\n" +
                "    \"visibility\": \"public\",\n" +
                "    \"forks\": 0,\n" +
                "    \"open_issues\": 0,\n" +
                "    \"watchers\": 1,\n" +
                "    \"default_branch\": \"main\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 850363843,\n" +
                "    \"node_id\": \"R_kgDOMq-Fww\",\n" +
                "    \"name\": \"booking-sys\",\n" +
                "    \"full_name\": \"conorheffron/booking-sys\",\n" +
                "    \"private\": true,\n" +
                "    \"html_url\": \"https://github.com/conorheffron/booking-sys\",\n" +
                "    \"description\": \"Sample Reservations & Bookings Viewer System\",\n" +
                "    \"homepage\": \"https://booking-sys-ebgefrdmh3afbhee.northeurope-01.azurewebsites.net/book/\"\n" +
                "  }\n" +
                "]"));
        assertThat(jsonInputStream, is(notNullValue()));
        jsonInputStream.close();
    }
}
