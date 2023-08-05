package com.summer.service.impl;

import cn.hutool.extra.emoji.EmojiUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.config.GlobalProperties;
import com.summer.common.config.LoginConfig;
import com.summer.common.config.SysSettingParam;
import com.summer.common.constant.CacheKeyPrefixConstants;
import com.summer.common.constant.SystemConstants;
import com.summer.common.enums.ReturnMessageEnum;
import com.summer.common.enums.UserTypeEnum;
import com.summer.common.model.JsonResult;
import com.summer.common.model.SysLoginLog;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.model.dto.UpdatePasswordDTO;
import com.summer.common.service.SysLoginLogService;
import com.summer.common.util.IdUtil;
import com.summer.common.util.JwtTokenUtil;
import com.summer.common.util.MyIncrementGeneratorUtil;
import com.summer.common.util.encrypt.AESUtil;
import com.summer.common.util.redis.RedisUtil;
import com.summer.constant.CarbonConfig;
import com.summer.enums.*;
import com.summer.mapper.AppDonaUsersMapper;
import com.summer.model.*;
import com.summer.model.dto.*;
import com.summer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/4 11:21
 **/
@Service
public class AppDonaUserServiceImpl extends ServiceImpl<AppDonaUsersMapper, AppDonaUsers> implements AppDonaUserService {
    
    @Autowired
    private SysLoginLogService sysLoginLogService;


//    @Autowired
//    private WalletsService walletsService;
//
//    @Autowired
//    private WalletTttLogsService walletTttLogsService;
//
//    @Autowired
//    private TreePathService treePathService;
//
//    @Autowired
//    private KLineDataService kLineDataService;
//
//    @Autowired
//    private UpdatePwdLogService updatePwdLogService;
//
//    @Resource
//    private BannerMapper bannerMapper;
    
    @Resource
    private LoginConfig loginConfig;
    
    @Autowired
    private UpdatePwdLogService updatePwdLogService;
    
    @Autowired
    private DonaUsersWalletsService donaUsersWalletsService;
    
    @Autowired
    private DonaUsersIntegralWalletsLogsService donaUsersIntegralWalletsLogsService;
    
    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private UserDonaLogsService userDonaLogsService;
    
    
    @Override
    public JsonResult login(AppDonaUserLoginDTO appDonaUserLoginDTO, String localIp) {
        AppDonaUsers appDonaUsers = this.getOne(
                new LambdaQueryWrapper<AppDonaUsers>()
                        .eq(AppDonaUsers::getEmail, appDonaUserLoginDTO.getEmail())
        );
        
        if (appDonaUsers == null) {
            return JsonResult.failureResult(ReturnMessageEnum.EMAIL_NOT_EXISTS);
        } else if (!appDonaUsers.getPassword().equals(AESUtil.encrypt(appDonaUserLoginDTO.getPassword(), appDonaUsers.getSalt()))) {
            return JsonResult.failureResult(ReturnMessageEnum.PASSWORD_ERROR);
        }
        
        //修改登录次数
        UpdateWrapper<AppDonaUsers> updateWrapper = Wrappers.update();
        updateWrapper.lambda()
                .set(AppDonaUsers::getLoginCount, appDonaUsers.getLoginCount() + 1)
                .eq(AppDonaUsers::getId, appDonaUsers.getId());
        this.update(updateWrapper);
        
        String token = JwtTokenUtil.getToken(appDonaUsers.getId());
        
        //单点登录需要删除用户在其它地方登录的Token
        if (SysSettingParam.isEnableSso()) {
            RedisUtil.deleteObjects(CacheKeyPrefixConstants.APP_TOKEN + appDonaUsers.getId() + ":*");
        }
        
        //把token存储到缓存中
        String tokenKey = CacheKeyPrefixConstants.APP_TOKEN + appDonaUsers.getId() + ":" + IdUtil.simpleUUID();
        RedisUtil.setCacheObject(tokenKey, token, Duration.ofSeconds(GlobalProperties.getTokenExpire()));
        sysLoginLogService.save(new SysLoginLog(appDonaUsers.getEmail(), localIp, UserTypeEnum.CLIENT.getValue()));
        
        //返回token给客户端
        JSONObject json = new JSONObject();
        json.put(SystemConstants.TOKEN_NAME, tokenKey);
        return JsonResult.successResult(json);
    }
    
