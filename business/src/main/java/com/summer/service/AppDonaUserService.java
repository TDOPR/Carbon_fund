package com.summer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.common.model.dto.UpdatePasswordDTO;
import com.summer.model.AppDonaUsers;
import com.summer.model.AppUsers;
import com.summer.model.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface AppDonaUserService extends IService<AppDonaUsers> {
    
        JsonResult login(AppDonaUserLoginDTO appUserLoginDTO, String localIp);

        JsonResult register(AppDonaUserRegisterDTO appUserRegisterDTO, String localIp);
        JsonResult findPassword(FindPasswordDTO findPasswordDTO);
        JsonResult modifyUserName(AppDonaUserDTO appUserDTO);
        
//        JsonResult uploadHeadImage(MultipartFile file) throws Exception;
        
//        JsonResult updatePassword(UpdatePasswordDTO updatePasswordDTO);
        
        AppDonaUsers selectColumnsByUserId(Integer userId, SFunction<AppDonaUsers, ?>... columns);
        
        JsonResult loginOut();
        
        JsonResult updatePassword(UpdatePasswordDTO updatePasswordDTO);

}
