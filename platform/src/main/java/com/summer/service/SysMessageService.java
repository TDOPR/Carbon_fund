package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.model.SysMessage;

import javax.servlet.http.HttpServletResponse;

public interface SysMessageService extends IService<SysMessage> {
    JsonResult saveMessage(SysMessage sysMessage);

    void exportJson(Integer type, HttpServletResponse httpServletResponse);
}
