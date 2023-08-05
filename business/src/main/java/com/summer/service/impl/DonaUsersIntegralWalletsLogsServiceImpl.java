package com.summer.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.model.JsonResult;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.model.vo.PageVO;
import com.summer.common.util.JwtTokenUtil;
import com.summer.enums.FlowingActionEnum;
import com.summer.enums.IntegralEnum;
import com.summer.mapper.DonaUsersIntegralWalletsLogsMapper;
import com.summer.model.DonaUsersIntegralLogs;
import com.summer.model.dto.ParticiTaskDTO;
import com.summer.model.dto.PaticiTaskDTO;
import com.summer.service.DonaUsersIntegralWalletsLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/1 18:54
 **/
@Slf4j
@Service
public class DonaUsersIntegralWalletsLogsServiceImpl extends ServiceImpl<DonaUsersIntegralWalletsLogsMapper, DonaUsersIntegralLogs> implements DonaUsersIntegralWalletsLogsService {
    
    @Resource
    private DonaUsersIntegralWalletsLogsMapper donaUsersIntegralWalletsLogsMapper;
    
    
    @Override
    public JsonResult<PageVO<PaticiTaskDTO>> carbonFootprint(Page page){
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        List<PaticiTaskDTO> paticiTask = donaUsersIntegralWalletsLogsMapper.particiTask(userId);
        Page<PaticiTaskDTO> paticiTaskDTOPage = donaUsersIntegralWalletsLogsMapper.task(page);
        for(PaticiTaskDTO taskId : paticiTaskDTOPage.getRecords()){
            if(paticiTask.contains(taskId)){
                taskId.setStatus(1);
//                json.put(taskId.getId().toString(), taskId.getTaskName() + "," + taskId.getTaskIntegral() + ", 1");
            } else {
                taskId.setStatus(0);
            }
        }
        return JsonResult.successResult(new PageVO<>(paticiTaskDTOPage));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertDonaUsersIntegralWalletsLogs(Integer userId, BigDecimal amount, Integer level, FlowingActionEnum flowingActionEnum, IntegralEnum integralEnum) {
        //添加积分钱包流水记录
        DonaUsersIntegralLogs donaUsersIntegralLogs = DonaUsersIntegralLogs.builder()
                .userId(userId)
                .integralAmount(amount)
                .action(flowingActionEnum.getValue())
                .buyLevel(level)
                .type(integralEnum.getType())
                .build();
        this.baseMapper.insert(donaUsersIntegralLogs);
        return Long.valueOf(donaUsersIntegralLogs.getId());
    }
    
//    @Override
//    @Transactional
//    public JsonResult doTask(Integer ){
////        Integer taskId = particiTaskDTO.getTaskId();
//
//    }
    
    
}
