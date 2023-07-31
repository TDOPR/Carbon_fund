package com.summer.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.mapper.SysLoginLogMapper;
import com.summer.common.model.SysLoginLog;
import com.summer.common.service.SysLoginLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;


@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Resource
    SysLoginLogMapper sysLoginLogMapper;

    @Override
    public Integer getTodayLoginCount() {
        return (int)sysLoginLogMapper.getTodayLoginCount(LocalDate.now().toString());
    }
}
