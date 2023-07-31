package com.summer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.enums.ReturnMessageEnum;
import com.summer.common.model.JsonResult;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.util.DateUtil;
import com.summer.common.util.JwtTokenUtil;
import com.summer.mapper.UpdatePwdLogMapper;
import com.summer.model.UpdatePwdLog;
import com.summer.service.UpdatePwdLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/5/16 11:23
 **/
@Service
public class UpdatePwdLogServiceImpl extends ServiceImpl<UpdatePwdLogMapper, UpdatePwdLog> implements UpdatePwdLogService {

    @Override
    public JsonResult checkUpdateTime() {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        UpdatePwdLog updatePwdLog = this.getById(userId);
        if (updatePwdLog != null) {
            boolean errorFlag = DateUtil.betweenDays(updatePwdLog.getLastmodifiedTime(), LocalDateTime.now()) == 0;
            if (errorFlag) {
                return JsonResult.failureResult(ReturnMessageEnum.UPDATE_PWD_TIME_LIMIT);
            }
        }
        return JsonResult.successResult();
    }
}
