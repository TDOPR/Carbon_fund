package com.summer.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.common.annotation.PrintLog;
import com.summer.common.annotation.RepeatSubmit;
import com.summer.common.config.GlobalProperties;
import com.summer.common.constant.CacheKeyPrefixConstants;
import com.summer.common.constant.SystemConstants;
import com.summer.common.enums.ReturnMessageEnum;
import com.summer.common.model.JsonResult;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.model.vo.PageVO;
import com.summer.common.util.IdUtil;
import com.summer.common.util.IpAddrUtil;
import com.summer.common.util.JwtTokenUtil;
import com.summer.common.util.redis.RedisUtil;
import com.summer.mapper.AppDonaUsersMapper;
import com.summer.mapper.DonaUsersWalletsLogsMapper;
import com.summer.mapper.DonaUsersWalletsMapper;
import com.summer.model.AppUsers;
import com.summer.model.dto.*;
import com.summer.model.vo.MyDirectPushVO;
import com.summer.model.vo.MyMemberInfoVO;
import com.summer.model.vo.MyTeamInfoVO;
import com.summer.model.vo.RechargeRecordInfoVO;
import com.summer.service.AppDonaUserService;
import com.summer.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;
import java.util.List;

/**
 * @author Dominick Li
 * @Description 首页
 * @CreateTime 2023/4/16 10:30
 **/
@RestController
@RequestMapping("/zhome")
public class ZHomeController {

    
    @Autowired
    private AppUserService appUserService;
    
    @Resource
    private AppDonaUsersMapper appDonaUsersMapper;
    
    @Resource
    private DonaUsersWalletsLogsMapper donaUsersWalletsLogsMapper;
    

    
    /**
     * 用户连接
     */
    @PrintLog
    @RepeatSubmit
    @PostMapping("/link")
    public JsonResult link(@Valid @RequestBody LinkDTO linkDTO, HttpServletRequest request) {
        return appUserService.link(linkDTO, IpAddrUtil.getLocalIp(request));
    }
    
    /**
     * 绑定邀请码
     */
    @PrintLog
    @RepeatSubmit
    @PostMapping("/bindInviteCode")
    public JsonResult bindInviteCode(@Valid @RequestBody BindInviteCodeDTO bindInviteCodeDTO, HttpServletRequest request) {
        return appUserService.bindInviteCode(bindInviteCodeDTO, IpAddrUtil.getLocalIp(request));
    }
    
    /**
     * 绑定邮箱
     */
    @PrintLog
    @RepeatSubmit
    @PostMapping("/bindMail")
    public JsonResult bindMail(@RequestBody AppDonaUserRegisterDTO appDonaUserRegisterDTO) {
        return appUserService.bindMail(appDonaUserRegisterDTO);
    }
    
    /**
     * 用户信息
     */
    @PrintLog
    @RepeatSubmit
    @PostMapping("/userInfo")
    public JsonResult userInfo() {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        
        return appUserService.getUserInfo(userId);
    }
    
//    /**
//     * 获取我的团队信息
//     */
//    @PrintLog
//    @RepeatSubmit
//    @PostMapping("/getMyTeamInfo")
//    public JsonResult<MyTeamInfoVO> getMyTeamInfo() {
//        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
//        List<MyMemberInfoVO> myTeamInfoVOList = appDonaUsersMapper.getMyTeamInfo(userId);
//        List<MyDirectPushVO> myDirectPushVOS = appDonaUsersMapper.getMyDirectPushVO(userId);
//
//        return JsonResult.successResult(MyTeamInfoVO.builder()
//                .myDirectPushVOS(myDirectPushVOS)
//                .memberInfoVOS(myTeamInfoVOList)
//                .build());
//    }
    
    /**
     * 获取我的团队信息
     */
    @PrintLog
    @RepeatSubmit
    @PostMapping("/getMyTeam")
    public JsonResult<List<MyMemberInfoVO>> getMyTeamInfo() {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        List<MyMemberInfoVO> memberInfoVOS = appDonaUsersMapper.getMyTeamInfo(userId);
        for(MyMemberInfoVO myMemberInfoVO : memberInfoVOS){
            if(myMemberInfoVO.getValue() == null){
                myMemberInfoVO.setValue(0);
            }
        }
        return JsonResult.successResult(memberInfoVOS);
    }
    
    /**
     * 获取我的直推信息
     */
    @PrintLog
    @RepeatSubmit
    @PostMapping("/getMyDirectPushInfo")
    public JsonResult<PageVO<MyDirectPushVO>> getMyDirectPushInfo(@RequestBody PageDTO pageDTO) {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        return JsonResult.successResult(new PageVO<>(appDonaUsersMapper.getMyDirectPushVO(userId, pageDTO.getPage())));
    }
    
    /**
     * 充值记录
     */
    @PrintLog
    @RepeatSubmit
    @PostMapping("/rechargeRecord")
    public JsonResult<PageVO<RechargeRecordInfoVO>> rechargeRecord(@RequestBody PageDTO pageDTO) {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        
        return JsonResult.successResult(new PageVO<>(donaUsersWalletsLogsMapper.rechargeRecord(userId, pageDTO.getPage())));
    }
    
    /**
     * 跳转第三方页面生成token
     */
    @PrintLog
    @RepeatSubmit
    @PostMapping("/getThirdPageToken")
    public JsonResult getThirdPageToken() {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        AppUsers appUsers = appUserService.selectColumnsByUserId(userId, AppUsers::getEmail, AppUsers::getUserAddress);
        if(appUsers.getEmail() != null){
            Integer appDonaUserId = appDonaUsersMapper.selectDonaUserIdByUserAddress(appUsers.getUserAddress());
            String token = JwtTokenUtil.getToken(appDonaUserId);
            String tokenKey = CacheKeyPrefixConstants.APP_TOKEN + appDonaUserId + ":" + IdUtil.simpleUUID();
            RedisUtil.setCacheObject(tokenKey, token, Duration.ofSeconds(GlobalProperties.getTokenExpire()));
            JSONObject json = new JSONObject();
            json.put(SystemConstants.TOKEN_NAME, tokenKey);
            return JsonResult.successResult(json);
        }
        return JsonResult.failureResult(ReturnMessageEnum.EMAIL_NOT_BIND);
    }



}
