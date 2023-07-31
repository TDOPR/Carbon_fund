package com.summer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 积分类型
 */
@Getter
@AllArgsConstructor
public enum IntegralEnum {

    ZERO(0, new Integer("100"), "注册奖励100积分"),
    FIRST(1, new Integer("30"), "绿色环保意识提高"),
    SECOND(2, new Integer("20"), "回收电子垃圾"),
    THIRD(3, new Integer("10"), "节约用电能源"),
    FORTH(4, new Integer("10"), "自带环保水瓶"),
    FIVTH(5, new Integer("10"), "节约日常用水量"),
    SIXTH(6, new Integer("20"), "减少食物浪费"),
    SEVENTH(7, new Integer("20"), "减少汽油车交通"),
    EIGHTH(8, new Integer("10"), "保护森林资源"),
    NINETH(9, new Integer("30"), "推广可再生能源"),
    TENTH(10, new Integer("30"), "购买节点获得积分");
    

    /**
     * 类型
     */
    private Integer type;

    /**
     * 积分
     */
    private Integer rewardIntegral;
    
    /**
     * 描述
     */
    private String description;

    public static Integer getRewardIntegralByType(int type) {
        for (IntegralEnum integralEnum : values()) {
            if (type == integralEnum.getType()) {
                return integralEnum.getRewardIntegral();
            }
        }
        return Integer.valueOf(0);
    }
    
    public static String getDescriptionByType(int type) {
        for (IntegralEnum integralEnum : values()) {
            if (type == integralEnum.getType()) {
                return integralEnum.getDescription();
            }
        }
        return "";
    }
    
    public static IntegralEnum getByType(int type) {
        for (IntegralEnum integralEnum : values()) {
            if (integralEnum.getType().equals(type)) {
                return integralEnum;
            }
        }
        return null;
    }
}
