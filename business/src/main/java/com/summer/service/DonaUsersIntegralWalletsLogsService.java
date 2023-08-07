package com.summer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.common.model.vo.PageVO;
import com.summer.enums.FlowingActionEnum;
import com.summer.enums.IntegralEnum;
import com.summer.model.DonaUsersIntegralLogs;
import com.summer.model.WalletTttLogs;
import com.summer.model.dto.ParticiTaskDTO;
import com.summer.model.dto.PaticiTaskDTO;

import java.math.BigDecimal;
import java.util.List;

public interface DonaUsersIntegralWalletsLogsService extends IService<DonaUsersIntegralLogs> {
    
    JsonResult<PageVO<PaticiTaskDTO>> carbonFootprint(Page page);
    Long insertDonaUsersIntegralWalletsLogs(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum, IntegralEnum integralEnum);
//    JsonResult doTask(ParticiTaskDTO particiTaskDTO);
}
