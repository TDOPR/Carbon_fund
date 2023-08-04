package com.summer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.summer.common.config.ChainIdsConfig;
import com.summer.common.config.GlobalProperties;
import com.summer.common.constant.CacheKeyPrefixConstants;
import com.summer.common.enums.ReturnMessageEnum;
import com.summer.common.model.JsonResult;
import com.summer.common.model.SysLoginLog;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.service.SysLoginLogService;
import com.summer.common.util.*;
import com.summer.common.util.redis.RedisUtil;
import com.summer.enums.EmailTypeEnum;
import com.summer.mapper.AppDonaUsersMapper;
import com.summer.mapper.AppUserMapper;
import com.summer.model.*;
import com.summer.model.dto.*;
import com.summer.model.vo.AppTokenVO;
import com.summer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/4 11:21
 **/
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUsers> implements AppUserService {
    
    @Autowired
    private SysLoginLogService sysLoginLogService;
    
    @Autowired
    private AppDonaUserService appDonaUserService;

    
    @Autowired
    private ChainIdsConfig chainIdsConfig;
    
    @Autowired
    private EvmUserWalletService evmUserWalletService;
    
    @Autowired
    private WalletsService walletsService;
    
    @Autowired
    private TreePathService treePathService;
    
    @Resource
    private AppDonaUsersMapper appDonaUsersMapper;

    
    @Override
    public JsonResult isRegisterCheck(String email, Integer type) {
        AppUsers appUsers = this.getOne(new LambdaQueryWrapper<AppUsers>().select(AppUsers::getId).eq(AppUsers::getEmail, email));
        if (EmailTypeEnum.REGISTER.getValue().equals(type)) {
            //注册的时候判断邮箱号是否被注册
            if (appUsers != null) {
                return JsonResult.failureResult(ReturnMessageEnum.EMAIL_EXISTS);
            }
        } else if (appUsers == null) {
            //找回密码的时候和支付验证的时候判断邮箱号是否存在
            return JsonResult.failureResult(ReturnMessageEnum.EMAIL_NOT_EXISTS);
        }
        return JsonResult.successResult();
    }
    
    @Override
    @Transactional
    public JsonResult bindInviteCode(BindInviteCodeDTO bindInviteCodeDTO, String localIp){
        AppUsers inviteUser;
        AppUsers user;
        user = this.getOne(new LambdaQueryWrapper<AppUsers>().eq(AppUsers::getUserAddress, bindInviteCodeDTO.getUserAddress()));
        inviteUser =this.getOne(new LambdaQueryWrapper<AppUsers>().eq(AppUsers::getInviteCode, bindInviteCodeDTO.getInviteCode()));
        if(inviteUser == null){
            return JsonResult.failureResult(ReturnMessageEnum.INVITE_CODE_ERROR);
        }else if(user != null){
            return JsonResult.failureResult(ReturnMessageEnum.ACCOUNT_EXISTS);
        }else{
            AppUsers appUsers = new AppUsers();
            appUsers.setUserAddress(bindInviteCodeDTO.getUserAddress().toLowerCase());
            appUsers.setInviteCode(getInviteCode());
            appUsers.setInviteId(inviteUser.getId());
            this.save(appUsers);
            //创建一条钱包记录
            Wallets wallets = new Wallets();
            wallets.setUserId(appUsers.getId());
            walletsService.save(wallets);
            //添加一条默认的treepath记录
            TreePath treePath =TreePath.builder()
                    .ancestor(appUsers.getId())
                    .descendant(appUsers.getId())
                    .level(0)
                    .build();
            treePathService.save(treePath);
            if (appUsers.getInviteId() != null) {
                //保存上下级关系
                treePathService.insertTreePath(appUsers.getId(), appUsers.getInviteId());
            }
            String token = JwtTokenUtil.getToken(appUsers.getId());
            //把token存储到缓存中
            String tokenKey = CacheKeyPrefixConstants.APP_TOKEN + appUsers.getId() + ":" + IdUtil.simpleUUID();
            RedisUtil.setCacheObject(tokenKey, token, Duration.ofSeconds(GlobalProperties.getTokenExpire()));
            sysLoginLogService.save(new SysLoginLog(appUsers.getUserAddress(), localIp, 2));
    
            //返回token给客户端
            AppTokenVO appTokenVO = new AppTokenVO();
            appTokenVO.setToken(tokenKey);
            appTokenVO.setInviteCode(appUsers.getInviteCode());
            return JsonResult.successResult(appTokenVO);
            
        }
    }
    
    
    @Override
    @Transactional
    public JsonResult link(LinkDTO linkDTO, String localIp) {
        AppUsers user;
        user = this.getOne(new LambdaQueryWrapper<AppUsers>().eq(AppUsers::getUserAddress, linkDTO.getUserAddress()));
        if (user == null) {
            return JsonResult.failureResult(ReturnMessageEnum.ACCOUNT_NOT_EXISTS.getCnMessage());
        } else {
            String token = JwtTokenUtil.getToken(user.getId());
            //把token存储到缓存中
            String tokenKey = CacheKeyPrefixConstants.APP_TOKEN + user.getId() + ":" + IdUtil.simpleUUID();
            RedisUtil.setCacheObject(tokenKey, token, Duration.ofSeconds(GlobalProperties.getTokenExpire()));
            sysLoginLogService.save(new SysLoginLog(user.getUserAddress(), localIp, 2));
            
            //返回token给客户端
            AppTokenVO appTokenVO = new AppTokenVO();
            appTokenVO.setToken(tokenKey);
            appTokenVO.setInviteCode(user.getInviteCode());
            return JsonResult.successResult(appTokenVO);
        }
    }
    
    /**
     * 生成唯一邀请码
     */
    private String getInviteCode() {
        String inviteCode = IdUtil.generateShortUUID(8);
        AppUsers exists;
        while (true) {
            exists = this.getOne(new LambdaQueryWrapper<AppUsers>().eq(AppUsers::getInviteCode, inviteCode));
            if (exists != null) {
                inviteCode = IdUtil.generateShortUUID(8);
            } else {
                break;
            }
        }
        return inviteCode;
    }

    @Override
    public JsonResult bindMail(HomeBindMailDTO homeBindMailDTO) {
        
        if (GlobalProperties.isProdEnv()) {
            String cacheKey = CacheKeyPrefixConstants.CAPTCHA_CODE + homeBindMailDTO.getUuid();
            String code = RedisUtil.getCacheObject(cacheKey);
            if (code == null) {
                return JsonResult.failureResult(ReturnMessageEnum.VERIFICATION_CODE_EXPIRE);
            }
            if (!code.equals(homeBindMailDTO.getCode())) {
                return JsonResult.failureResult(ReturnMessageEnum.VERIFICATION_CODE_ERROR);
            }
        }
        AppUsers appUsers;
        appUsers = this.getOne(new LambdaQueryWrapper<AppUsers>().eq(AppUsers::getEmail, homeBindMailDTO.getEmail()));
        if (appUsers == null) {
            UpdateWrapper<AppUsers> updateWrapper = Wrappers.update();
            updateWrapper.lambda()
                    .set(AppUsers::getEmail, homeBindMailDTO.getEmail())
                    .eq(AppUsers::getId, JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken()));
            boolean flag = this.update(updateWrapper);
            appDonaUserService.registerDonaUser(homeBindMailDTO);
            return JsonResult.build(flag);
        } else {
            return JsonResult.failureResult(ReturnMessageEnum.EMAIL_EXISTS);
        }
    }
    
    @Override
    public JsonResult getUserInfo(Integer userId){
        AppUsers appUsers;
        AppDonaUsers appDonaUsers;
        appUsers = this.getOne(new LambdaQueryWrapper<AppUsers>().eq(AppUsers::getId, userId));
        appDonaUsers = appDonaUserService.getOne(new LambdaQueryWrapper<AppDonaUsers>().eq(AppDonaUsers::getEmail, appUsers.getEmail()));
        if(appDonaUsers == null){
            JSONObject json = new JSONObject();
            json.put("level", null);
            json.put("inviteCode", appUsers.getInviteCode());
            json.put("userIntegralAmount", Integer.valueOf(0));
            json.put("walletAmount", Integer.valueOf(0));
            json.put("isEmailRegister", false);
            json.put("imgUrl", null);
            json.put("title", null);
            return JsonResult.successResult(json);
        }else{
            appDonaUsers.getId();
            HomeUserInfoDTO homeUserInfoDTO = appDonaUsersMapper.getUserInfo(appDonaUsers.getId(), appDonaUsers.getEmail());
            homeUserInfoDTO.setIsEmailRegister(true);
            return JsonResult.successResult(homeUserInfoDTO);
        }
    }
    
    
    
    
    
    
}
