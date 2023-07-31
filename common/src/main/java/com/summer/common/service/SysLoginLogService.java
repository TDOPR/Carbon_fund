package com.summer.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.SysLoginLog;

public interface SysLoginLogService extends IService<SysLoginLog> {

    Integer getTodayLoginCount();

}
