package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.enums.FlowingActionEnum;
import com.summer.enums.FlowingTypeEnum;
import com.summer.model.Wallets;

import java.math.BigDecimal;

public interface WalletsService extends IService<Wallets> {
    
//    /**
//     * 更新钱包余额
//     * @param blockAddress  区块链地址
//     * @param amount  需要加或减的金额
//     * @param flowingActionEnum 收入或支出
//     * @param flowingTypeEnum 流水类型
//     * @return 执行结果
//     */
//    boolean updateWallet(String blockAddress, BigDecimal amount, FlowingActionEnum flowingActionEnum, FlowingTypeEnum flowingTypeEnum, Integer chainId);
//
//    /**
//     * 通过数据库字段计算的方式修改钱包余额
//     * @param userId 用户Id
//     * @param amount 修改的金额
//     * @param flowingActionEnum 增加或减少
//     * @return
//     */
//    boolean lookUpdateWallets(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum, Integer chainId);

}
