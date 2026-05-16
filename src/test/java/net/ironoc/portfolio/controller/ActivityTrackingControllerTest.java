package net.ironoc.portfolio.controller;

import module java.base;

import jakarta.servlet.http.HttpServletRequest;
import net.ironoc.portfolio.dto.ClickOutActivity;
import net.ironoc.portfolio.service.ActivityTrackingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityTrackingControllerTest {

    @InjectMocks
    private ActivityTrackingController activityTrackingController;

    @Mock
    private ActivityTrackingService activityTrackingService;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Test
    void testTrackClickOutSuccess() {
        ClickOutActivity clickOutActivity = new ClickOutActivity("github", "https://github.com/conorheffron/ironoc");

        ResponseEntity<Void> response = activityTrackingController.trackClickOut(httpServletRequestMock, clickOutActivity);

        verify(httpServletRequestMock).getRequestURI();
        verify(httpServletRequestMock, times(2)).getHeader(anyString());
        verify(activityTrackingService).incrementClickOut("github", "https://github.com/conorheffron/ironoc");
        assertThat(response.getStatusCode(), is(HttpStatus.ACCEPTED));
    }

    @Test
    void testTrackClickOutBadRequestWhenPayloadBlank() {
        ClickOutActivity clickOutActivity = new ClickOutActivity(" ", " ");

        ResponseEntity<Void> response = activityTrackingController.trackClickOut(httpServletRequestMock, clickOutActivity);

        verify(httpServletRequestMock, never()).getRequestURI();
        verify(httpServletRequestMock, never()).getHeader(anyString());
        verify(activityTrackingService, never()).incrementClickOut(anyString(), anyString());
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testGetClickOutReportSuccess() {
        Map<String, Long> mockReport = new HashMap<>();
        mockReport.put("github:https://github.com/conorheffron/ironoc", 5L);
        when(activityTrackingService.getClickOutReport()).thenReturn(mockReport);

        ResponseEntity<Map<String, Long>> response = activityTrackingController.getClickOutReport();

        verify(activityTrackingService).getClickOutReport();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().get("github:https://github.com/conorheffron/ironoc"), is(5L));
    }
}
