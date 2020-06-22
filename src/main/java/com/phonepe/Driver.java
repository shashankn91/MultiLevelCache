package com.phonepe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.config.SystemConfiguration;
import com.phonepe.metrics.IMetricsGenerator;
import com.phonepe.metrics.ReadTimeMetrics;
import com.phonepe.metrics.UsageMetrics;
import com.phonepe.metrics.WriteTimeMetrics;
import com.phonepe.models.MultiLevelCacheResponse;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Log
public class Driver {

    public static void main(String[] args) {
        try {
            String content = new String(Driver.class.getResourceAsStream("/config.json").readAllBytes());
            ObjectMapper objectMapper = new ObjectMapper();

            SystemConfiguration config = objectMapper.readValue(content, SystemConfiguration.class);

            List<IMetricsGenerator> metricsGenerators = new ArrayList<>();
            metricsGenerators.add(new ReadTimeMetrics());
            metricsGenerators.add(new WriteTimeMetrics());
            metricsGenerators.add(new UsageMetrics());

            DecoratedMultiLevelCache multiLevelCache = new DecoratedMultiLevelCache(new MultiLevelCache(config), metricsGenerators);

            Random rand = new Random();
            for (int i=0; i< 100;i++){
                Integer key = rand.nextInt(1);
                if(i%3 == 0){
                    Integer value = rand.nextInt(100);
                    MultiLevelCacheResponse writeResponse = multiLevelCache.write(key.toString(), value.toString());
                    Integer totalTime = writeResponse.getCacheResponses().stream().mapToInt(c -> c.getTime()).sum();
                    log.info("writeResponse Size = " + writeResponse.getCacheResponses().size() + " state = " + writeResponse.getState() + " total Time =" + totalTime);
                }
                else {
                    MultiLevelCacheResponse readResponse =  multiLevelCache.read(key.toString());
                    Integer totalTime = readResponse.getCacheResponses().stream().mapToInt(c -> c.getTime()).sum();
                    log.info("readResponse Size = " + readResponse.getCacheResponses().size() + " state = " + readResponse.getState() + " total Time =" + totalTime);

                }

                if(i%10 == 0){
                    metricsGenerators.stream().forEach(me -> System.out.println(me.getMetrics()));
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
            log.severe("Failed to load config");
        }
    }
}

