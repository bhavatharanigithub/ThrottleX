package com.throttlex.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final int maxRequests;
    private final long windowSeconds;

    private final Map<String, Deque<Instant>> requestLog = new ConcurrentHashMap<>();

    public RateLimiterService(
            @Value("${throttlex.rate-limit.requests}") int maxRequests,
            @Value("${throttlex.rate-limit.window-seconds}") long windowSeconds
    ) {
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
    }

    public boolean isAllowed(String key) {
        Instant now = Instant.now();
        Instant windowStart = now.minusSeconds(windowSeconds);

        Deque<Instant> deque = requestLog.computeIfAbsent(key, k -> new ArrayDeque<>());

        synchronized (deque) {
            while (!deque.isEmpty() && deque.peekFirst().isBefore(windowStart)) {
                deque.pollFirst();
            }

            if (deque.size() >= maxRequests) {
                return false;
            }

            deque.addLast(now);
            return true;
        }
    }
}

