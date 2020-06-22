package com.phonepe.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CacheConfiguration {

    @JsonProperty("writeTime")
    private Integer writeTime;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("readTime")
    private Integer readTime;

    @JsonProperty("capacity")
    private Integer capacity;

}
