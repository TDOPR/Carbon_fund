package com.summer.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/10 12:08
 **/
@Data
public class UserRecordVO {
    
    /**
     * 下级用户地址
     */
    private String subUserAddress;
    
    /**
     * 下级用户等级
     */
    private Integer subUserLevel;
    
    /**
     * 下级用户奖励金额
     */
    private BigDecimal amount;

}
