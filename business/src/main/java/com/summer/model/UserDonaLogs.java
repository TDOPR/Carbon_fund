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
@TableName("user_dona_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDonaLogs extends BaseModel {
    
    
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 等级
     */
    private Integer level;
    
    /**
     * title
     */
    private String title;
    
    
    /**
     * 证书编号
     */
    private String certificate;
    
    
}
