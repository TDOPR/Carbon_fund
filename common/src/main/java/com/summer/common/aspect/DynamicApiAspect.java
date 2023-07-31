package com.summer.common.aspect;

import com.summer.common.config.GlobalProperties;
import com.summer.common.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/4/12 11:20
 **/
@Component
@Aspect
@Slf4j
public class DynamicApiAspect {

    @Pointcut("@annotation(com.summer.common.annotation.DynamicApi)")
    public void redisLockPointcut() {
    }

    @Around("redisLockPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (GlobalProperties.isProdEnv()) {
            return JsonResult.failureResult("正式环境测试接口暂未开放!");
        }
        return proceedingJoinPoint.proceed();
    }

}
