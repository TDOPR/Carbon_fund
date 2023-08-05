package com.summer.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/16 11:35
 **/
@Data
@Builder
@AllArgsConstructor
public class CarbonFootprintRemarkVO {

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
    private String remark;
    
    /**
     * 参与状态
     *
     */
    private Integer joinedCount;

}
