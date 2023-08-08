package com.summer.model.vo;

import lombok.Data;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/10 12:08
 **/
@Data
public class AppTokenLinkVO {
    /**
     * token信息
     */
    private String token;
    
    /**
     * inviteCode信息
     */
    private String inviteCode;
    
    /**
     * 是否绑定邮箱
     */
    private Integer emailStatus;
}
