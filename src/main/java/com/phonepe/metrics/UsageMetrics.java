package com.phonepe.metrics;

import com.phonepe.Cache;
import com.phonepe.models.MultiLevelCacheResponse;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class UsageMetrics implements IMetricsGenerator{

    private Map<Cache, Integer> usage = new HashMap<>();


    @Override
    public void addLogs(MultiLevelCacheResponse multiLevelCacheResponse) {
        multiLevelCacheResponse.getCacheResponses().stream().forEach(cr -> usage.put(cr.getCache(), cr.getUsage()));
    }

    @Override
    public String getMetrics() {
        return usage.entrySet().stream().map(e-> "Cache Level = " + e.getKey().getLevel() + " Capacity = "+ e.getValue() + " ").collect(Collectors.joining()).concat("\n");
    }
}
