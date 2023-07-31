package com.summer.service;


import com.summer.common.model.JsonResult;
import com.summer.controller.monitor.server.SystemServer;
import com.summer.model.vo.BusinessVO;

public interface SystemService {

    JsonResult<BusinessVO> getHomeInfo();

    JsonResult<SystemServer> getServerInfo();
}
