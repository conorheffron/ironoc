package net.ironoc.portfolio.service;

import net.ironoc.portfolio.domain.CoffeeDomain;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

class CoffeeCacheServiceTest {

    @Test
    void test_coffeeCacheService_operations() {
        // given
        CoffeeCacheService cacheService = new CoffeeCacheService();
        List<CoffeeDomain> brews = List.of(new CoffeeDomain());

        // when & then: initially empty
        assertThat(cacheService.get(), is(nullValue()));

        // when & then: put and get
        cacheService.put(brews);
        assertThat(cacheService.get(), is(notNullValue()));
        assertThat(cacheService.get().size(), is(1));

        // when & then: remove
        cacheService.remove();
        assertThat(cacheService.get(), is(nullValue()));

        // when & then: put and tearDown (PreDestroy)
        cacheService.put(brews);
        assertThat(cacheService.get(), is(notNullValue()));
        cacheService.tearDown();
        assertThat(cacheService.get(), is(nullValue()));
    }
}
