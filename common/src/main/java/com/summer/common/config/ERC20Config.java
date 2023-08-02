package com.summer.common.config;

import com.summer.service.impl.HttpSerivceEx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.JsonRpc2_0Web3j;

@Configuration
@Slf4j
public class ERC20Config {
    @Autowired
    private TokenConfig tokenConfig;
    
    
    @Bean
    public Web3jService web3jService() {
        return new HttpSerivceEx(tokenConfig.getWeb3EthServiceUrl());
    }
    
    @Bean
    public Web3jService web3jBscService() {
        return new HttpSerivceEx(tokenConfig.getWeb3BscServiceUrl());
    }
    
    @Bean
    public Web3jService web3jTronService() {
        return new HttpSerivceEx(tokenConfig.getWeb3TronServiceUrl());
    }
    
    @Bean
    public Admin admin() {
        return Admin.build(web3jService());
    }
    
    @Bean
    public Admin adminBsc() {
        return Admin.build(web3jBscService());
    }
    
    @Bean
    public Admin adminTron() {
        return Admin.build(web3jTronService());
    }
    
    @Bean
    public JsonRpc2_0Web3j jsonRpc() {
        return new JsonRpc2_0Web3j(web3jService());
    }
    
    @Bean
    public JsonRpc2_0Web3j jsonBscRpc() {
        return new JsonRpc2_0Web3j(web3jBscService());
    }
    
    @Bean
    public JsonRpc2_0Web3j jsonTronRpc() {
        return new JsonRpc2_0Web3j(web3jTronService());
    }
}
