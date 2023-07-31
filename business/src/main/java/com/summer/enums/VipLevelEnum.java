package com.summer.enums;

import com.summer.common.model.ThreadLocalManager;
import com.summer.common.util.MessageUtil;
import com.summer.constant.TiktokConfig;
import com.summer.model.vo.VipLevelVO;
import com.summer.utils.BigDecimalUtils;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/5 10:23
 **/
@Getter
public enum VipLevelEnum {

    ZERO(0, new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), "Zero Carbon Volunteer/"),
    ONE(1, new BigDecimal("9.9"), new BigDecimal("990"), new BigDecimal("0.3"), new BigDecimal("0.15"), new BigDecimal("0"), "Zero Carbon Envoy/"),
    TWO(2, new BigDecimal("50"), new BigDecimal("5000"), new BigDecimal("0.4"), new BigDecimal("0.15"), new BigDecimal("0"), "Zero Carbon Ambassador/"),
    THREE(3, new BigDecimal("150"), new BigDecimal("15000"), new BigDecimal("0.5"), new BigDecimal("0.15"), new BigDecimal("0.05"), "Zero Carbon Special/");
    VipLevelEnum(Integer level, BigDecimal amount, BigDecimal integralAmount, BigDecimal oneLevelNum, BigDecimal twoLevelNum, BigDecimal threeLevelNum, String description) {
        this.level = level;
        this.amount = amount;
        this.oneLevelNum = oneLevelNum;
        this.twoLevelNum = twoLevelNum;
        this.threeLevelNum = threeLevelNum;
        this.integralAmount = integralAmount;
        this.description = description;
    }

//    VipLevelEnum(Integer level, int taskNumLimit, BigDecimal outOfSaleAmount) {
//        this.level = level;
//        this.taskNumLimit = taskNumLimit;
//        //每天的收益上限等于可获取的除以总金额
//        this.earningsLimit = BigDecimalUtils.divideSaveTwoDecimal(outOfSaleAmount, new BigDecimal("30"));
//        this.outOfSaleAmount = outOfSaleAmount;
//        //每单的收益等于 单量
//        this.earnings = BigDecimalUtils.divideSaveTwoDecimal(earningsLimit, new BigDecimal(taskNumLimit));
//        this.opening = true;
//    }

    /**
     * VIP等级
     */
    private Integer level;

    /**
     * 购买金额
     */
    private BigDecimal amount;
    
    /**
     * 购买金额对应积分额度
     */
    private BigDecimal integralAmount;

    /**
     * 一代奖励
     */
    private BigDecimal oneLevelNum;
    
    /**
     * 二代奖励
     */
    private BigDecimal twoLevelNum;
    
    /**
     * 三代奖励
     */
    private BigDecimal threeLevelNum;
    
    /**
     * 英文描述
     */
    private String description;

    /**
     * 获取页面显示的Vip套餐列表
     *
     * @param level 当前用户的等级
     * @return
     */
//    public static List<VipLevelVO> getVipList(Integer level) {
//        List<VipLevelVO> list = new ArrayList<>();
//        boolean has, noBuy;
//        for (VipLevelEnum vipLevelEnum : values()) {
//            has = false;
//            noBuy = false;
//            if (vipLevelEnum.level.equals(VipLevelEnum.ZERO.getLevel())) {
//                continue;
//            }
//            if (vipLevelEnum.level <= level) {
//                has = true;
//                if (vipLevelEnum.level < level) {
//                    noBuy = true;
//                }
//            }
//            list.add(new VipLevelVO(vipLevelEnum.level, BigDecimal.ZERO, vipLevelEnum.amount, vipLevelEnum.opening, has, noBuy, vipLevelEnum.opening ? vipLevelEnum.getTextList() : Collections.emptyList()));
//        }
//        return list;
//    }
//
//    public static List<VipLevelVO> getVipListByZero(BigDecimal usd) {
//        List<VipLevelVO> list = new ArrayList<>();
//        BigDecimal usdResult;
//        for (VipLevelEnum vipLevelEnum : values()) {
//            if (vipLevelEnum == ZERO || vipLevelEnum == ZERO_SECOND_MONTH) {
//                continue;
//            }
//            if (vipLevelEnum.level.equals(VipLevelEnum.ONE.getLevel())) {
//                usdResult = TiktokConfig.V1_USD.compareTo(usd) > 0 ? usd : TiktokConfig.V1_USD;
//            } else if (vipLevelEnum.level.equals(VipLevelEnum.TWO.getLevel())) {
//                usdResult = TiktokConfig.V2_USD.compareTo(usd) > 0 ? usd : TiktokConfig.V2_USD;
//            } else {
//                usdResult = usd;
//            }
//            if (usdResult.compareTo(vipLevelEnum.getAmount()) > 0) {
//                //抵扣价格大于vip的价格则购买vip免费
//                usdResult = vipLevelEnum.getAmount();
//            }
//            list.add(new VipLevelVO(vipLevelEnum.level, usdResult, vipLevelEnum.amount.subtract(usdResult), vipLevelEnum.opening, false, false, vipLevelEnum.opening ? vipLevelEnum.getTextList() : Collections.emptyList()));
//        }
//        return list;
//    }
//
//    /**
//     * 获取国际化文本展示的内容
//     *
//     * @return
//     */
//    private List<String> getTextList() {
//        return Arrays.asList(
//                MessageUtil.get("vip.text1", new Object[]{amount.toPlainString()}, ThreadLocalManager.getLanguage()),
//                MessageUtil.get("vip.text2", new Object[]{num, outOfSaleAmount.toPlainString()}, ThreadLocalManager.getLanguage()),
//                MessageUtil.get("vip.text3", new Object[]{taskNumLimit, earningsLimit.toPlainString()}, ThreadLocalManager.getLanguage())
//        );
//    }

    public static VipLevelEnum getByLevel(Integer level) {
        for (VipLevelEnum vipLevelEnum : values()) {
            if (vipLevelEnum.getLevel().equals(level)) {
                return vipLevelEnum;
            }
        }
        return null;
    }

}