    @Override
    @Transactional
    public JsonResult register(AppDonaUserRegisterDTO appDonaUserRegisterDTO, String localIp) {
        if (GlobalProperties.isProdEnv()) {
            String cacheKey = CacheKeyPrefixConstants.CAPTCHA_CODE + appDonaUserRegisterDTO.getUuid();
            String code = RedisUtil.getCacheObject(cacheKey);
            if (code == null) {
                return JsonResult.failureResult(ReturnMessageEnum.VERIFICATION_CODE_EXPIRE);
            }
            if (!code.equals(appDonaUserRegisterDTO.getCode())) {
                return JsonResult.failureResult(ReturnMessageEnum.VERIFICATION_CODE_ERROR);
            }
        }
        Certificate certificate;
        AppDonaUsers appDonaUsers;
        UserDonaLogs userDonaLogs;
        appDonaUsers = this.getOne(new LambdaQueryWrapper<AppDonaUsers>().eq(AppDonaUsers::getEmail, appDonaUserRegisterDTO.getEmail()));
        if (appDonaUsers == null) {
            //注册
            String zeroCertificate = MyIncrementGeneratorUtil.usingMath(CarbonConfig.STRING_LENGTH);
            appDonaUsers = new AppDonaUsers();
            appDonaUsers.setEmail(appDonaUserRegisterDTO.getEmail());
            appDonaUsers.setLevel(VipLevelEnum.ZERO.getLevel());
            appDonaUsers.setNickName(appDonaUsers.getEmail());
            //设置密码加密用的盐
            appDonaUsers.setSalt(IdUtil.simpleUUID());
            appDonaUsers.setPassword(AESUtil.encrypt(appDonaUserRegisterDTO.getPassword(), appDonaUsers.getSalt()));
            this.save(appDonaUsers);
            
            certificateService.generateCertificate(appDonaUsers.getId(), appDonaUsers.getNickName(), appDonaUsers.getZeroCertificate(), VipLevelEnum.ZERO.getLevel().toString(), appDonaUsers.getCreateTime());
            userDonaLogs = new UserDonaLogs();
            userDonaLogs.setUserId(appDonaUsers.getId());
            userDonaLogs.setCertificate(zeroCertificate);
            userDonaLogs.setLevel(VipLevelEnum.ZERO.getLevel());
            userDonaLogs.setTitle(MedalEnum.ZERO.getTitle());
            userDonaLogsService.save(userDonaLogs);
            
            //创建一条钱包记录
            DonaUsersWallets donaUsersWallets = new DonaUsersWallets();
            //注册即送100积分
            donaUsersWallets.setUserIntegralAmount(CarbonConfig.REGISTER_REWARDS);
            donaUsersWallets.setUserId(appDonaUsers.getId());
            donaUsersWalletsService.save(donaUsersWallets);
            DonaUsersIntegralLogs donaUsersIntegralLogs = new DonaUsersIntegralLogs();
            donaUsersIntegralLogs.setUserId(appDonaUsers.getId());
            donaUsersIntegralLogs.setIntegralAmount(CarbonConfig.REGISTER_REWARDS);
            donaUsersIntegralLogs.setBuyLevel(VipLevelEnum.ZERO.getLevel());
            donaUsersIntegralLogs.setAction(FlowingActionEnum.INCOME.getValue());
            donaUsersIntegralLogs.setType(IntegralEnum.ZERO.getType());
            donaUsersIntegralWalletsLogsService.save(donaUsersIntegralLogs);
            
            String token = JwtTokenUtil.getToken(appDonaUsers.getId());
            //把token存储到缓存中
            String tokenKey = CacheKeyPrefixConstants.APP_TOKEN + appDonaUsers.getId() + ":" + IdUtil.simpleUUID();
            RedisUtil.setCacheObject(tokenKey, token, Duration.ofSeconds(GlobalProperties.getTokenExpire()));
            sysLoginLogService.save(new SysLoginLog(appDonaUsers.getEmail(), localIp, 2));
            //返回token给客户端
            JSONObject json = new JSONObject();
            json.put(SystemConstants.TOKEN_NAME, tokenKey);
            return JsonResult.successResult(json);
        } else {
            return JsonResult.failureResult(ReturnMessageEnum.EMAIL_EXISTS);
        }
    }
    
    @Override
    @Transactional
    public JsonResult findPassword(FindPasswordDTO findPasswordDTO) {
        if (GlobalProperties.isProdEnv()) {
            String cacheKey = CacheKeyPrefixConstants.CAPTCHA_CODE + findPasswordDTO.getUuid();
            String code = RedisUtil.getCacheObject(cacheKey);
            if (code == null) {
                return JsonResult.failureResult(ReturnMessageEnum.VERIFICATION_CODE_EXPIRE);
            }
            if (!code.equals(findPasswordDTO.getCode())) {
                return JsonResult.failureResult(ReturnMessageEnum.VERIFICATION_CODE_ERROR);
            }
        }
        AppDonaUsers appDonaUsers;
        appDonaUsers = this.getOne(new LambdaQueryWrapper<AppDonaUsers>().eq(AppDonaUsers::getEmail, findPasswordDTO.getEmail()));
        if (appDonaUsers == null) {
            return JsonResult.failureResult(ReturnMessageEnum.EMAIL_NOT_EXISTS);
        }
        appDonaUsers.setPassword(AESUtil.encrypt(findPasswordDTO.getPassword(), appDonaUsers.getSalt()));
        this.saveOrUpdate(appDonaUsers);
        UpdatePwdLog updatePwdLog = new UpdatePwdLog(appDonaUsers.getId(), LocalDateTime.now());
        updatePwdLogService.saveOrUpdate(updatePwdLog);
        return JsonResult.successResult();
    }
    
