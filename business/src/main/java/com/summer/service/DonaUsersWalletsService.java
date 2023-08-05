package com.summer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.enums.CoinUnitEnum;
import com.summer.enums.FlowingActionEnum;
import com.summer.enums.IntegralEnum;
import com.summer.enums.UsdLogTypeEnum;
import com.summer.model.DonaUsersWallets;
import com.summer.model.WalletTttLogs;
import com.summer.model.dto.DonaNodeDTO;

import java.math.BigDecimal;
import java.util.List;

public interface DonaUsersWalletsService extends IService<DonaUsersWallets> {
    
    /**
     * 购买VIP套餐
     *
     * @return
     */
    JsonResult donaNode(DonaNodeDTO donaNodeDTO);
    
    /**
     * 查询钱包对象
     *
     * @param userId  根据用户Id查询
     * @param columns 需要查询的字段
     * @return
     */
    DonaUsersWallets selectColumnsByUserId(Integer userId, SFunction<DonaUsersWallets, ?>... columns);
    
    /**
     * 更新钱包Usd余额
     *
     * @param amount            需要加或减的金额
     * @param userId            用户Id
     * @param flowingActionEnum 收入或支出
     * @param usdLogTypeEnum    流水类型
     * @return 执行结果
     */
    boolean updateUsdWallet(BigDecimal amount, Integer userId, FlowingActionEnum flowingActionEnum, UsdLogTypeEnum usdLogTypeEnum);
    
    /**
     * 通过数据库字段计算的方式修改钱包余额
     *
     * @param userId            用户Id
     * @param amount            修改的金额
     * @param flowingActionEnum 增加或减少
     * @return
     */
    boolean lookUsdUpdateWallets(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum);
    
    
    
    /**
     * 发放代数将
     *
     * @param userId            用户Id
     * @return
     */
    JsonResult sendAlgebraReward(Integer userId, BigDecimal donaAmount, Integer level);
    
    JsonResult superReward();
    
    boolean lookIntegralUpdateWallets(BigDecimal amount, Integer userId, FlowingActionEnum flowingActionEnum);
    
    boolean updateIntegralWallet(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum, IntegralEnum integralEnum);
    
    int sendAlgebraRewardUpdateUsdWallet(BigDecimal amount, Integer userId, FlowingActionEnum flowingActionEnum, UsdLogTypeEnum usdLogTypeEnum);
    
    void clearTodayTask();
    
    boolean transferUpdateUsdWallet(BigDecimal amount, Integer userId, FlowingActionEnum flowingActionEnum, UsdLogTypeEnum usdLogTypeEnum);
    
    boolean transferUsdUpdateWallets(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum);
}
