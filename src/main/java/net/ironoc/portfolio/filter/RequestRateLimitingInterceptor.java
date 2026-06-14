package net.ironoc.portfolio.filter;

import module java.base;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestRateLimitingInterceptor implements HandlerInterceptor {

    static final long GET_REQUEST_LIMIT = 60L;

    static final long WRITE_REQUEST_LIMIT = 10L;

    private static final int MAX_TRACKED_BUCKETS = 10_000;

    private final Map<String, Bucket> buckets = Collections.synchronizedMap(new LinkedHashMap<>(128, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Bucket> eldest) {
            return size() > MAX_TRACKED_BUCKETS;
        }
    });

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        RequestLimitProfile requestLimitProfile = RequestLimitProfile.from(request.getMethod());
        if (requestLimitProfile == null) {
            return true;
        }

        ConsumptionProbe probe = resolveBucket(request, requestLimitProfile).tryConsumeAndReturnRemaining(1);
        response.setHeader("X-Rate-Limit-Limit", Long.toString(requestLimitProfile.limit()));
        response.setHeader("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()));

        if (probe.isConsumed()) {
            return true;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setHeader(HttpHeaders.RETRY_AFTER, Long.toString(calculateRetryAfterSeconds(probe)));
        response.getWriter().write("Rate limit exceeded for " + requestLimitProfile.methodName() + " requests.");
        return false;
    }

    private Bucket resolveBucket(HttpServletRequest request, RequestLimitProfile requestLimitProfile) {
        String bucketKey = requestLimitProfile.methodName() + ":" + request.getRemoteAddr();
        return buckets.computeIfAbsent(bucketKey, unused -> Bucket.builder()
                .addLimit(Bandwidth.classic(requestLimitProfile.limit(),
                        Refill.greedy(requestLimitProfile.limit(), Duration.ofMinutes(1))))
                .build());
    }

    private long calculateRetryAfterSeconds(ConsumptionProbe probe) {
        long nanosToWaitForRefill = probe.getNanosToWaitForRefill();
        return Math.max(1L, (nanosToWaitForRefill + 999_999_999L) / 1_000_000_000L);
    }

    private enum RequestLimitProfile {
        GET(HttpMethod.GET.name(), GET_REQUEST_LIMIT),
        POST(HttpMethod.POST.name(), WRITE_REQUEST_LIMIT),
        PUT(HttpMethod.PUT.name(), WRITE_REQUEST_LIMIT);

        private final String methodName;

        private final long limit;

        RequestLimitProfile(String methodName, long limit) {
            this.methodName = methodName;
            this.limit = limit;
        }

        static RequestLimitProfile from(String method) {
            for (RequestLimitProfile value : values()) {
                if (value.methodName.equalsIgnoreCase(method)) {
                    return value;
                }
            }
            return null;
        }

        String methodName() {
            return methodName;
        }

        long limit() {
            return limit;
        }
    }
}
