package com.summer.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tron")
public class TronConfig {
    
    /**
     * Tron合约地址
     */
    private String eventContract;
    /**
     * 区块间隔
     */
    private Integer blockDiff;
    /**
     * 最新区块url
     */
    private String urlLastBlock;
    /**
     * firstUrl
     */
    private String firstUrl;
    /**
     * lastUrl
     */
    private String lastUrl;
    
}
