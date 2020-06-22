package com.phonepe.models;

import com.phonepe.Cache;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CacheResponse {
    @NonNull
    private final String key;

    private final String value;

    @NonNull
    private final State state;

    @NonNull
    private final Integer time;

    @NonNull
    private final OperationType operationType;

    @NonNull
    private final Cache cache;

    @NonNull
    private final Integer usage;

    public CacheResponse(@NonNull State state, @NonNull Integer time, @NonNull OperationType operationType, @NonNull Cache cache,@NonNull String key) {
        this.state = state;
        this.time = time;
        this.operationType = operationType;
        this.cache = cache;
        this.key = key;
        this.value = null;
        this.usage =cache.getElements().size();
    }

    public CacheResponse(@NonNull State state, @NonNull Integer time, @NonNull OperationType operationType,  @NonNull Cache cache, @NonNull String key, final String value) {
        this.state = state;
        this.time = time;
        this.operationType = operationType;
        this.cache = cache;
        this.key = key;
        this.value = value;
        this.usage =cache.getElements().size();
    }
}
