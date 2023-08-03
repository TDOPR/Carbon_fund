package com.summer.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/18 10:16
 **/
@Data
public class SelectDoTaskVO {
    
    
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 任务Id
     */
    private Integer taskId;
    /**
     * 创建时间
     * @ignore
     */
    private LocalDateTime createTime;

}
