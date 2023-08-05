package com.summer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.mapper.EvmRechargeMapper;
import com.summer.mapper.UserDonaLogsMapper;
import com.summer.model.UserDonaLogs;
import com.summer.model.usd.EvmRecharge;
import com.summer.service.EvmRechargeService;
import com.summer.service.UserDonaLogsService;
import org.springframework.stereotype.Service;

@Service
public class UserDonaLogsServiceImpl extends ServiceImpl<UserDonaLogsMapper, UserDonaLogs> implements UserDonaLogsService {

}
