package com.summer.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedalDTO {
    
    /**
     * 购买的等级
     */
    private Long id;
    
    /**
     * title
     */
    private String title;
    
    /**
     * 任务名称
     */
    private String imgUrl;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 证书
     */
    private String certificate;
    
    /**
     * 状态
     */
    private Integer status;
}
