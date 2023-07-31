package com.summer.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.common.model.PageParam;
import com.summer.model.SysRole;
import com.summer.model.condition.SysRoleCondition;
import com.summer.model.dto.SysRoleDTO;
import com.summer.model.vo.SelectVO;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    JsonResult<List<SelectVO>> findSelectList();

    JsonResult queryByCondition(PageParam<SysRole, SysRoleCondition> pageParam);

    JsonResult saveRole(SysRoleDTO sysRoleDTO);

    JsonResult deleteByIdList(List<Integer> idList);

    JsonResult export(SysRoleCondition sysRoleCondition);
}
