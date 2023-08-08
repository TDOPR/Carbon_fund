package com.summer.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/18 10:16
 **/
@Data
public class AllIntegralUsersVO {

    /**
     * 用户昵称
     */
    private String nickName;
    
    /**
     * 用户积分
     */
    private BigDecimal userIntegralAmount;
    
    /**
     * 钱包余额
     */
    private BigDecimal walletAmount;
    
    /**
     * 钱包余额
     */
    private BigDecimal donaUsdtAmount;
    
    
    /**
     * 我的等级
     */
    private BigDecimal level;
    
    /**
     * 等级title
     */
    private String title;
    

    /**
     *用户排名
     */
    private Integer rank;
    
}
