package net.ironoc.portfolio.service;

import net.ironoc.portfolio.domain.RepositoryIssueDomain;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class GitProjectCacheServiceTest {

    @Test
    void test_gitProjectCacheService_operations() {
        // given
        GitProjectCacheService cacheService = new GitProjectCacheService();
        List<RepositoryIssueDomain> issues = List.of(new RepositoryIssueDomain("title", "url", "body", "state", List.of("label")));

        // when & then: initially empty
        assertThat(cacheService.get("user", "project"), is(nullValue()));

        // when & then: put and get
        cacheService.put("user", "project", issues);
        assertThat(cacheService.get("user", "project"), is(notNullValue()));
        assertThat(cacheService.get("user", "project").size(), is(1));

        // when & then: remove
        cacheService.remove("userproject");
        assertThat(cacheService.get("user", "project"), is(nullValue()));

        // when & then: put and tearDown (PreDestroy)
        cacheService.put("user", "project", issues);
        assertThat(cacheService.get("user", "project"), is(notNullValue()));
        cacheService.tearDown();
        assertThat(cacheService.get("user", "project"), is(nullValue()));
    }
}
