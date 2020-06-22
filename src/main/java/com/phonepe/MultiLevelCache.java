package com.phonepe;

import com.phonepe.config.SystemConfiguration;
import com.phonepe.models.CacheResponse;
import com.phonepe.models.MultiLevelCacheResponse;
import com.phonepe.models.OperationType;
import com.phonepe.models.State;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cache Responses can be used to collect metrics
 */
@Log
public class MultiLevelCache implements IMultiLevelCache {


    private final List<ICache> caches;

    public MultiLevelCache(SystemConfiguration systemConfiguration) {
        caches = systemConfiguration.getCacheConfigurations().stream().map(cc -> new Cache(cc.getCapacity(), cc.getReadTime(), cc.getWriteTime(), cc.getLevel())).collect(Collectors.toList());
    }


    @Override
    public MultiLevelCacheResponse read(String key){

        List<CacheResponse> cacheResponses = new ArrayList<>();
        for (ICache cache: caches) {
            CacheResponse cacheResponse = cache.read(key);
            cacheResponses.add(cacheResponse);
            if(cacheResponse.getState() == State.FOUND){
                break;
            }

        }
        if(cacheResponses.size() == 0){
            throw new RuntimeException("Invalid state");
        }
        CacheResponse lastResponse = cacheResponses.get(cacheResponses.size() -1);

        if (lastResponse.getState() == State.FOUND){
                for (ICache cache: caches) {
                    if(cache == lastResponse.getCache()){
                        break;
                    }
                    cacheResponses.add(cache.write(lastResponse.getKey(), lastResponse.getValue()));
                }

                return (new MultiLevelCacheResponse(lastResponse.getKey(),lastResponse.getValue(), State.FOUND, OperationType.READ, cacheResponses));
        }

        return (new MultiLevelCacheResponse(lastResponse.getKey(),lastResponse.getValue(), State.NOT_FOUND, OperationType.READ, cacheResponses));
    }


    @Override
    public synchronized MultiLevelCacheResponse  write(String key, String value){
        List<CacheResponse> cacheResponses = new ArrayList<>();
        for (ICache cache: caches) {
            cacheResponses.add(cache.read(key));
            cacheResponses.add(cache.write(key, value));
        }

        return new MultiLevelCacheResponse(key, value, State.DOES_NOT_MATTER, OperationType.WRITE, cacheResponses);
        }


}
