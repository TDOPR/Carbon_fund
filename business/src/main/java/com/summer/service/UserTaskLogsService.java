package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.model.UserDonaLogs;
import com.summer.model.UserTaskLogs;

public interface UserTaskLogsService extends IService<UserTaskLogs> {
    
    void updateUserTaskLogs(Integer userId, Integer taskId);
}
