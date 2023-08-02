package com.summer.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/18 10:16
 **/
@Data
@AllArgsConstructor
public class MyDirectPushVO {

    /**
     * 用户昵称
     */
    private String userAddress;
    
    /**
     * 用户等级
     */
    private Integer userLevel;
    


}
