package com.summer.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.common.annotation.PrintLog;
import com.summer.common.annotation.RepeatSubmit;
import com.summer.common.model.JsonResult;
import com.summer.common.util.IpAddrUtil;
import com.summer.model.dto.*;
import com.summer.service.AppDonaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Dominick Li
 * @Description 首页
 * @CreateTime 2023/4/16 10:30
 **/
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private AppDonaUserService appDonaUserService;


    /**
     * 注册 奖励
     */
    @PrintLog
    @RepeatSubmit
    @PostMapping("/register")
    public JsonResult register(@Valid @RequestBody AppDonaUserRegisterDTO appDonaUserRegisterDTO, HttpServletRequest request) {
        return appDonaUserService.register(appDonaUserRegisterDTO, IpAddrUtil.getLocalIp(request));
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public JsonResult login(@Valid @RequestBody AppDonaUserLoginDTO appDonaUserLoginDTO, HttpServletRequest request) {
        return appDonaUserService.login(appDonaUserLoginDTO, IpAddrUtil.getLocalIp(request));
    }

    /**
     * 找回密码
     */
    @PostMapping("/findPassword")
    public JsonResult findPassword(@Valid @RequestBody FindPasswordDTO findPasswordDTO) {
        return appDonaUserService.findPassword(findPasswordDTO);
    }

//    /**
//     * 获取隐私政策和用户协议pdf下载链接
//     */
//    @GetMapping("/getPdfUrl")
//    public JsonResult getPdfUrl() {
//        String language = ThreadLocalManager.getLanguage();
//        //GlobalProperties.
//        String prevUrl = GlobalProperties.getCallBackUrl() + appParamProperties.getVirtualPathPrefix() + "/";
//        String userAgreementUrl = String.format("%spdf/%s/user.pdf", prevUrl, language);
//        String privacyPolicyUrl = String.format("%spdf/%s/privacy.pdf", prevUrl, language);
//        return JsonResult.successResult(new PdfVO(userAgreementUrl, privacyPolicyUrl));
//    }
//
//    /**
//     * 检测版本更新信息
//     */
//    @PostMapping("/checkVersion")
//    public JsonResult<CheckVersionVO> checkVersion(@RequestBody CheckVersionDTO checkVersionDTO) {
//        return appVersionService.checkVersion(checkVersionDTO);
//    }
//
//    /**
//     * 获取官网需要的app下载地址
//     */
//    @GetMapping("/getAppDownloadAddress")
//    public JsonResult<AppDownloadVO> getAppDownloadAddress() {
//        return JsonResult.successResult(AppDownloadVO.builder()
//                .ios(TiktokSettingEnum.IOS.stringValue())
//                .android(TiktokSettingEnum.ANDROID.stringValue())
//                .build());
//    }


}
