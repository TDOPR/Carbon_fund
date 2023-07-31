package com.summer.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/16 11:35
 **/
@Data
public class AppDonaUserTaskDTO {
    /**
     * 任务Id
     */
    private Integer id;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 积分
     */
    private BigDecimal integralAmount;

}
