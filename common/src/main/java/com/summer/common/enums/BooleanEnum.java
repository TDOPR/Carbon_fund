package com.summer.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum  BooleanEnum {

    TRUE(1),
    FALSE(0);

    private Integer intValue;

    public Integer intValue() {
        return intValue;
    }
}
