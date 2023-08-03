package com.summer.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/16 11:35
 **/
@Data
public class HomeUserInfoDTO {
    /**
     * tzh钱包余额
     */
    private BigDecimal walletAmount;
    

    /**
     * 积分
     */
    private BigDecimal userIntegralAmount;
    
    /**
     * 图片地址
     */
    private String imgUrl;
    
    /**
     * 等级title
     */
    private String title;
    
    /**
     * 用户等级
     */
    private Integer level;
    
    /**
     * 用户邀请码
     */
    private String inviteCode;
    
    /**
     * 用户邀请码
     */
    private Boolean isEmailRegister;

}
