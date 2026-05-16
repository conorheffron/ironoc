package net.ironoc.portfolio.service;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ActivityTrackingServiceTest {

    private final ActivityTrackingService activityTrackingService = new ActivityTrackingService();

    @Test
    void testIncrementClickOutCountsByCategoryAndTarget() {
        activityTrackingService.incrementClickOut("github", "https://github.com/conorheffron/ironoc");
        activityTrackingService.incrementClickOut("github", "https://github.com/conorheffron/ironoc");
        activityTrackingService.incrementClickOut("charity", "https://example.org/donate");

        Map<String, Long> report = activityTrackingService.getClickOutReport();

        assertThat(report.get("github:https://github.com/conorheffron/ironoc"), is(2L));
        assertThat(report.get("charity:https://example.org/donate"), is(1L));
    }
}
