package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.model.AppDonaUserTask;
import com.summer.model.AppUsers;
import com.summer.model.dto.AppDonaUserTaskDTO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppDonaUserTaskMapper extends BaseMapper<AppDonaUserTask> {
    List<AppDonaUserTaskDTO> getAutoCheckList(@Param("status") Integer status, @Param("localDateTime") LocalDateTime localDateTime);
}
