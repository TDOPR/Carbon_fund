package com.summer.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/18 10:16
 **/
@Data
public class AllIntegralUsersDTO {

    /**
     * 用户昵称
     */
    private String nickName;
    
    /**
     * 用户积分
     */
    private Integer userIntegralAmount;
    
    /**
     * 用户捐赠USDT
     */
    private BigDecimal donaUsdtAmount;
    
    /**
     * 用户钱包余额
     */
    private BigDecimal rechargeUsdtAmount;
    
    /**
     * 我的等级
     */
    private BigDecimal level;

    /**
     *用户排名
     */
    private Integer rank;
    
    

}
