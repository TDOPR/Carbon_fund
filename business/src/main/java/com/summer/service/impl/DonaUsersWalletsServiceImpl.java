package com.summer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.enums.BooleanEnum;
import com.summer.common.enums.ReturnMessageEnum;
import com.summer.common.model.JsonResult;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.util.JwtTokenUtil;
import com.summer.common.util.MyIncrementGenerator;
import com.summer.constant.CarbonConfig;
import com.summer.enums.*;
import com.summer.mapper.*;
import com.summer.model.*;
import com.summer.model.dto.DonaNodeDTO;
import com.summer.model.dto.TreePathLevelDTO;
import com.summer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/1 18:54
 **/
@Slf4j
@Service
public class DonaUsersWalletsServiceImpl extends ServiceImpl<DonaUsersWalletsMapper, DonaUsersWallets> implements DonaUsersWalletsService {
    
    @Autowired
    private DonaUsesrsWalletsLogsService donaUsesrsWalletsLogsService;
    
    @Autowired
    private AppDonaUserService appDonaUserService;
    
    @Autowired
    private TreePathService treePathService;
    
    @Resource
    private AppDonaUsersMapper appDonaUsersMapper;
    
    @Resource
    private  TreePathMapper treePathMapper;
    
    @Autowired
    private AppUserServiceImpl appUserService;
    
    @Resource
    private MyIncrementGenerator myIncrementGenerator;
    
    @Autowired
    private CertificateService certificateService;
    
    
//    BigDecimal totalAmount;
    
    List<AppDonaUsers> appDonaSuperUsers = new ArrayList<>();
    
    /**
     * 根据用户ID查询指定列数据
     *
     * @param userId  用户Id
     * @param columns 需要查询的指定列
     * @return
     */
    @Override
    public DonaUsersWallets selectColumnsByUserId(Integer userId, SFunction<DonaUsersWallets, ?>... columns) {
        DonaUsersWallets donaUsersWallets = this.getOne(new LambdaQueryWrapper<DonaUsersWallets>().select(columns).eq(DonaUsersWallets::getUserId, userId));
        return donaUsersWallets;
    }
    
    @Override
    public boolean updateIntegralWallet(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum, IntegralEnum integralEnum) {
        boolean flag = this.lookIntegralUpdateWallets(amount, userId, flowingActionEnum);
        if (flag) {
            //插入流水记录
            donaUsesrsWalletsLogsService.insertDonaUsersIntegralWalletsLogs(userId, amount, flowingActionEnum, integralEnum);
        }
        return flag;
    }
    
    @Override
    public boolean lookIntegralUpdateWallets(BigDecimal amount, Integer userId, FlowingActionEnum flowingActionEnum) {
        int ret;
        if (flowingActionEnum.equals(FlowingActionEnum.INCOME)) {
            ret = this.baseMapper.lockUpdateAddIntegralWallet(userId, amount);
        } else {
            //减
            ret = this.baseMapper.lockUpdateReduceUsdWallet(userId, amount);
        }
        return ret == 1;
    }
    
    @Override
    public boolean updateUsdWallet(BigDecimal amount, Integer userId, FlowingActionEnum flowingActionEnum, UsdLogTypeEnum usdLogTypeEnum) {
        boolean flag = this.lookUsdUpdateWallets(userId, amount, flowingActionEnum);
        if (flag) {
            //插入流水记录
            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, amount, flowingActionEnum, usdLogTypeEnum);
        }
        return flag;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lookUsdUpdateWallets(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum) {
        int ret;
        if (flowingActionEnum.equals(FlowingActionEnum.INCOME)) {
            ret = this.baseMapper.lockUpdateAddUsdWallet(userId, amount);
        } else {
            //减
            ret = this.baseMapper.lockUpdateReduceUsdWallet(userId, amount);
        }
        return ret == 1;
    }
    
