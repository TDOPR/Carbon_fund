package com.summer.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.summer.common.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dominick Li
 * @Description 用户接取的任务
 * @CreateTime 2023/2/24 15:20
 **/
@Data
@TableName("user_task_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTaskLogs extends BaseModel {
    
    
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 任务Id
     */
    private Integer taskId;
    
    /**
     * 任务积分
     */
    private Integer taskIntegral;
    
    /**
     * 今日是否参与任务
     */
    private Integer status;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    
}
