package com.summer.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.summer.common.base.BaseModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description 用户钱包
 * @CreateTime 2022/11/1 10:57
 **/
@Data
@TableName("wallets")
public class Wallets extends BaseModel {

    /**
     * 所属用户Id
     */
    private Integer userId;
    /**
     * 法币账号
     */
    private String legalCurrencyAccount;
    /**
     * USD账户余额
     */
    private BigDecimal usdWalletAmount;
    /**
     * 积分余额
     */
    private BigDecimal integralWalletAmount;
    /**
     * 提现冻结的USD金额
     */
    private BigDecimal frozenAmount;
    /**
     * 已购买的任务总次数
     */
    private Long totalTaskNum;
    /**
     * 剩余可使用次数
     */
    private Long hasTaskNum;

}
