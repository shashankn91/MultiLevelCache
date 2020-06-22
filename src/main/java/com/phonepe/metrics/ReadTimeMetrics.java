package com.phonepe.metrics;

import com.phonepe.models.MultiLevelCacheResponse;
import com.phonepe.models.OperationType;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;
@NoArgsConstructor
public class ReadTimeMetrics implements IMetricsGenerator{


    private final Integer MAX_LOG_LIMIT = 10;
        private final Queue<MultiLevelCacheResponse> multiLevelCacheLogs = new LinkedList<>();


        @Override
        public void addLogs(MultiLevelCacheResponse multiLevelCacheResponse) {
            if(multiLevelCacheResponse.getOperationType() == OperationType.WRITE){
                return;
            }

            if(multiLevelCacheLogs.size() >= MAX_LOG_LIMIT){
                multiLevelCacheLogs.poll();
            }
            multiLevelCacheLogs.add(multiLevelCacheResponse);
        }

        @Override
        public String getMetrics() {
            Double totalTime = multiLevelCacheLogs.stream().flatMapToInt(mcl -> mcl.getCacheResponses().stream().mapToInt(cr -> cr.getTime())).average().orElse(0.0);
            return "ReadTimeMetrics totalTime = " + totalTime.toString() + "\n";
        }


}
