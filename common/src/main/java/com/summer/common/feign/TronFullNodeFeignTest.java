package com.summer.common.feign;

import com.alibaba.fastjson.JSONObject;
import com.summer.common.feign.dt.GetTransactionSign;
import com.summer.common.feign.dt.TriggerSmartContract;
import feign.Headers;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * tron-full-node接口
 *
 * @Autor Shadow 2020/10/22
 * @Date 2020-10-22 15:36:02
 */
@FeignClient(url = "${tron.testurl}", name = "tron-node-test", configuration = {JacksonEncoder.class, JacksonDecoder.class})
public interface TronFullNodeFeignTest {

    /**
     * 智能合约调用接口
     *
     * @param param
     * @return
     */
    @PostMapping("/wallet/triggersmartcontract")
    TriggerSmartContract.Result triggerSmartContract(@RequestBody TriggerSmartContract.Param param);

    /**
     * 智能合约调用接口
     *
     * @param param
     * @return
     */
    @PostMapping("/wallet/triggerconstantcontract")
    JSONObject triggerconstantcontract(@RequestBody TriggerSmartContract.Param param);


    /**
     * 使用私钥签名交易.（存在安全风险，trongrid已经关闭此接口服务，请使用离线方式或者自己部署的节点）
     *
     * @param param
     * @return
     */
    @PostMapping("/wallet/gettransactionsign")
    JSONObject getTransactionSign(@RequestBody GetTransactionSign.Param param);


    /**
     * 广播签名后的交易.
     *
     * @param rawBody
     * @return
     */
    @PostMapping("/wallet/broadcasttransaction")
    JSONObject broadcastTransaction(@RequestBody Object rawBody);


    /**
     * 创建地址
     *
     * @return
     */
    @PostMapping("/wallet/generateaddress")
    JSONObject generateAddress();

    /**
     * 获取账号信息
     *
     * @param param
     * @return
     */
    @PostMapping("/wallet/getaccount")
    JSONObject getAccount(@RequestBody Map<String, Object> param);


    /**
     * 通过交易id 获取交易信息

     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PostMapping("/wallet/gettransactioninfobyid")
    JSONObject getConfirmedTransaction (@RequestBody Map<String, Object> param);


    /**
     * 通过交易id 获取交易信息

     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PostMapping("/wallet/gettransactioninfobyblocknum")
    JSONObject gettransactioninfobyblocknum (@RequestBody Map<String, Object> param);
}
