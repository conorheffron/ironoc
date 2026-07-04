package net.ironoc.portfolio.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ActivityTrackingService {

    private final ConcurrentHashMap<String, LongAdder> clickOutCounts = new ConcurrentHashMap<>();

    public void incrementClickOut(String category, String target) {
        clickOutCounts.computeIfAbsent(getKey(category, target), key -> new LongAdder()).increment();
    }

    public Map<String, Long> getClickOutReport() {
        return clickOutCounts.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().longValue()));
    }

    private String getKey(String category, String target) {
        return category + ":" + target;
    }
}
