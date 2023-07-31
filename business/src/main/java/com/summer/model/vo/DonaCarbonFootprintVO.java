package com.summer.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author Dominick Li
 * @CreateTime 2023/3/26 14:48
 **/
@Data
@Builder
@AllArgsConstructor
public class DonaCarbonFootprintVO {

    /**
     * 参与的活动名称
     */
    private String donaProjectName;
    /**
     * 是否参与
     */
    private Integer type;
    /**
     * 积分
     */
    private Integer integral;

}
