package com.summer.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.summer.common.util.MyIncrementGeneratorUtil;
import com.summer.constant.CarbonConfig;
import com.summer.enums.MedalEnum;
import com.summer.model.AppUsers;
import com.summer.model.UserDonaLogs;
import com.summer.model.UserTaskLogs;
import com.summer.model.usd.EvmRecharge;
import com.summer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
public class TradeManager {

//    @Autowired
//    private EvmWithdrawService evmWithdrawService;
    
    @Autowired
    private EvmRechargeService evmRechargeService;
    
    @Autowired
    private WalletsService walletsService;
    
    @Autowired
    private DonaUsersWalletsService donaUsersWalletsService;
    
    @Autowired
    private AppUserService appUserService;
    
    @Autowired
    private UserDonaLogsService userDonaLogsService;
    
    @Autowired
    private UserTaskLogsService userTaskLogsService;
    
    @Transactional(rollbackFor = Exception.class)
    public void evmRecharge(String userAddress, BigDecimal rechargeAmount, Integer chainId) {
        
        AppUsers userId = appUserService.getOne(new LambdaQueryWrapper<AppUsers>().select(AppUsers::getId).eq(AppUsers::getUserAddress, userAddress));
        
        EvmRecharge evmRecharge = EvmRecharge.builder()
                .uid(userId.getId())
                .address(userAddress)
                .rechargeAmount(rechargeAmount)
                .chainId(chainId)
                .build();
        evmRechargeService.save(evmRecharge);
        
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void updateUserDonaLogs(Integer userId, Integer level) {
        String certiString = MyIncrementGeneratorUtil.usingMath(CarbonConfig.STRING_LENGTH);
        UserDonaLogs userDonaLogs = UserDonaLogs.builder()
                .userId(userId)
                .level(level)
                .title(MedalEnum.getByLevel(level).getTitle())
                .certificate(certiString)
                .build();
        userDonaLogsService.save(userDonaLogs);
        
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void updateUserTaskLogs(Integer userId, Integer taskId) {
        
        UserTaskLogs userTaskLogs = UserTaskLogs.builder()
                .userId(userId)
                .taskId(taskId)
                .build();
        userTaskLogsService.save(userTaskLogs);
        
    }

}
