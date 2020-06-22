package com.phonepe;

import com.phonepe.models.CacheResponse;

public interface ICache {
    CacheResponse write(String key, String value);
    CacheResponse read(String key);
}
