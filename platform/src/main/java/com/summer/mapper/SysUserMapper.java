package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.summer.model.SysUser;
import com.summer.model.condition.SysUserCondition;
import com.summer.model.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {

    List<String> findAllByUsernameByRoleId(@Param("roleId") Integer roleId);

    IPage<UserVO> selectPageVo(IPage<SysUser> page, @Param("user") SysUserCondition sysUserCondition);

    List<Integer> findExistsByRoleId(@Param("idList") List<Integer> idList);
}
