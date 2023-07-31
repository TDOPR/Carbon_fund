package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.model.SysMenu;
import com.summer.model.vo.RouterVO;

import java.util.List;

/**
 * @author MCQ
 * @Description
 * @date 2021-04-15 14:53
 */
public interface SysMenuService extends IService<SysMenu> {

    JsonResult findAll();

    JsonResult<RouterVO> findAllByRoleId(Integer roleId);

    JsonResult reloadMenu(List<SysMenu> sysMenus, String token);

    JsonResult deleteByIdList(List<Integer> id);

    JsonResult saveMenu(SysMenu sysMenu);

    JsonResult getTree();

    List<String> findAuthorityByRoleId(Integer roleId);

    void updateRoleMenu();
}
