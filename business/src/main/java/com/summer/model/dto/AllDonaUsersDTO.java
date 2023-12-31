package com.summer.model.dto;

import lombok.Data;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/18 10:16
 **/
@Data
public class AllDonaUsersDTO {

    /**
     * 用户昵称
     */
    private String nickName;
    
    /**
     * 用户等级
     */
    private String level;
    
    /**
     * 用户等级描述
     */
    private String title;
    
    /**
     *
     */
    private String donaUsdtAmount;
    
    

}
