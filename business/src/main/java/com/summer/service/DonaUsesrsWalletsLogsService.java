package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.enums.CoinUnitEnum;
import com.summer.enums.FlowingActionEnum;
import com.summer.enums.IntegralEnum;
import com.summer.enums.UsdLogTypeEnum;
import com.summer.model.DonaUsersWalletsLogs;

import java.math.BigDecimal;

public interface DonaUsesrsWalletsLogsService extends IService<DonaUsersWalletsLogs> {

//    /**
//     * 插入流水记录
//     * @param userId  用户Id
//     * @param amount  变更的金额
//     * @param flowingActionEnum 收入或支出
//     * @param usdLogTypeEnum 流水类型
//     * @return 执行结果
//     */
//    Long insertWalletLogs(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum, UsdLogTypeEnum usdLogTypeEnum);

    /**
     * 插入流水记录
     * @param userId  用户Id
     * @param amount  变更的金额
     * @param flowingActionEnum 收入或支出
     * @param usdLogTypeEnum 流水类型
     * @return 执行结果
     */
    Long insertDonaUsersWalletsLogs(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum, UsdLogTypeEnum usdLogTypeEnum);

//    JsonResult<WalletsUsdLogDetailVO> getMybillDetails(PageParam<WalletUsdLogs, BillDetailsCondition> pageParam);
    Long insertDonaUsersIntegralWalletsLogs(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum, IntegralEnum integralEnum);
}
