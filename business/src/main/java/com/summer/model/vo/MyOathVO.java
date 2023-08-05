package com.summer.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/16 11:35
 **/
@Data
@Builder
@AllArgsConstructor
public class MyOathVO {

    /**
     * 活动名称
     *
     */
    private String taskName;

    /**
     * 活动积分
     *
     */
    private Integer taskIntegral;
    
    /**
     * 活动图
     *
     */
    private Integer imgUrl;
    
    /**
     * 参与状态
     *
     */
    private Integer status;

}
