package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.model.AppDonaUsers;
import com.summer.model.AppUsers;
import com.summer.model.dto.AppUserLoginDTO;
import com.summer.model.dto.AppUserRegisterDTO;

public interface AppUserService extends IService<AppUsers> {

//    JsonResult login(AppUserLoginDTO appUserLoginDTO, String localIp);

//    JsonResult register(AppUserRegisterDTO appUserRegisterDTO, String localIp);
//
//    JsonResult findPassword(FindPasswordDTO findPasswordDTO);
//
//    JsonResult home(PageDTO pageDTO);
//
//    JsonResult<PageVO<AppUsersVO>> pageList(PageParam<AppUsers, AppUsersCondition> pageParam);
//
//    JsonResult<List<TreeUserIdDTO>> treeDiagram(Integer userId);
//
//    JsonResult modifyUserName(AppUserDTO appUserDTO);
//
//    JsonResult uploadHeadImage(MultipartFile file) throws Exception;
//
//    JsonResult updatePassword(UpdatePasswordDTO updatePasswordDTO);
//
//    JsonResult<MyDetailVO> getMyDetail();
//
//    AppUsers selectColumnsByUserId(Integer userId, SFunction<AppUsers, ?>... columns);
//
//    JsonResult batchAddUser();
//
//    JsonResult loginOut();

    JsonResult isRegisterCheck(String email, Integer type);

//    JsonResult bindTiktok(TikTokAccount tikTokAccount);
//
//    JsonResult bindMobile(MobileDTO mobileDTO);

//    JsonResult sendSms(String mobile, Integer type);


//    JsonResult editTiktok(TikTokAccount tikTokAccount);
//
//    JsonResult<TiktokInfoVO> getTiktokInfo();
//
//    JsonResult portrait();
//
//    JsonResult batchAddSubUser(BatchAddSubUserDTO batchAddSubUserDTO);
//
//    JsonResult addUser(AppUsers appUsers);
//
//    JsonResult skip();
//
//    JsonResult<ItemInfoVO> getItemInfo();
//
//    JsonResult editProxyStatus(UpdateStatusDTO updateStatusDTO);
}
