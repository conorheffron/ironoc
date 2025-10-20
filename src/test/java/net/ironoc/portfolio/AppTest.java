package net.ironoc.portfolio;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class AppTest {
    // mocks
    private final MockedStatic<SpringApplication> springApplicationMockedStatic = mockStatic(SpringApplication.class);

    @Test
    void test_run_success() {
        // given
        String[] args = { "test" };

        // when
        App.main(args);

        // then
        springApplicationMockedStatic.verify(() -> SpringApplication.run(App.class, args),
                times(1)
        );
    }
}
