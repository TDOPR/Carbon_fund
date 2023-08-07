package com.summer.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.summer.common.base.BaseModel;
import com.summer.common.base.BaseModelCID;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Dominick Li
 * @Description 用户接取的任务
 * @CreateTime 2023/2/24 15:20
 **/
@Data
@TableName("carbon_task")
public class CarbonTask extends BaseModelCID {



    /**
     * 任务积分
     */
    private Integer taskIntegral;

    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 类型
     */
    private Integer type;
    
    /**
     * 状态
     */
    private Integer checkStatus;
    
    /**
     * 活动图像
     */
    private String imgUrl;
    
    /**
     * 状态
     */
    private Integer joinedCount;
    
    /**
     * 活动图像
     */
    private String remark;
    

}
