package com.summer.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.config.GlobalProperties;
import com.summer.common.config.LoginConfig;
import com.summer.common.constant.CacheKeyPrefixConstants;
import com.summer.common.enums.DataSavePathEnum;
import com.summer.common.enums.ReturnMessageEnum;
import com.summer.common.model.JsonResult;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.service.SysLoginLogService;
import com.summer.common.util.*;
import com.summer.common.util.redis.RedisUtil;
import com.summer.enums.*;
import com.summer.mapper.AppDonaUserTaskMapper;
import com.summer.mapper.AppUserMapper;
import com.summer.model.AppDonaUserTask;
import com.summer.model.AppDonaUsers;
import com.summer.model.AppUsers;
import com.summer.model.dto.AppDonaUserTaskDTO;
import com.summer.model.dto.CarbonTaskDTO;
import com.summer.service.AppDonaUsersDoTaskService;
import com.summer.service.AppUserService;
import com.summer.service.DonaUsersWalletsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/4 11:21
 **/
@Slf4j
@Service
public class AppDonaUsersDoTaskServiceImpl extends ServiceImpl<AppDonaUserTaskMapper, AppDonaUserTask> implements AppDonaUsersDoTaskService {
    
    @Autowired
    AppDonaUsersDoTaskService appDonaUsersDoTaskService;
    
    @Autowired
    DonaUsersWalletsService donaUsersWalletsService;
    
