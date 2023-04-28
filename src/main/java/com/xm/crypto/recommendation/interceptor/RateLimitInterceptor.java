package com.xm.crypto.recommendation.interceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limiter limits requests from the same ip per time
 * limits are defined in properties under 'rate-limit.capacity' and 'rate-limit.duration'
 * default values are 5 requests per 1 minute
 * in case limit is exceeded the request is rejected with http status 429 - Too many requests
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);
    public static final int DEFAULT_CAPACITY = 5;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final int capacity;
    private Duration duration = Duration.ofMinutes(1);

    public RateLimitInterceptor(Environment env) {
        var capacity = env.getProperty("rate-limit.capacity", Integer.class);
        this.capacity = capacity != null ? capacity : DEFAULT_CAPACITY;
        var durationString = env.getProperty("rate-limit.duration");
        try {
            if (durationString != null) {
                duration = Duration.parse(durationString);
            }
        } catch (DateTimeParseException exception) {
            logger.error("Unable to parse duration. You need to fix property 'rate-limit.duration', current value: {}", duration);
        }
        logger.info("Rate Limiter capacity: {}, duration: {}", capacity, duration.toString());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var host = request.getRemoteHost();
        var bucket = buckets.computeIfAbsent(host, k -> {
            Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(capacity, duration));
            return Bucket.builder()
                    .addLimit(limit)
                    .build();
        });

        var allow = bucket.tryConsume(1);
        if (!allow) {
            logger.warn("Rate Limit is blocking request from: {}, to: {}", host, request.getRequestURI());
            response.setStatus(429);
        }
        return allow;
    }
}