    @Override
    public JsonResult modifyUserName(AppDonaUserDTO appDonaUserDTO) {
        if (EmojiUtil.containsEmoji(appDonaUserDTO.getNickName())) {
            return JsonResult.failureResult(ReturnMessageEnum.NO_SUPPORT_EMPJI);
        }
        
        UpdateWrapper<AppDonaUsers> updateWrapper = Wrappers.update();
        updateWrapper.lambda()
                .set(AppDonaUsers::getNickName, appDonaUserDTO.getNickName())
                .eq(AppDonaUsers::getId, JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken()));
        boolean flag = this.update(updateWrapper);
        return JsonResult.build(flag);
    }
    
    
    @Override
    @Transactional
    public JsonResult updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        AppDonaUsers sysUser = this.selectColumnsByUserId(userId, AppDonaUsers::getSalt, AppDonaUsers::getPassword);
        
        String oldPwd = AESUtil.encrypt(updatePasswordDTO.getOldPassword(), sysUser.getSalt());
        if (sysUser.getPassword().equals(oldPwd)) {
            UpdateWrapper<AppDonaUsers> wrapper = Wrappers.update();
            wrapper.lambda()
                    .set(AppDonaUsers::getPassword, AESUtil.encrypt(updatePasswordDTO.getPassword(), sysUser.getSalt()))
                    .eq(AppDonaUsers::getId, userId);
            update(wrapper);
            UpdatePwdLog updatePwdLog = new UpdatePwdLog(userId, LocalDateTime.now());
            updatePwdLogService.saveOrUpdate(updatePwdLog);
            return JsonResult.successResult();
        }
        return JsonResult.failureResult(ReturnMessageEnum.ORIGINAL_PASSWORD_ERROR);
    }
    
    @Override
    public AppDonaUsers selectColumnsByUserId(Integer userId, SFunction<AppDonaUsers, ?>... columns) {
        return this.getOne(
                new LambdaQueryWrapper<AppDonaUsers>()
                        .select(columns)
                        .eq(AppDonaUsers::getId, userId)
        );
    }
    
    
    @Override
    public JsonResult loginOut() {
        RedisUtil.deleteObject(ThreadLocalManager.getToken());
        return JsonResult.successResult();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerDonaUser(HomeBindMailDTO homeBindMailDTO) {
        AppDonaUsers appDonaUsers;
        appDonaUsers = this.getOne(new LambdaQueryWrapper<AppDonaUsers>().eq(AppDonaUsers::getEmail, homeBindMailDTO.getEmail()));
        if (appDonaUsers == null) {
            //注册
            String zeroCertificate = MyIncrementGeneratorUtil.usingMath(CarbonConfig.STRING_LENGTH);
            appDonaUsers = new AppDonaUsers();
            appDonaUsers.setEmail(homeBindMailDTO.getEmail());
            appDonaUsers.setLevel(VipLevelEnum.ZERO.getLevel());
            appDonaUsers.setZeroCertificate(zeroCertificate);
            appDonaUsers.setNickName(appDonaUsers.getEmail());
            //设置密码加密用的盐
            appDonaUsers.setSalt(IdUtil.simpleUUID());
            this.save(appDonaUsers);
            
            certificateService.generateCertificate(appDonaUsers.getId(), appDonaUsers.getNickName(), appDonaUsers.getZeroCertificate(), VipLevelEnum.ZERO.getLevel().toString(), appDonaUsers.getCreateTime());
            
            
            DonaUsersWallets donaUsersWallets = new DonaUsersWallets();
            //注册即送100积分
            donaUsersWallets.setUserIntegralAmount(CarbonConfig.REGISTER_REWARDS);
            donaUsersWallets.setUserId(appDonaUsers.getId());
            donaUsersWalletsService.save(donaUsersWallets);
            DonaUsersIntegralLogs donaUsersIntegralLogs = new DonaUsersIntegralLogs();
            donaUsersIntegralLogs.setUserId(appDonaUsers.getId());
            donaUsersIntegralLogs.setIntegralAmount(CarbonConfig.REGISTER_REWARDS);
            donaUsersIntegralLogs.setAction(FlowingActionEnum.INCOME.getValue());
            donaUsersIntegralLogs.setType(IntegralEnum.ZERO.getType());
            donaUsersIntegralWalletsLogsService.save(donaUsersIntegralLogs);
        }
    }
    
}
