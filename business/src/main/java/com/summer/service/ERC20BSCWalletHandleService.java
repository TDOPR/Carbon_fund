package com.summer.service;

import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

public interface ERC20BSCWalletHandleService {
    /**
     * 查询订单属于哪个区块
     */
    BigInteger queryBlockByTransactionId(String transactionId);

    /**
     * 查询最新区块高度
     *
     * @return
     */
    BigInteger queryBlockLast();

    String createTransferUsdtEvent();

    /**
     * 获取区块的交易信息
     *
     * @param blockNo
     * @return
     */
    List<EthBlock.TransactionResult> getTransactionsByBlockNo(BigInteger blockNo);

    /**
     * 获取区块的交易信息
     *
     * @param txHash
     * @return
     */
    TransactionReceipt getTransactionsByTxHash(String txHash);

    /**
     * 创建filter对象
     *
     * @return
     */
    EthFilter createFilter(BigInteger startBlockNumber, BigInteger endBlockNumber, String contractAddress);

    /**
     * 创建event对象
     *
     * @return
     */
    String createBuyNodeEvent();


    /**
     * 获取filter日志log
     *
     * @return
     */
    List<Log> getLogByFilter(EthFilter filter);

    /**
     * 根据区块hash获取区块信息
     *
     * @return
     */
    EthBlock.Block getBlockByHash(String blockHash);
}
