//package com.summer.manager;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.summer.common.config.ChainIdsConfig;
//import com.summer.common.config.TokenConfig;
//import com.summer.common.config.TronConfig;
//import com.summer.common.util.Help;
//import com.summer.common.util.TronUiltNew;
//import com.summer.common.util.TronUtils;
//import com.summer.enums.FlowingActionEnum;
//import com.summer.enums.FlowingTypeEnum;
//import com.summer.enums.LevelEnum;
//import com.summer.enums.RechargeStatusEnum;
//import com.summer.mapper.AppUserMapper;
//import com.summer.mapper.CoinConfigMapper;
//import com.summer.mapper.EvmUserWalletMapper;
//import com.summer.model.CoinConfig;
//import com.summer.model.EvmEvent;
//import com.summer.model.EvmUserWallet;
//import com.summer.service.CoinConfigService;
//import com.summer.service.EvmEventService;
//import com.summer.service.EvmUserWalletService;
//import com.summer.service.WalletsService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//@Slf4j
//@Service
//public class TronEventManager {
//    @Autowired
//    private CoinConfigService coinConfigService;
//
//    @Autowired
//    private EvmEventService eventService;
//
////    @Autowired
////    private Tron20Service tron20Service;
//
//    @Autowired
//    private TokenConfig tokenConfig;
//
//    @Autowired
//    private TronConfig tronConfig;
//
//    @Autowired
//    private TradeManager tradeManager;
//
//    @Autowired
//    private ChainIdsConfig chainIdsConfig;
//
//    @Resource
//    private EvmUserWalletMapper evmUserWalletMapper;
//
//    @Resource
//    private AppUserMapper appUserMapper;
//
//    @Autowired
//    private EvmUserWalletService evmUserWalletService;
//
//    @Autowired
//    private WalletsService walletsService;
//
//    @Resource
//    private CoinConfigMapper coinConfigMapper;
//
////    private final static String EVENT_CONTRACT = "TPUBjsQNoAyVuxJmg1n8pKjA4YtejbGy3L";
////
////    private final static BigInteger BLOCK_DIFF = new BigInteger("15");
////
////    private static String URL_LAST_BLOCK = "https://api.trongrid.io/wallet/getnowblock";
//
//    public void analyzeTRONEvent() throws Exception {
//        //获取事务
//        String contractAddress = tronConfig.getEventContract();
//        CoinConfig scanDataConfig = coinConfigMapper.getScanDataConfig(chainIdsConfig.getTron());
//        BigInteger blockNumber = scanDataConfig.getBlockNo();
//        blockNumber = blockNumber.add(BigInteger.ONE);
//        //获取当前最新区块
//        String blockStr = this.doGet(tronConfig.getUrlLastBlock());
//        TronUiltNew.BlockResultBean blockInfo = (TronUiltNew.BlockResultBean) JSON.parseObject(blockStr, TronUiltNew.BlockResultBean.class);
//        BigInteger currentBlock = new BigInteger(blockInfo.block_header.raw_data.number + "");
//        if (currentBlock.compareTo(blockNumber.add(new BigInteger(tronConfig.getBlockDiff().toString()))) == 1) {
//            String url = tronConfig.getFirstUrl() + contractAddress + tronConfig.getLastUrl() + blockNumber.toString();
//
//            //根据事务处理逻辑
//            String res = this.doGet(url);
//            JSONObject data = JSONObject.parseObject(res);
//            JSONArray events = data.getJSONArray("data");
//            for (int i = 0; i < events.size(); i++) {
//                JSONObject eventObj = JSONObject.parseObject(events.get(i).toString());
//                String eventName = eventObj.get("event_name").toString();
//                JSONObject eventParams = JSONObject.parseObject(eventObj.get("result").toString());
//                String userAddress = TronUtils.fromHexAddress("41" + eventParams.get(0).toString().substring(2)).toLowerCase();
//                BigDecimal rechargeAmount = new BigDecimal(eventParams.get(1).toString());
//                Integer userLevel = new Integer(eventParams.get(2).toString());
////                tradeManager.evmRecharge(userAddress, rechargeAmount, userLevel, chainIdsConfig.getTron(), RechargeStatusEnum.RECHARGE_SUCCESS.getStatus());
//                EvmEvent eventEntity = new EvmEvent();
//                if (eventName.equals("BuyNode")) {
//                    tradeManager.evmRecharge(userAddress, rechargeAmount, userLevel, chainIdsConfig.getTron(), RechargeStatusEnum.RECHARGE_SUCCESS.getStatus());
////                    Integer a = evmUserWalletMapper.getUserLevel(appUserMapper.findUserIdByUserAddress(userAddress));
////                    BigDecimal b = LevelEnum.getRechargeAmountByLevel(userLevel);
////                    BigDecimal c = evmUserWalletMapper.getRechargeAmount(appUserMapper.findUserIdByUserAddress(userAddress));
//
//                    if (userLevel.compareTo(evmUserWalletMapper.getUserLevel(appUserMapper.findUserIdByUserAddress(userAddress))) <= 0 || LevelEnum.getRechargeAmountByLevel(userLevel).compareTo(evmUserWalletMapper.getRechargeAmount(appUserMapper.findUserIdByUserAddress(userAddress)).add(rechargeAmount)) > 0) {
//                        continue;
//                    }
//                    UpdateWrapper<EvmUserWallet> updateWrapper = Wrappers.update();
//                    updateWrapper.lambda()
//                            .set(EvmUserWallet::getUserAddress, userAddress)
//                            .set(EvmUserWallet::getUserLevel, userLevel)
//                            .eq(EvmUserWallet::getUserId, appUserMapper.findUserIdByUserAddress(userAddress)).eq(EvmUserWallet::getChainId, chainIdsConfig.getTron());
//                    evmUserWalletService.update(updateWrapper);
//                    walletsService.updateWallet(userAddress, rechargeAmount, FlowingActionEnum.INCOME, FlowingTypeEnum.RECHARGE, chainIdsConfig.getTron());
//
//
//                    log.info("当前扫描到的区块是" + blockNumber);
//
//                    eventEntity.setBlockNum(blockNumber);
//                }else if(eventName.equals("TransferUsdt")){
//
//                }
////                } else if (eventName.equals("BuyPowerTwo")) {
//////                    this.buyPower(addr, inviteAddr, amount, 1);
////                    log.info("当前扫描到的区块是" + blockNumber);
////
////                    eventEntity.setBlockNum(blockNumber.toString());
////                } else if (eventName.equals("BuyLpPower")) {
//////                    this.buyLpPower(addr, inviteAddr, amount);
////                    log.info("当前扫描到的区块是" + blockNumber);
////
////                    eventEntity.setBlockNum(blockNumber.toString());
////                }
//                if (Help.isNotNull(eventEntity)) {
//                    eventService.save(eventEntity);
//                }
//            }
//            //更新事务处理块no
//            coinConfigMapper.updateActionSeqById(scanDataConfig.getId(), blockNumber);
//            log.info("当前扫描到的区块是" + blockNumber);
//        }
//    }
//
////    public R debugAnalyzeEvent(String txHash) throws Exception {
////        //获取事务
////        String contractAddress = EVENT_CONTRACT;
////        //根据hash查询区块号
////        R infoResult = tron20Service.getConfirmedTransaction(txHash);
////        if (Help.isNotNull(infoResult) && "0".equals(infoResult.get("code").toString())) {
////            String blockNumber = infoResult.get("data").toString();
////            //查看是否处理过该区块
////            QueryWrapper<EvmEvent> eventEntityQueryWrapper = new QueryWrapper<>();
////            eventEntityQueryWrapper.eq("block_num", blockNumber);
////            EvmEvent checkEventEntity = eventService.getOne(eventEntityQueryWrapper);
////            if (Help.isNotNull(checkEventEntity)) {
////                return R.error("该交易已完成，不可重复添加");
////            }
////            //查看该区块是否领先系统区块
////            QueryWrapper<CoinConfig> coinConfigEntityQueryWrapper = new QueryWrapper<>();
////            coinConfigEntityQueryWrapper.eq("coin", "EVENT");
////            CoinConfig coinConfig = coinConfigService.getOne(coinConfigEntityQueryWrapper);
////            if (new BigInteger(blockNumber).compareTo(coinConfig.getBlockNo()) > -1) {
////                return R.error("该交易系统还未扫描到，请耐心等待扫描，暂时无法手动添加");
////            }
////            //获取当前区块事务
////            String url = "https://api.trongrid.io/v1/contracts/" + contractAddress + "/events?block_number=" + blockNumber.toString();
////            //根据事务处理逻辑
////            String res = this.doGet(url);
////            JSONObject data = JSONObject.parseObject(res);
////            JSONArray events = data.getJSONArray("data");
////            for (int i = 0; i < events.size(); i++) {
////                JSONObject eventObj = JSONObject.parseObject(events.get(i).toString());
////                String eventName = eventObj.get("event_name").toString();
////                JSONObject eventParams = JSONObject.parseObject(eventObj.get("result").toString());
////                String addr = TronUtils.fromHexAddress("41" + eventParams.get(0).toString().substring(2));
////                String inviteAddr = TronUtils.fromHexAddress("41" + eventParams.get(1).toString().substring(2));
////                String amount = eventParams.get(2).toString();
////                EvmEvent eventEntity = new EvmEvent();
////                if (eventName.equals("BuyPowerOne")) {
//////                    this.buyPower(addr, inviteAddr, amount, 0);
////                    log.info("当前扫描到的区块是" + blockNumber);
////
////                    eventEntity.setBlockNum(new BigInteger(blockNumber));
////                } else if (eventName.equals("BuyPowerTwo")) {
//////                    this.buyPower(addr, inviteAddr, amount, 1);
////                    log.info("当前扫描到的区块是" + blockNumber);
////
////                    eventEntity.setBlockNum(new BigInteger(blockNumber));
////                } else if (eventName.equals("BuyLpPower")) {
//////                    this.buyLpPower(addr, inviteAddr, amount);
////                    log.info("当前扫描到的区块是" + blockNumber);
////
//////                    eventEntity.setBlockNum(blockNumber.toString());
////                }
////                if (Help.isNotNull(eventEntity)) {
////                    eventService.save(eventEntity);
////                    return R.ok("确认交易成功");
////                }
////            }
////            return R.error("交易hash非法，没有投入成功");
////        } else {
////            return R.error("交易hash错误，获取区块交易失败");
////        }
////    }
//
//    public static String doGet(String httpurl) {
//        HttpURLConnection connection = null;
//        InputStream is = null;
//        BufferedReader br = null;
//        String result = null;
//
//        try {
//            URL url = new URL(httpurl);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(15000);
//            connection.setReadTimeout(60000);
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.addRequestProperty("TRON-PRO-API-KEY", "c15b86be-e273-462e-a2b3-3ff4fe713dc1");
//
//            connection.connect();
//            if (connection.getResponseCode() == 200) {
//                is = connection.getInputStream();
//                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                StringBuffer sbf = new StringBuffer();
//                String temp = null;
//
//                while ((temp = br.readLine()) != null) {
//                    sbf.append(temp);
//                    sbf.append("\r\n");
//                }
//
//                result = sbf.toString();
//            }
//        } catch (MalformedURLException var23) {
//            var23.printStackTrace();
//        } catch (IOException var24) {
//            var24.printStackTrace();
//        } finally {
//            if (null != br) {
//                try {
//                    br.close();
//                } catch (IOException var22) {
//                    var22.printStackTrace();
//                }
//            }
//
//            if (null != is) {
//                try {
//                    is.close();
//                } catch (IOException var21) {
//                    var21.printStackTrace();
//                }
//            }
//
//            connection.disconnect();
//        }
//
//        return result;
//    }
//}
