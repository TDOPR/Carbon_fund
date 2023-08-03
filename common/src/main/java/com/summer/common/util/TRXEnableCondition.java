package com.summer.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Slf4j
public class TRXEnableCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        boolean bool = "true".equals(conditionContext.getEnvironment().getProperty("trx.enabled"));
        if (!bool) {
            log.error(log.getName() + "matches bool:{}, 系统定时任务未开启!", bool);
        }
        return bool;
    }
}
