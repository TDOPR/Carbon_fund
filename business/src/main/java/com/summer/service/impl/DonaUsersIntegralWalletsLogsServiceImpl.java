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
    public Long insertDonaUsersIntegralWalletsLogs(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum, IntegralEnum integralEnum) {
        //添加积分钱包流水记录
        DonaUsersIntegralLogs donaUsersIntegralLogs = DonaUsersIntegralLogs.builder()
                .userId(userId)
                .integralAmount(amount)
                .action(flowingActionEnum.getValue())
                .type(integralEnum.getType())
                .build();
        this.baseMapper.insert(donaUsersIntegralLogs);
        return donaUsersIntegralLogs.getId();
    }
    
//    public List<PaticiTaskDTO> getDataList(int pageNum, int pageSize) {
//        // 模拟从数据库中查询分页数据
//        List<PaticiTaskDTO> list = new ArrayList<>();
//        for (int i = 0; i < pageSize; i++) {
//            list.add(new PaticiTaskDTO((pageNum - 1) * pageSize + i)); // 生成模拟数据
//        }
//        return list;
//    }
}