    @Override
    @Transactional
    public JsonResult sendAlgebraReward(Integer userId, BigDecimal donaAmount, Integer level) {

//        BigDecimal algebraReward=new BigDecimal(0);
        AppDonaUsers appDonaUsers = appDonaUserService.selectColumnsByUserId(userId, AppDonaUsers::getEmail);
        AppUsers appUsers;
        appUsers = appUserService.getOne(new LambdaQueryWrapper<AppUsers>().eq(AppUsers::getEmail, appDonaUsers.getEmail()));
        List<TreePathLevelDTO> treePathLevelDTOS = treePathService.getTreePathLevelOrderByLevel(appUsers.getId());
        DonaUsersWalletsLogs donaUsersWalletsLogs;
        Integer num = 1;
        for (TreePathLevelDTO treePathLevelDTO : treePathLevelDTOS) {
            if(treePathLevelDTO.getUserLevel() == null || treePathLevelDTO.getUserLevel().equals(0)){
                continue;
            }
            if (treePathLevelDTO.getUserLevel().compareTo(level) > 0) {
                if (treePathLevelDTO.getTreePathLevel().equals(1)) {
                    switch (treePathLevelDTO.getUserLevel()) {
                        case 2:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, donaAmount.multiply(VipLevelEnum.TWO.getOneLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
                        case 3:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, donaAmount.multiply(VipLevelEnum.THREE.getOneLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
                        default:
                            break;
                    }
                } else if (treePathLevelDTO.getTreePathLevel().equals(2)) {
                    switch (treePathLevelDTO.getUserLevel()) {
                        case 2:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, donaAmount.multiply(VipLevelEnum.TWO.getTwoLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
                        case 3:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, donaAmount.multiply(VipLevelEnum.THREE.getTwoLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (treePathLevelDTO.getUserLevel()) {
//                        case 1:
//                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, donaAmount.multiply(VipLevelEnum.ONE.getOneLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
//                            break;
//                        case 2:
//                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, donaAmount.multiply(VipLevelEnum.TWO.getOneLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
//                            break;
                        case 3:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, donaAmount.multiply(VipLevelEnum.TWO.getThreeLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
                        default:
                            break;
                    }
                    
                }
                
            } else {
                if (treePathLevelDTO.getTreePathLevel().equals(1)) {
                    switch (treePathLevelDTO.getUserLevel()) {
                        case 1:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, VipLevelEnum.getByLevel(1).getAmount().multiply(VipLevelEnum.ONE.getOneLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
                        case 2:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, VipLevelEnum.getByLevel(2).getAmount().multiply(VipLevelEnum.TWO.getOneLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
                        case 3:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, VipLevelEnum.getByLevel(3).getAmount().multiply(VipLevelEnum.THREE.getOneLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
                        default:
                            break;
                    }
                } else if (treePathLevelDTO.getTreePathLevel().equals(2)) {
                    switch (treePathLevelDTO.getUserLevel()) {
                        case 3:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, VipLevelEnum.getByLevel(3).getAmount().multiply(VipLevelEnum.THREE.getTwoLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
                        case 2:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, VipLevelEnum.getByLevel(2).getAmount().multiply(VipLevelEnum.TWO.getTwoLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (treePathLevelDTO.getUserLevel()) {
                        case 3:
                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, VipLevelEnum.getByLevel(3).getAmount().multiply(VipLevelEnum.THREE.getThreeLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
                            break;
//                        case 2:
//                            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(userId, donaAmount.multiply(VipLevelEnum.TWO.getOneLevelNum()), FlowingActionEnum.INCOME, UsdLogTypeEnum.ALGEBRA_REWARD);
//                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return JsonResult.successResult();
    }
    
    
    @Override
    @Transactional
    public JsonResult donaNode(DonaNodeDTO donaNodeDTO) {
        
        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
        VipLevelEnum vipLevelEnum = VipLevelEnum.getByLevel(donaNodeDTO.getLevel());
        String certiString;
        if (vipLevelEnum == null) {
            return JsonResult.failureResult();
        }
        //判断钱包里的Usd余额是否可以购买vip
        DonaUsersWallets donaUsersWallets = this.selectColumnsByUserId(userId, DonaUsersWallets::getRechargeUsdtAmount);
        if (donaUsersWallets.getRechargeUsdtAmount().compareTo(donaNodeDTO.getAmount()) < 0) {
            return JsonResult.failureResult(ReturnMessageEnum.AMOUNT_EXCEEDS_BALANCE);
        }
        
        //扣usd费用 增加积分余额
        if (donaNodeDTO.getAmount().intValue() > 0) {
            this.updateUsdWallet(donaNodeDTO.getAmount(), userId, FlowingActionEnum.EXPENDITURE, UsdLogTypeEnum.DONA_NODE);
            this.updateIntegralWallet(userId, VipLevelEnum.getByLevel(donaNodeDTO.getLevel()).getIntegralAmount(), FlowingActionEnum.INCOME, IntegralEnum.TENTH);
        }
        
//        totalAmount = totalAmounselectColumnsByUserIdt.add(donaNodeDTO.getAmount());
        AppDonaUsers appDonaUsers = appDonaUserService.selectColumnsByUserId(userId, AppDonaUsers::getLevel);
        //更新用户的vip等级
        if(appDonaUsers.getLevel().compareTo(donaNodeDTO.getLevel())<=0){
            switch (donaNodeDTO.getLevel()){
                case 1:
                    if(appDonaUsers.getOneCertificate() == null){
                        certiString = myIncrementGenerator.usingMath(CarbonConfig.STRING_LENGTH);
                        UpdateWrapper<AppDonaUsers> updateWrapper = Wrappers.update();
                        updateWrapper.lambda().
                                set(AppDonaUsers::getOneCertificate, certiString)

//                .set(AppDonaUsers::getValid, BooleanEnum.TRUE.intValue())
                                .eq(AppDonaUsers::getId, userId);
                        appDonaUserService.update(updateWrapper);
                        certificateService.generateCertificate(userId, appDonaUsers.getNickName(), appDonaUsers.getOneCertificate(),VipLevelEnum.ONE.getLevel().toString(), appDonaUsers.getCreateTime());
                    }
                break;
                case 2:
                    if(appDonaUsers.getTwoCertificate() == null){
                        certiString = myIncrementGenerator.usingMath(CarbonConfig.STRING_LENGTH);
                        UpdateWrapper<AppDonaUsers> updateWrapper = Wrappers.update();
                        updateWrapper.lambda().
                                set(AppDonaUsers::getTwoCertificate, certiString)

//                .set(AppDonaUsers::getValid, BooleanEnum.TRUE.intValue())
                                .eq(AppDonaUsers::getId, userId);
                        appDonaUserService.update(updateWrapper);
                        certificateService.generateCertificate(userId, appDonaUsers.getNickName(), appDonaUsers.getOneCertificate(),VipLevelEnum.TWO.getLevel().toString(), appDonaUsers.getCreateTime());
                    }
                    
                    break;
                case 3:
                    if(appDonaUsers.getThreeCertificate() == null){
                        certiString = myIncrementGenerator.usingMath(CarbonConfig.STRING_LENGTH);
                        UpdateWrapper<AppDonaUsers> updateWrapper = Wrappers.update();
                        updateWrapper.lambda().
                                set(AppDonaUsers::getThreeCertificate, certiString)

//                .set(AppDonaUsers::getValid, BooleanEnum.TRUE.intValue())
                                .eq(AppDonaUsers::getId, userId);
                        appDonaUserService.update(updateWrapper);
                        certificateService.generateCertificate(userId, appDonaUsers.getNickName(), appDonaUsers.getOneCertificate(),VipLevelEnum.THREE.getLevel().toString(), appDonaUsers.getCreateTime());
                    }
                    break;
                default:
                    break;
            }
            UpdateWrapper<AppDonaUsers> updateWrapper = Wrappers.update();
            updateWrapper.lambda().
                    set(AppDonaUsers::getLevel, donaNodeDTO.getLevel())
                    
//                .set(AppDonaUsers::getValid, BooleanEnum.TRUE.intValue())
                    .eq(AppDonaUsers::getId, userId);
            appDonaUserService.update(updateWrapper);
        }
        
        
        sendAlgebraReward(userId, donaNodeDTO.getAmount(), donaNodeDTO.getLevel());
        
        return JsonResult.successResult();
    }
    
    @Override
    public JsonResult superReward() {
//        List<AppDonaUsers> appDonaSuperUsers = new ArrayList<>();
        List<AppDonaUsers> allUsers = appDonaUsersMapper.findAllUsers();
        for(AppDonaUsers user: allUsers){
            BigDecimal myTeamRechargeAmount = treePathMapper.getTeamRechargeAmount(user.getId());
            Integer myTeamNum = treePathMapper.getTeamNum(user.getId());
            if(myTeamNum.compareTo(300)<0){
                continue;
            }
            if(myTeamRechargeAmount.compareTo(new BigDecimal(30000))<0){
                continue;
            }
            if(user.getLevel().compareTo(VipLevelEnum.THREE.getLevel())<0){
                continue;
            }
            UpdateWrapper<AppDonaUsers> updateWrapper = Wrappers.update();
            updateWrapper = Wrappers.update();
            updateWrapper.lambda()
                    .set(AppDonaUsers::getSuperNode, BooleanEnum.TRUE.intValue())
                    .eq(AppDonaUsers::getId, user.getId());
            appDonaUserService.update(null, updateWrapper);
            appDonaSuperUsers.add(user);
        }
        Integer divideNum = appDonaSuperUsers.size();
        BigDecimal allUsersRechargeAmount = appDonaUsersMapper.allUsersRechargeAmount();
        BigDecimal superReward = allUsersRechargeAmount.multiply(CarbonConfig.SPECIAL_AWARD_RATE).divide(new BigDecimal(divideNum));
        for(AppDonaUsers appDonaUser: appDonaSuperUsers){
            donaUsesrsWalletsLogsService.insertDonaUsersWalletsLogs(appDonaUser.getId(), superReward, FlowingActionEnum.INCOME, UsdLogTypeEnum.SUPER_REWARD);
        }
        return JsonResult.successResult();
    }
}
