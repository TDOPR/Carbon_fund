package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.model.UpdatePwdLog;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/5/16 11:23
 **/
public interface UpdatePwdLogService extends IService<UpdatePwdLog>  {

    JsonResult checkUpdateTime();

}