    @Override
    public JsonResult uploadHeadImage(MultipartFile file, Long id) {
        try {
            Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
//            AppDonaUserTask appDonaUserTask = this.getOne(new LambdaQueryWrapper<AppDonaUserTask>().select(AppDonaUserTask::getImgUrl).eq(AppUserTask::getId, id));
            String suffix = FileUtil.getSuffix(file.getOriginalFilename());
            String fileName = id + "_" + IdUtil.getSnowflakeNextId() + "." + suffix;
            DataSavePathEnum dataSavePathEnum = DataSavePathEnum.TASK_IMAGE;
            String savePath = dataSavePathEnum.getPath();
            
//            //删除之前上传过的截图
//            if (StringUtil.isNoneBlank(appUserTask.getImgUrl())) {
//                String headName = appUserTask.getImgUrl().substring(appUserTask.getImgUrl().lastIndexOf("/") + 1);
//                FileUtil.del(new File(savePath, headName));
//            }
//
            String url = GlobalProperties.getVirtualPathURL() + StringUtil.replace(savePath, GlobalProperties.getRootPath(), "") + fileName;
            //复制文件流到本地文件
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(dataSavePathEnum.getFile(), fileName));
            AppDonaUserTask appDonaUserTask = AppDonaUserTask.builder()
                    .imgUrl(url)
                    .taskId(id)
                    .userId(userId)
                    .status(TaskStatusEnum.TO_BE_CHECK.getCode())
                    .taskIntegral(IntegralEnum.getRewardIntegralByType(Integer.valueOf(id.intValue())))
                    .build();
            boolean flag = appDonaUsersDoTaskService.save(appDonaUserTask);
//            UpdateWrapper<AppDonaUserTask> updateWrapper = Wrappers.update();
//            updateWrapper.lambda()
//                    .set(AppDonaUserTask::getImgUrl, url)
//                    .eq(AppDonaUserTask::getId, id);
//            boolean flag = this.update(updateWrapper);
            if (flag) {
                JSONObject object = new JSONObject();
                object.put("url", url);
                return JsonResult.successResult(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("upload error:{}", e.getMessage());
        }
        return JsonResult.failureResult();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkTaskList() {
        if (!CarbonSettingEnum.ENABLED_CARBON_AUTO_CHECK.boolValue()) {
            return;
        }
        //只处理比当前时间早指定分钟的任务
        List<AppDonaUserTaskDTO> appDonaUserTaskDTOList = this.baseMapper.getAutoCheckList(TaskStatusEnum.TO_BE_CHECK.getCode(), LocalDateTime.now().minusMinutes(CarbonSettingEnum.CARBON_AUTO_CHECK_LAZY_TIME.intValue()));
        if (appDonaUserTaskDTOList.size() == 0) {
            return;
        }
        UpdateWrapper<AppDonaUserTask> updateWrapper;
//        VipLevelEnum hasLevel;
//        List<VipOrders> vipOrdersList;
//        UpdateWrapper<VipOrders> vipOrdersUpdateWrapper;
//        Integer vipOrderLevel = 0;
        for (AppDonaUserTaskDTO appDonaUserTaskDTO : appDonaUserTaskDTOList) {
            updateWrapper = Wrappers.update();
//            vipOrdersList = vipOrdersMapper.selectBatchIds(JSONObject.parseArray(appUserTaskDTO.getVipOrderIds(), Integer.class));
//            if (vipOrdersList.size() == 0) {
//                this.removeById(appUserTaskDTO.getId());
//                continue;
//            }
//            Integer maxLevel = vipOrdersList.stream().max(Comparator.comparingInt(VipOrders::getLevel)).get().getLevel();
//            hasLevel = VipLevelEnum.getByLevel(maxLevel);
//            for (VipOrders vipOrders : vipOrdersList) {
//                vipOrdersUpdateWrapper = Wrappers.update();
//                vipOrdersUpdateWrapper.lambda()
//                        .set(VipOrders::getFrozenAmount, vipOrders.getFrozenAmount().subtract(EarningUtil.getEarningByFrozen(hasLevel, vipOrders)))
//                        .eq(VipOrders::getId, vipOrders.getId());
//                vipOrdersService.update(vipOrdersUpdateWrapper);
//                vipOrderLevel = vipOrders.getLevel();
//            }
            donaUsersWalletsService.updateIntegralWallet(appDonaUserTaskDTO.getUserId(), appDonaUserTaskDTO.getIntegralAmount(), FlowingActionEnum.INCOME, IntegralEnum.getByType(appDonaUserTaskDTO.getId()));
            updateWrapper.lambda()
                    .set(AppDonaUserTask::getStatus, TaskStatusEnum.SUCCESS.getCode())
                    .eq(AppDonaUserTask::getId, appDonaUserTaskDTO.getId());
            this.update(updateWrapper);
        }
        log.info("自动审核了{}条记录!", appDonaUserTaskDTOList.size());
    }
    
//    @Override
//    @Transactional
//    public JsonResult submit(Long id) {
//        CarbonTaskDTO carbonTaskDTO = this.baseMapper.getTiktokTaskDTO(id);
//        if (StringUtil.isBlank(tiktokTaskDTO.getImgUrl())) {
//            return JsonResult.failureResult(ReturnMessageEnum.PLEASE_UPLOAD_IMG);
//        }
//        //如果提交的任务已是审核中或成功,则不处理
//        if (TaskStatusEnum.TO_BE_CHECK.getCode() == tiktokTaskDTO.getStatus() || TaskStatusEnum.SUCCESS.getCode() == tiktokTaskDTO.getStatus()) {
//            return JsonResult.failureResult();
//        }
//        if (tiktokTaskDTO.getBuilt() > 0) {
//            //如果是内置任务类型需要查看用户是否做过新手教程
//            AppUsers appUsers = appUserService.selectColumnsByUserId(tiktokTaskDTO.getUserId(), AppUsers::getGreenhorn);
//            if (BooleanEnum.TRUE.intValue().equals(appUsers.getGreenhorn())) {
//                //修改用户做过新手教程
//                UpdateWrapper<AppUsers> appUsersUpdateWrapper = Wrappers.update();
//                appUsersUpdateWrapper.lambda()
//                        .set(AppUsers::getGreenhorn, BooleanEnum.FALSE.intValue())
//                        .eq(AppUsers::getId, tiktokTaskDTO.getUserId());
//                appUserService.update(appUsersUpdateWrapper);
//                //新手教程则自动审核发放任务奖励
////                AuditCheckDTO auditCheckDTO = new AuditCheckDTO();
////                auditCheckDTO.setId(id);
////                auditCheckDTO.setState(TaskStatusEnum.SUCCESS.getCode());
////                return this.checkTask(auditCheckDTO);
//            }
//        }
//
//        UpdateWrapper<AppUserTask> updateWrapper = Wrappers.update();
//        updateWrapper.lambda()
//                .set(AppUserTask::getStatus, TaskStatusEnum.TO_BE_CHECK.getCode())
//                .eq(AppUserTask::getId, id);
//        this.update(updateWrapper);
//        return JsonResult.successResult();
    }
