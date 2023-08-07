package com.summer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.mapper.UserDonaLogsMapper;
import com.summer.mapper.UserTaskLogsMapper;
import com.summer.model.UserDonaLogs;
import com.summer.model.UserTaskLogs;
import com.summer.service.UserDonaLogsService;
import com.summer.service.UserTaskLogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserTaskLogsServiceImpl extends ServiceImpl<UserTaskLogsMapper, UserTaskLogs> implements UserTaskLogsService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserTaskLogs(Integer userId, Integer taskId) {
        
        UserTaskLogs userTaskLogs = UserTaskLogs.builder()
                .userId(userId)
                .taskId(taskId)
                .build();
        this.save(userTaskLogs);
        
    }
}
