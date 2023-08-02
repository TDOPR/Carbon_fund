package com.summer.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 代数奖励比例
 */
@Getter
@AllArgsConstructor
public enum LevelEnum {
    FIRST(1, new BigDecimal("50")),
    SECOND(2, new BigDecimal("1000")),
    THIRD(3, new BigDecimal("5000")),
    FORTH(4, new BigDecimal("20000"));
    
    /**
     * 用户等级
     */
    private Integer level;
    
    /**
     * 等级对应金额
     */
    private BigDecimal rechargeAmount;
    
    public static BigDecimal getRechargeAmountByLevel(int level) {
        for (LevelEnum algebraEnum : values()) {
            if (level == algebraEnum.getLevel()) {
                return algebraEnum.getRechargeAmount();
            }
        }
        return BigDecimal.ZERO;
    }
}
