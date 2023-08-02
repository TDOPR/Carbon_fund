package com.summer.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class HomeBindMailDTO  {
    
    
    /**
     * 邮箱账号
     * @required
     */
    @NotEmpty
    @Email
    private String email;
    
    /**
     * 验证码Id
     * @required
     */
    @NotEmpty
    private String uuid;
    
    /**
     * 验证码
     * @required
     */
    @NotEmpty
    private String code;
}
