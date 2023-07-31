package com.summer.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/16 11:35
 **/
@Data
public class PaticiTaskDTO {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 任务积分
     */
    private Integer taskIntegral;

    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 是否参与
     */
    private Integer status;
    
    /**
     * 活动图片
     */
    private String imgUrl;

}
