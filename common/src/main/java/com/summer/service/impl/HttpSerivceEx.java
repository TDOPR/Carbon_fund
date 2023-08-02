package com.summer.service.impl;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class HttpSerivceEx extends HttpService {
    public HttpSerivceEx() {
        super();
    }
    
    public HttpSerivceEx(String url) {
        super(url,createOkHttpClient(),false);
    }
    
    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(true);
        log.info("---------设置超时时间--------{}秒", 120);
        builder.connectionPool(new ConnectionPool())
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS).build();
        
        return builder.build();
    }
}
