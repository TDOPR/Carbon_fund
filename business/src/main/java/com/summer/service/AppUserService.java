package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.model.AppUsers;
import com.summer.model.dto.*;

public interface AppUserService extends IService<AppUsers> {


    JsonResult isRegisterCheck(String email, Integer type);
    
    
    JsonResult bindInviteCode(BindInviteCodeDTO bindInviteCodeDTO, String localIp);
    
    JsonResult link(LinkDTO linkDTO, String localIp);
    JsonResult bindMail(HomeBindMailDTO homeBindMailDTO);
    JsonResult getUserInfo(Integer userId);
    
}
