package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.summer.model.AppUsers;
import com.summer.model.condition.AppUsersCondition;
import com.summer.model.dto.AppUserCountDTO;
import com.summer.model.vo.AppUsersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppUserMapper extends BaseMapper<AppUsers> {

    Integer findInviteIdByUserId(Integer userId);

    IPage<AppUsersVO> page(IPage<AppUsers> page, @Param("param") AppUsersCondition searchParam);

    Integer getValidUserCountByInviteId(Integer userId);
    int insertBatchNoticeByUserId(Integer userId);

    List<AppUserCountDTO> selectUserCountGroupByLevel();

    String getParentEmail(Integer userId);

    List<String> getChildList(Integer userId);

    List<AppUserCountDTO> selectUserCountGroupByLevelParentId(Integer userId);
}
