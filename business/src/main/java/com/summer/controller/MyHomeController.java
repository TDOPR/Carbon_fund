package com.summer.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summer.common.annotation.PrintLog;
import com.summer.common.model.JsonResult;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.model.dto.UpdatePasswordDTO;
import com.summer.common.model.vo.PageVO;
import com.summer.common.util.JwtTokenUtil;
import com.summer.constant.CarbonConfig;
import com.summer.mapper.AppDonaUsersMapper;
import com.summer.mapper.DonaUsersIntegralWalletsLogsMapper;
import com.summer.mapper.DonaUsersWalletsLogsMapper;
import com.summer.mapper.DonaUsersWalletsMapper;
import com.summer.model.Medal;
import com.summer.model.dto.*;
import com.summer.service.AppDonaUserService;
import com.summer.service.DonaUsersIntegralWalletsLogsService;
import com.summer.service.DonaUsersWalletsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * 我的主页
 * @author Dominick Li
 * @CreateTime 2022/11/18 11:04
 **/
@RestController
@RequestMapping("/myhome")
public class MyHomeController {

    @Autowired
    private AppDonaUserService appDonaUserService;
    
    @Resource
    private AppDonaUsersMapper appDonaUsersMapper;
    
    @Resource
    private DonaUsersWalletsLogsMapper donaUsersWalletsLogsMapper;
    
    @Resource
    private DonaUsersWalletsMapper donaUsersWalletsMapper;
    
    @Resource
    private DonaUsersIntegralWalletsLogsMapper donaUsersIntegralWalletsLogsMapper;
    
    @Autowired
    private DonaUsersIntegralWalletsLogsService donaUsersIntegralWalletsLogsService;
    
    @Autowired
    private DonaUsersWalletsService donaUsersWalletsService;

//    /**
//     * 获取用户和平台详细信息
//     */
//    @GetMapping
//    public JsonResult<MyDetailVO> getMyDetail() {
//        return appUserService.getMyDetail();
//    }

    /**
     * 修改用户名
     */
    @PrintLog
    @PostMapping("/modifyUserName")
    public JsonResult modifyUserDetail(@RequestBody AppDonaUserDTO appDonaUserDTO) {
        return appDonaUserService.modifyUserName(appDonaUserDTO);
    }

//    /**
//     * 获取发送短信验证码需要填写的国家地区编码
//     */
//    @GetMapping("/getCountryCode")
//    public JsonResult getCountryCode() {
//        return JsonResult.successResult(CountryTelephoneCode.getCountryTelephoneCode());
//    }
//
//    /**
//     * 发送短信验证码
//     */
//    @GetMapping("/sendSms/{mobile}/{type}")
//    public JsonResult sendSms(@PathVariable String mobile,@PathVariable Integer type) {
//        return appUserService.sendSms(mobile,type);
//    }
//
//    /**
//     * 绑定手机号
//     */
//    @PostMapping("/bindMobile")
//    public JsonResult bindMobile(@RequestBody MobileDTO mobileDTO) {
//        return appUserService.bindMobile(mobileDTO);
//    }
//
//    /**
//     * 获取已关联的tiktok账号信息
//     *
//     * @return
//     */
//    @GetMapping("/getTiktokInfo")
//    public JsonResult<TiktokInfoVO> tiktok() {
//        return appUserService.getTiktokInfo();
//    }
//
//    /**
//     * 绑定Tiktok账号
//     */
//    @PostMapping("/bindTiktok")
//    public JsonResult bindTiktok(@RequestBody TikTokAccount tikTokAccount) {
//        return appUserService.bindTiktok(tikTokAccount);
//    }
//
//    /**
//     * 更换Tiktok账号
//     */
//    @PostMapping("/editTiktok")
//    public JsonResult editTiktok(@RequestBody TikTokAccount tikTokAccount) {
//        return appUserService.editTiktok(tikTokAccount);
//    }

//    /**
//     * 上传头像
//     */
//    @PostMapping("/uploadHeadImage")
//    public JsonResult uploadHeadImage(@RequestParam("file") MultipartFile file) throws Exception {
//        return appDonaUserService.uploadHeadImage(file);
//    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    public JsonResult updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return appDonaUserService.updatePassword(updatePasswordDTO);
    }
    
    /**
     * 全球捐款名单
     */
    @PostMapping("/allUsers")
    public JsonResult<PageVO<AllDonaUsersDTO>> allusers(@RequestBody PageDTO pageDTO) {
        return JsonResult.successResult(new PageVO<>(appDonaUsersMapper.allDonaUsers(pageDTO.getPage())));
    }
    
    /**
     * 查询用户
     */
    @PostMapping("/selectUsers")
    public JsonResult<List<AllDonaUsersDTO>> selectUsers(@RequestBody SelectUserDTO selectUserDTO) {
        return JsonResult.successResult(appDonaUsersMapper.selectUsers(selectUserDTO.getNickName()));
    }
    
    /**
     * 积分排行榜
     */
    @PostMapping("/integralRanking")
    public JsonResult<PageVO<AllIntegralUsersDTO>> integralRanking(@RequestBody PageDTO pageDTO) {
        Page<AllIntegralUsersDTO> ranking = appDonaUsersMapper.integralUsersRanking(pageDTO.getPage());
        return JsonResult.successResult(new PageVO<>(ranking));
    }
    
