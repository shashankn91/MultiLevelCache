package com.phonepe.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SystemConfiguration {

    @JsonProperty("levels")
    private Integer levels;


    @JsonProperty("cacheConfigurations")
    private List<CacheConfiguration> cacheConfigurations;

}
