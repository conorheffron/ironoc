package net.ironoc.portfolio.config;

import module java.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestRateLimitingInterceptorTest {

    private final Object handler = new Object();

    private RequestRateLimitingInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new RequestRateLimitingInterceptor();
    }

    @Test
    void preHandle_shouldBlockPutRequestsBeforeGetRequests() throws Exception {
        for (int i = 0; i < RequestRateLimitingInterceptor.WRITE_REQUEST_LIMIT; i++) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            assertTrue(interceptor.preHandle(request("PUT", "127.0.0.1"), response, handler));
            assertThat(response.getHeader("X-Rate-Limit-Limit"),
                    is(Long.toString(RequestRateLimitingInterceptor.WRITE_REQUEST_LIMIT)));
        }

        MockHttpServletResponse blockedResponse = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(request("PUT", "127.0.0.1"), blockedResponse, handler));
        assertThat(blockedResponse.getStatus(), is(429));
        assertThat(blockedResponse.getHeader("Retry-After"), is(notNullValue()));
        assertThat(blockedResponse.getContentAsString(), is("Rate limit exceeded for PUT requests."));
    }

    @Test
    void preHandle_shouldKeepSeparateBucketsPerMethod() throws Exception {
        for (int i = 0; i < RequestRateLimitingInterceptor.WRITE_REQUEST_LIMIT; i++) {
            assertTrue(interceptor.preHandle(request("POST", "127.0.0.1"), new MockHttpServletResponse(), handler));
        }

        MockHttpServletResponse getResponse = new MockHttpServletResponse();
        assertTrue(interceptor.preHandle(request("GET", "127.0.0.1"), getResponse, handler));
        assertThat(getResponse.getHeader("X-Rate-Limit-Limit"),
                is(Long.toString(RequestRateLimitingInterceptor.GET_REQUEST_LIMIT)));
        assertThat(getResponse.getHeader("X-Rate-Limit-Remaining"),
                is(Long.toString(RequestRateLimitingInterceptor.GET_REQUEST_LIMIT - 1)));
    }

    @Test
    void preHandle_shouldIgnoreUnsupportedMethods() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request("DELETE", "127.0.0.1"), response, handler));
        assertThat(response.getHeader("X-Rate-Limit-Limit"), is((String) null));
    }

    private MockHttpServletRequest request(String method, String remoteAddr) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, "/api/test");
        request.setRemoteAddr(remoteAddr);
        return request;
    }
}
