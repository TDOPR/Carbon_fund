package com.summer.model.dto;

import lombok.Data;

@Data
public class SendCaptchaDTO {
    /**
     * 用户邮箱
     */
    private String email;
    
    /**
     * 邮件类型
     */
    private Integer type;
}
