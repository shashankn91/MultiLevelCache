package com.phonepe;

import com.phonepe.metrics.IMetricsGenerator;
import com.phonepe.models.MultiLevelCacheResponse;

import java.util.List;

public class DecoratedMultiLevelCache implements IMultiLevelCache {

    private final IMultiLevelCache multiLevelCache;
    private final List<IMetricsGenerator> metricsGenerators;

    public DecoratedMultiLevelCache(IMultiLevelCache multiLevelCache, List<IMetricsGenerator> metricsGenerators) {
        this.multiLevelCache = multiLevelCache;
        this.metricsGenerators = metricsGenerators;
    }


    @Override
    public MultiLevelCacheResponse read(String key) {
        return updateMetricObservers(multiLevelCache.read(key));
    }

    @Override
    public MultiLevelCacheResponse write(String key, String value) {

        return updateMetricObservers(multiLevelCache.write(key, value));
    }

    private MultiLevelCacheResponse updateMetricObservers(final MultiLevelCacheResponse multiLevelCacheResponse) {
        metricsGenerators.stream().forEach(mg -> mg.addLogs(multiLevelCacheResponse));
        return multiLevelCacheResponse;
    }
}
