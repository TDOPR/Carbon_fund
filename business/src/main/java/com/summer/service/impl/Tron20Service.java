//package com.summer.service.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.google.gson.Gson;
//import com.pp.config.TronConfig;
//import com.pp.feign.TronFullNodeFeign;
//import com.pp.feign.dt.GetTransactionSign;
//import com.pp.feign.dt.TriggerSmartContract;
//import com.pp.feign.result.TranscationByIdView;
//import com.pp.utils.Help;
//import com.pp.utils.R;
//import com.pp.utils.TronUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.tron.common.utils.ByteArray;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.math.RoundingMode;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * trc20-service
// *
// * @Autor Shadow 2020/10/22
// * @Date 2020-10-22 15:49:41
// */
//@Service
//@Slf4j
//public class Tron20Service {
//
////    @Value("${tron.contract-address}")
////    private String contractAddress;//hex格式
////
////    @Value("${tron.address}")
////    private String address;//发币地址 hex格式
//
//    //@Value("${gcp.private-key}")
//    @Autowired
//    TronConfig tronConfig;
//    private String privateKey;//私钥
//
//    //token的精度  就是小数点后面有多少位小数 然后1后面加多少个0就可以
//    private static final BigDecimal DECIMAL = new BigDecimal("1000000");
//
//    //TRX的小数尾数
//    private static final int NORMAL_ROUND = 6;
//
//    @Autowired
//    private TronFullNodeFeign feign;
//
//    /**
//     * 发送trc20交易  返回交易id
//     *
//     * @param toAddress 收币地址
//     * @param amount    转出数量
//     * @param remark    备注
//     * @return
//     */
//    public String sendTrc20Transaction(String toAddress, String amount, String remark) {
//        try {
//
//            String hexAddress = toAddress;
//            if (toAddress.startsWith("T")) {
//                hexAddress = TronUtils.toHexAddress(toAddress);
//            }
//            if (StringUtils.isEmpty(hexAddress)) {
//                log.error("转账失败:收款地址为空");
//                return null;
//            }
//            if (StringUtils.isEmpty(amount)) {
//                log.error("转账失败:额度为空");
//                return null;
//            }
//            BigDecimal a = new BigDecimal(amount);
//            if (a.compareTo(BigDecimal.ZERO) <= 0) {
//                log.error("转账失败:额度不符合规则 " + amount);
//                return null;
//            }
//            if (remark == null) {
//                remark = "";
//            }
//            String params = hexAddress + "@" + amount + "@" + remark;
//
//            TriggerSmartContract.Param param = createTriggerSmartContractParam();
//            param.setFunction_selector("transfer(address,uint256)");
//            String addressParam = addZero(hexAddress, 64);
//            String amountParam = addZero(new BigDecimal(amount).multiply(DECIMAL).toBigInteger().toString(16), 64);
//            param.setParameter(addressParam + amountParam);
//            log.info("创建交易参数:" + JSONObject.toJSONString(param));
//            TriggerSmartContract.Result obj = feign.triggerSmartContract(param);
//            log.info("创建交易结果:" + JSONObject.toJSONString(obj));
//            if (!obj.isSuccess()) {
//                log.error("创建交易失败|" + params);
//                return null;
//            }
//
//            GetTransactionSign.Param signParam = new GetTransactionSign.Param();
//            TriggerSmartContract.Transaction transaction = obj.getTransaction();
//            transaction.getRaw_data().put("data", ByteArray.toHexString(remark.getBytes()));
//            signParam.setTransaction(transaction);
//            signParam.setPrivateKey(privateKey);
//            log.info("签名交易参数:" + JSONObject.toJSONString(signParam));
//            Object dt = feign.getTransactionSign(signParam);
//            log.info("签名交易结果:" + JSONObject.toJSONString(dt));
//            //广播交易
//            if (dt != null) {
//                log.info("广播交易参数:" + JSONObject.toJSONString(dt));
//                JSONObject rea = feign.broadcastTransaction(dt);
//                log.info("广播交易结果:" + JSONObject.toJSONString(rea));
//                if (rea != null) {
//                    Object result = rea.get("result");
//                    if (result instanceof Boolean) {
//                        if ((boolean) result) {
//                            return (String) rea.get("txid");
//                        }
//                    }
//                }
//            }
//        } catch (Throwable t) {
//            log.error(t.getMessage(), t);
//        }
//        return null;
//    }
//
//    /**
//     * 创建智能合约参数
//     *
//     * @return
//     */
//    private TriggerSmartContract.Param createTriggerSmartContractParam() {
//        TriggerSmartContract.Param tscParam = new TriggerSmartContract.Param();
////        tscParam.setOwner_address(TronUtils.toHexAddress(address));
//        tscParam.setContract_address(TronUtils.toHexAddress(tronConfig.getEventContract()));
//        tscParam.setFee_limit(1000000L);
//        return tscParam;
//    }
//
//    /**
//     * 补充0到64个字节
//     *
//     * @param dt
//     * @return
//     */
//    private String addZero(String dt, int length) {
//        StringBuilder builder = new StringBuilder();
//        final int count = length;
//        int zeroAmount = count - dt.length();
//        for (int i = 0; i < zeroAmount; i++) {
//            builder.append("0");
//        }
//        builder.append(dt);
//        return builder.toString();
//    }
//
//    /**
//     * 查询代币地址余额
//     *
//     * @param contract 合约地址
//     * @param address  查询地址
//     * @return
//     */
//    public BigDecimal balanceOf(String contract, String address, int round) {
//
//        String hexAddress = address.startsWith("T") ? TronUtils.toHexAddress(address) : address;
//
//        String hexContract = contract.startsWith("T") ? TronUtils.toHexAddress(contract) : contract;
//
//        TriggerSmartContract.Param param = new TriggerSmartContract.Param();
//
//        param.setContract_address(hexContract);
//        param.setOwner_address(hexAddress);
//        param.setFunction_selector("balanceOf(address)");
//        String addressParam = addZero(hexAddress.substring(2), 64);
//        param.setParameter(addressParam);
//        TriggerSmartContract.Result result = feign.triggerSmartContract(param);
//        if (result != null && result.isSuccess()) {
//            String value = result.getConstantResult(0);
//            if (Help.isNotNull(value)) {
//                return TronUtils.bigIntegerToBigDecimal(new BigInteger(value, 16), round);
//            }
//        }
//        return BigDecimal.ZERO;
//    }
//
//    //justswap查询1个trx兑换多少个代币
//    //查询的地址可以随意填写
//    public BigDecimal getSccTokenToTrx(String contract, int round) {
//        String address = "TAmL3AQNgJ6XinpnLxGKzRzo1fweMz8VpE";
//        String hexAddress = address.startsWith("T") ? TronUtils.toHexAddress(address) : address;
//        String hexContract = contract.startsWith("T") ? TronUtils.toHexAddress(contract) : contract;
////        String amountParam = addZero(BigDecimal.ONE.multiply(BigDecimal.ONE.pow(round)).toBigInteger().toString(16), 64);
//        String amountParam = addZero(BigInteger.TEN.pow(6).toString(16), 64);
//
//        TriggerSmartContract.Param param = new TriggerSmartContract.Param();
//        param.setContract_address(hexContract);
//        param.setOwner_address(hexAddress);
//        param.setFunction_selector("getTrxToTokenInputPrice(uint256)");
//        param.setParameter(amountParam);
//        JSONObject result = feign.triggerconstantcontract(param);
//        if (result != null) {
//            String value = result.get("constant_result").toString().substring(1, result.get("constant_result").toString().length() - 1);
//            if (Help.isNotNull(value)) {
//                return TronUtils.bigIntegerToBigDecimal(new BigInteger(value, 16), round);
//            }
//        }
//        return BigDecimal.ZERO;
//    }
//
//    public List<BigDecimal> showUserBonus(String address) {
//        address = TronUtils.toHexAddress(address);
//        String addressParam = addZero(address.substring(2), 64);
//        TriggerSmartContract.Param param = createTriggerSmartContractParam();
//        param.setFunction_selector("showUserBonus(address)");
//        param.setParameter(addressParam);
//        TriggerSmartContract.Result result = feign.triggerSmartContract(param);
//        //log.info(log.getName() + " 返回参数:" + JSONObject.toJSONString(result));
//        List<Object> constant_result = result.getConstant_result();
//        if (null == constant_result || constant_result.size() <= 0) {
//            return null;
//        }
//        String resultStr = constant_result.get(0).toString();
//        if (Help.isNull(resultStr)) {
//            log.error(log.getName() + " resultStr为空!");
//            return null;
//        }
//
//        String amountStr1 = resultStr.substring(0, 64);
//        String amountStr2 = resultStr.substring(64, 128);
//        String amountStr3 = resultStr.substring(128, 192);
//        String amountStr4 = resultStr.substring(192, 256);
//
//        BigDecimal amount1 = Help.isNull(amountStr1) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr1, 16));
//        BigDecimal amount2 = Help.isNull(amountStr1) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr2, 16));
//        BigDecimal amount3 = Help.isNull(amountStr1) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr3, 16));
//        BigDecimal amount4 = Help.isNull(amountStr1) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr4, 16));
//
//        List<BigDecimal> list = new ArrayList<>();
//        list.add(amount1);
//        list.add(amount2);
//        list.add(amount3);
//        list.add(amount4);
//        return list;
//
//    }
//
//    public List<BigDecimal> showUserBonusDy(String address) {
//        address = TronUtils.toHexAddress(address);
//        String addressParam = addZero(address.substring(2), 64);
//        TriggerSmartContract.Param param = createTriggerSmartContractParam();
//        param.setFunction_selector("showUserBonusDy(address)");
//        param.setParameter(addressParam);
//        TriggerSmartContract.Result result = feign.triggerSmartContract(param);
//        //log.info(log.getName() + " 返回参数:" + JSONObject.toJSONString(result));
//        List<Object> constant_result = result.getConstant_result();
//        if (null == constant_result || constant_result.size() <= 0) {
//            return null;
//        }
//        String resultStr = constant_result.get(0).toString();
//        if (Help.isNull(resultStr)) {
//            log.error(log.getName() + " resultStr为空!");
//            return null;
//        }
//
//        String amountStr1 = resultStr.substring(0, 64);
//        String amountStr2 = resultStr.substring(64, 128);
//        String amountStr3 = resultStr.substring(128, 192);
//        String amountStr4 = resultStr.substring(192, 256);
//        String amountStr5 = resultStr.substring(256, 320);
//        String amountStr6 = resultStr.substring(320, 384);
//        String amountStr7 = resultStr.substring(384, 448);
//        String amountStr8 = resultStr.substring(448, 512);
//
//        BigDecimal amount1 = Help.isNull(amountStr1) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr1, 16));
//        BigDecimal amount2 = Help.isNull(amountStr2) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr2, 16));
//        BigDecimal amount3 = Help.isNull(amountStr3) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr3, 16));
//        BigDecimal amount4 = Help.isNull(amountStr4) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr4, 16));
//        BigDecimal amount5 = Help.isNull(amountStr5) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr5, 16));
//        BigDecimal amount6 = Help.isNull(amountStr6) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr6, 16));
//        BigDecimal amount7 = Help.isNull(amountStr7) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr7, 16));
//        BigDecimal amount8 = Help.isNull(amountStr8) ? BigDecimal.ZERO : new BigDecimal(new BigInteger(amountStr8, 16));
//
//        List<BigDecimal> list = new ArrayList<>();
//        list.add(amount1);
//        list.add(amount2);
//        list.add(amount3);
//        list.add(amount4);
//        list.add(amount5);
//        list.add(amount6);
//        list.add(amount7);
//        list.add(amount8);
//        return list;
//
//    }
//
//    private String castHexAddress(String address) {
//        if (address.startsWith("T")) {
//            return TronUtils.toHexAddress(address);
//        }
//        return address;
//    }
//
//    /**
//     * 代币转账  trc20
//     *
//     * @param contract
//     * @param fromAddress
//     * @param privateKey  fromAddress的私钥
//     * @param amount
//     * @param toAddress
//     * @return
//     */
//    public String transferTokenTransaction(String contract, String fromAddress, String privateKey, String amount, String toAddress) {
//        try {
//            String hexFromAddress = castHexAddress(fromAddress);
//            String hexToAddress = castHexAddress(toAddress);
//            String hexContract = castHexAddress(contract);
//
//            amount = amount.split("\\.")[0];
//            BigInteger a = new BigInteger(amount);
//
//            TriggerSmartContract.Param param = new TriggerSmartContract.Param();
//            param.setOwner_address(hexFromAddress);
//            param.setContract_address(hexContract);
//            param.setFee_limit(1000000000L);
//            param.setFunction_selector("transfer(address,uint256)");
//            String addressToParam = addZero(hexToAddress, 64);
//            String amountParam = addZero(a.toString(16), 64);
//            param.setParameter(addressToParam + amountParam);
//            log.info("创建交易参数:" + JSONObject.toJSONString(param));
//            TriggerSmartContract.Result obj = feign.triggerSmartContract(param);
//            log.info("创建交易结果:" + JSONObject.toJSONString(obj));
//            if (!obj.isSuccess()) {
//                log.error("创建交易失败");
//                return null;
//            }
//            //交易签名
//            GetTransactionSign.Param signParam = new GetTransactionSign.Param();
//            TriggerSmartContract.Transaction transaction = obj.getTransaction();
//            transaction.getRaw_data().put("data", ByteArray.toHexString("".getBytes()));
//            signParam.setTransaction(transaction);
//            signParam.setPrivateKey(privateKey);
//            log.info("签名交易参数:" + JSONObject.toJSONString(signParam));
//            Object dt = feign.getTransactionSign(signParam);
//            log.info("签名交易结果:" + JSONObject.toJSONString(dt));
//            //广播交易
//            if (dt != null) {
//                log.info("广播交易参数:" + JSONObject.toJSONString(dt));
//                JSONObject rea = feign.broadcastTransaction(dt);
//                log.info("广播交易结果:" + JSONObject.toJSONString(rea));
//                if (rea != null) {
//                    Object result = rea.get("result");
//                    if (result instanceof Boolean) {
//                        if ((boolean) result) {
//                            return (String) rea.get("txid");
//                        }
//                    }
//                }
//            }
//        } catch (Throwable t) {
//            log.error(t.getMessage(), t);
//        }
//        return null;
//    }
//
//    /**
//     * 代币转账  trc20
//     *
//     * @param contract
//     * @param fromAddress
//     * @param privateKey  fromAddress的私钥
//     * @param amount
//     * @param toAddress
//     * @param remark
//     * @return
//     */
//    public String sendTokenTransaction(String contract, String fromAddress, String privateKey, String amount, String toAddress, String remark) {
//        try {
//            String hexFromAddress = castHexAddress(fromAddress);
//            String hexToAddress = castHexAddress(toAddress);
//            String hexContract = castHexAddress(contract);
//
//            BigInteger a = new BigInteger(amount);
//            if (a.compareTo(BigInteger.ZERO) <= 0) {
//                log.error("转账失败:额度不符合规则 " + amount);
//                return null;
//            }
//            if (remark == null) {
//                remark = "";
//            }
//            TriggerSmartContract.Param param = new TriggerSmartContract.Param();
//            param.setOwner_address(hexFromAddress);
//            param.setContract_address(hexContract);
//            param.setFee_limit(1000000L);
//            param.setFunction_selector("transfer(address,uint256)");
//            String addressParam = addZero(hexToAddress, 64);
//            String amountParam = addZero(a.toString(16), 64);
//            param.setParameter(addressParam + amountParam);
//            log.info("创建交易参数:" + JSONObject.toJSONString(param));
//            TriggerSmartContract.Result obj = feign.triggerSmartContract(param);
//            log.info("创建交易结果:" + JSONObject.toJSONString(obj));
//            if (!obj.isSuccess()) {
//                log.error("创建交易失败");
//                return null;
//            }
//            //交易签名
//            GetTransactionSign.Param signParam = new GetTransactionSign.Param();
//            TriggerSmartContract.Transaction transaction = obj.getTransaction();
//            transaction.getRaw_data().put("data", ByteArray.toHexString(remark.getBytes()));
//            signParam.setTransaction(transaction);
//            signParam.setPrivateKey(privateKey);
//            log.info("签名交易参数:" + JSONObject.toJSONString(signParam));
//            Object dt = feign.getTransactionSign(signParam);
//            log.info("签名交易结果:" + JSONObject.toJSONString(dt));
//            //广播交易
//            if (dt != null) {
//                log.info("广播交易参数:" + JSONObject.toJSONString(dt));
//                JSONObject rea = feign.broadcastTransaction(dt);
//                log.info("广播交易结果:" + JSONObject.toJSONString(rea));
//                if (rea != null) {
//                    Object result = rea.get("result");
//                    if (result instanceof Boolean) {
//                        if ((boolean) result) {
//                            return (String) rea.get("txid");
//                        }
//                    }
//                }
//            }
//        } catch (Throwable t) {
//            log.error(t.getMessage(), t);
//        }
//        return null;
//    }
//
//    /**
//     * 查询tron币数量
//     *
//     * @param address
//     * @return
//     */
//    public BigDecimal balanceOfTron(String address) {
//        final BigDecimal decimal = new BigDecimal("1000000");
//
//        Map<String, Object> param = new HashMap<>();
//        param.put("address", castHexAddress(address));
//        JSONObject obj = feign.getAccount(param);
//        if (null != obj) {
//            BigInteger balance = obj.getBigInteger("balance");
//            if (Help.isNotNull(balance)) {
//                return new BigDecimal(balance).divide(decimal, NORMAL_ROUND, RoundingMode.FLOOR);
//            }
//        }
//        return BigDecimal.ZERO;
//    }
//
//    /**
//     * 生成一个地址
//     *
//     * @return
//     */
//    public JSONObject createAddress() {
//        return feign.generateAddress();
//    }
//
//    public BigInteger testBalanceOf(String address, String contract) {
//
//        String hexAddress = address;
//        if (address.startsWith("T")) {
//            hexAddress = TronUtils.toHexAddress(address);
//        }
//        String hexContract = contract;
//        if (contract.startsWith("T")) {
//            hexContract = TronUtils.toHexAddress(contract);
//        }
//        TriggerSmartContract.Param param = new TriggerSmartContract.Param();
//        param.setContract_address(hexContract);
//        param.setOwner_address(hexAddress);
//        param.setFunction_selector("balanceOf(address)");
//        String addressParam = addZero(hexAddress.substring(2), 64);
//        param.setParameter(addressParam);
//        TriggerSmartContract.Result result = feign.triggerSmartContract(param);
//        log.info("返回参数:" + JSONObject.toJSONString(result));
//        if (result != null && result.isSuccess()) {
//            String value = result.getConstantResult(0);
//            if (value != null) {
//                return new BigInteger(value, 16);
//            }
//        }
//        return BigInteger.ZERO;
//    }
//
//    /**
//     * 通过交易ID获取是否成功
//     *
//     * @param txId
//     * @return
//     */
//    public R getConfirmedTransaction(String txId) {
//        Map<String, Object> param = new HashMap<>();
//        param.put("value", txId);
//        param.put("visible", "true");
//        JSONObject obj = feign.getConfirmedTransaction(param);
//        log.info("通过交易ID获取已确认的交易返回信息：{}", obj);
//        if (null != obj && obj.size() != 0) {
//            TranscationByIdView transcation = new Gson().fromJson(obj.toJSONString(), TranscationByIdView.class);
//            if (null != transcation) {
//                String result = transcation.getReceipt().getResult();
//                if ("SUCCESS".equals(result)) {
//                    return R.OkData(0, transcation.getBlockNumber().toString());
//                }
//            }
//        }
//        return R.error(500, "交易失败");
//    }
//
//    /**
//     * 获取特定区块的所有交易 Info 信息
//     *
//     * @param num
//     * @return
//     */
//    public R getTransactionInfoByBlockNum(BigInteger num) {
//        /**
//         *  25656644
//         *     包含:
//         *             78 Txns
//         *     区块收益: 16 TRX
//         *     出块者:
//         */
//        Map<String, Object> param = new HashMap<>();
//        param.put("num", num);
//        //JSONObject obj = feign.gettransactioninfobyblocknum(param);
//        //log.info("获取特定区块的所有交易 Info 信息返回信息：{}", obj);
//        List<JSONObject> list = feign.gettransactioninfobyblocknum(param);
//        log.info("获取特定区块的所有交易 Info 信息返回信息：{}", list);
//        /*if (null != obj && obj.size() != 0) {
//            TranscationByIdView transcation = new Gson().fromJson(obj.toJSONString(), TranscationByIdView.class);
//            if (null != transcation) {
//                String result = transcation.getReceipt().getResult();
//                if ("SUCCESS".equals(result)) {
//                    return R.OkData(200, null);
//                }
//            }
//        }*/
//        return R.error(500, "交易失败");
//    }
//
//    public R getTransactionFromBlock(BigInteger num) {
//        /**
//         *  25656644
//         *     包含:
//         *             78 Txns
//         *     区块收益: 16 TRX
//         *     出块者:
//         */
//        Map<String, Object> param = new HashMap<>();
//        param.put("num", num);
//        JSONObject obj = feign.getTransactionFromBlock(param);
//        log.info("获取特定区块的所有交易 Info 信息返回信息：{}", obj);
//
//        return R.error(500, "交易失败");
//    }
//
//}
