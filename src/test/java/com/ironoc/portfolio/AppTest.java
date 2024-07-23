package com.ironoc.portfolio;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

public class AppTest {

    // mocks
    private MockedStatic<SpringApplication> springApplicationMockedStatic = mockStatic(SpringApplication.class);

    @Test
    public void test() {
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