//    /**
//     * 我的积分
//     */
//    @PostMapping("/myIntegral")
//    public JsonResult<PageVO<AllIntegralUsersDTO>> myIntegral(@RequestBody SelectMyIntegralDTO selectMyIntegralDTO) {
//        Page<AllIntegralUsersDTO> ranking = appDonaUsersMapper.integralUsersRanking(pageDTO.getPage());
//        return JsonResult.successResult(new PageVO<>(ranking));
//    }
    
    /**
     * 查询积分排行榜
     */
    @PostMapping("/integralRankingSelect")
    public JsonResult<List<AllIntegralUsersDTO>> integralRankingSelect(@RequestBody SelectMyIntegralDTO selectMyIntegralDTO) {
        return JsonResult.successResult(appDonaUsersMapper.integralRankingSelect(selectMyIntegralDTO.getEmail()));
    }
    
    /**
     * 减碳足迹
     */
    @PostMapping("/carbonFootprint")
    public JsonResult<PageVO<PaticiTaskDTO>> carbonFootprint(@RequestBody PageDTO pageDTO) {
        return donaUsersIntegralWalletsLogsService.carbonFootprint(pageDTO.getPage());
    }
    
    /**
     * 捐赠等级
     */
    @PostMapping("/donaAllLevel")
    public JsonResult donaUsersAllLevel() {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        JSONObject json = new JSONObject();
        json.put("level", donaUsersWalletsLogsMapper.donaUsersLevel(userId));
        return JsonResult.successResult(json);
    }
    
    /**
     * 已购买的等级
     */
    @PostMapping("/buyLevel")
    public JsonResult<List<MedalDTO>> buyLevel() {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
//        JSONObject json = new JSONObject();
//        donaUsersWalletsLogsMapper.buyLevel(userId)
        List<MedalDTO> medalDTOS = donaUsersWalletsLogsMapper.medalInfo();
        List<MedalDTO> donaLevel = donaUsersWalletsLogsMapper.donaUsersMedalDTO(userId);
        for(MedalDTO medalDTO: medalDTOS){
            if(donaLevel.contains(medalDTO)){
                medalDTO.setStatus(1);
            }else{
                medalDTO.setStatus(0);
            }
        }
        return JsonResult.successResult(medalDTOS);
    }
    
    /**
     * 钱包明细
     */
    @PostMapping("/walletInfo")
    public JsonResult<AllIntegralUsersDTO> selectWallet() {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        return JsonResult.successResult(appDonaUsersMapper.myIntegralRank(userId));
    }
    
    /**
     * 已参与任务
     */
    @PostMapping("/paticiTask")
    public JsonResult<PageVO<PaticiTaskDTO>> paticiTask(@RequestBody PageDTO pageDTO) {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        return JsonResult.successResult(new PageVO<>(donaUsersIntegralWalletsLogsMapper.particiTask(userId, pageDTO.getPage())));
    }
    
    /**
     * 钱包记录
     */
    @PostMapping("/selectWalletsLogsRecord")
    public JsonResult<PageVO<WalletRecordInfoDTO>> selectWalletsLogsRecord(@RequestBody PageDTO pageDTO) {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        return JsonResult.successResult(new PageVO<>(donaUsersWalletsLogsMapper.selectWalletsLogsRecord(CarbonConfig.WALLET_TYPE, userId, pageDTO.getPage())));
    }
    
    /**
     * 购买节点
     */
    @PostMapping("/donaNode")
    public JsonResult donaNode(@RequestBody DonaNodeDTO donaNodeDTO) {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        return JsonResult.successResult(donaUsersWalletsService.donaNode(donaNodeDTO));
    }
    
    

//    /**
//     * 跳过新手教程
//     */
//    @GetMapping("/skip")
//    public JsonResult skip(){
//        return appUserService.skip();
//    }
//
//    /**
//     * 查看团队信息
//     */
//    @GetMapping("/itemInfo")
//    public JsonResult<ItemInfoVO> itemInfo(){
//        return  appUserService.getItemInfo();
//    }
//
    /**
     * 退出登录
     */
    @GetMapping("/loginOut")
    public JsonResult loginOut() {
        return appDonaUserService.loginOut();
    }


}
