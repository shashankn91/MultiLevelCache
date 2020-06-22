package com.phonepe;

import com.phonepe.models.MultiLevelCacheResponse;

public interface IMultiLevelCache {
    MultiLevelCacheResponse read(String key);

    MultiLevelCacheResponse write(String key, String value);
}
