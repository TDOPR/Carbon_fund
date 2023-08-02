package com.summer.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.summer.common.base.BaseModelCID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/12/6 16:46
 **/
@Data
@Builder
@TableName("evm_user_wallet")
@NoArgsConstructor
@AllArgsConstructor
public class EvmUserWallet extends BaseModelCID {
    
    /**
     * 用户id
     */
    @TableField(value = "uid")
    private Integer uId;
    
    /**
     * 用户等级
     */
    @TableField(value = "userLevel")
    private Integer userLevel;
    
    /**
     * 币种Id
     */
    @TableField(value = "chainId")
    private Integer chainId;
    
    
    /**
     * 网络名称
     */
    @TableField(value = "coinType")
    private String coinType;
    
    /**
     * 钱包地址
     */
    @TableField(value = "userAddress")
    private String userAddress;
    
    /**
     * 充值金额
     */
    @TableField(value = "rechargeAmount")
    private BigDecimal rechargeAmount;
    
    /**
     * 钱包地址
     */
    @TableField(value = "lowerAddress")
    private String lowerAddress;
    
    /**
     * keystore
     */
    private String keystore;
    
    /**
     * 秘钥
     */
    private String password;
    
    /**
     * 是否可用：E可用，D不可用
     */
    private String valid;
    
}
