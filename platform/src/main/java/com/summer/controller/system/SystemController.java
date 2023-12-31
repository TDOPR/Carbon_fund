package com.summer.controller.system;

import com.summer.common.annotation.IgnoreWebSecurity;
import com.summer.common.annotation.OperationLog;
import com.summer.common.config.SysSettingParam;
import com.summer.common.constant.OperationAction;
import com.summer.common.constant.OperationModel;
import com.summer.common.model.JsonResult;
import com.summer.controller.monitor.server.SystemServer;
import com.summer.model.vo.BusinessVO;
import com.summer.service.SysDictionaryService;
import com.summer.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统主页
 *
 * @author Dominick Li
 * @CreateTime 2022/11/23 15:41
 **/
@RestController
@RequestMapping("/admin")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private SysDictionaryService sysDictionaryService;

    /**
     * 获取主页数据
     *
     * @return
     */
    @GetMapping("/home")
    public JsonResult<BusinessVO> getHomeInfo() {
        return systemService.getHomeInfo();
    }

    /**
     * 获取服务器信息
     *
     * @return
     */
    @GetMapping("/serverInfo")
    public JsonResult<SystemServer> getServerInfo() {
        return systemService.getServerInfo();
    }

    /**
     * 获取字典信息
     */
    @GetMapping("/setting")
    @PreAuthorize("hasAuthority('settings:system:list')")
    public JsonResult getSetting() {
        return JsonResult.successResult(SysSettingParam.getDictionaryParam());
    }

    /**
     * 修改字典信息
     */
    @OperationLog(module = OperationModel.SYS_SETTING, description = OperationAction.ADD_OR_EDIT)
    @PostMapping("/setting")
    @PreAuthorize("hasAuthority('settings:system:list')")
    public JsonResult saveSetting(@RequestBody Map<String, String> map) {
        return sysDictionaryService.modifyBaseDictionary(map);
    }

    @IgnoreWebSecurity
    @GetMapping("/reloadSetting")
    public JsonResult reloadSetting() {
        return sysDictionaryService.reloadSetting();
    }
}

