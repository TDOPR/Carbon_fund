package com.summer.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WalletRecordInfoDTO {
    /**
     * 变动金额
     */
    private BigDecimal amount;
    
    /**
     * 类型
     */
    private String typeZh;
    
    /**
     * 时间
     */
    private LocalDateTime createTime;
    
}
