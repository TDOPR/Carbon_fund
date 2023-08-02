package com.summer.service.impl;

import com.summer.service.ERC20BSCWalletHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.*;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ERC20BSCWalletHandleServiceImpl implements ERC20BSCWalletHandleService {
    @Resource
    private JsonRpc2_0Web3j jsonBscRpc;

    /**
     * 查询区块高度
     *
     * @param transactionId
     */
    @Override
    public BigInteger queryBlockByTransactionId(String transactionId) {
        try {
            EthGetTransactionReceipt ethGetTransactionReceipt = jsonBscRpc.ethGetTransactionReceipt(transactionId).send();
            TransactionReceipt transactionReceipt = ethGetTransactionReceipt.getResult();
            if (transactionReceipt == null) {
                return BigInteger.ZERO;
            }
            return new BigInteger(transactionReceipt.getBlockNumber().toString());
        } catch (Exception e) {
            log.error(log.getName() + ".queryBlockByTransactionId.error.transactionId=", transactionId, e);
            return BigInteger.ZERO;
        }
    }

    /**
     * 查询最新区块高度
     *
     * @return
     */
    @Override
    public BigInteger queryBlockLast() {

        try {
            EthBlockNumber ethBlockNumber = jsonBscRpc.ethBlockNumber().send();
            return ethBlockNumber.getBlockNumber();
        } catch (IOException e) {
            log.error(log.getName() + ".queryBlockLast.error", e);
            return BigInteger.ZERO;
        }
    }

    /**
     * 获取区块的信息
     *
     * @param blockNo
     * @return
     */
    @Override
    public List<EthBlock.TransactionResult> getTransactionsByBlockNo(BigInteger blockNo) {
        Request request = jsonBscRpc.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockNo), true);
        Response response = null;
        try {
            response = request.send();
        } catch (IOException e) {
            log.error(log.getName() + ".getTransactionsByBlockNo.error.blockNo=", blockNo, e);
            throw new RuntimeException(e.getMessage());
        }
        EthBlock.Block block = (EthBlock.Block) response.getResult();
        return block.getTransactions();
    }

    /**
     * 获取区块logs的信息
     *
     * @param txHash
     * @return
     */
    @Override
    public TransactionReceipt getTransactionsByTxHash(String txHash) {
        Request request = jsonBscRpc.ethGetTransactionReceipt(txHash);
        Response response = null;
        try {
            response = request.send();
        } catch (IOException e) {
            log.error(log.getName() + ".getTransactionsByTxHash.error.txHash=", txHash, e);
            throw new RuntimeException(e.getMessage());
        }
        TransactionReceipt transactionReceipt = (TransactionReceipt) response.getResult();
        return transactionReceipt;
    }

    /**
     * 创建filter对象
     *
     * @return
     */
    @Override
    public EthFilter createFilter(BigInteger startBlockNumber, BigInteger endBlockNumber, String contractAddress) {
        EthFilter filter = new EthFilter(
                DefaultBlockParameter.valueOf(startBlockNumber),
                DefaultBlockParameter.valueOf(endBlockNumber),
                contractAddress);
        return filter;
    }

//    /**
//     * 创建Withdraw的event对象
//     *
//     * @return
//     */
//    @Override
//    public String createWithdrawEvent() {
//        Event event = new Event("Withdraw",
//                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {},
//                        (new TypeReference<Uint256>() {}),
//                        (new TypeReference<Uint256>() {})));
//        return EventEncoder.encode(event);
//    }

    /**
     * 创建BuyNode的event对象
     *
     * @return
     */
    @Override
    public String createBuyNodeEvent() {
        Event event = new Event("BuyNode",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {},
                        new TypeReference<Uint256>() {},
                        new TypeReference<Uint256>() {}));
        return EventEncoder.encode(event);
    }

    /**
     * 创建TransferUsdt的event对象
     *
     * @return
     */
    @Override
    public String createTransferUsdtEvent() {
        Event event = new Event("TransferUsdt",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {},
                        new TypeReference<Uint256>() {}));
        return EventEncoder.encode(event);
    }

    /**
     * 获取filter日志log
     *
     * @return
     */
    @Override
    public List<Log> getLogByFilter(EthFilter filter) {
        Request request = jsonBscRpc.ethGetLogs(filter);
        Response response = null;
        try {
            response = request.send();
        } catch (IOException e) {
            log.error(log.getName() + ".getLogByFilter.error.txHash=", filter, e);
            throw new RuntimeException(e.getMessage());
        }
        List<Log> ethLog = (List) response.getResult();
        return ethLog;
    }

    /**
     * 根据区块hash获取区块信息
     *
     * @return
     */
    @Override
    public EthBlock.Block getBlockByHash(String blockHash) {
        Request request = jsonBscRpc.ethGetBlockByHash(blockHash,true);
        Response response = null;
        try {
            response = request.send();
        } catch (IOException e) {
            log.error(log.getName() + ".getBlockByHash.error.blockHash=", blockHash, e);
            throw new RuntimeException(e.getMessage());
        }
        EthBlock.Block ethBlock = (EthBlock.Block) response.getResult();
        return ethBlock;
    }
}
