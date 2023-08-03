package com.summer.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.summer.model.AppUsers;
import com.summer.model.usd.EvmRecharge;
import com.summer.service.AppUserService;
import com.summer.service.DonaUsersWalletsService;
import com.summer.service.WalletsService;
import com.summer.service.impl.EvmRechargeService;
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

}
