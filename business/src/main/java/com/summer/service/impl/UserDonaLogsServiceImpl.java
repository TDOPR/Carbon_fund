package com.summer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.util.MyIncrementGeneratorUtil;
import com.summer.constant.CarbonConfig;
import com.summer.enums.MedalEnum;
import com.summer.enums.VipLevelEnum;
import com.summer.mapper.EvmRechargeMapper;
import com.summer.mapper.UserDonaLogsMapper;
import com.summer.model.AppDonaUsers;
import com.summer.model.UserDonaLogs;
import com.summer.model.usd.EvmRecharge;
import com.summer.service.AppDonaUserService;
import com.summer.service.CertificateService;
import com.summer.service.EvmRechargeService;
import com.summer.service.UserDonaLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDonaLogsServiceImpl extends ServiceImpl<UserDonaLogsMapper, UserDonaLogs> implements UserDonaLogsService {
    

    @Autowired
    private CertificateService certificateService;
    @Autowired
    private AppDonaUserService appDonaUserService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserDonaLogs(Integer userId, Integer level) {
        String certiString = MyIncrementGeneratorUtil.usingMath(CarbonConfig.STRING_LENGTH);
        UserDonaLogs userDonaLogs = UserDonaLogs.builder()
                .userId(userId)
                .level(level)
                .title(MedalEnum.getByLevel(level).getTitle())
                .certificate(certiString)
                .build();
        this.save(userDonaLogs);
        certificateService.generateCertificate(userId, appDonaUserService.selectColumnsByUserId(userId, AppDonaUsers::getNickName).getNickName(), certiString, level.toString(), userDonaLogs.getCreateTime());
    }
}
