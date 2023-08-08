package com.summer.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summer.common.annotation.PrintLog;
import com.summer.common.enums.ReturnMessageEnum;
import com.summer.common.model.JsonResult;
import com.summer.common.model.PageParam;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.model.dto.UpdatePasswordDTO;
import com.summer.common.model.vo.PageVO;
import com.summer.common.util.JwtTokenUtil;
import com.summer.constant.CarbonConfig;
import com.summer.enums.FlowingActionEnum;
import com.summer.enums.IntegralEnum;
import com.summer.enums.VipLevelEnum;
import com.summer.manager.TradeManager;
import com.summer.mapper.*;
import com.summer.model.CarbonTask;
import com.summer.model.Medal;
import com.summer.model.PaticiTaskToday;
import com.summer.model.UserTaskLogs;
import com.summer.model.condition.GlobalUserCondition;
import com.summer.model.dto.*;
import com.summer.model.vo.*;
import com.summer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * 我的主页
 *
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
    
    @Autowired
    private PaticiTaskTodayService paticiTaskTodayService;
    
    @Autowired
    private CarbonTaskService carbonTaskService;
    
    @Resource
    private ArticleMapper articleMapper;
    
    @Autowired
    private UserTaskLogsService userTaskLogsService;
    
    @Autowired
    private UserDonaLogsService userDonaLogsService;
    
    
    /**
     * 修改用户名
     */
    @PrintLog
    @PostMapping("/modifyUserName")
    public JsonResult modifyUserDetail(@RequestBody AppDonaUserDTO appDonaUserDTO) {
        return appDonaUserService.modifyUserName(appDonaUserDTO);
    }
    
    
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
    public JsonResult<PageVO<AllDonaUsersDTO>> allusers(@RequestBody PageParam<AllDonaUsersDTO, GlobalUserCondition> pageParam) {
        return appDonaUserService.allUsers(pageParam);
    }
    
    /**
     * 查询用户
     */
    @PostMapping("/selectUsers")
    public JsonResult<PageVO<AllDonaUsersDTO>> selectUsers(@RequestBody SelectUserDTO selectUserDTO) {
        return JsonResult.successResult(new PageVO<>(appDonaUsersMapper.selectUsers(selectUserDTO.getNickName(), selectUserDTO.getPage())));
    }
    
    /**
     * 积分排行榜
     */
    //@PrintLog
    @PostMapping("/integralRanking")
    public JsonResult<PageVO<AllIntegralUsersDTO>> integralRanking(@RequestBody PageParam<AllIntegralUsersDTO, GlobalUserCondition> pageParam) {
        return appDonaUserService.integralRanking(pageParam);
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
    public JsonResult<PageVO<AllIntegralUsersDTO>> integralRankingSelect(@RequestBody SelectMyIntegralDTO selectMyIntegralDTO) {
        return JsonResult.successResult(new PageVO<>(appDonaUsersMapper.integralRankingSelect(selectMyIntegralDTO.getNickName(), selectMyIntegralDTO.getPage())));
    }
    
    /**
     * 减碳足迹
     */
    @PostMapping("/carbonFootprint")
    public JsonResult<PageVO<PaticiTaskDTO>> carbonFootprint(@RequestBody PageDTO pageDTO) {
        return donaUsersIntegralWalletsLogsService.carbonFootprint(pageDTO.getPage());
    }
    
    /**
     * 减碳足迹
     */
    @PostMapping("/carbonFootprintRemark")
    public JsonResult carbonFootprintRemark(@RequestBody ParticiTaskDTO particiTaskDTO) {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        CarbonFootprintRemarkVO carbonFootprintRemarkVO = donaUsersIntegralWalletsLogsMapper.carbonFootprintRemark(particiTaskDTO.getTaskId());
        SelectDoTaskVO selectDoTaskVO = donaUsersIntegralWalletsLogsMapper.selectDoTask(userId, particiTaskDTO.getTaskId());
        if(selectDoTaskVO == null){
            carbonFootprintRemarkVO.setStatus(Integer.valueOf(0));
        }else{
            carbonFootprintRemarkVO.setStatus(Integer.valueOf(1));
        }
        CarbonTask carbonTask = carbonTaskService.getOne(new LambdaQueryWrapper<CarbonTask>().select(CarbonTask::getId, CarbonTask::getJoinedCount).eq(CarbonTask::getId, particiTaskDTO.getTaskId()));
        if (carbonFootprintRemarkVO.getJoinedCount() == null) {
            Integer joinedCount = donaUsersIntegralWalletsLogsMapper.joinedCount(particiTaskDTO.getTaskId());
            carbonFootprintRemarkVO.setJoinedCount(joinedCount);
        } else {
            carbonFootprintRemarkVO.setJoinedCount(carbonTask.getJoinedCount());
        }
        return JsonResult.successResult(carbonFootprintRemarkVO);
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
        for (MedalDTO medalDTO : medalDTOS) {
            if (donaLevel.contains(medalDTO)) {
                medalDTO.setStatus(1);
            } else {
                medalDTO.setStatus(0);
            }
        }
        return JsonResult.successResult(medalDTOS);
    }
    
    /**
     * 钱包明细
     */
    @PostMapping("/walletInfo")
    public JsonResult<AllIntegralUsersVO> selectWallet() {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        return JsonResult.successResult(appDonaUsersMapper.myIntegralRank(userId));
    }
    
    /**
     * 我的誓言
     */
    @PostMapping("/myOath")
    public JsonResult<PageVO<MyOathVO>> myOath(@RequestBody PageDTO pageDTO) {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        Page<MyOathVO> myOathVOS = donaUsersIntegralWalletsLogsMapper.myOath(userId, pageDTO.getPage());
        for (MyOathVO myOathVO : myOathVOS.getRecords()) {
            myOathVO.setStatus(Integer.valueOf(1));
        }
        return JsonResult.successResult(new PageVO<>(myOathVOS));
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
        return donaUsersWalletsService.donaNode(donaNodeDTO);
    }
    
    /**
     * 参与活动
     */
    @PostMapping("/doTask")
    public JsonResult doTask(@RequestBody ParticiTaskDTO particiTaskDTO) {
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        SelectDoTaskVO selectDoTaskVO = donaUsersIntegralWalletsLogsMapper.selectDoTask(userId, particiTaskDTO.getTaskId());
        if (selectDoTaskVO == null) {
            Boolean flag = donaUsersWalletsService.updateIntegralWallet(userId, new BigDecimal(IntegralEnum.getByType(particiTaskDTO.getTaskId()).getRewardIntegral()), FlowingActionEnum.INCOME, IntegralEnum.getByType(particiTaskDTO.getTaskId()));
            if (flag == true) {
                PaticiTaskToday paticiTaskToday = new PaticiTaskToday();
                paticiTaskToday.setTaskId(particiTaskDTO.getTaskId());
                paticiTaskToday.setUserId(userId);
                paticiTaskTodayService.save(paticiTaskToday);
                
                UserTaskLogs userTaskLogs = userTaskLogsService.getOne(
                        new LambdaQueryWrapper<UserTaskLogs>()
                                .eq(UserTaskLogs::getUserId, userId).eq(UserTaskLogs::getTaskId, particiTaskDTO.getTaskId())
                );
                if (userTaskLogs == null) {
                    userTaskLogsService.updateUserTaskLogs(userId, particiTaskDTO.getTaskId());
                }
                
                return JsonResult.successResult(ReturnMessageEnum.OK);
            } else {
                return JsonResult.failureResult(ReturnMessageEnum.FAILED);
            }
        } else {
            return JsonResult.failureResult(ReturnMessageEnum.TASK_NUM_BALANCE);
        }
    }
    
    
    /**
     * 退出登录
     */
    @GetMapping("/loginOut")
    public JsonResult loginOut() {
        return appDonaUserService.loginOut();
    }
    
    /**
     * 碳索咨询
     */
    @PostMapping("/carbonInfo")
    public JsonResult<PageVO<CarbonInfoVO>> carbonInfo(@RequestBody PageDTO pageDTO) {
        return JsonResult.successResult(new PageVO<>(articleMapper.carbonInfo(pageDTO.getPage())));
    }
    
    /**
     * 探索咨询详情
     */
    @PostMapping("/carbonInfoDetail")
    public JsonResult<CarbonInfoDetailVO> carbonInfoDetail(@RequestBody CarbonInfoDetailDTO carbonInfoDetailDTO) {
        return JsonResult.successResult(articleMapper.carbonInfoDetail(carbonInfoDetailDTO.getId()));
    }
    
}
