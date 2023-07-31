package com.summer.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/14 18:44
 **/
@Data
public class SuperUsersDTO {

    /**
     * 托管金额
     */
    private BigDecimal rechargeAmount;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 级别
     */
    private Integer level;
}
