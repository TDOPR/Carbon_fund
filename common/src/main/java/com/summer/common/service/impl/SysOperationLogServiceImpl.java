package com.summer.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.mapper.SysOperationLogMapper;
import com.summer.common.model.SysOperationLog;
import com.summer.common.service.SysOperationLogService;
import com.summer.common.util.AopPointUtil;
import com.summer.common.util.IpAddrUtil;
import com.summer.common.util.JwtTokenUtil;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {

    @Override
    public void saveOperationLog(JoinPoint joinPoint, String methodName, String module, String description) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SysOperationLog sysOperationLog = new SysOperationLog();
        sysOperationLog.setIpAddr(IpAddrUtil.getIpAddr(request));
        sysOperationLog.setModule(module);
        sysOperationLog.setDescription(description);
        sysOperationLog.setUsername(JwtTokenUtil.getUserNameFromToken(request.getHeader("token")));
        sysOperationLog.setContent(JSONObject.toJSONString(AopPointUtil.getRequestParamsByJoinPoint(joinPoint)));
        this.save(sysOperationLog);
    }


}
