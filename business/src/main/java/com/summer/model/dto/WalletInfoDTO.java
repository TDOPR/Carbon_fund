package com.summer.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/16 11:35
 **/
@Data
public class WalletInfoDTO {
    /**
     * 钱包余额
     */
    private BigDecimal rechargeUsdtAmount;
    
    /**
     * 贡献奖
     */
    private BigDecimal algebraReward;

    /**
     * 积分
     */
    private BigDecimal userIntegralAmount;
    
    /**
     * 我的昵称
     */
    private String nickName;

}
