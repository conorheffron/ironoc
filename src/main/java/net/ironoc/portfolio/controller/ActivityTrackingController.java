package net.ironoc.portfolio.controller;

import module java.base;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import net.ironoc.portfolio.dto.ClickOutActivity;
import net.ironoc.portfolio.logger.AbstractLogger;
import net.ironoc.portfolio.service.ActivityTrackingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activity")
public class ActivityTrackingController extends AbstractLogger {

    private final ActivityTrackingService activityTrackingService;

    public ActivityTrackingController(ActivityTrackingService activityTrackingService) {
        this.activityTrackingService = activityTrackingService;
    }

    @Operation(summary = "Track click-out user activity",
            description = "Stores counts for click-out activity to external links such as GitHub and charity links.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully accepted click-out activity.")
    })
    @PostMapping(value = "/click-out", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> trackClickOut(HttpServletRequest request,
                                              @RequestBody ClickOutActivity clickOutActivity) {
        if (clickOutActivity == null
                || StringUtils.isBlank(clickOutActivity.getCategory())
                || StringUtils.isBlank(clickOutActivity.getTarget())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String category = sanitizeValue(clickOutActivity.getCategory());
        String target = sanitizeValue(clickOutActivity.getTarget());
        if (category.length() > 50 || target.length() > 2048) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        info("Track click-out activity, category={}, target={}, host={}, uri={}, user-agent={}",
                category, target, request.getHeader("host"), request.getRequestURI(), request.getHeader("user-agent"));
        activityTrackingService.incrementClickOut(category, target);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Get click-out user activity report",
            description = "Returns counts for click-out activity grouped by category and target.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved click-out activity report.")
    })
    @GetMapping(value = "/click-out-report", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> getClickOutReport() {
        return ResponseEntity.ok(activityTrackingService.getClickOutReport());
    }

    private String sanitizeValue(String value) {
        String sanitizedValue = value.trim();
        return sanitizedValue.replaceAll("\\p{M}", "");
    }
}
