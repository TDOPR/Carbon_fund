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
public class MyMemberInfoVO {
    /**
     * 零碳志愿者数量
     */
    private Integer myLevel;
    
    
    /**
     * 零碳使者数量
     */
    private Integer sum;

}
