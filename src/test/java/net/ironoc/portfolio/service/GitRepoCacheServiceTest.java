package net.ironoc.portfolio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ironoc.portfolio.domain.RepositoryDetailDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@ExtendWith(MockitoExtension.class)
public class GitRepoCacheServiceTest {

    @InjectMocks
    private GitRepoCacheService gitRepoCacheService;

    @Test
    public void test_cache_success() throws IOException {
        // given
        InputStream jsonInputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("json" + File.separator + "test_repo_detail_response.json");
        RepositoryDetailDomain[] dtos = new ObjectMapper().readValue(jsonInputStream, RepositoryDetailDomain[].class);
        gitRepoCacheService.put("user1", Collections.emptyList());
        gitRepoCacheService.put("user-2", List.of(dtos));

        // when
        List<RepositoryDetailDomain> results = gitRepoCacheService.get("user1");
        List<RepositoryDetailDomain> results2 = gitRepoCacheService.get("user-2");
        List<RepositoryDetailDomain> results3 = gitRepoCacheService.get("conorheffron-3");

        // then
        assertThat(results, is(notNullValue()));
        assertThat(results, is(hasSize(0)));
        assertThat(results2, is(hasSize(2)));
        Optional<RepositoryDetailDomain> result = results2.stream().findFirst();
        assertThat(result.get().getName(), is("bio-cell-red-edge"));
        assertThat(result.get().getFullName(), is("conorheffron/bio-cell-red-edge"));
        assertThat(result.get().getDescription(),
                is("Edge Detection of Biological Cell (Image Processing Script)"));
        assertThat(result.get().getTopics(),
                is("[Biology, computer-vision, image-processing, scikitlearn-machine-learning]"));
        assertThat(result.get().getAppHome(),
                is("https://conorheffron.github.io/bio-cell-red-edge/"));
        assertThat(result.get().getRepoUrl(),
                is("https://github.com/conorheffron/bio-cell-red-edge"));
        RepositoryDetailDomain result2 = results2.get(1);
        assertThat(result2.getName(), is("booking-sys"));
        assertThat(result2.getFullName(), is(nullValue()));
        assertThat(result2.getDescription(), is("python3 and django5 web app"));
        assertThat(result2.getTopics(), is(emptyString()));
        assertThat(result2.getAppHome(),
                is("https://booking-sys-ebgefrdmh3afbhee.northeurope-01.azurewebsites.net/book/"));
        assertThat(result2.getRepoUrl(), is("https://github.com/conorheffron/booking-sys"));
        jsonInputStream.close();
        assertThat(results3, is(nullValue()));
        gitRepoCacheService.tearDown();
        assertThat(gitRepoCacheService.get("user1"), is(nullValue()));
        assertThat(gitRepoCacheService.get("user-2"), is(nullValue()));
    }
}
