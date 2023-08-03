package com.summer.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.summer.common.base.BaseModel;
import com.summer.common.base.BaseModelCID;
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
@Builder
@TableName("patici_task_today")
@NoArgsConstructor
@AllArgsConstructor
public class PaticiTaskToday extends BaseModelCID {



    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 任务id
     */
    private Integer taskId;
    
    

}
