package com.summer.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.common.model.SysLoginLog;

public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    long getTodayLoginCount(String now);

}
