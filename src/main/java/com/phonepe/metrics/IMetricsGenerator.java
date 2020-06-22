package com.phonepe.metrics;

import com.phonepe.models.MultiLevelCacheResponse;

public interface IMetricsGenerator {
    void addLogs(MultiLevelCacheResponse multiLevelCacheResponse);
    String getMetrics();

}
