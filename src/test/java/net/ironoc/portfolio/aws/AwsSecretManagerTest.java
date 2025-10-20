package net.ironoc.portfolio.aws;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(MockitoExtension.class)
class AwsSecretManagerTest {

    @InjectMocks
    private AwsSecretManager awsSecretManager;

    @Test
    void test_getGitSecret_success() {
        // when
        String result = awsSecretManager.getGitSecret();

        // then
        assertThat(result, is(notNullValue()));
    }
}
