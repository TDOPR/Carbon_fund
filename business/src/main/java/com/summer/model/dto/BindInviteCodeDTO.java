package com.summer.model.dto;

import lombok.Data;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/18 10:16
 **/
@Data
public class BindInviteCodeDTO {

    /**
     * 邀请码
     */
    private String inviteCode;
    
    /**
     * 用户地址
     */
    private String userAddress;

}
