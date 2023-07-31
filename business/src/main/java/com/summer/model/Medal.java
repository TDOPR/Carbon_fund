package com.summer.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.summer.common.base.BaseModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description 用户接取的任务
 * @CreateTime 2023/2/24 15:20
 **/
@Data
@TableName("medal")
public class Medal extends BaseModel {





    /**
     * title
     */
    private String title;

    /**
     * 任务名称
     */
    private String imgUrl;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 证书
     */
    private String certificate;
    
    /**
     * 状态
     */
    private Integer status;
    

}
