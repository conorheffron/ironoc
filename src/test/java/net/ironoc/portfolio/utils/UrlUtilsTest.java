package net.ironoc.portfolio.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class UrlUtilsTest {

    @InjectMocks
    private UrlUtils urlUtils;

    @Test
    void test_isValidURL_false() {
        // when
        boolean result = urlUtils.isValidURL("test_url");

        // then
        assertThat(result, is(false));
    }

    @Test
    void test_isValidURL_true() {
        // when
        boolean result = urlUtils.isValidURL("https://api.github.com/users/conorheffron/repos");

        // then
        assertThat(result, is(true));
    }
}
