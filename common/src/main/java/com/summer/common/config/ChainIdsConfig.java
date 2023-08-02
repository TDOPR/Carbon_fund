package com.summer.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "chainids")
public class ChainIdsConfig {
    
    /**
     *
     */
    private String eth;
    /**
     *
     */
    private String bsc;
    /**
     *
     */
    private String tron;
    
}
