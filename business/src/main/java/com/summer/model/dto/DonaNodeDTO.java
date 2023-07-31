package com.summer.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/13 17:36
 **/
@Data
public class DonaNodeDTO {

    private Integer level;

//    /**
//     * 链ID
//     */
//    private Integer chainId;

    /**
     * 实际购买金额
     */
    private BigDecimal amount;


}
