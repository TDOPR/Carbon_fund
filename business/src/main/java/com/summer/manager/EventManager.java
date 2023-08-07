package com.summer.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.summer.common.config.ChainIdsConfig;
import com.summer.common.config.TokenConfig;
import com.summer.enums.*;
import com.summer.mapper.AppDonaUsersMapper;
import com.summer.mapper.AppUserMapper;
import com.summer.mapper.CoinConfigMapper;
import com.summer.mapper.EvmUserWalletMapper;
import com.summer.model.CoinConfig;
import com.summer.model.EvmEvent;
import com.summer.model.EvmUserWallet;
import com.summer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class EventManager {
    @Resource
    private CoinConfigMapper coinConfigMapper;

    @Resource
    private ERC20WalletHandleService erc20WalletHandleService;

    @Resource
    private ERC20BSCWalletHandleService erc20BSCWalletHandleService;
    @Resource
    private CoinConfigService coinConfigService;

    @Resource
    private EvmEventService evmEventService;

    @Autowired
    private WalletsService walletsService;

    @Resource
    private TradeManager tradeManager;

    @Autowired
    private EvmUserWalletService evmUserWalletService;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private EvmUserWalletMapper evmUserWalletMapper;

    @Autowired
    private TokenConfig tokenConfig;

    @Autowired
    private ChainIdsConfig chainIdsConfig;
    
    @Autowired
    private DonaUsersWalletsService donaUsersWalletsService;
    
    @Resource
    private AppDonaUsersMapper appDonaUsersMapper;

    public void analyzeETHEvent() throws Exception {
        CoinConfig scanDataConfig = coinConfigMapper.getScanDataConfig(Integer.valueOf(chainIdsConfig.getEth()));
        if (scanDataConfig == null) {
            return;
        }
        BigInteger currentBlock = erc20WalletHandleService.queryBlockLast();
        BigInteger oldBlock = scanDataConfig.getBlockNo();
        oldBlock = oldBlock.add(BigInteger.ONE);
        if (currentBlock.subtract(oldBlock).compareTo(new BigInteger("5000")) > -1) {
            currentBlock = oldBlock.add(new BigInteger("4999"));
        }
        log.info(log.getName() + ".scanDataJob currentBlock:{}, oldBlock:{}", currentBlock, oldBlock);
        executeETHOneBlock(oldBlock, currentBlock);
        coinConfigMapper.updateActionSeqById(scanDataConfig.getId(), currentBlock);
    }

    public void analyzeBSCEvent() throws Exception {
        CoinConfig scanDataConfig = coinConfigMapper.getScanDataConfig(Integer.valueOf(chainIdsConfig.getEth()));
        if (scanDataConfig == null) {
            return;
        }
        BigInteger currentBlock = erc20BSCWalletHandleService.queryBlockLast();
        BigInteger oldBlock = scanDataConfig.getBlockNo();
        oldBlock = oldBlock.add(BigInteger.ONE);
        if (currentBlock.subtract(oldBlock).compareTo(new BigInteger("5000")) > -1) {
            currentBlock = oldBlock.add(new BigInteger("4999"));
        }
        log.info(log.getName() + ".scanDataJob currentBlock:{}, oldBlock:{}", currentBlock, oldBlock);
        executeBscOneBlock(oldBlock, currentBlock);
        coinConfigMapper.updateActionSeqById(scanDataConfig.getId(), currentBlock);
    }

    private void executeETHOneBlock(BigInteger oldBlock, BigInteger toBlock) throws Exception {

        CoinConfig eventConfig = coinConfigService.getOne(
                new LambdaQueryWrapper<CoinConfig>()
                        .select(CoinConfig::getContract)
                        .eq(CoinConfig::getCoinType, "EVENT")
                        .eq(CoinConfig::getChainId, chainIdsConfig.getEth())
        );

        String eventContract = eventConfig.getContract();
    
        String transferUsdtCreated = erc20WalletHandleService.createTransferUsdtEvent();

        EthFilter ethFilter = erc20WalletHandleService.createFilter(oldBlock, toBlock, eventContract);
        ethFilter.addOptionalTopics(transferUsdtCreated);
        List<Log> logsList = erc20WalletHandleService.getLogByFilter(ethFilter);
        if (CollectionUtil.isEmpty(logsList)) {
            return;
        }
        List<EvmEvent> evmEventEntityArrayList = new ArrayList<>();
        for (Log logsLog : logsList) {
            String txHash = logsLog.getTransactionHash();
            if (evmEventService.getEventExistByTxHash(txHash)) {
                continue;
            }
            ArrayList topics = (ArrayList) logsLog.getTopics();
            String eventName = topics.get(0).toString();

            if (eventName.equalsIgnoreCase(transferUsdtCreated)) {
                String userAddress = "0x" + topics.get(1).toString().substring(2).replace("000000000000000000000000", "");
                BigDecimal rechargeAmount = new BigDecimal(new BigInteger(topics.get(2).toString().substring(2), 16)).divide(new BigDecimal(10).pow(6), 2, RoundingMode.FLOOR);
                tradeManager.evmRecharge(userAddress, rechargeAmount, Integer.valueOf(chainIdsConfig.getEth()));
                donaUsersWalletsService.transferUpdateUsdWallet(rechargeAmount, appDonaUsersMapper.selectDonaUserIdByUserAddress(userAddress), FlowingActionEnum.INCOME, UsdLogTypeEnum.RECHARGE);

                String blockHash = logsLog.getBlockHash();
                EthBlock.Block ethBlock = erc20WalletHandleService.getBlockByHash(blockHash);
                Date date = new Date(ethBlock.getTimestamp().intValue() * 1000L);

                EvmEvent ethScanDataEntity = EvmEvent.builder()
                        .txHash(txHash)
                        .blockNum(new BigInteger(logsLog.getBlockNumber().toString(10)))
                        .createTime(date)
                        .chainId(Integer.valueOf(chainIdsConfig.getEth()))
                        .build();
                evmEventEntityArrayList.add(ethScanDataEntity);
            }
        }
        if (CollectionUtil.isNotEmpty(evmEventEntityArrayList)) {
            evmEventService.saveBatch(evmEventEntityArrayList);
        }
    }

    private void executeBscOneBlock(BigInteger oldBlock, BigInteger toBlock) throws Exception {

        CoinConfig eventConfig = coinConfigService.getOne(
                new LambdaQueryWrapper<CoinConfig>()
                        .select(CoinConfig::getContract)
                        .eq(CoinConfig::getCoinType, "EVENT")
                        .eq(CoinConfig::getChainId, chainIdsConfig.getBsc())
        );

        String eventContract = eventConfig.getContract();
    
        String transferUsdtCreated = erc20WalletHandleService.createTransferUsdtEvent();

        EthFilter ethFilter = erc20BSCWalletHandleService.createFilter(oldBlock, toBlock, eventContract);
        ethFilter.addOptionalTopics(transferUsdtCreated);
        List<Log> logsList = erc20BSCWalletHandleService.getLogByFilter(ethFilter);
        if (CollectionUtil.isEmpty(logsList)) {
            return;
        }
        List<EvmEvent> evmEventEntityArrayList = new ArrayList<>();
        for (Log logsLog : logsList) {
            String txHash = logsLog.getTransactionHash();
            if (evmEventService.getEventExistByTxHash(txHash)) {
                continue;
            }
            ArrayList topics = (ArrayList) logsLog.getTopics();
            String eventName = topics.get(0).toString();

            if (eventName.equalsIgnoreCase(transferUsdtCreated)) {
                String userAddress = "0x" + topics.get(1).toString().substring(2).replace("000000000000000000000000", "");
                BigDecimal rechargeAmount = new BigDecimal(new BigInteger(topics.get(2).toString().substring(2), 16)).divide(new BigDecimal(10).pow(18), 2, RoundingMode.FLOOR);
                tradeManager.evmRecharge(userAddress, rechargeAmount, Integer.valueOf(chainIdsConfig.getBsc()));
                donaUsersWalletsService.transferUpdateUsdWallet(rechargeAmount, appDonaUsersMapper.selectDonaUserIdByUserAddress(userAddress), FlowingActionEnum.INCOME, UsdLogTypeEnum.RECHARGE);
                String blockHash = logsLog.getBlockHash();
                EthBlock.Block ethBlock = erc20BSCWalletHandleService.getBlockByHash(blockHash);
                Date date = new Date(ethBlock.getTimestamp().intValue() * 1000L);

                EvmEvent ethScanDataEntity = EvmEvent.builder()
                        .txHash(txHash)
                        .blockNum(new BigInteger(logsLog.getBlockNumber().toString(10)))
                        .createTime(date)
                        .chainId(Integer.valueOf(chainIdsConfig.getBsc()))
                        .build();
                evmEventEntityArrayList.add(ethScanDataEntity);
            }
        }
        if (CollectionUtil.isNotEmpty(evmEventEntityArrayList)) {
            evmEventService.saveBatch(evmEventEntityArrayList);
        }
    }
}
