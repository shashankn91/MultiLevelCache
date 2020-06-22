package com.phonepe.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@Getter
public class MultiLevelCacheResponse {

    @NonNull
    private final String key;

    private final String value;

    @NonNull
    private final State state;

    @NonNull
    private final OperationType operationType;

    @NonNull
    private final List<CacheResponse> cacheResponses;




}
