package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.enums.FlowingActionEnum;
import com.summer.enums.FlowingTypeEnum;
import com.summer.model.WalletLogs;

import java.math.BigDecimal;

public interface WalletLogsService extends IService<WalletLogs> {
    /**
     * 插入流水记录
     * @param userId  用户Id
     * @param amount  变更的金额
     * @param flowingActionEnum 收入或支出
     * @param flowingTypeEnum 流水类型
     * @return 执行结果
     */
    boolean insertWalletLogs(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum, FlowingTypeEnum flowingTypeEnum, Integer chainId);
    boolean insertWalletAlegraLogs(Integer userId, Integer subId, String subUserAddress, Integer subUserLevel, BigDecimal amount, FlowingActionEnum flowingActionEnum, FlowingTypeEnum flowingTypeEnum, Integer chainId);
//    /**
//     * 根据流水类型查询流水明细
//     * @param userId 用户Id
//     * @param type 流水类型
//     * @return 明细记录
//     */
//    JsonResult<List<WalletLogVO>> listByUserIdAndType(Integer userId, Integer type);
//
//    /**
//     * 获取代理奖
//     * @param userId
//     * @return
//     */
//    MyWalletsVO.Proxy getMyProxyWalletLogs(Integer userId);
//
//    /**
//     * 获取我的钱包账单明细
//     * @return
//     */
//    JsonResult<WalletLogsDetailVO> getMybillDetails(BillDetailsDTO billDetailsDTO);
//
//    /**
//     * 获取量化奖励明细
//     * @return
//     */
//    JsonResult<ProfitLogsDetailVO> quantificationDetail(TypeDTO typeDTO);
//
//    /**
//     * 获取动态奖励明细
//     * @return
//     */
//    JsonResult<ProxyWalletLogsDetailVO> proxyDetail(TypeDTO typeDTO);
//
//    /**
//     * 根据流水类型统计总金额
//     */
//    BigDecimal sumTotalAmountByType(Integer type);
//
//    /**
//     * 根据流水类型列表统计总金额
//     */
//    BigDecimal sumTotalAmountByTypeIn(List<Integer> type);
//
//    /**
//     * 获取动态收益和静态收益总金额和提现金额
//     * @return
//     */
//    BigDecimal sumProfitTotalAmount();
//
//    BigDecimal getDynamicAmountByUserId(Integer userId);
}
