package com.summer.controller.system;


import com.summer.common.annotation.IgnoreWebSecurity;
import com.summer.common.annotation.OperationLog;
import com.summer.common.annotation.PrintLog;
import com.summer.common.constant.OperationAction;
import com.summer.common.constant.OperationModel;
import com.summer.common.model.JsonResult;
import com.summer.common.model.PageParam;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.model.dto.IntIdListDTO;
import com.summer.common.model.dto.UpdateStatusDTO;
import com.summer.common.model.vo.PageVO;
import com.summer.common.util.IpAddrUtil;
import com.summer.common.util.JwtTokenUtil;
import com.summer.model.SysUser;
import com.summer.model.condition.SysUserCondition;
import com.summer.model.dto.LoginDTO;
import com.summer.model.dto.UpdateUserInfoDTO;
import com.summer.model.vo.RouterVO;
import com.summer.model.vo.UserVO;
import com.summer.service.SysMenuService;
import com.summer.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 系统用户管理
 *
 * @author Dominick Li
 **/
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 分页查询
     */
    @PostMapping("/pagelist")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public JsonResult<PageVO<UserVO>> queryByCondition(@RequestBody PageParam<SysUser, SysUserCondition> pageParam) {
        if (pageParam.getSearchParam() == null) {
            pageParam.setSearchParam(new SysUserCondition());
        }
        return sysUserService.queryByCondition(pageParam);
    }

    /**
     * 添加或修改用户
     */
    @OperationLog(module = OperationModel.SYS_USER, description = OperationAction.ADD_OR_EDIT)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('sys:user:add','sys:user:edit')")
    public JsonResult save(@RequestBody SysUser sysUser) {
        return sysUserService.saveUser(sysUser);
    }

    /**
     * 导出用户信息
     */
    @PostMapping("/export")
    @PreAuthorize("hasAuthority('sys:user:export')")
    public JsonResult exportUsers(@RequestBody SysUserCondition sysUserCondition) {
        return sysUserService.export(sysUserCondition);
    }

    /**
     * 启用或禁用用户
     */
    @OperationLog(module = OperationModel.SYS_USER, description = OperationAction.ENABLED_OR_DIABLED)
    @PostMapping("/userEnabled")
    @PreAuthorize("hasAuthority('sys:user:enabled')")
    public JsonResult userEnabled(@RequestBody UpdateStatusDTO updateStatusDTO) {
        return sysUserService.userEnabled(updateStatusDTO);
    }

    /**
     * 批量删除用户
     *
     * @param idList id数组
     */
    @OperationLog(module = OperationModel.SYS_USER, description = OperationAction.REMOVE)
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:remove')")
    public JsonResult deleteUsersByIds(@RequestBody IntIdListDTO intIdListDTO) {
        return sysUserService.deleteByIdList(intIdListDTO.getIdList());
    }

    /**
     * 重置密码
     */
    @OperationLog(module = OperationModel.SYS_USER, description = OperationAction.RESET_PASSWORD)
    @GetMapping("/restPassword/{id}")
    @PreAuthorize("hasAuthority('sys:user:resetPwd')")
    public JsonResult restPassword(@PathVariable Integer id) {
        return sysUserService.restPassword(id);
    }

    /**
     * 修改个人设置
     */
    @PostMapping("/updateInfo")
    public JsonResult updatePassword(@RequestBody UpdateUserInfoDTO updatePasswordDTO) {
        return sysUserService.updatePassword(updatePasswordDTO);
    }

    /**
     * 用户登录
     */
    @PrintLog
    @PostMapping("/login")
    @IgnoreWebSecurity
    public JsonResult login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return sysUserService.login(loginDTO, IpAddrUtil.getLocalIp(request));
    }


    /**
     * 代理商用户登录
     */
    @PrintLog
    @PostMapping("/proxylogin")
    @IgnoreWebSecurity
    public JsonResult proxylogin(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return sysUserService.proxylogin(loginDTO, IpAddrUtil.getLocalIp(request));
    }

    /**
     * 谷歌认证器二维码
     */
    @PrintLog
    @PostMapping("/getGoogleQRCode")
    @IgnoreWebSecurity
    public JsonResult getGoogleQRCode(@Valid @RequestBody LoginDTO loginDTO) {
        return sysUserService.getGoogleQRCode(loginDTO);
    }

    /**
     * 获取用户的菜单权限
     */
    @GetMapping("/getRouters")
    public JsonResult<RouterVO> getRouters() {
        return sysMenuService.findAllByRoleId(JwtTokenUtil.getRoleIdFromToken(ThreadLocalManager.getToken()));
    }

    /**
     * 生成用户google认证器的二维码图片
     */
    @GetMapping("/generateGoogleQRCode/{id}")
    public void generateGoogleQRCode(@PathVariable Integer id, HttpServletResponse response) throws Exception {
        sysUserService.generateGoogleQRCode(id, response);
    }
}
