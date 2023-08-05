package com.summer.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 代数奖励比例
 * @author Administrator
 */
@Getter
@AllArgsConstructor
public enum MedalEnum {
    ZERO(0, "零碳志愿者", "注册即可获得", new BigDecimal("0"), new BigDecimal(0)),
    FIRST(1, "零碳使者", "捐赠9.9￥成为阅读使者", new BigDecimal("9.9"), new BigDecimal(9900)),
    SECOND(2, "零碳大使", "捐赠50￥成为年度使者", new BigDecimal("50"), new BigDecimal(50000)),
    THIRD(3, "零碳特使", "捐赠150￥成为终生使者", new BigDecimal("150"), new BigDecimal(150000));
    
    /**
     * 代数
     */
    private Integer level;
    
    /**
     * title
     */
    private String  title;
    
    /**
     * 描述
     */
    private String  description;
    
    
    
    /**
     * 捐赠额度
     */
    private BigDecimal donaAmount;
    
    /**
     * 对应积分
     */
    private BigDecimal IntegralAmount;
    
    public static MedalEnum getByLevel(int level) {
        for (MedalEnum medalEnum : values()) {
            if (medalEnum.getLevel().equals(level)) {
                return medalEnum;
            }
        }
        return null;
    }
}
