package com.summer.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BindMailDTO extends FindPasswordDTO {
    
    /**
     * 用户地址
     * @required
     */
    @NotEmpty
    private String userAddress;
}
